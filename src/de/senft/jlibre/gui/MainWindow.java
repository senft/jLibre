package de.senft.jlibre.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;

import de.senft.jlibre.io.DBHandler;

public class MainWindow extends JFrame {

	private static Logger logger = Logger.getLogger(MainWindow.class);

	private JPanel contentPane;
	private BookOverviewPanel bookPanel;
	private AuthorOverviewPanel authorPanel;
	private QuoteOverviewPanel quotePanel;
	private JTabbedPane tabbedPane;
	private JLabel lblStatusbar;
	private JButton btnEditSelected;
	private JButton btnDeleteSelected;

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		setTitle("jLibre");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 539);

		// TODO Setting colors here, probably isnt needed
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setForeground(SystemColor.text);
		menuBar.add(mnFile);

		mnFile.add(new JSeparator());

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setForeground(SystemColor.text);
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = new AboutDialog();
				dialog.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.NORTH);

		JButton btnAddAuthor = new JButton("");
		btnAddAuthor.setToolTipText("Add new author");
		btnAddAuthor.setIcon(new ImageIcon(MainWindow.class
				.getResource("/images/new-author.png")));
		btnAddAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				authorPanel.addNew();
			}
		});
		toolBar.add(btnAddAuthor);

		JButton btnAddBook = new JButton("");
		btnAddBook.setToolTipText("Add new book");
		btnAddBook.setIcon(new ImageIcon(MainWindow.class
				.getResource("/images/new-book.png")));
		btnAddBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookPanel.addNew();
			}
		});
		toolBar.add(btnAddBook);

		JButton btnAddQuote = new JButton("");
		btnAddQuote.setToolTipText("Add new quote");
		btnAddQuote.setIcon(new ImageIcon(MainWindow.class
				.getResource("/images/new-quote.png")));
		btnAddQuote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quotePanel.addNew();
			}
		});
		toolBar.add(btnAddQuote);

		btnEditSelected = new JButton("");
		btnEditSelected.setIcon(new ImageIcon(MainWindow.class
				.getResource("/images/edit.png")));
		btnEditSelected
				.setToolTipText("Edit currently selected author/book/quote");
		btnEditSelected.setEnabled(false);
		btnEditSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OverviewPanel currentPanel = (OverviewPanel) tabbedPane
						.getSelectedComponent();

				boolean dataChanged = currentPanel.editSelected();

				if (dataChanged) {
					bookPanel.updateData();
					quotePanel.updateData();
				}
			}
		});

		toolBar.addSeparator();

		toolBar.add(btnEditSelected);

		btnDeleteSelected = new JButton("");
		btnDeleteSelected.setMnemonic(KeyEvent.VK_DELETE);
		btnDeleteSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OverviewPanel currentPanel = (OverviewPanel) tabbedPane
						.getSelectedComponent();

				boolean dataChanged = currentPanel.deleteSelected();

				if (dataChanged) {
					bookPanel.updateData();
					quotePanel.updateData();
				}
			}
		});
		btnDeleteSelected.setIcon(new ImageIcon(MainWindow.class
				.getResource("/images/delete.png")));
		btnDeleteSelected
				.setToolTipText("Delete currently selected author/book/quote");
		btnDeleteSelected.setEnabled(false);
		toolBar.add(btnDeleteSelected);

		toolBar.addSeparator();

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				onTableSelectionChange();
			}
		});

		contentPane.add(tabbedPane, BorderLayout.CENTER);

		bookPanel = new BookOverviewPanel();
		bookPanel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (tabbedPane.getSelectedComponent().equals(bookPanel)) {
					// bookPanel is currently visible
					onTableSelectionChange();
				}
			}
		});
		tabbedPane.addTab("Books", null, bookPanel, null);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		authorPanel = new AuthorOverviewPanel();
		authorPanel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (tabbedPane.getSelectedComponent().equals(authorPanel)) {
					// authorPanel is currently visible
					onTableSelectionChange();
				}
			}
		});
		tabbedPane.addTab("Authors", null, authorPanel, null);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		quotePanel = new QuoteOverviewPanel();
		quotePanel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (tabbedPane.getSelectedComponent().equals(quotePanel)) {
					// quotePanel is currently visible
					onTableSelectionChange();
				}
			}
		});
		tabbedPane.addTab("Quotes", null, quotePanel, null);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		JPanel settingPanel = new JPanel();
		tabbedPane.addTab("Settings", null, settingPanel, null);
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		lblStatusbar = new JLabel();
		lblStatusbar.setVerticalAlignment(SwingConstants.BOTTOM);
		lblStatusbar.setHorizontalAlignment(SwingConstants.LEFT);
		updateStatusbar();

		bookPanel.tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				updateStatusbar();
			}
		});

		authorPanel.tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				updateStatusbar();
			}
		});

		quotePanel.tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				updateStatusbar();
			}
		});

		panel.add(lblStatusbar);
	}

	private void updateStatusbar() {
		int numBook = bookPanel.table.getRowCount();
		int numAuthor = authorPanel.table.getRowCount();
		int numQuote = quotePanel.table.getRowCount();

		lblStatusbar.setText(String.format(
				"%d authors, %d books, %d quotes in database", numAuthor,
				numBook, numQuote));
	}

	private void onTableSelectionChange() {
		if (tabbedPane.getSelectedIndex() == 3) {
			// Settingspane
			return;
		}
		OverviewPanel currentPanel = (OverviewPanel) tabbedPane
				.getSelectedComponent();

		if (currentPanel.hasSelectedOne()) {
			btnEditSelected.setEnabled(true);
			btnDeleteSelected.setEnabled(true);
		} else {
			btnEditSelected.setEnabled(false);
			btnDeleteSelected.setEnabled(false);
		}
	}

	private void close() {
		logger.info("Closing MainWindow");
		DBHandler.getInstance().closeConnection();
		System.exit(0);
	}
}