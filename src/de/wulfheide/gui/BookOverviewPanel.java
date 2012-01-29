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
				if (firstIndex > -1 && firstIndex == lastIndex) {

					DateFormat df = DateFormat.getDateInstance();

					// Column 0 is id
					int id = (Integer) table.getValueAt(firstIndex, 0);
					Book book = dbHandler.getBook(id);

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
					for (Quote q : quotes) {
						sb.append(
								String.format("<li><i>\"%s\"</i></li>",
										q.toString())).append("</p>");
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
		AddBookDialog dialog = new AddBookDialog();
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
		return false;
	}
}