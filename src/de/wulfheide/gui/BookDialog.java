package de.wulfheide.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

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

import com.toedter.calendar.JDateChooser;

import de.wulfheide.io.DBHandler;
import de.wulfheide.model.Author;
import de.wulfheide.model.Book;

public class BookDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtTitle;
	private JComboBox<String> cmbAuthor;
	private JDateChooser dtStarted;
	private JTextArea txtComment;
	private JDateChooser dtFinished;
	private JTextField txtPublished;
	private JComboBox<String> cmbEpoche;
	private JComboBox<String> cmbGenre;

	private DBHandler dbHandler = DBHandler.getInstance();

	private Object[][] authors = dbHandler.getAuthorsForComboBox();

	private Book book;

	/**
	 * Create the dialog.
	 */
	public BookDialog() {
		setTitle("New book...");
		setModal(true);
		setBounds(100, 100, 622, 402);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new TitledBorder(null, "New book...",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 116, 218, 0 };
		gbl_contentPanel.rowHeights = new int[] { 23, 23, 0, 0, 0, 23, 23, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("Title:");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
			gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			txtTitle = new JTextField();
			GridBagConstraints gbc_txtTitle = new GridBagConstraints();
			gbc_txtTitle.fill = GridBagConstraints.BOTH;
			gbc_txtTitle.insets = new Insets(0, 0, 5, 0);
			gbc_txtTitle.gridx = 1;
			gbc_txtTitle.gridy = 0;
			contentPanel.add(txtTitle, gbc_txtTitle);
			txtTitle.setColumns(10);
		}
		{
			JLabel lblAuthor = new JLabel("Author:");
			GridBagConstraints gbc_lblAuthor = new GridBagConstraints();
			gbc_lblAuthor.anchor = GridBagConstraints.WEST;
			gbc_lblAuthor.fill = GridBagConstraints.VERTICAL;
			gbc_lblAuthor.insets = new Insets(0, 0, 5, 5);
			gbc_lblAuthor.gridx = 0;
			gbc_lblAuthor.gridy = 1;
			contentPanel.add(lblAuthor, gbc_lblAuthor);
		}
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.WEST;
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.gridx = 1;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 131, 50, 0 };
			gbl_panel.rowHeights = new int[] { 24, 0 };
			gbl_panel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
			{
				cmbAuthor = new JComboBox<String>(
						new DefaultComboBoxModel<String>() {
							@Override
							public int getSize() {
								return authors.length;
							}

							@Override
							public String getElementAt(int index) {
								return authors[index][1].toString();
							}
						});
				try {
					cmbAuthor.setSelectedIndex(0);
				} catch (IllegalArgumentException e) {
					cmbAuthor.setSelectedIndex(1);
				}

				GridBagConstraints gbc_cmbAuthor = new GridBagConstraints();
				gbc_cmbAuthor.fill = GridBagConstraints.HORIZONTAL;
				gbc_cmbAuthor.anchor = GridBagConstraints.NORTH;
				gbc_cmbAuthor.insets = new Insets(0, 0, 0, 5);
				gbc_cmbAuthor.gridx = 0;
				gbc_cmbAuthor.gridy = 0;
				panel.add(cmbAuthor, gbc_cmbAuthor);
			}
			{
				JButton btnNewAuthor = new JButton("New author");
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
			JLabel lblEpoche = new JLabel("Epoche:");
			GridBagConstraints gbc_lblEpoche = new GridBagConstraints();
			gbc_lblEpoche.anchor = GridBagConstraints.WEST;
			gbc_lblEpoche.insets = new Insets(0, 0, 5, 5);
			gbc_lblEpoche.gridx = 0;
			gbc_lblEpoche.gridy = 2;
			contentPanel.add(lblEpoche, gbc_lblEpoche);
		}
		{
			cmbEpoche = new JComboBox<String>(dbHandler.getCommonEpoches());
			cmbEpoche.setSelectedIndex(-1);
			cmbEpoche.setEditable(true);

			GridBagConstraints gbc_txtEpoche = new GridBagConstraints();
			gbc_txtEpoche.insets = new Insets(0, 0, 5, 0);
			gbc_txtEpoche.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtEpoche.gridx = 1;
			gbc_txtEpoche.gridy = 2;
			contentPanel.add(cmbEpoche, gbc_txtEpoche);
		}
		{
			JLabel lblGenre = new JLabel("Genre:");
			GridBagConstraints gbc_lblGenre = new GridBagConstraints();
			gbc_lblGenre.anchor = GridBagConstraints.WEST;
			gbc_lblGenre.insets = new Insets(0, 0, 5, 5);
			gbc_lblGenre.gridx = 0;
			gbc_lblGenre.gridy = 3;
			contentPanel.add(lblGenre, gbc_lblGenre);
		}
		{
			cmbGenre = new JComboBox<String>(dbHandler.getCommonGenres());
			cmbGenre.setSelectedIndex(-1);
			cmbGenre.setEditable(true);

			GridBagConstraints gbc_txtGenre = new GridBagConstraints();
			gbc_txtGenre.insets = new Insets(0, 0, 5, 0);
			gbc_txtGenre.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtGenre.gridx = 1;
			gbc_txtGenre.gridy = 3;
			contentPanel.add(cmbGenre, gbc_txtGenre);
		}
		{
			JLabel lblYearOfPublication = new JLabel("Year of publication:");
			GridBagConstraints gbc_lblYearOfPublication = new GridBagConstraints();
			gbc_lblYearOfPublication.anchor = GridBagConstraints.EAST;
			gbc_lblYearOfPublication.insets = new Insets(0, 0, 5, 5);
			gbc_lblYearOfPublication.gridx = 0;
			gbc_lblYearOfPublication.gridy = 4;
			contentPanel.add(lblYearOfPublication, gbc_lblYearOfPublication);
		}
		{
			txtPublished = new JTextField();
			GridBagConstraints gbc_txtPublished = new GridBagConstraints();
			gbc_txtPublished.anchor = GridBagConstraints.NORTH;
			gbc_txtPublished.insets = new Insets(0, 0, 5, 0);
			gbc_txtPublished.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtPublished.gridx = 1;
			gbc_txtPublished.gridy = 4;
			contentPanel.add(txtPublished, gbc_txtPublished);
			txtPublished.setColumns(10);
		}
		{
			JLabel lblStartedReading = new JLabel("Started reading:");
			GridBagConstraints gbc_lblStartedReading = new GridBagConstraints();
			gbc_lblStartedReading.anchor = GridBagConstraints.WEST;
			gbc_lblStartedReading.fill = GridBagConstraints.VERTICAL;
			gbc_lblStartedReading.insets = new Insets(0, 0, 5, 5);
			gbc_lblStartedReading.gridx = 0;
			gbc_lblStartedReading.gridy = 5;
			contentPanel.add(lblStartedReading, gbc_lblStartedReading);
		}
		{
			dtStarted = new JDateChooser();
			GridBagConstraints gbc_dtStarted = new GridBagConstraints();
			gbc_dtStarted.fill = GridBagConstraints.BOTH;
			gbc_dtStarted.insets = new Insets(0, 0, 5, 0);
			gbc_dtStarted.gridx = 1;
			gbc_dtStarted.gridy = 5;
			contentPanel.add(dtStarted, gbc_dtStarted);
		}
		{
			JLabel lblFinishedReading = new JLabel("Finished reading:");
			GridBagConstraints gbc_lblFinishedReading = new GridBagConstraints();
			gbc_lblFinishedReading.anchor = GridBagConstraints.WEST;
			gbc_lblFinishedReading.fill = GridBagConstraints.VERTICAL;
			gbc_lblFinishedReading.insets = new Insets(0, 0, 5, 5);
			gbc_lblFinishedReading.gridx = 0;
			gbc_lblFinishedReading.gridy = 6;
			contentPanel.add(lblFinishedReading, gbc_lblFinishedReading);
		}
		{
			dtFinished = new JDateChooser();
			GridBagConstraints gbc_dtFinished = new GridBagConstraints();
			gbc_dtFinished.insets = new Insets(0, 0, 5, 0);
			gbc_dtFinished.fill = GridBagConstraints.BOTH;
			gbc_dtFinished.gridx = 1;
			gbc_dtFinished.gridy = 6;
			contentPanel.add(dtFinished, gbc_dtFinished);
		}
		{
			JLabel lblComment = new JLabel("Comment");
			GridBagConstraints gbc_lblComment = new GridBagConstraints();
			gbc_lblComment.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblComment.insets = new Insets(0, 0, 0, 5);
			gbc_lblComment.gridx = 0;
			gbc_lblComment.gridy = 7;
			contentPanel.add(lblComment, gbc_lblComment);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED,
					null, null, null, null));
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 7;
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
	 * Constructor for creating a dialog to edit a book. Fills all passed values
	 * in the corresponding widget.
	 * 
	 * @param id
	 *            the id of the book to edit
	 * @param title
	 * @param author
	 * @param started
	 * @param finished
	 * @param comment
	 * @param published
	 * @param epoche
	 * @param genre
	 */
	public BookDialog(int id, String title, Author author, Date started,
			Date finished, String comment, int published, String epoche,
			String genre) {
		this();
		setTitle("Edit book...");
		txtTitle.setText(title);

		dtStarted.setDate(started);
		dtFinished.setDate(finished);
		txtComment.setText(comment);
		txtPublished.setText(Integer.valueOf(published).toString());
		cmbEpoche.setSelectedItem(epoche);
		cmbGenre.setSelectedItem(genre);

		for (int i = 0; i < authors.length; i++) {
			if (Integer.valueOf(authors[i][0].toString()) == author.getId()) {
				cmbAuthor.setSelectedIndex(i);
				break;
			}
		}
	}

	public Book showDialog() {
		setVisible(true);
		return book;
	}

	private void clickedOK() {
		String title = txtTitle.getText().trim();
		String epoche = cmbEpoche.getSelectedItem().toString();
		String genre = cmbGenre.getSelectedItem().toString();
		String comment = txtComment.getText().trim();
		int authorId = -1;
		int published;
		Date started = dtStarted.getDate();
		Date finished = dtFinished.getDate();

		// Internally we keep an Object[][] in the JComboBox that holds
		// Object<int><String>, or Object<ID><Authorname> (only the name
		// Object[i][1] is displayed of course).
		// Here we then fetch the selected author's name, and iterate over the
		// Object[][] to get the author's id (Object[i][0])
		String selectedAuthor = cmbAuthor.getSelectedItem().toString();
		for (int i = 0; i < authors.length; i++) {
			if (authors[i][1].toString().equals(selectedAuthor))
				authorId = (Integer) authors[i][0];
		}

		if (title.equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter a title.",
					"Warning", JOptionPane.WARNING_MESSAGE);
		} else {
			try {
				published = Integer.valueOf(txtPublished.getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this,
						"Please enter a valid year of publication.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			book = new Book();
			book.setTitle(title);
			book.setEpoche(epoche);
			book.setComment(comment);
			book.setGenre(genre);
			book.setAuthor(dbHandler.getAuthor(authorId)); // ugly
			book.setPublicationYear(published);
			book.setStartedReading(started);
			book.setFinishedReading(finished);

			setVisible(false);
		}
	}

	private void clickedCancel() {
		setVisible(false);
	}

}
