package fr.loockeeer.altilimity.launcher;

import javax.swing.JFrame;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame {

	private static LauncherFrame instance;
	private LauncherPanel launcherPanel;

	
	public LauncherFrame() {
		this.setTitle("Altilimity -- Launcher");
		this.setSize(980, 625);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setUndecorated(false);
		this.setResizable(false);
		this.setContentPane(launcherPanel = new LauncherPanel());
		this.setIconImage(Swinger.getResource("logo.png"));

		
		WindowMover mover = new WindowMover(this);
		this.setVisible(true);
		this.addMouseListener(mover);
		this.addMouseMotionListener(mover);
	}
	
	public static void main (String[] args) {
	
		Swinger.setSystemLookNFeel();
		Swinger.setResourcePath("/fr/loockeeer/altilimity/launcher/ressources");
		Launcher.A_CRASHS_DIR.mkdirs();
		
		instance = new LauncherFrame();
	}
	
	public static LauncherFrame getInstance() {
		
		return instance;
		
	}
	
	
	public LauncherPanel getLauncherPanel() {
		return this.launcherPanel;
	
	}
}


