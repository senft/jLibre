package de.senft.jlibre.gui;

import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.senft.jlibre.model.Author;
import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.LibreCollection;
import de.senft.jlibre.model.Quote;

public class AuthorOverviewPanel extends OverviewPanel {

	private List<Author> authors;

	public AuthorOverviewPanel(LibreCollection collection) {
		super();
		this.authors = collection.getAuthors();
		tableModel = new AuthorTableModel(authors);
		table.setModel(tableModel);

		tableModel.fireTableStructureChanged();
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
	protected void addNew() {
		Author author = new Author();
		AuthorDialog dialog = new AuthorDialog(author);

		if (dialog.showDialog()) {
			authors.add(author);
			tableModel.fireTableDataChanged();
		}
		int lastRow = table.getRowCount() - 1;
		table.getSelectionModel().setSelectionInterval(lastRow, lastRow);
	}

	public void editSelected() {
		int selectedIndex = table.getSelectedRow();
		Author oldAuthor = this.getSelected();
		AuthorDialog dialog = new AuthorDialog(oldAuthor);

		if (dialog.showDialog()) {
			tableModel.fireTableDataChanged();
		}
		table.getSelectionModel().setSelectionInterval(selectedIndex,
				selectedIndex);
	}

	@Override
	protected void deleteSelected() {
		int choice = JOptionPane.showOptionDialog(this,
				"This will also delete all books and quotes by this authors.\n"
						+ "Do you want to continue?", "Continue?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				new String[] { "No", "Yes" }, null);

		if (choice == 1) { // User clicked "Yes"
			int selectedRow = table.getSelectedRow();
			authors.remove(selectedRow);
			tableModel.fireTableDataChanged();
		}
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
}
