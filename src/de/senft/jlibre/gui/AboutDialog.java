package de.senft.jlibre.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class AboutDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutDialog dialog = new AboutDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblJkibre = new JLabel("jLibre");
		lblJkibre.setFont(new Font("Dialog", Font.BOLD, 14));
		contentPanel.add(lblJkibre, BorderLayout.NORTH);

		JTextPane lblVersionBuild = new JTextPane();
		lblVersionBuild.setContentType("text/html");
		lblVersionBuild
				.setText("Version: dev\n<p>\n(c) senft 20011.  All rights reserved.\n</p>\n<ul>\n<li>HSQLDB - <a href=\"http://hsqldb.org/\">http://hsqldb.org/</a></li>\n<li>log4j - <a href=\"http://logging.apache.org/log4j/\">http://logging.apache.org/log4j/</a></li>\n<li>jCalendar - <a href=\"http://www.toedter.com/en/jcalendar/\">http://www.toedter.com/en/jcalendar/</a></li></ul>\n<p>Some icons taken from the <a href=\"http://tango.freedesktop.org/Tango_Desktop_Project\">Tango Desktop Projekt</a>.</p>");
		lblVersionBuild.setBackground(UIManager.getColor("window"));
		lblVersionBuild.setEditable(false);
		lblVersionBuild.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPanel.add(lblVersionBuild, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
