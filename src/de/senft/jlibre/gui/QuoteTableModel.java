package de.senft.jlibre.gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.senft.jlibre.model.Quote;

public class QuoteTableModel extends AbstractTableModel {

	private static final int COLUMN_ID = 0;
	private static final int COLUMN_TEXT = 1;
	private static final int COLUMN_BOOK = 2;
	private static final int COLUMN_AUTHOR = 3;

	List<Quote> quotes = null;
	private String[] columnNames = new String[] { "ID", "Text", "Book",
			"Author" };

	@SuppressWarnings("rawtypes")
	private Class[] columnClasses = new Class[] { Integer.class, String.class,
			String.class, String.class };

	public QuoteTableModel(List<Quote> books) {
		this.quotes = books;
	}

	@Override
	public int getRowCount() {
		return quotes.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Quote quote = quotes.get(rowIndex);

		switch (columnIndex) {
		case COLUMN_ID:
			return quote.getId();
		case COLUMN_TEXT:
			return quote.getText();
		case COLUMN_BOOK:
			return quote.getBook();
		case COLUMN_AUTHOR:
			return quote.getBook().getAuthor();
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
