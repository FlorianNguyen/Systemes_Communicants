package Menus;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JSlider;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Box;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Label;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;


public class Option {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void optionDisplay(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Option window = new Option();
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
	public Option() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//    http://fr.openclassrooms.com/forum/sujet/jouer-de-la-musique-96546
		//Audio son = new Audio();
		//son.start();
		frame = new JFrame();
		frame.setBounds(0, 0, 1368, 728);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 3, 2, 2));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		frame.getContentPane().add(panel_1);
		
		JPanel panel = new JPanel();
		panel.setForeground(Color.ORANGE);
		panel.setBackground(Color.GRAY);
		frame.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{20, 0, 20, 0, 50, 0};
		gbl_panel.rowHeights = new int[]{150, 0, 50, 0, 50, 0, 50, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		final JSlider slider = new JSlider();
		slider.setValue(MenuLauncher.volumeMusique);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int value = slider.getValue();
				System.out.println("slider haut: " + value);
				MenuLauncher.volumeMusique = value;
			}
		});
		
		Label label = new Label("Settings");
		label.setForeground(Color.ORANGE);
		label.setFont(new Font("Swis721 Blk BT", Font.PLAIN, 12));
		label.setAlignment(Label.CENTER);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.VERTICAL;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 3;
		gbc_label.gridy = 1;
		panel.add(label, gbc_label);
		
		Label label_1 = new Label("Musique");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 1;
		gbc_label_1.gridy = 3;
		panel.add(label_1, gbc_label_1);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 3;
		gbc_slider.gridy = 3;
		panel.add(slider, gbc_slider);
		
		final JSlider slider_1 = new JSlider();
		slider_1.setValue(MenuLauncher.volumeEffetSonores);
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int value_1 = slider_1.getValue();
				System.out.println("slider bas: " + value_1);
				MenuLauncher.volumeEffetSonores = value_1;
			}
		});
		
		Label label_2 = new Label("Effets sonores");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 1;
		gbc_label_2.gridy = 5;
		panel.add(label_2, gbc_label_2);
		GridBagConstraints gbc_slider_1 = new GridBagConstraints();
		gbc_slider_1.insets = new Insets(0, 0, 5, 5);
		gbc_slider_1.gridx = 3;
		gbc_slider_1.gridy = 5;
		panel.add(slider_1, gbc_slider_1);
		
		JButton btnSaveLeave = new JButton("Save & leave option");
		btnSaveLeave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			
				System.out.println("Settings saved!");
				frame.setVisible(false);
				frame.dispose();
				PhoenixMenu.PhoenixMenuDisplay();
			}
		});
		GridBagConstraints gbc_btnSaveLeave = new GridBagConstraints();
		gbc_btnSaveLeave.fill = GridBagConstraints.VERTICAL;
		gbc_btnSaveLeave.insets = new Insets(0, 0, 5, 5);
		gbc_btnSaveLeave.gridx = 3;
		gbc_btnSaveLeave.gridy = 7;
		panel.add(btnSaveLeave, gbc_btnSaveLeave);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		frame.getContentPane().add(panel_2);
	}

}

