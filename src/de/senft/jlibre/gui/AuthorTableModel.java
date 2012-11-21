package de.senft.jlibre.gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.senft.jlibre.model.Author;

public class AuthorTableModel extends AbstractTableModel {

	public static final int COLUMN_FIRSTNAME = 0;
	public static final int COLUMN_LASTNAME = 1;
	public static final int COLUMN_BORN = 2;
	public static final int COLUMN_DIED = 3;
	public static final int COLUMN_COUNTRY = 4;

	List<Author> authors = null;

	private String[] columnNames = new String[] { "Firstname", "Lastname",
			"Born", "Died", "Country" };

	@SuppressWarnings("rawtypes")
	private Class[] columnClasses = new Class[] { String.class,
			String.class, Integer.class, Integer.class, String.class };

	public AuthorTableModel(List<Author> books) {
		this.authors = books;
	}

	@Override
	public int getRowCount() {
		return authors.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Author author = authors.get(rowIndex);

		switch (columnIndex) {
		case COLUMN_FIRSTNAME:
			return author.getFirstname();
		case COLUMN_LASTNAME:
			return author.getLastname();
		case COLUMN_BORN:
			return author.getBorn();
		case COLUMN_DIED:
			return author.getDied();
		case COLUMN_COUNTRY:
			return author.getCountry();
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
