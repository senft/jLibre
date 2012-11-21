package de.senft.jlibre.gui;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import de.senft.jlibre.model.Book;

public class BookTableModel extends AbstractTableModel {

	private static final int COLUMN_ID = 0;
	private static final int COLUMN_TITLE = 1;
	private static final int COLUMN_AUTHOR = 2;
	private static final int COLUMN_PUBYEAR = 3;
	private static final int COLUMN_EPOCHE = 4;
	private static final int COLUMN_GENRE = 5;
	private static final int COLUMN_STATUS = 6;

	List<Book> books = null;
	private String[] columnNames = new String[] { "ID", "Title", "Author",
			"Year of publication", "Epoche", "Genre", "Status" };

	@SuppressWarnings("rawtypes")
	private Class[] columnClasses = new Class[] { Integer.class, String.class,
			String.class, Integer.class, String.class, String.class,
			ImageIcon.class };

	public BookTableModel(List<Book> books) {
		this.books = books;
	}

	@Override
	public int getRowCount() {
		return books.size();
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	public Book getBookAt(int rowIndex) {
		return books.get(rowIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Book book = books.get(rowIndex);

		switch (columnIndex) {
		case COLUMN_ID:
			return book.getId();
		case COLUMN_TITLE:
			return book.getTitle();
		case COLUMN_AUTHOR:
			return book.getAuthor();
		case COLUMN_PUBYEAR:
			return book.getPublicationYear();
		case COLUMN_EPOCHE:
			return book.getEpoche();
		case COLUMN_GENRE:
			return book.getGenre();
		case COLUMN_STATUS:
			return book.isRead();
		default:
			return "<unknown>";
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}
}
