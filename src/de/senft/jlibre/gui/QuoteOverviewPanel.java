package de.senft.jlibre.gui;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import de.senft.jlibre.model.Author;
import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.Quote;

public class QuoteOverviewPanel extends OverviewPanel {


	public QuoteOverviewPanel(List<Quote> quotes) {
		super();
		this.quotes = quotes;
		tableModel = new QuoteTableModel(quotes);
		table.setModel(tableModel);

		// tableModel.fireTableStructureChanged();
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setMaxWidth(75);

		// TODO What the fuck is this shit? There has got to be an easier way!
		table.getColumnModel().getColumn(0)
				.setCellRenderer(new DefaultTableCellRenderer() {
				});
		((DefaultTableCellRenderer) table.getColumnModel().getColumn(0)
				.getCellRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
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
								"<br><br><b>Comment:</b><br /><div style=\"margin-left: " + INFOPANEL_LINEFEED + "px;\">")
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
	protected boolean addNew() {
		boolean success = false;
		QuoteDialog dialog = new QuoteDialog();
		Quote quote = dialog.showDialog();

		if (quote != null) {
			int id = dbHandler.makeQuote(quote);

			if (id != -1) {
				Vector<Object> vecQuote = new Vector<Object>();
				vecQuote.add(id);
				vecQuote.add(quote.getText());
				vecQuote.add(quote.getBook().getTitle());
				vecQuote.add(quote.getBook().getAuthor().toString());
				rowData.add(vecQuote);

				tableModel.fireTableDataChanged();
				int newRow = rowData.size() - 1;
				table.getSelectionModel().setSelectionInterval(newRow, newRow);

				success = true;
			} else {
				JOptionPane.showMessageDialog(this,
						"Could not add the quote to the database.",
						"Database error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return success;
	}

	@Override
	protected boolean deleteSelected() {
		return false;
	}

	@Override
	protected boolean editSelected() {
		boolean success = false;
		Quote oldQuote = this.getSelected();

		QuoteDialog dialog = new QuoteDialog(oldQuote.getId(),
				oldQuote.getText(), oldQuote.getComment(), oldQuote.getBook());
		Quote newQuote = dialog.showDialog();

		if (newQuote != null) {
			newQuote.setId(oldQuote.getId()); // Set new authors ID to old
												// authors ID, so we can
												// overwrite

			boolean dataChanged = dbHandler.updateQuote(newQuote);

			if (dataChanged) {
				// Publish changes to table
				int selectedRow = table.getSelectedRow();

				Vector<Object> vecQuote = new Vector<Object>();
				vecQuote.add(newQuote.getId());
				vecQuote.add(newQuote.getText());
				vecQuote.add(newQuote.getBook().getTitle());
				vecQuote.add(newQuote.getBook().getAuthor().toString());

				rowData.set(selectedRow, vecQuote);

				tableModel.fireTableDataChanged();
				table.getSelectionModel().setSelectionInterval(selectedRow,
						selectedRow);
			} else {
				JOptionPane.showMessageDialog(this,
						"Couldn't update the quote. "
								+ "No data has been changed.",
						"Database error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return success;
	}

	/**
	 * Fetches the complete Quote object, of the currently selected quote, from
	 * the DB.
	 * 
	 * @return the quote currently selected in the table
	 */
	protected Quote getSelected() {
		int selectedRow = table.getSelectedRow();
		int id = Integer.parseInt(table.getModel().getValueAt(selectedRow, 0)
				.toString()); // Column 0 is id
		return dbHandler.getQuote(id);
	}

	@Override
	public void updateData() {
		rowData = dbHandler.getQuotesForTable();
	}
}