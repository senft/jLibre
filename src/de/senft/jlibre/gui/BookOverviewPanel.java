package de.senft.jlibre.gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.Quote;

public class BookOverviewPanel extends OverviewPanel {

	public BookOverviewPanel() {
		columnNames.add("ID");
		columnNames.add("Title");
		columnNames.add("Author");
		columnNames.add("Year of publication");
		columnNames.add("Epoche");
		columnNames.add("Genre");
		columnNames.add("Read?");
		columnClasses = new Class[] { Integer.class, String.class,
				String.class, Integer.class, String.class, String.class,
				ImageIcon.class };

		tableModel.fireTableStructureChanged();

		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setMaxWidth(75);

		// TODO What the fuck is this shit? There has got to be an easier way!
		table.getColumnModel().getColumn(0)
				.setCellRenderer(new DefaultTableCellRenderer() {
				});
		((DefaultTableCellRenderer) table.getColumnModel().getColumn(0)
				.getCellRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

		table.getColumnModel().getColumn(6)
				.setCellRenderer(new IsReadCellRenderer());

		rowData = dbHandler.getBooksForTable();

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
	protected boolean addNew() {
		boolean success = false;
		BookDialog dialog = new BookDialog();
		Book book = dialog.showDialog();

		if (book != null) {
			int id = dbHandler.makeBook(book);

			if (id != -1) {
				Vector<Object> vecBook = new Vector<Object>();
				vecBook.add(id);
				vecBook.add(book.getTitle());
				vecBook.add(book.getAuthor().toString());
				vecBook.add(book.getPublicationYear());
				vecBook.add(book.getEpoche());
				vecBook.add(book.getGenre());
				vecBook.add(Book.datesRead(book.getStartedReading(),
						book.getFinishedReading()));
				rowData.add(vecBook);

				tableModel.fireTableDataChanged();
				int newRow = rowData.size() - 1;
				table.getSelectionModel().setSelectionInterval(newRow, newRow);

				success = true;
			} else {
				JOptionPane.showMessageDialog(this,
						"Could not add the book to the database.",
						"Database error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return success;
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
			boolean dataChanged = dbHandler.delete(getSelected());

			if (dataChanged) {
				int selectedRow = table.getSelectedRow();
				rowData.remove(selectedRow);
				tableModel.fireTableDataChanged();
				success = true;
			}
		}
		return success;
	}

	@Override
	protected boolean editSelected() {
		boolean success = false;
		Book oldBook = this.getSelected();

		BookDialog dialog = new BookDialog(oldBook.getId(), oldBook.getTitle(),
				oldBook.getAuthor(), oldBook.getStartedReading(),
				oldBook.getFinishedReading(), oldBook.getComment(),
				oldBook.getPublicationYear(), oldBook.getEpoche(),
				oldBook.getGenre());
		Book newBook = dialog.showDialog();

		if (newBook != null) {
			newBook.setId(oldBook.getId()); // Set new books ID to old books ID,
											// so we can overwrite

			boolean dataChanged = dbHandler.updateBook(newBook);

			if (dataChanged) {
				// Publish changes to table
				int selectedRow = table.getSelectedRow();

				Vector<Object> vecBook = new Vector<Object>();
				vecBook.add(newBook.getId());
				vecBook.add(newBook.getTitle());
				vecBook.add(newBook.getAuthor().toString());
				vecBook.add(newBook.getPublicationYear());
				vecBook.add(newBook.getEpoche());
				vecBook.add(newBook.getGenre());

				rowData.set(selectedRow, vecBook);

				tableModel.fireTableDataChanged();
				table.getSelectionModel().setSelectionInterval(selectedRow,
						selectedRow);

				success = true;
			} else {
				JOptionPane.showMessageDialog(this,
						"Couldn't update the book. "
								+ "No data has been changed.",
						"Database error", JOptionPane.ERROR_MESSAGE);
			}
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
		int id = Integer.parseInt(table.getModel().getValueAt(selectedRow, 0)
				.toString()); // Column 0 is id
		return dbHandler.getBook(id);
	}

	@Override
	public void updateData() {
		rowData = dbHandler.getBooksForTable();
	}
}