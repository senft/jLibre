package de.senft.jlibre.io;

import java.util.Vector;

import de.senft.jlibre.model.Author;
import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.Quote;

public interface DBHandler {

	/**
	 * Fetches all books from the database and puts them in a 2-dimensional
	 * array for direct use in a {@link javax.swing.table.TableModel TableModel}
	 * . The array has this structure:
	 * 
	 * <pre>
	 * {@code new Object[] { "ID", "Title", "Author", "Year of publication" };}
	 * </pre>
	 * 
	 * @return 2-dimensional array containing data of books
	 */
	public Vector<Vector<Object>> getBooksForTable();

	/**
	 * Fetches all authors from the database and puts them in a 2-dimensional
	 * array for direct use in a {@link javax.swing.table.TableModel TableModel}
	 * . The array has this structure:
	 * 
	 * <pre>
	 * {@code new Object[] { "ID", "Firstname", "Lastname" };}
	 * </pre>
	 * 
	 * @return 2-dimensional array containing data of authors
	 */
	public Vector<Vector<Object>> getAuthorsForTable();

	/**
	 * Fetches all quotes from the database and puts them in a 2-dimensional
	 * array for direct use in a {@link javax.swing.table.TableModel TableModel}
	 * . The array has this structure:
	 * 
	 * <pre>
	 * {@code new Object[] { "ID", "Text", "Book", "Author" };}
	 * </pre>
	 * 
	 * @return 2-dimensional array containing data of quotes
	 */
	public Vector<Vector<Object>> getQuotesForTable();

	public Object[][] getBooksForComboBox();

	public Object[][] getAuthorsForComboBox();

	/**
	 * Fetches all necessary data from the DB to create a
	 * {@link de.senft.jlibre.model.Book Book} object, containing all data
	 * displayed on an info panel.
	 * 
	 * @param id
	 *            the books id
	 * @return an {@link de.senft.jlibre.model.Book Book} object containing all
	 *         the data
	 */
	public Book getBook(int id);

	/**
	 * Fetches all necessary data from the DB to create an
	 * {@link de.senft.jlibre.model.Author Author} object, containing all data
	 * displayed on an info panel.
	 * 
	 * @param id
	 *            the authors id
	 * @return an {@link de.senft.jlibre.model.Author Author} object containing
	 *         all the data
	 */
	public Author getAuthor(int id);

	/**
	 * Fetches all necessary data from the DB to create a
	 * {@link de.senft.jlibre.model.Quote Quote} object, containing all data
	 * displayed on an info panel.
	 * 
	 * @param id
	 *            the books id
	 * @return an {@link de.senft.jlibre.model.Book Book} object containing all
	 *         the data
	 */
	public Quote getQuote(int id);

	/**
	 * Stores an {@link de.senft.jlibre.model.Book Book} in the DB and returns
	 * the id (created by the DB) for this quote.
	 * 
	 * @param book
	 *            the quote to store
	 * @return the id of this book (-1 if something went wrong)
	 */
	public int makeBook(Book book);

	/**
	 * Stores an {@link de.senft.jlibre.model.Author Author} in the DB and
	 * returns the id (created by the DB) for this author.
	 * 
	 * @param author
	 *            the author to store
	 * @return the id of this author (-1 if someting went wrong)
	 */
	public int makeAuthor(Author author);

	/**
	 * Stores an {@link de.senft.jlibre.model.Quote Quote} in the DB, and
	 * returns the id (created by the DB) for this quote.
	 * 
	 * @param quote
	 *            the quote to store
	 * @return the id of this quote (-1 if someting went wrong)
	 */
	public int makeQuote(Quote quote);

	public boolean updateBook(Book book);

	public boolean updateAuthor(Author author);

	public boolean updateQuote(Quote quote);

	public boolean delete(Author author);

	public boolean delete(Book book);

	public boolean delete(Quote quote);

	public boolean delete(Author[] authors);

	public boolean delete(Book[] books);

	public boolean delete(Quote[] quotes);

	public String[] getCommonEpoches();

	public String[] getCommonGenres();

}