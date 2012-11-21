package de.senft.jlibre.io;

import java.util.List;

import de.senft.jlibre.model.Author;
import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.Quote;

public interface DBHandler {

	// TODO: JavaDoc

	/**
	 * Fetches all books from the database.
	 * 
	 * @return a list of {@link de.senft.jlibre.model.Book books}
	 */
	public List<Book> getBooks();

	/**
	 * Fetches all authors from the database.
	 * 
	 * @return a list of {@link de.senft.jlibre.model.Author authors}
	 */
	public List<Author> getAuthors();

	/**
	 * Fetches all quotes from the database.
	 * 
	 * @return a list of {@link de.senft.jlibre.model.Quote quotes}
	 */
	public List<Quote> getQuotes();


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

	public void updateBook(Book book);

	public void updateAuthor(Author author);

	public void updateQuote(Quote quote);

	public boolean delete(Author author);

	public boolean delete(Book book);

	public boolean delete(Quote quote);

	public boolean delete(Author[] authors);

	public boolean delete(Book[] books);

	public boolean delete(Quote[] quotes);

	public String[] getCommonEpoches();

	public String[] getCommonGenres();

}