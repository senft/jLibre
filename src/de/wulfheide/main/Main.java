package de.wulfheide.main;

import java.awt.EventQueue;

import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.wulfheide.gui.MainWindow;

public class Main {

	private static Logger logger = Logger.getLogger("Main");

	private MainWindow mainWindow;

	public Main() {
		logger.info("Started application...");

		mainWindow = new MainWindow();

		mainWindow.setVisible(true);
		logger.info("Succesfully initialized all vital components...");
	}

	public static void main(String args[]) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			logger.error("Couldnt set LAF to \"com.sun.java.swing.plaf.gtk.GTKLookAndFeel\"");
		}

		PropertyConfigurator.configureAndWatch("log4j.properties");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Main();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
