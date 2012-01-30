package de.wulfheide.gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import de.wulfheide.model.Book;
import de.wulfheide.model.Quote;

public class BookOverviewPanel extends OverviewPanel {

	public BookOverviewPanel() {
		columnNames.add("ID");
		columnNames.add("Title");
		columnNames.add("Author");
		columnNames.add("Year of publication");
		columnNames.add("Epoche");
		columnNames.add("Genre");
		columnClasses = new Class[] { Integer.class, String.class,
				String.class, Integer.class, String.class, String.class };

		tableModel.fireTableStructureChanged();

		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setMaxWidth(75);

		// TODO What the fuck is this shit? There has got to be an easier way!
		table.getColumnModel().getColumn(0)
				.setCellRenderer(new DefaultTableCellRenderer() {
				});
		((DefaultTableCellRenderer) table.getColumnModel().getColumn(0)
				.getCellRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

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
				DefaultListSelectionModel model = (DefaultListSelectionModel) e
						.getSource();

				int firstIndex = model.getMinSelectionIndex();
				int lastIndex = model.getMaxSelectionIndex();

				// Make sure only 1 item is selected, otherwise display nothing
				// on infopane
				if (firstIndex > -1 && firstIndex == lastIndex) {

					DateFormat df = DateFormat.getDateInstance();

					Book book = getSelected();

					String title = book.getTitle();
					String author = book.getAuthor().toString();
					int pubYear = book.getPublicationYear();
					Date startedReading = book.getStartedReading();
					Date finishedReading = book.getFinishedReading();
					Set<Quote> quotes = book.getQuotes();

					sb.append("<html><B>Title:</B><br>")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(title);
					sb.append("<p><B>Author:</B><br>")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(author)
							.append("</p>");
					sb.append("<p><B>Year of publication:</B><br>")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(pubYear)
							.append("</p>");
					sb.append("<p><B>Started reading:</B><br>")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;")
							.append(df.format(startedReading)).append("</p>");
					sb.append("<p><B>Finished reading:</B><br>")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;")
							.append(df.format(finishedReading)).append("</p>");

					sb.append("<p><B>Quotes from this book:</B><br>");
					if (quotes.isEmpty())
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;-");
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
				rowData.add(vecBook);

				tableModel.fireTableDataChanged();
				int newRow = rowData.size() - 1;
				table.getSelectionModel().setSelectionInterval(newRow, newRow);
			} else {
				JOptionPane.showMessageDialog(this,
						"Could not add book to database", "Database error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	protected void deleteSelected() {
	}

	@Override
	protected boolean editSelected() {
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

			boolean success = dbHandler.updateBook(newBook);

			if (success) {
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
				table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
			} else {
				JOptionPane.showMessageDialog(this, "Could not edit quote.",
						"Database error", JOptionPane.ERROR_MESSAGE);
			}
		}
		// TODO return something real here, or even return the index that has
		// been updated
		return false;
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
}