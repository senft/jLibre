package de.wulfheide.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import de.wulfheide.persistency.DBHandler;
import javax.swing.border.TitledBorder;

public abstract class OverviewPanel extends JPanel {

	protected static Logger logger = Logger.getLogger("OverviewPanel");

	protected DBHandler dbHandler;

	private JTextField filterTextField;
	protected JTable table;
	protected JTextPane infoPane;
	protected JPopupMenu popupMenu;

	protected AbstractTableModel tableModel;

	protected Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	protected Vector<Object> columnNames = new Vector<Object>();
	@SuppressWarnings("rawtypes")
	protected Class[] columnClasses = new Class[0];
	private JPanel panel;

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

	protected abstract void addNew();

	protected abstract boolean editSelected();

	protected abstract void deleteSelected();

	/**
	 * Create the panel.
	 */
	public OverviewPanel() {
		dbHandler = DBHandler.getInstance();

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

		JPanel tablePanel = new JPanel();
		add(tablePanel, BorderLayout.CENTER);
		GridBagLayout gbl_tablePanel = new GridBagLayout();
		gbl_tablePanel.columnWidths = new int[] { 400, 210, 0 };
		gbl_tablePanel.rowHeights = new int[] { 418, 0 };
		gbl_tablePanel.columnWeights = new double[] { 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_tablePanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		tablePanel.setLayout(gbl_tablePanel);

		tableModel = new AbstractTableModel() {

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return rowData.elementAt(rowIndex).elementAt(columnIndex);
			}

			@Override
			public int getRowCount() {
				return rowData.size();
			}

			@Override
			public int getColumnCount() {
				return columnNames.size();
			}

			@Override
			public String getColumnName(int columnIndex) {
				return columnNames.elementAt(columnIndex).toString();
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnClasses[columnIndex];
			}
		};

		table = new JTable(tableModel);

		table.setFillsViewportHeight(true);
		table.setRowSorter(new TableRowSorter<TableModel>(table.getModel()));
		table.getSelectionModel().addListSelectionListener(
				makeListSelectionListener());

		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				logger.debug(String.format("Table data in %s changed",
						this.toString()));
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		tablePanel.add(scrollPane, gbc_scrollPane);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Info", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		tablePanel.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 210, 0 };
		gbl_panel.rowHeights = new int[] { 418, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		// TODO Maybe add forced word wrap: http://java-sl.com/wrap.html
		infoPane = new JTextPane();
		GridBagConstraints gbc_infoPane = new GridBagConstraints();
		gbc_infoPane.fill = GridBagConstraints.BOTH;
		gbc_infoPane.gridx = 0;
		gbc_infoPane.gridy = 0;
		panel.add(infoPane, gbc_infoPane);
		infoPane.setContentType("text/html");
		infoPane.setEditable(false);
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
