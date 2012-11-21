package de.senft.jlibre.gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.LibreCollection;
import de.senft.jlibre.model.Quote;

public class BookOverviewPanel extends OverviewPanel {

	private List<Book> books;
	private LibreCollection collection;

	public BookOverviewPanel(LibreCollection collection) {
		super();
		this.books = collection.getBooks();
		this.collection = collection;
		tableModel = new BookTableModel(books);
		table.setModel(tableModel);

		table.setFillsViewportHeight(true);
		table.setRowSorter(new TableRowSorter<TableModel>(table.getModel()));
		table.getSelectionModel().addListSelectionListener(
				makeListSelectionListener());

		table.getColumnModel().getColumn(BookTableModel.COLUMN_STATUS)
				.setCellRenderer(new IsReadCellRenderer());

		popupMenu = new JPopupMenu();

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Edit");
		popupMenu.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Delete");
		popupMenu.add(mntmNewMenuItem_4);

		popupMenu.add(new JSeparator());

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Mark finished today");
		popupMenu.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Mark finished on...");
		popupMenu.add(mntmNewMenuItem_3);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				checkPopup(e);
			}

			public void mouseReleased(MouseEvent e) {
				checkPopup(e);
			}

			private void checkPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					int clickedRow = table.rowAtPoint(new Point(e.getX(), e
							.getY()));
					table.getSelectionModel().setSelectionInterval(clickedRow,
							clickedRow);

					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});
	}

	@Override
	protected ListSelectionListener makeListSelectionListener() {
		// TODO Move out!
		return new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				StringBuilder sb = new StringBuilder(512);

				if (hasSelectedOne()) {

					DateFormat df = DateFormat.getDateInstance();

					Book book = getSelected();

					String title = book.getTitle();
					String author = book.getAuthor().toString();
					int pubYear = book.getPublicationYear();
					Date startedReading = book.getStartedReading();
					Date finishedReading = book.getFinishedReading();
					Set<Quote> quotes = book.getQuotes();

					String started = "-";
					String finished = "-";

					if (startedReading != null)
						started = df.format(startedReading);

					if (finishedReading != null)
						finished = df.format(finishedReading);

					sb.append("<html><B>Title:</B><br>")
							.append("<div style=\"margin-left: "
									+ INFOPANEL_LINEFEED + "px;\">")
							.append(title).append("</div>");
					sb.append("<p><B>Author:</B><br>")
							.append("<div style=\"margin-left: "
									+ INFOPANEL_LINEFEED + "px;\">")
							.append(author).append("</div>").append("</p>");
					sb.append("<p><B>Year of publication:</B><br>")
							.append("<div style=\"margin-left: "
									+ INFOPANEL_LINEFEED + "px;\">")
							.append(pubYear).append("</div>").append("</p>");
					sb.append("<p><B>Started reading:</B><br>")
							.append("<div style=\"margin-left: "
									+ INFOPANEL_LINEFEED + "px;\">")
							.append(started).append("</div>").append("</p>");
					sb.append("<p><B>Finished reading:</B><br>")
							.append("<div style=\"margin-left: "
									+ INFOPANEL_LINEFEED + "px;\">")
							.append(finished).append("</div>").append("</p>");

					sb.append("<p><B>Quotes from this book:</B><br>");
					if (quotes.isEmpty())
						sb.append("<div style=\"margin-left: "
								+ INFOPANEL_LINEFEED + "px;\">-</div>");
					else {
						sb.append("<ul>");

						for (Quote q : quotes) {
							sb.append(
									String.format("<li><i>\"%s\"</i></li>",
											q.toString())).append("</p>");
						}
						sb.append("</ul>");
					}
					sb.append("</p>");

					sb.append("</html>");

					infoPane.setText(sb.toString());
				} else {
					infoPane.setText(" ");
				}
			}
		};
	}

	@Override
	protected void addNew() {
		Book book = new Book();
		BookDialog dialog = new BookDialog(book, collection.getAuthors());

		if (dialog.showDialog()) {
			books.add(book);
			tableModel.fireTableDataChanged();
		}
	}

	public void editSelected() {
		Book oldBook = this.getSelected();
		BookDialog dialog = new BookDialog(oldBook, collection.getAuthors());

		if (dialog.showDialog()) {
			tableModel.fireTableDataChanged();
		}
	}

	@Override
	protected boolean deleteSelected() {
		boolean success = false;
		int choice = JOptionPane.showOptionDialog(this,
				"This will also delete all quotes from this book.\n"
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
	 * Fetches the complete book object, of the currently selected book, from
	 * the DB.
	 * 
	 * @return the book currently selected in the table
	 */
	protected Book getSelected() {
		int selectedRow = table.getSelectedRow();
		return books.get(selectedRow);
	}

	@Override
	public void updateData() {
		// rowData = dbHandler.getBooksForTable();
		((AbstractTableModel) this.table.getModel())
				.fireTableStructureChanged();
		((AbstractTableModel) this.table.getModel()).fireTableDataChanged();
	}
}