package Menus;

import java.awt.EventQueue;
import game.BackgroundDisplay_etienne;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Label;
import java.awt.Panel;


public class PhoenixMenu {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	//public static void main(String[] args) { 
	public static void PhoenixMenuDisplay(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PhoenixMenu window = new PhoenixMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PhoenixMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setBounds(0, 0, 1368, 728);//gère la taille du Jframe setbounds(position de la fenetreX,position de la fenetre Y, tailleX, taille Y)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);


		JPanel panel = new JPanel();

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.gridwidth = 3;
		gbc_panel.gridheight = 9;
		gbc_panel.gridx = 4;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{65, 63, 83, 81, 0};
		gbl_panel.rowHeights = new int[]{125, 0, 100, 0, 50, 0, 50, 0, 50, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);





		//boutton solo
		JButton btnModeSolo = new JButton("mode solo");
		GridBagConstraints gbc_btnModeSolo = new GridBagConstraints();
		gbc_btnModeSolo.insets = new Insets(0, 0, 5, 5);
		gbc_btnModeSolo.anchor = GridBagConstraints.NORTH;
		gbc_btnModeSolo.gridx = 2;
		gbc_btnModeSolo.gridy = 3;
		panel.add(btnModeSolo, gbc_btnModeSolo);
		btnModeSolo.setHorizontalAlignment(SwingConstants.RIGHT);


		//boutton multi
		JButton btnModeMulti = new JButton("mode multi");
		GridBagConstraints gbc_btnModeMulti = new GridBagConstraints();
		gbc_btnModeMulti.anchor = GridBagConstraints.NORTH;
		gbc_btnModeMulti.insets = new Insets(0, 0, 5, 5);
		gbc_btnModeMulti.gridx = 2;
		gbc_btnModeMulti.gridy = 5;
		panel.add(btnModeMulti, gbc_btnModeMulti);

		//boutton option
		JButton btnOption = new JButton("option");
		btnOption.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("Welcome to Option");
				frame.setVisible(false);
				frame.dispose();
				//				System.exit(0);    // ici system.exit(0) ne fonctionne pas car on se ressert du jframe par la suite
				Option.optionDisplay();

			}
		});
		GridBagConstraints gbc_btnOption = new GridBagConstraints();
		gbc_btnOption.anchor = GridBagConstraints.NORTH;
		gbc_btnOption.insets = new Insets(0, 0, 5, 5);
		gbc_btnOption.gridx = 2;
		gbc_btnOption.gridy = 7;
		panel.add(btnOption, gbc_btnOption);


		//bouton quitter
		JButton btnQuitter = new JButton("quitter");
		GridBagConstraints gbc_btnQuitter = new GridBagConstraints();
		gbc_btnQuitter.anchor = GridBagConstraints.NORTH;
		gbc_btnQuitter.insets = new Insets(0, 0, 0, 5);
		gbc_btnQuitter.gridx = 2;
		gbc_btnQuitter.gridy = 9;
		panel.add(btnQuitter, gbc_btnQuitter);
		btnQuitter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.print("Hasta la vista");
				//			    frame.setVisible(false);
				//              frame.dispose();
				System.exit(0);   // manière plus propre de quitter le jframe (comparé au deux lignes ci-dessus)
			}
		});

		//JLabel Phoenix
		JLabel lblPhoenixtheGame = new JLabel("Phoenix - A game for the best !");
		GridBagConstraints gbc_lblPhoenixtheGame = new GridBagConstraints();
		gbc_lblPhoenixtheGame.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhoenixtheGame.gridx = 2;
		gbc_lblPhoenixtheGame.gridy = 1;
		panel.add(lblPhoenixtheGame, gbc_lblPhoenixtheGame);
		lblPhoenixtheGame.setForeground(Color.ORANGE);
		lblPhoenixtheGame.setHorizontalAlignment(SwingConstants.CENTER);


		JButton[] otherframe = new JButton[4];
		otherframe[0]= btnQuitter;
		otherframe[1] = btnOption;
		otherframe[2] = btnModeMulti;
		otherframe[3] = btnModeSolo;

		/////////////
		BackgroundDisplay_etienne animatedBackground = new BackgroundDisplay_etienne(frame,otherframe,lblPhoenixtheGame,"tile-a0.png");
		GridBagConstraints gbc_animatedBackground = new GridBagConstraints();
		gbc_animatedBackground.insets = new Insets(0, 0, 5, 5);
		gbc_animatedBackground.gridx = 0;
		gbc_animatedBackground.gridy = 0;
		panel.add(animatedBackground, gbc_animatedBackground);

	}

}
