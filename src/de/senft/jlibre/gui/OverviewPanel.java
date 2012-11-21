package de.senft.jlibre.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

public abstract class OverviewPanel extends JPanel {

	protected static Logger logger = Logger.getLogger(OverviewPanel.class);

	private JTextField filterTextField;
	protected JTable table;
	protected JTextPane infoPane;
	protected JPopupMenu popupMenu;

	protected AbstractTableModel tableModel;

	protected Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	protected Vector<Object> columnNames = new Vector<Object>();
	@SuppressWarnings("rawtypes")
	protected Class[] columnClasses = new Class[0];
	private JScrollPane scrollPane_1;

	// Determines the left margin of the data in the info panel (not the
	// "headings")
	final int INFOPANEL_LINEFEED = 15;

	/**
	 * Creates a {@link javax.swing.event.ListSelectionListener
	 * ListSelectionListener} that changes the displayed data of this panels
	 * {@link OverviewPanel#infoPane infoPane} according to the current
	 * selection of this panels {@link OverviewPanel#table table}.
	 * 
	 * @return a new {@link javax.swing.event.ListSelectionListener
	 *         ListSelectionListener}
	 */
	protected abstract ListSelectionListener makeListSelectionListener();

	/**
	 * Updates all data from the DB.
	 */
	public abstract void updateData();

	/**
	 * Adds a new entity to this table.
	 * 
	 * @return true if successful
	 */
	protected abstract boolean addNew();

	/**
	 * Pops up an editing dialog to edit the currently selected entity.
	 * 
	 * @return true if successful
	 */
	protected abstract boolean editSelected();

	/**
	 * Deletes the currently selected entity.
	 * 
	 * @return true if successful
	 */
	protected abstract boolean deleteSelected();

	// /**
	// * Deletes the entity with the given ID.<br />
	// * <b>IMPORTANT:</b> This does not delete the entity in the row with the
	// ID
	// * "ID", but the row where the entity's ID (column 0) is ID.
	// *
	// * @param id
	// * the ID of the entity to delete
	// * @return true if successful
	// */
	// protected abstract boolean delete(int id);

	/**
	 * Create the panel.
	 */
	public OverviewPanel() {
		setLayout(new BorderLayout(0, 0));

		filterTextField = new JTextField();
		filterTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				TableRowSorter<?> sorter = (TableRowSorter<?>) table
						.getRowSorter();

				if (sorter != null) {
					String expr = filterTextField.getText();
					sorter.setRowFilter(RowFilter.regexFilter(expr));
					sorter.setSortKeys(null);
				}
			}
		});
		add(filterTextField, BorderLayout.NORTH);
		filterTextField.setColumns(10);

		JSplitPane tablePanel = new JSplitPane();
		tablePanel.setResizeWeight(0.7);
		add(tablePanel, BorderLayout.CENTER);

		table = new JTable();

		table.setFillsViewportHeight(true);

		table.getSelectionModel().addListSelectionListener(
				makeListSelectionListener());

		// TODO
//		table.setRowSorter(new TableRowSorter<TableModel>(table.getModel()));
//
//		tableModel.addTableModelListener(new TableModelListener() {
//			@Override
//			public void tableChanged(TableModelEvent e) {
//				logger.debug(String.format("Table data in %s changed", this
//						.getClass().toString()));
//			}
		// });

		JScrollPane scrollPane = new JScrollPane(table);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(new LineBorder(null));
		scrollPane_1
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

		infoPane = new JTextPane();
		infoPane.setContentType("text/html");
		infoPane.setFont(UIManager.getFont("Label.font"));
		scrollPane_1.setViewportView(infoPane);
		infoPane.setEditable(false);
		infoPane.setFocusable(false);

		tablePanel.setLeftComponent(scrollPane);
		tablePanel.setRightComponent(scrollPane_1);
	}

	protected int getMinSelectionIndex() {
		return table.getSelectionModel().getMinSelectionIndex();
	}

	protected int getMaxSelectionIndex() {
		return table.getSelectionModel().getMaxSelectionIndex();
	}

	/**
	 * Returns true if there is <b>exactly</b> one item selected in the table.
	 * 
	 * @return true if there is <b>exactly</b> one item selected in the table
	 */
	protected boolean hasSelectedOne() {
		int firstIndex = table.getSelectionModel().getMinSelectionIndex();
		int lastIndex = table.getSelectionModel().getMaxSelectionIndex();

		return firstIndex != -1 && firstIndex == lastIndex;
	}

	protected void addListSelectionListener(ListSelectionListener listener) {
		table.getSelectionModel().addListSelectionListener(listener);
	}
}
