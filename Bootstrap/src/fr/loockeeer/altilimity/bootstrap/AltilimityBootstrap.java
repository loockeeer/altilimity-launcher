package fr.loockeeer.altilimity.bootstrap;

import java.io.File;
import java.io.IOException;

import fr.theshark34.openlauncherlib.bootstrap.Bootstrap;
import fr.theshark34.openlauncherlib.bootstrap.LauncherClasspath;
import fr.theshark34.openlauncherlib.bootstrap.LauncherInfos;
import fr.theshark34.openlauncherlib.launcher.util.WindowMover;
import fr.theshark34.openlauncherlib.util.ErrorUtil;
import fr.theshark34.openlauncherlib.util.GameDir;
import fr.theshark34.openlauncherlib.util.SplashScreen;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.swinger.Swinger;

public class AltilimityBootstrap {
	
	private static SplashScreen splash;
	private static Thread barThread;
	
	
	private static final LauncherInfos A_B_INFOS = new LauncherInfos("Altilimity", "fr.loockeeer.altilimity.launcher.LauncherFrame");
	private static final File A_DIR = GameDir.createGameDir("Altilimity");
	private static final LauncherClasspath A_B_CP = new LauncherClasspath(new File(A_DIR, "Launcher/launcher.jar"), new File(A_DIR, "Launcher/libs/"));
	private static ErrorUtil errorUtil = new ErrorUtil(new File(A_DIR, "/Launcher/crashs/"));
	

	
	public static void main(String[] args) {
		Swinger.setResourcePath("/fr/loockeeer/altilimity/bootstrap/resources/");
		displaySplash();

	    
		try {
			doUpdate();
		}catch (Exception e) {
			errorUtil.catchError(e, "Impossible de mettre a jour le launcher.");
			barThread.interrupt();
		}
		try {
			launchLauncher();
		}catch (IOException e){
			errorUtil.catchError(e, "Impossible de lancer le launcher.");
			
		}

	}
	
	private static void displaySplash() {
		splash = new SplashScreen("Altilimity -- Mise a jour du launcher.", Swinger.getResource("splash.png"));
		splash.setBackground(Swinger.TRANSPARENT);
		splash.getContentPane().setBackground(Swinger.TRANSPARENT);
		WindowMover mover = new WindowMover(splash);
		splash.addMouseListener(mover);
		splash.addMouseMotionListener(mover);
		splash.setVisible(true);
		splash.setIconImage(Swinger.getResource("logo.png"));


	
		}
	

	private static void doUpdate() throws Exception {
		SUpdate su = new SUpdate("http://62.4.23.118/Altilimity/LauncherMAJS/launcher/", new File(A_DIR, "Launcher"));
		su.getServerRequester().setRewriteEnabled(true);
		
		barThread = new Thread() {
			@Override
			public void run() {
				while (!this.isInterrupted()) {

				}
				
				
			}
			
		};
		barThread.start();
		su.start();
		barThread.interrupt();
	}
	
	
	private static void launchLauncher() throws IOException{
		Bootstrap bootstrap = new Bootstrap(A_B_CP, A_B_INFOS);
		Process p = bootstrap.launch();
		splash.setVisible(false);
		try {
			p.waitFor();
		}catch (InterruptedException e) {
			
			
		}
		System.exit(0);
		
	}
}
