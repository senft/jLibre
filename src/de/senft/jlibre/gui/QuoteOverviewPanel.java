package de.senft.jlibre.gui;

import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.senft.jlibre.model.Author;
import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.LibreCollection;
import de.senft.jlibre.model.Quote;

public class QuoteOverviewPanel extends OverviewPanel {

	private LibreCollection collection;
	private List<Quote> quotes;

	public QuoteOverviewPanel(LibreCollection collection) {
		super();
		this.quotes = collection.getQuotes();
		this.collection = collection;
		tableModel = new QuoteTableModel(quotes);
		table.setModel(tableModel);
	}

	@Override
	protected ListSelectionListener makeListSelectionListener() {
		return new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				StringBuilder sb = new StringBuilder(512);

				if (hasSelectedOne()) {

					Quote quote = getSelected();

					String text = quote.getText();
					String comment = quote.getComment();
					Book book = quote.getBook();
					Author author = book.getAuthor();

					sb.append("<p><font size=6>\"").append(text)
							.append("\"</font>");

					sb.append("</p><div align=\"right\">");
					sb.append(author.toString()).append(": <i>")
							.append(book.getTitle()).append("</i>, ")
							.append(book.getPublicationYear()).append("</div>");

					if (comment != null) {
						sb.append(
								"<br><br><b>Comment:</b><br /><div style=\"margin-left: "
										+ INFOPANEL_LINEFEED + "px;\">")
								.append(quote.getComment()).append("</div>");
					}

					infoPane.setText(sb.toString());
				} else {
					infoPane.setText("");
				}
			}
		};
	}

	@Override
	protected void addNew() {
		Quote quote = new Quote();
		QuoteDialog dialog = new QuoteDialog(quote, collection.getBooks());
		if (dialog.showDialog()) {
			quotes.add(quote);
			tableModel.fireTableDataChanged();
		}
	}

	@Override
	protected boolean deleteSelected() {
		return false;
	}

	@Override
	protected void editSelected() {
		Quote oldQuote = this.getSelected();
		QuoteDialog dialog = new QuoteDialog(oldQuote, collection.getBooks());

		if (dialog.showDialog()) {
			tableModel.fireTableDataChanged();
		}
	}

	/**
	 * Fetches the complete Quote object, of the currently selected quote, from
	 * the DB.
	 * 
	 * @return the quote currently selected in the table
	 */
	protected Quote getSelected() {
		int selectedRow = table.getSelectedRow();
		return quotes.get(selectedRow);
	}

	@Override
	public void updateData() {
		// rowData = dbHandler.getQuotesForTable();
	}
}