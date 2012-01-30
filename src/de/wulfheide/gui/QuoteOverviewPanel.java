package de.wulfheide.gui;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import de.wulfheide.model.Quote;

public class QuoteOverviewPanel extends OverviewPanel {

	public QuoteOverviewPanel() {
		columnNames.add("ID");
		columnNames.add("Text");
		columnNames.add("Book");
		columnNames.add("Author");

		columnClasses = new Class[] { Integer.class, String.class,
				String.class, String.class };

		tableModel.fireTableStructureChanged();
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setMaxWidth(75);

		// TODO What the fuck is this shit? There has got to be an easier way!
		table.getColumnModel().getColumn(0)
				.setCellRenderer(new DefaultTableCellRenderer() {
				});
		((DefaultTableCellRenderer) table.getColumnModel().getColumn(0)
				.getCellRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

		rowData = dbHandler.getQuotesForTable();
	}

	@Override
	protected ListSelectionListener makeListSelectionListener() {
		return null;
	}

	@Override
	protected void addNew() {
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
			} else {
				JOptionPane.showMessageDialog(this,
						"Could not add author to database", "Database error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	@Override
	protected void deleteSelected() {
	}

	@Override
	protected boolean editSelected() {
		Quote oldQuote = this.getSelected();

		QuoteDialog dialog = new QuoteDialog(oldQuote.getId(),
				oldQuote.getText(), oldQuote.getComment(), oldQuote.getBook());
		Quote newQuote = dialog.showDialog();

		if (newQuote != null) {
			newQuote.setId(oldQuote.getId()); // Set new authors ID to old
												// authors ID, so we can
												// overwrite

			boolean success = dbHandler.updateQuote(newQuote);

			if (success) {
				// Publish changes to table
				int selectedRow = table.getSelectedRow();

				Vector<Object> vecQuote = new Vector<Object>();
				vecQuote.add(newQuote.getId());
				vecQuote.add(newQuote.getText());
				vecQuote.add(newQuote.getBook().getTitle());
				vecQuote.add(newQuote.getBook().getAuthor().toString());

				rowData.set(selectedRow, vecQuote);

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
}