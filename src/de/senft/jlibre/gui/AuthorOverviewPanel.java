package de.senft.jlibre.gui;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import de.senft.jlibre.model.Author;
import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.Quote;

public class AuthorOverviewPanel extends OverviewPanel {

	private List<Author> authors;

	public AuthorOverviewPanel(List<Author> authors) {
		super();
		this.authors = authors;
		tableModel = new AuthorTableModel(authors);
		table.setModel(tableModel);

		tableModel.fireTableStructureChanged();
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setMaxWidth(75);

		// TODO What the fuck is this shit? There has got to be an easier way!
		table.getColumnModel().getColumn(0)
				.setCellRenderer(new DefaultTableCellRenderer() {
				});
		((DefaultTableCellRenderer) table.getColumnModel().getColumn(0)
				.getCellRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

		updateData();
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
							.append("<div style=\"margin-left: "
									+ INFOPANEL_LINEFEED + "px;\">")
							.append(firstname).append(" ").append(lastname)
							.append("</div>");

					sb.append("<p><B>Books from this author:</B><br>");
					if (books.isEmpty())
						sb.append("<div style=\"margin-left: "
								+ INFOPANEL_LINEFEED + "px;\">-</div>");
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
						sb.append("<div style=\"margin-left: "
								+ INFOPANEL_LINEFEED + "px;\">-</div>");
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
	protected boolean addNew() {
		boolean success = false;
		AuthorDialog dialog = new AuthorDialog();
		Author author = dialog.showDialog();

		if (author != null) {
			// int id = dbHandler.makeAuthor(author);

			authors.add(author);
			
			tableModel.fireTableDataChanged();
//			int newRow = rowData.size() - 1;
//			table.getSelectionModel().setSelectionInterval(newRow, newRow);

			success = true;
		}
		return success;
	}

	public boolean editSelected() {
		boolean success = false;
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

			// dbHandler.updateAuthor(newAuthor);

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
			table.getSelectionModel().setSelectionInterval(selectedRow,
					selectedRow);

			success = true;

		}
		return success;
	}

	@Override
	protected boolean deleteSelected() {
		boolean success = false;
		int choice = JOptionPane.showOptionDialog(this,
				"This will also delete all books and quotes by this authors.\n"
						+ "Do you want to continue?", "Continue?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				new String[] { "No", "Yes" }, null);

		if (choice == 1) { // User clicked "Yes"
			// dbHandler.delete(getSelected());

			int selectedRow = table.getSelectedRow();
			rowData.remove(selectedRow);
			tableModel.fireTableDataChanged();
			success = true;
		}
		return success;
	}

	/**
	 * Fetches the complete author object, of the currently selected author,
	 * from the DB.
	 * 
	 * @return the author currently selected in the table
	 */
	protected Author getSelected() {
		int selectedRow = table.getSelectedRow();
		return authors.get(selectedRow);
	}

	@Override
	public void updateData() {
		// tableModel.sdbHandler.getAuthors();
	}
}
