package de.senft.jlibre.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.Quote;

public class QuoteDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private Quote quote;
	private JComboBox<String> cmbBook;
	private JTextArea txtText;
	private JTextArea txtComment;

	private List<Book> books;

	private JTextField txtPages;

	private boolean result = false;

	/**
	 * Creates a Dialog to edit a quote. Fills the values of the passed quote in
	 * the corresponding widgets.
	 * 
	 * @param quote
	 *            the quote to edit
	 */
	public QuoteDialog(Quote quote, List<Book> theBooks) {
		this.quote = quote;
		this.books = theBooks;

		setTitle("Edit quote...");
		setModal(true);
		setBounds(100, 100, 534, 373);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new TitledBorder(null, "Edit quote...",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 70, 218, 0 };
		gbl_contentPanel.rowHeights = new int[] { 23, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 1.0, 1.0,
				Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblBook = new JLabel("Book:");
			GridBagConstraints gbc_lblBook = new GridBagConstraints();
			gbc_lblBook.anchor = GridBagConstraints.WEST;
			gbc_lblBook.fill = GridBagConstraints.VERTICAL;
			gbc_lblBook.insets = new Insets(0, 0, 5, 5);
			gbc_lblBook.gridx = 0;
			gbc_lblBook.gridy = 0;
			contentPanel.add(lblBook, gbc_lblBook);
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
								return books.size();
							}

							@Override
							public String getElementAt(int index) {
								return books.get(index).toString();
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
			JLabel lblPages = new JLabel("Pages");
			GridBagConstraints gbc_lblPages = new GridBagConstraints();
			gbc_lblPages.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblPages.insets = new Insets(0, 0, 5, 5);
			gbc_lblPages.gridx = 0;
			gbc_lblPages.gridy = 1;
			contentPanel.add(lblPages, gbc_lblPages);
		}
		{
			txtPages = new JTextField();
			GridBagConstraints gbc_txtPages = new GridBagConstraints();
			gbc_txtPages.insets = new Insets(0, 0, 5, 0);
			gbc_txtPages.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtPages.gridx = 1;
			gbc_txtPages.gridy = 1;
			contentPanel.add(txtPages, gbc_txtPages);
			txtPages.setColumns(10);
		}
		{
			JLabel lblText = new JLabel("Text:");
			GridBagConstraints gbc_lblText = new GridBagConstraints();
			gbc_lblText.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblText.insets = new Insets(0, 0, 5, 5);
			gbc_lblText.gridx = 0;
			gbc_lblText.gridy = 2;
			contentPanel.add(lblText, gbc_lblText);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED,
					null, null, null, null));
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 2;
			contentPanel.add(scrollPane, gbc_scrollPane);
			{
				txtText = new JTextArea();
				txtText.setLineWrap(true);
				scrollPane.setViewportView(txtText);
			}
		}
		{
			JLabel lblComment = new JLabel("Comment:");
			GridBagConstraints gbc_lblComment = new GridBagConstraints();
			gbc_lblComment.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblComment.insets = new Insets(0, 0, 0, 5);
			gbc_lblComment.gridx = 0;
			gbc_lblComment.gridy = 3;
			contentPanel.add(lblComment, gbc_lblComment);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED,
					null, null, null, null));
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 3;
			contentPanel.add(scrollPane, gbc_scrollPane);
			{
				txtComment = new JTextArea();
				txtComment.setLineWrap(true);
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

		txtText.setText(quote.getText());
		txtComment.setText(quote.getComment());
		txtPages.setText(quote.getPages());

		if (quote.getBook() != null) {
			for (int i = 0; i < books.size(); i++) {
				if (Integer.valueOf(books.get(i).getId()) == quote.getBook()
						.getId()) {
					cmbBook.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	public boolean showDialog() {
		setVisible(true);
		return result;
	}

	private void clickedOK() {
		String text = txtText.getText().trim();
		String comment = txtComment.getText().trim();

		if (text.equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter a text.",
					"Warning", JOptionPane.WARNING_MESSAGE);
		} else {
			result = true;
			quote.setText(text);
			quote.setComment(comment);
			quote.setBook(books.get(cmbBook.getSelectedIndex()));
			quote.setPages(txtPages.getText());
			dispose();
		}
	}

	private void clickedCancel() {
		result = false;
		dispose();
	}
}
