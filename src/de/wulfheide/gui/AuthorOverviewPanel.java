package de.wulfheide.gui;

import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import de.wulfheide.model.Author;
import de.wulfheide.model.Book;
import de.wulfheide.model.Quote;

public class AuthorOverviewPanel extends OverviewPanel {

	public AuthorOverviewPanel() {
		columnNames.add("ID");
		columnNames.add("Firstname");
		columnNames.add("Lastname");
		columnNames.add("Born");
		columnNames.add("Died");
		columnNames.add("Country");

		columnClasses = new Class[] { Integer.class, String.class,
				String.class, Integer.class, Integer.class, String.class };

		tableModel.fireTableStructureChanged();
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setMaxWidth(75);

		// TODO What the fuck is this shit? There has got to be an easier way!
		table.getColumnModel().getColumn(0)
				.setCellRenderer(new DefaultTableCellRenderer() {
				});
		((DefaultTableCellRenderer) table.getColumnModel().getColumn(0)
				.getCellRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

		rowData = dbHandler.getAuthorsForTable();
	}

	@Override
	protected ListSelectionListener makeListSelectionListener() {
		return new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				StringBuilder sb = new StringBuilder(512);
				
				if (hasSelectedOne()) {

					Author author = getSelected();

					String firstname = author.getFirstname();
					String lastname = author.getLastname();
					Set<Book> books = author.getBooks();
					Set<Quote> quotes = author.getQuotes();

					sb.append("<html><B>Name:</B><br>")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;")
							.append(firstname).append(" ").append(lastname);

					sb.append("<p><B>Books from this author:</B><br>");
					if (books.isEmpty())
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;-");
					else {
						sb.append("<ul>");
						for (Book b : books) {
							sb.append("<li>\"").append(b.getTitle())
									.append("\", ")
									.append(b.getPublicationYear())
									.append("</li>");
						}
						sb.append("</ul>");
					}
					sb.append("</p>");

					sb.append("<p><B>Quotes from this author:</B><br>");
					if (quotes.isEmpty())
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;-");
					else {
						sb.append("<ul>");
						for (Quote q : quotes) {
							sb.append("<li><i>\"").append(q.getText())
									.append("\"</i></li>");
						}
						sb.append("</ul>");
					}
					sb.append("</p>");

					sb.append("</html>");

					infoPane.setText(sb.toString());
				} else {
					infoPane.setText("");
				}
			}
		};
	}

	@Override
	protected void addNew() {
		AuthorDialog dialog = new AuthorDialog();
		Author author = dialog.showDialog();

		if (author != null) {

			int id = dbHandler.makeAuthor(author);

			if (id != -1) {
				Vector<Object> vecAuthor = new Vector<Object>();
				vecAuthor.add(id);
				vecAuthor.add(author.getFirstname());
				vecAuthor.add(author.getLastname());
				vecAuthor.add(author.getBorn());
				vecAuthor.add(author.getDied());
				vecAuthor.add(author.getCountry());
				rowData.add(vecAuthor);

				tableModel.fireTableDataChanged();
				int newRow = rowData.size() - 1;
				table.getSelectionModel().setSelectionInterval(newRow, newRow);
			} else {
				JOptionPane.showMessageDialog(this,
						"Could not add author to database.", "Database error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public boolean editSelected() {
		Author oldAuthor = this.getSelected();

		AuthorDialog dialog = new AuthorDialog(oldAuthor.getId(),
				oldAuthor.getFirstname(), oldAuthor.getLastname(),
				oldAuthor.getCountry(), oldAuthor.getBorn(),
				oldAuthor.getDied());
		Author newAuthor = dialog.showDialog();

		if (newAuthor != null) {

			newAuthor.setId(oldAuthor.getId()); // Set new authors ID to old
												// authors ID, so we can
												// overwrite

			boolean success = dbHandler.updateAuthor(newAuthor);

			if (success) {
				// Publish changes to table
				int selectedRow = table.getSelectedRow();

				Vector<Object> vecAuthor = new Vector<Object>();
				vecAuthor.add(newAuthor.getId());
				vecAuthor.add(newAuthor.getFirstname());
				vecAuthor.add(newAuthor.getLastname());
				vecAuthor.add(newAuthor.getBorn());
				vecAuthor.add(newAuthor.getDied());
				vecAuthor.add(newAuthor.getCountry());

				rowData.set(selectedRow, vecAuthor);

				tableModel.fireTableDataChanged();
				table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
			} else {
				JOptionPane.showMessageDialog(this, "Could not edit author.",
						"Database error", JOptionPane.ERROR_MESSAGE);
			}
		}
		// TODO return something real here, or even return the index that has
		// been updated
		return false;
	}

	@Override
	protected void deleteSelected() {
	}

	/**
	 * Fetches the complete author object, of the currently selected author,
	 * from the DB.
	 * 
	 * @return the author currently selected in the table
	 */
	protected Author getSelected() {
		int selectedRow = table.getSelectedRow();
		int id = Integer.parseInt(table.getModel().getValueAt(selectedRow, 0)
				.toString()); // Column 0 is id
		return dbHandler.getAuthor(id);
	}

}
