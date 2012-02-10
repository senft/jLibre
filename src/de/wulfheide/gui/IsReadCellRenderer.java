package de.wulfheide.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import de.wulfheide.model.Book;

public class IsReadCellRenderer implements TableCellRenderer {

	// This method is called each time a cell in a column
	// using this renderer needs to be rendered.
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

		JLabel label = new JLabel();

		int isRead = Integer.valueOf((Integer) value);

		label.setHorizontalAlignment(SwingConstants.CENTER);

		if (isSelected) {
			label.setOpaque(true);
			label.setBackground(table.getSelectionBackground());
			label.setForeground(table.getSelectionForeground());
		}

		if (isRead == Book.NOT_READ)
			label.setIcon(new ImageIcon(MainWindow.class
					.getResource("/images/not-read.png")));
		else if (isRead == Book.STARTED_READING)
			label.setIcon(new ImageIcon(MainWindow.class
					.getResource("/images/started.png")));
		else
			label.setIcon(new ImageIcon(MainWindow.class
					.getResource("/images/read.png")));

		return label;
	}
}
