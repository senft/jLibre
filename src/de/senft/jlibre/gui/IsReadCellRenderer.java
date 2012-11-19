package de.senft.jlibre.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import de.senft.jlibre.model.Book;

/**
 * A TableCellRenderer that renders exactly one icon to the cell.
 * 
 * @author jln
 * 
 */
public class IsReadCellRenderer implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,

	boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
		JLabel label = new JLabel();
		int isRead;

		try {
			isRead = Integer.valueOf((Integer) value);
		} catch (NumberFormatException e) {
			isRead = -1;
		}

		label.setHorizontalAlignment(SwingConstants.CENTER);

		if (isSelected) {
			label.setOpaque(true);
			label.setBackground(table.getSelectionBackground());
			label.setForeground(table.getSelectionForeground());
		}

		switch (isRead) {
		case Book.NOT_READ:
			label.setIcon(new ImageIcon(MainWindow.class
					.getResource("/images/not-read.png")));
			break;
		case Book.STARTED_READING:
			label.setIcon(new ImageIcon(MainWindow.class
					.getResource("/images/started.png")));
			break;
		case Book.FINISHED_READING:
			label.setIcon(new ImageIcon(MainWindow.class
					.getResource("/images/read.png")));
			break;
		default:
			label.setText("unknown");
		}

		return label;
	}
}
