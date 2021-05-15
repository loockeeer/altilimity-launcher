package fr.loockeeer.altilimity.launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;


@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener{
	
	private Image background = Swinger.getResource("background.png");
	
	private JTextField usernameField = new JTextField();
	private JTextField passwordField = new JPasswordField();
	private STexturedButton playButton = new STexturedButton(Swinger.getResource("play.png"));
	private STexturedButton discordButton = new STexturedButton(Swinger.getResource("discord.png"));
	private SColoredBar progressBar = new SColoredBar(Swinger.getTransparentWhite(100), Swinger.getTransparentWhite(175));
	private JLabel infoLabel = new JLabel("Clique sur jouer !", SwingConstants.CENTER);
	
	public LauncherPanel() {
		this.setLayout(null);
		usernameField.setBounds(315, 290, 385, 30);
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setForeground(Color.WHITE);
		usernameField.setFont(usernameField.getFont().deriveFont(25F));
		usernameField.setCaretColor(Color.WHITE);
		this.add(usernameField);
		
		passwordField.setBounds(315, 400, 360, 40);
		passwordField.setOpaque(false);
		passwordField.setBorder(null);
		passwordField.setForeground(Color.WHITE);
		passwordField.setFont(usernameField.getFont());
		passwordField.setCaretColor(Color.WHITE);
		this.add(passwordField);
		
		playButton.setBounds(337, 480, 335, 59);
		playButton.addEventListener(this);
		this.add(playButton);
		
	  	discordButton.setBounds(10, 300, 100, 100);
		discordButton.addEventListener(this);
		this.add(discordButton);

		progressBar.setBounds(0, 568, 974, 624);
		this.add(progressBar);
		
		infoLabel.setBounds(10, 450, 974, 200);
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(usernameField.getFont());
		this.add(infoLabel);
		

	}
	
	@Override
	public void onEvent (SwingerEvent e) {
		
		if (e.getSource() == playButton) {
			System.out.println("playButton is clicked.");
			
			setFieldsEnabled(false);
		
			if (usernameField.getText().replaceAll(" ", "").length() == 0) {
				JOptionPane.showMessageDialog(this, "ERREUR : Veuilliez entrer un pseudo valide.", "ERREUR", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
					
						Launcher.auth(usernameField.getText(), passwordField.getText());
					} catch (AuthenticationException e) {
						
						JOptionPane.showMessageDialog(LauncherPanel.this, "ERREUR : Impossible de se connecter à la base de donnée de MOJANG, verifiez vos identifiants. (" + e.getErrorModel().getErrorMessage() + " )", "ERREUR", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}

					try {
						
						Launcher.update();
					} catch (Exception e) {
						Launcher.interruptThread();
						Launcher.getErrorUtil().catchError(e, "Impossible de mettre a jour le launcher !");
						LauncherFrame.getInstance().getLauncherPanel().changeInfoText("Clique sur jouer !");
						setFieldsEnabled(true);
						return;
					}
					
					try {
						
						Launcher.launcher();
					} catch (IOException e) {
						Launcher.getErrorUtil().catchError(e, "Impossible de lancer le jeu !");
						LauncherFrame.getInstance().getLauncherPanel().changeInfoText("Clique sur jouer !");
						setFieldsEnabled(true);
						return;
					}
				}
			};
			t.start();
			
			
		}
		
		if (e.getSource() == discordButton) {
			System.out.println("discordButton is clicked.");
			String url_open = "https://discord.gg/dDQUC6m";
			try {
				java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
			
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	private void setFieldsEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		playButton.setEnabled(enabled);
	}
	
	public SColoredBar getProgressBar() {
		return progressBar;
	}
	
	public void changeInfoText(String text) {
		infoLabel.setText(text);
	};
}
	
