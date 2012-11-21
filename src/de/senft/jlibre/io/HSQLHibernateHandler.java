package de.senft.jlibre.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.senft.jlibre.model.Author;
import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.Quote;
import de.senft.jlibre.util.HibernateUtil;
import de.senft.jlibre.util.ListUtil;

public class HSQLHibernateHandler implements DBHandler {
	private static HSQLHibernateHandler dbHandler;
	private static Logger logger = Logger.getLogger(HSQLHibernateHandler.class);

	private Connection conn;

	private HSQLHibernateHandler() {
		// Not needed anymore when switched to hibernate
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (Exception e) {
			logger.error("ERROR: failed to load HSQLDB JDBC driver.");
			e.printStackTrace();
			return;
		}

		try {
			String path = System.getProperty("user.dir");
			conn = DriverManager.getConnection(String.format(
					"jdbc:hsqldb:file:/%s/bookdb;shutdown=true", path), "sa",
					"");
		} catch (SQLException se) {
			handleSQLException(se);
		}
	}

	public static HSQLHibernateHandler getInstance() {
		if (dbHandler == null) {
			logger.debug("initialising new DBHandler");
			dbHandler = new HSQLHibernateHandler();
		}
		return dbHandler;
	}

	public void closeConnection() {
		// Not needed anymore when switched to hibernate
		logger.info("Closing database connection");
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getBooks()
	 */
	public List<Book> getBooks() {
		logger.info("Fetching books");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		List<Book> books = ListUtil.castList(Book.class,
				session.createQuery("from Book").list());
		session.getTransaction().commit();
		session.close();

		logger.info("Fetched " + books.size() + " books");
		return books;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getAuthors()
	 */
	public List<Author> getAuthors() {
		logger.info("Fetching authors");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		List<Author> authors = ListUtil.castList(Author.class, session
				.createQuery("from Author").list());
		session.getTransaction().commit();
		session.close();

		logger.info("Fetched " + authors.size() + " authors");
		return authors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getQuotes()
	 */
	public List<Quote> getQuotes() {
		logger.info("Fetching quotes");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		List<Quote> quotes = ListUtil.castList(Quote.class, session
				.createQuery("from Quote").list());
		session.getTransaction().commit();
		session.close();

		logger.info("Fetched " + quotes.size() + " quotes");
		return quotes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getBooksForComboBox()
	 */
	@Override
	public Object[][] getBooksForComboBox() {
		Statement stmt;
		ArrayList<Object[]> result = new ArrayList<Object[]>();

		try {
			stmt = conn.createStatement();

			// Execute the query
			ResultSet rs = stmt
					.executeQuery("SELECT book.book_id, book.title, book.pubyear, author.firstname, "
							+ "author.lastname FROM BOOK, AUTHOR WHERE book.author_id = author.author_id;");

			while (rs.next()) {
				int id = rs.getInt("book_id");
				String title = rs.getString("title");
				String author = rs.getString("firstname") + " "
						+ rs.getString("lastname");
				int pubYear = rs.getInt("pubyear");

				result.add(new Object[] { id,
						author + ": " + title + ", " + pubYear });
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}

		logger.debug(String.format(
				"Loaded combo box represetation for %d books", result.size()));

		return result.toArray(new Object[][] { {} });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getAuthorsForComboBox()
	 */
	@Override
	public Object[][] getAuthorsForComboBox() {
		Statement stmt;
		ArrayList<Object[]> result = new ArrayList<Object[]>();

		try {
			stmt = conn.createStatement();

			// Execute the query
			ResultSet rs = stmt
					.executeQuery("SELECT author_id, firstname, lastname FROM AUTHOR;");

			while (rs.next()) {
				result.add(new Object[] {
						rs.getInt("author_id"),
						rs.getString("firstname") + " "
								+ rs.getString("lastname") });
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}

		logger.debug(String.format(
				"Loaded combo box represetation for %d authors", result.size()));

		return result.toArray(new Object[][] { {} });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getBook(int)
	 */
	@Override
	public Book getBook(int id) {
		logger.info("Fetching book with id " + id);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Book book = (Book) session.byId(Book.class).load(id);
		session.close();
		logger.info("Fetched book(" + id + "): " + book);
		return book;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getAuthor(int)
	 */
	@Override
	public Author getAuthor(int id) {
		logger.info("Fetching author with id " + id);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Author author = (Author) session.byId(Author.class).load(id);
		session.close();
		logger.info("Fetched author(" + id + "): " + author);
		return author;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getQuote(int)
	 */
	@Override
	public Quote getQuote(int id) {
		logger.info("Fetching quote with id " + id);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Quote quote = (Quote) session.byId(Quote.class).load(id);
		session.close();
		logger.info("Fetched quote(" + id + "): " + quote);
		return quote;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#makeBook(de.senft.jlibre.model.Book)
	 */
	@Override
	public int makeBook(Book book) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(book);
		tx.commit();
		session.close();
		return book.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.senft.jlibre.io.DBHandler#makeAuthor(de.senft.jlibre.model.Author)
	 */
	@Override
	public int makeAuthor(Author author) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(author);
		tx.commit();
		session.close();
		return author.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#makeQuote(de.senft.jlibre.model.Quote)
	 */
	@Override
	public int makeQuote(Quote quote) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(quote);
		tx.commit();
		session.close();
		return quote.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#updateBook(de.senft.jlibre.model.Book)
	 */
	@Override
	public void updateBook(Book book) {
		makeBook(book);
		// PreparedStatement stmt;
		// int rowsChanged = 0;
		//
		// int bookId = book.getId();
		// String title = book.getTitle();
		// String epoche = book.getEpoche();
		// String comment = book.getComment();
		// String genre = book.getGenre();
		// int authorId = book.getAuthor().getId();
		// int published = book.getPublicationYear();
		// Date started = book.getStartedReading();
		// Date finished = book.getFinishedReading();
		//
		// java.sql.Date sqlStarted = null;
		// java.sql.Date sqlFinished = null;
		//
		// if (started != null)
		// sqlStarted = new java.sql.Date(started.getTime());
		//
		// if (finished != null)
		// sqlFinished = new java.sql.Date(finished.getTime());
		//
		// try {
		// stmt = conn.prepareStatement(String.format(
		// "UPDATE Book SET authorid = %d, title = '%s',"
		// + " pubyear = %d, startread = ?, finishread = ?,"
		// + " comment = '%s', epoche = '%s', genre = '%s'"
		// + " WHERE id = %d;", authorId, title, published,
		// comment, epoche, genre, bookId));
		//
		// if (sqlStarted == null)
		// stmt.setNull(1, java.sql.Types.DATE);
		// else
		// stmt.setDate(1, sqlStarted);
		//
		// if (sqlFinished == null)
		// stmt.setNull(2, java.sql.Types.DATE);
		// else
		// stmt.setDate(2, sqlFinished);
		//
		// rowsChanged = stmt.executeUpdate();
		//
		// } catch (SQLException se) {
		// handleSQLException(se);
		// }
		//
		// return rowsChanged != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.senft.jlibre.io.DBHandler#updateAuthor(de.senft.jlibre.model.Author)
	 */
	@Override
	public void updateAuthor(Author author) {
		makeAuthor(author);
		// Statement stmt;
		// int result = 0;
		//
		// try {
		// stmt = conn.createStatement();
		// result = stmt
		// .executeUpdate(String.format(
		// "UPDATE Author SET firstname = '%s',"
		// + "lastname = '%s', country = '%s', born = %d, died = %d"
		// + "WHERE id = %d;", author.getFirstname(),
		// author.getLastname(), author.getCountry(),
		// author.getBorn(), author.getDied(), author.getId()));
		//
		// } catch (SQLException se) {
		// handleSQLException(se);
		// }
		//
		// return result != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.senft.jlibre.io.DBHandler#updateQuote(de.senft.jlibre.model.Quote)
	 */
	@Override
	public void updateQuote(Quote quote) {
		makeQuote(quote);
		// Statement stmt;
		// int result = 0;
		//
		// try {
		// stmt = conn.createStatement();
		// result = stmt.executeUpdate(String.format(
		// "UPDATE Quote SET quotetext = '%s',"
		// + "comment = '%s', bookid= %d" + "WHERE id = %d ",
		// quote.getText(), quote.getComment(), quote.getBook()
		// .getId(), quote.getId()));
		//
		// } catch (SQLException se) {
		// handleSQLException(se);
		// }
		//
		// return result != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#delete(de.senft.jlibre.model.Author)
	 */
	@Override
	public boolean delete(Author author) {
		Statement stmt;
		int result = 0;

		int authorId = author.getId();

		try {
			stmt = conn.createStatement();
			// Delete quotes of author (the inner query gets all IDs of quotes
			// by this author)
			stmt.executeUpdate(String
					.format("DELETE FROM QUOTE "
							+ "WHERE id IN "
							+ "(SELECT id FROM QUOTE, BOOK WHERE quote.bookid = book.id AND book.authorid = %d);",
							authorId));

			// Delete books of author
			stmt.executeUpdate(String.format(
					"DELETE FROM BOOK WHERE authorid = %d", authorId));

			// Delete author
			result = stmt.executeUpdate(String.format(
					"DELETE FROM AUTHOR WHERE id = %d", authorId));

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return result != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#delete(de.senft.jlibre.model.Book)
	 */
	@Override
	public boolean delete(Book book) {
		Statement stmt;
		int result = 0;

		int bookId = book.getId();

		try {
			stmt = conn.createStatement();
			// Delete quotes of this book
			result = stmt.executeUpdate(String.format(
					"DELETE FROM QUOTE WHERE bookid = %d", bookId));

			// Delete book
			result = stmt.executeUpdate(String.format(
					"DELETE FROM BOOK WHERE id = %d", bookId));

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return result != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#delete(de.senft.jlibre.model.Quote)
	 */
	@Override
	public boolean delete(Quote quote) {
		Statement stmt;
		int result = 0;

		try {
			stmt = conn.createStatement();
			result = stmt.executeUpdate(String.format(
					"DELETE FROM QUOTE WHERE id = %d", quote.getId()));

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return result != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#delete(de.senft.jlibre.model.Author[])
	 */
	@Override
	public boolean delete(Author[] authors) {
		boolean result = true;
		for (int i = 0; i < authors.length; i++) {
			if (!delete(authors[i]))
				result = false;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#delete(de.senft.jlibre.model.Book[])
	 */
	@Override
	public boolean delete(Book[] books) {
		boolean result = true;
		for (int i = 0; i < books.length; i++) {
			if (!delete(books[i]))
				result = false;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#delete(de.senft.jlibre.model.Quote[])
	 */
	@Override
	public boolean delete(Quote[] quotes) {
		boolean result = true;
		for (int i = 0; i < quotes.length; i++) {
			if (!delete(quotes[i]))
				result = false;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getCommonEpoches()
	 */
	@Override
	public String[] getCommonEpoches() {
		List<String> epoches = new ArrayList<String>();

		String sql = "SELECT DISTINCT epoche FROM BOOK;";

		Statement stmt;

		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String epoche = rs.getString("epoche");
				if (epoche != null && !epoche.equals(""))
					epoches.add(epoche);
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return epoches.toArray(new String[] {});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getCommonGenres()
	 */
	@Override
	public String[] getCommonGenres() {
		List<String> genres = new ArrayList<String>();

		String sql = "SELECT DISTINCT genre FROM BOOK;";

		Statement stmt;

		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String genre = rs.getString("genre");
				if (genre != null && !genre.equals(""))
					genres.add(genre);
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return genres.toArray(new String[] {});
	}

	private void handleSQLException(SQLException se) {
		logger.error("SQL Exception:");

		// Loop through the SQL Exceptions
		while (se != null) {
			logger.error("Message: " + se.getMessage());
			logger.error("Error  : " + se.getErrorCode());

			se = se.getNextException();
		}
	}
}