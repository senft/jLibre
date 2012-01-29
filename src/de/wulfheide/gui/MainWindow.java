package de.wulfheide.gui;

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

import org.apache.log4j.Logger;

import de.wulfheide.persistency.DBHandler;

public class MainWindow extends JFrame {

	private static Logger logger = Logger.getLogger("MainWindow");

	private JPanel contentPane;
	private BookOverviewPanel bookPanel;
	private AuthorOverviewPanel authorPanel;
	private QuoteOverviewPanel quotePanel;
	private JTabbedPane tabbedPane;
	private JLabel lblStatusbar;

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				logger.info("Closing MainWindow");
				DBHandler.getInstance().closeConnection();
				System.exit(0);
			}
		});
		setTitle("jLibre");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		// Setting colors here, probably isnt needed
		JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(SystemColor.text);
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setForeground(SystemColor.text);
		menuBar.add(mnFile);

		mnFile.add(new JSeparator());

		JMenuItem mntmExit = new JMenuItem("Exit");
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
		btnAddQuote.setIcon(new ImageIcon(MainWindow.class
				.getResource("/images/new-quote.png")));
		btnAddQuote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quotePanel.addNew();
			}
		});
		toolBar.add(btnAddQuote);

		JButton btnNewButton = new JButton("editAuthor");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean dataChanged = authorPanel.editSelected();
				if (dataChanged) {
					// TODO bookOverview and quoteOverview has to be updated
					// (possible new author names..)
				}
			}
		});
		toolBar.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("editBook");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookPanel.editSelected();
			}
		});
		toolBar.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("editQuote");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quotePanel.editSelected();
			}
		});
		toolBar.add(btnNewButton_2);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		bookPanel = new BookOverviewPanel();
		tabbedPane.addTab("Books", null, bookPanel, null);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		authorPanel = new AuthorOverviewPanel();
		tabbedPane.addTab("Authors", null, authorPanel, null);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		quotePanel = new QuoteOverviewPanel();
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
		lblStatusbar.setText("hellerr");
		panel.add(lblStatusbar);
	}

	public BookOverviewPanel getBookPanel() {
		return bookPanel;
	}

	public AuthorOverviewPanel getAuthorPanel() {
		return authorPanel;
	}

	public QuoteOverviewPanel getQuotePanel() {
		return quotePanel;
	}

}