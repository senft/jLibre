import java.awt.EventQueue;

import org.apache.log4j.PropertyConfigurator;

import de.senft.jlibre.gui.MainWindow;

public class Main {

	public Main() {
		MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}

	public static void main(String args[]) {
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