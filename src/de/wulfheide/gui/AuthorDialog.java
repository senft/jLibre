package de.wulfheide.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JYearChooser;

import de.wulfheide.io.WikipediaAuthorParser;
import de.wulfheide.model.Author;

public class AuthorDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtFirstname;
	private JTextField txtLastname;

	private JTextField txtCountry;
	private JYearChooser dtBorn;
	private JYearChooser dtDied;

	/**
	 * The author object the eventually gets returned if after adding/editing
	 */
	private Author author;

	public AuthorDialog() {
		setTitle("New author...");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 381, 239);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new TitledBorder(null, "New author...",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 77, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblFirstname = new JLabel("Firstname:");
			GridBagConstraints gbc_lblFirstname = new GridBagConstraints();
			gbc_lblFirstname.anchor = GridBagConstraints.WEST;
			gbc_lblFirstname.insets = new Insets(0, 0, 5, 5);
			gbc_lblFirstname.gridx = 0;
			gbc_lblFirstname.gridy = 0;
			contentPanel.add(lblFirstname, gbc_lblFirstname);
		}
		{
			txtFirstname = new JTextField();
			GridBagConstraints gbc_txtFirstname = new GridBagConstraints();
			gbc_txtFirstname.insets = new Insets(0, 0, 5, 0);
			gbc_txtFirstname.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtFirstname.gridx = 1;
			gbc_txtFirstname.gridy = 0;
			contentPanel.add(txtFirstname, gbc_txtFirstname);
			txtFirstname.setColumns(10);
		}
		{
			JLabel lblLastname = new JLabel("Lastname:");
			GridBagConstraints gbc_lblLastname = new GridBagConstraints();
			gbc_lblLastname.anchor = GridBagConstraints.WEST;
			gbc_lblLastname.insets = new Insets(0, 0, 5, 5);
			gbc_lblLastname.gridx = 0;
			gbc_lblLastname.gridy = 1;
			contentPanel.add(lblLastname, gbc_lblLastname);
		}
		{
			txtLastname = new JTextField();
			GridBagConstraints gbc_txtLastname = new GridBagConstraints();
			gbc_txtLastname.insets = new Insets(0, 0, 5, 0);
			gbc_txtLastname.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtLastname.gridx = 1;
			gbc_txtLastname.gridy = 1;
			contentPanel.add(txtLastname, gbc_txtLastname);
			txtLastname.setColumns(10);
		}
		{
			JLabel lblCountry = new JLabel("Country:");
			GridBagConstraints gbc_lblCountry = new GridBagConstraints();
			gbc_lblCountry.anchor = GridBagConstraints.WEST;
			gbc_lblCountry.insets = new Insets(0, 0, 5, 5);
			gbc_lblCountry.gridx = 0;
			gbc_lblCountry.gridy = 2;
			contentPanel.add(lblCountry, gbc_lblCountry);
		}
		{
			txtCountry = new JTextField();
			GridBagConstraints gbc_txtCountry = new GridBagConstraints();
			gbc_txtCountry.insets = new Insets(0, 0, 5, 0);
			gbc_txtCountry.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtCountry.gridx = 1;
			gbc_txtCountry.gridy = 2;
			contentPanel.add(txtCountry, gbc_txtCountry);
			txtCountry.setColumns(10);
		}
		{
			JLabel lblDateOfBirth = new JLabel("Born:");
			GridBagConstraints gbc_lblDateOfBirth = new GridBagConstraints();
			gbc_lblDateOfBirth.anchor = GridBagConstraints.WEST;
			gbc_lblDateOfBirth.insets = new Insets(0, 0, 5, 5);
			gbc_lblDateOfBirth.gridx = 0;
			gbc_lblDateOfBirth.gridy = 3;
			contentPanel.add(lblDateOfBirth, gbc_lblDateOfBirth);
		}
		{
			dtBorn = new JYearChooser();
			GridBagConstraints gbc_dtBorn = new GridBagConstraints();
			gbc_dtBorn.insets = new Insets(0, 0, 5, 0);
			gbc_dtBorn.fill = GridBagConstraints.HORIZONTAL;
			gbc_dtBorn.gridx = 1;
			gbc_dtBorn.gridy = 3;
			contentPanel.add(dtBorn, gbc_dtBorn);
		}
		{
			JLabel lblDied = new JLabel("Died:");
			GridBagConstraints gbc_lblDied = new GridBagConstraints();
			gbc_lblDied.anchor = GridBagConstraints.WEST;
			gbc_lblDied.insets = new Insets(0, 0, 0, 5);
			gbc_lblDied.gridx = 0;
			gbc_lblDied.gridy = 4;
			contentPanel.add(lblDied, gbc_lblDied);
		}
		{
			dtDied = new JYearChooser();
			GridBagConstraints gbc_dtDied = new GridBagConstraints();
			gbc_dtDied.fill = GridBagConstraints.HORIZONTAL;
			gbc_dtDied.gridx = 1;
			gbc_dtDied.gridy = 4;
			contentPanel.add(dtDied, gbc_dtDied);
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
				{
					JButton btnGetDataFrom = new JButton(
							"Get data from wikipedia");
					btnGetDataFrom.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Author fetchedAuthor = WikipediaAuthorParser.getAuthor(
									txtFirstname.getText().trim(), txtLastname
											.getText().trim());
							
							dtBorn.setYear(fetchedAuthor.getBorn());
							dtDied.setYear(fetchedAuthor.getDied());
						}
					});
					buttonPane.add(btnGetDataFrom);
				}
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
	 * Constructor for creating a Dialog to edit an author. Fills all passed
	 * values in the corresponding widget.
	 * 
	 * @param id
	 *            the id of the author to edit
	 * @param firstname
	 * @param lastname
	 * @param country
	 * @param born
	 * @param died
	 */
	public AuthorDialog(int id, String firstname, String lastname,
			String country, int born, int died) {
		this();
		setTitle("Edit author...");

		txtFirstname.setText(firstname);
		txtLastname.setText(lastname);
		txtCountry.setText(country);
		dtBorn.setYear(born);
		dtDied.setYear(died);
	}

	public Author showDialog() {
		setVisible(true);
		return author;
	}

	private void clickedOK() {
		String firstname = txtFirstname.getText().trim();
		String lastname = txtLastname.getText().trim();
		int born = dtBorn.getYear();
		int died = dtDied.getYear();
		String country = txtCountry.getText().trim();

		if (firstname.equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter a firstname.",
					"Warning", JOptionPane.WARNING_MESSAGE);
		} else if (lastname.equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter a lastname.",
					"Warning", JOptionPane.WARNING_MESSAGE);
		} else if (born > Calendar.getInstance().get(Calendar.YEAR)) {
			JOptionPane.showMessageDialog(this,
					"The date of birth is in the future.", "Warning",
					JOptionPane.WARNING_MESSAGE);
		} else if (died < born) {
			JOptionPane.showMessageDialog(this,
					"The date of birth is bigger than the death.", "Warning",
					JOptionPane.WARNING_MESSAGE);
		} else {
			author = new Author();
			author.setFirstname(firstname);
			author.setLastname(lastname);
			author.setBorn(born);
			author.setDied(died);
			author.setCountry(country);
			setVisible(false);
		}
	}

	private void clickedCancel() {
		setVisible(false);
	}

}
