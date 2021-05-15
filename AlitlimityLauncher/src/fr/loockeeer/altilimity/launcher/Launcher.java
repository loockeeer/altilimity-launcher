package fr.loockeeer.altilimity.launcher;

import java.io.File;
import java.io.IOException;

import fr.theshark34.openauth.AuthPoints;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openauth.Authenticator;
import fr.theshark34.openauth.model.AuthAgent;
import fr.theshark34.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.launcher.AuthInfos;
import fr.theshark34.openlauncherlib.launcher.GameFolder;
import fr.theshark34.openlauncherlib.launcher.GameInfos;
import fr.theshark34.openlauncherlib.launcher.GameLauncher;
import fr.theshark34.openlauncherlib.launcher.GameTweak;
import fr.theshark34.openlauncherlib.launcher.GameType;
import fr.theshark34.openlauncherlib.launcher.GameVersion;
import fr.theshark34.openlauncherlib.util.ErrorUtil;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;



public class Launcher {


	public static final GameVersion A_VERSION = new GameVersion ("1.7.10", GameType.V1_7_10);
	public static final GameInfos A_INFOS = new GameInfos ("Altilimity", A_VERSION, true, new GameTweak[] {GameTweak.FORGE});
	public static final File A_DIR = A_INFOS.getGameDir();
	public static final File A_CRASHS_DIR = new File(A_DIR, "crashs");
	
	private static AuthInfos authInfos;
	
	private static Thread updateThread;
	private static ErrorUtil errorUtil = new ErrorUtil(new File(A_DIR, "crashs"));
	
	
	public static void auth(String username, String password) throws AuthenticationException {
		Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
		AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, "");
		authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
	}
	public static void update () throws Exception{
		SUpdate su = new SUpdate("http://62.4.23.118/Altilimity/LauncherMAJS/minecraft/", A_DIR);
		su.getServerRequester().setRewriteEnabled(true);

		
		updateThread = new Thread() {
			int val;
			int max;			
			
			@Override
			public void run() {
				while (!this.isInterrupted()) {
					
					if (BarAPI.getNumberOfFileToDownload() == 0) {
						LauncherFrame.getInstance().getLauncherPanel().changeInfoText("Vérification des fichiers...");
						continue;
					}
					
					LauncherFrame.getInstance().getLauncherPanel().changeInfoText("Téléchargement des fichiers... "+BarAPI.getNumberOfDownloadedFiles()+"/"+
					BarAPI.getNumberOfFileToDownload());
					
					val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);		
					max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);
					
					LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
					LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(val);
					

				};
			};
		};
		updateThread.start();
		su.start();
		updateThread.interrupt();
	}
	
	public static void launcher() throws IOException{
		GameLauncher gameLauncher = new GameLauncher(A_INFOS, GameFolder.BASIC, authInfos);
		Process p = gameLauncher.launch();
		try {
			Thread.sleep(5000L);
		}catch (InterruptedException e) {
		}
		LauncherFrame.getInstance().setVisible(false);
		try {
			p.waitFor();
		}catch (InterruptedException e) {
		}
		LauncherFrame.getInstance().getLauncherPanel().changeInfoText("Lancement du jeu...");
		System.exit(0);
		
	}
	
	
	public static void interruptThread() {
		updateThread.interrupt();
		
	}

	public static ErrorUtil getErrorUtil() {
		return errorUtil;		
	}
	
}
