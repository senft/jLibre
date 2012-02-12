package de.wulfheide.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import de.wulfheide.model.Book;
import de.wulfheide.model.Quote;
import de.wulfheide.persistency.DBHandler;

public class QuoteDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private Quote quote;
	private JComboBox<String> cmbBook;
	private JTextArea txtText;
	private JTextArea txtComment;

	private DBHandler dbHandler = DBHandler.getInstance();

	private Object[][] books = dbHandler.getBooksForComboBox();

	/**
	 * Create the dialog.
	 */
	public QuoteDialog() {
		setTitle("New quote...");
		setModal(true);
		setBounds(100, 100, 534, 373);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new TitledBorder(null, "New quote...",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 70, 218, 0 };
		gbl_contentPanel.rowHeights = new int[] { 23, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 1.0, 1.0,
				Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblAuthor = new JLabel("Book:");
			GridBagConstraints gbc_lblAuthor = new GridBagConstraints();
			gbc_lblAuthor.anchor = GridBagConstraints.WEST;
			gbc_lblAuthor.fill = GridBagConstraints.VERTICAL;
			gbc_lblAuthor.insets = new Insets(0, 0, 5, 5);
			gbc_lblAuthor.gridx = 0;
			gbc_lblAuthor.gridy = 0;
			contentPanel.add(lblAuthor, gbc_lblAuthor);
		}
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.WEST;
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.gridx = 1;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 131, 50, 0 };
			gbl_panel.rowHeights = new int[] { 24, 0 };
			gbl_panel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
			{
				cmbBook = new JComboBox<String>(
						new DefaultComboBoxModel<String>() {
							@Override
							public int getSize() {
								return books.length;
							}

							@Override
							public String getElementAt(int index) {
								return books[index][1].toString();
							}
						});
				try {
					cmbBook.setSelectedIndex(0);
				} catch (IllegalArgumentException e) {
					cmbBook.setSelectedIndex(1);
				}

				GridBagConstraints gbc_cmbAuthor = new GridBagConstraints();
				gbc_cmbAuthor.fill = GridBagConstraints.HORIZONTAL;
				gbc_cmbAuthor.anchor = GridBagConstraints.NORTH;
				gbc_cmbAuthor.insets = new Insets(0, 0, 0, 5);
				gbc_cmbAuthor.gridx = 0;
				gbc_cmbAuthor.gridy = 0;
				panel.add(cmbBook, gbc_cmbAuthor);
			}
			{
				JButton btnNewAuthor = new JButton("New book");
				btnNewAuthor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// JDialog dialog = new AddAuthorDialog();
						// dialog.setVisible(true);
					}
				});
				GridBagConstraints gbc_btnNewAuthor = new GridBagConstraints();
				gbc_btnNewAuthor.anchor = GridBagConstraints.NORTHEAST;
				gbc_btnNewAuthor.gridx = 1;
				gbc_btnNewAuthor.gridy = 0;
				panel.add(btnNewAuthor, gbc_btnNewAuthor);
			}
		}
		{
			JLabel lblDescription = new JLabel("Text:");
			GridBagConstraints gbc_lblDescription = new GridBagConstraints();
			gbc_lblDescription.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
			gbc_lblDescription.gridx = 0;
			gbc_lblDescription.gridy = 1;
			contentPanel.add(lblDescription, gbc_lblDescription);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED,
					null, null, null, null));
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 1;
			contentPanel.add(scrollPane, gbc_scrollPane);
			{
				txtText = new JTextArea();
				scrollPane.setViewportView(txtText);
			}
		}
		{
			JLabel lblComment = new JLabel("Comment:");
			GridBagConstraints gbc_lblComment = new GridBagConstraints();
			gbc_lblComment.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblComment.insets = new Insets(0, 0, 0, 5);
			gbc_lblComment.gridx = 0;
			gbc_lblComment.gridy = 2;
			contentPanel.add(lblComment, gbc_lblComment);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED,
					null, null, null, null));
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 2;
			contentPanel.add(scrollPane, gbc_scrollPane);
			{
				txtComment = new JTextArea();
				scrollPane.setViewportView(txtComment);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						clickedOK();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						clickedCancel();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * Constructor for creating a Dialog to edit a quote. Fills all passed
	 * values in the corresponding widget.
	 * @param id the id of the quote to edit
	 * @param text
	 * @param comment
	 * @param book
	 */
	public QuoteDialog(int id, String text, String comment, Book book) {
		this();
		setTitle("Edit quote...");

		txtText.setText(text);
		txtComment.setText(comment);

		for (int i = 0; i < books.length; i++) {
			if (Integer.valueOf(books[i][0].toString()) == book.getId()) {
				cmbBook.setSelectedIndex(i);
				break;
			}
		}
	}

	public Quote showDialog() {
		setVisible(true);
		return quote;
	}

	private void clickedOK() {
		String text = txtText.getText().trim();
		String comment = txtComment.getText().trim();
		int bookId = -1;

		// Internally we keep an Object[][] in the JComboBox that holds
		// Object<int><String>, or Object<ID><Title> (only the title
		// Object[i][1] is displayed of course).
		// Here we then fetch the selected book's title, and iterate over the
		// Object[][] to get the book's id (Object[i][0])
		String selectedBook = cmbBook.getSelectedItem().toString();
		for (int i = 0; i < books.length; i++) {
			if (books[i][1].toString().equals(selectedBook))
				bookId = (Integer) books[i][0];
		}

		if (text.equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter a text.",
					"Warning", JOptionPane.WARNING_MESSAGE);
		} else {
			quote = new Quote();
			quote.setText(text);
			quote.setComment(comment);
			quote.setBook(dbHandler.getBook(bookId)); // ugly

			setVisible(false);
		}
	}

	private void clickedCancel() {
		setVisible(false);
	}

}
