package de.senft.jlibre.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.senft.jlibre.model.Author;
import de.senft.jlibre.model.Book;
import de.senft.jlibre.model.Quote;
import de.senft.jlibre.util.HibernateUtil;

public class HSQLHibernateHandler implements DBHandler {
	private static HSQLHibernateHandler dbHandler;
	private static Logger logger = Logger.getLogger(HSQLHibernateHandler.class);

	private Connection conn;

	private HSQLHibernateHandler() {
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
	 * @see de.senft.jlibre.io.DBHandler#getBooksForTable()
	 */
	@Override
	public Vector<Vector<Object>> getBooksForTable() {
		Statement stmt;
		Vector<Vector<Object>> result = new Vector<Vector<Object>>();

		try {
			stmt = conn.createStatement();

			// Execute the query
			ResultSet rs = stmt
					.executeQuery("SELECT book_id, book.title, author.firstname,"
							+ " author.lastname, book.pubyear, book.genre, book.epoche,"
							+ " book.startread, book.finishread"
							+ " FROM BOOK, AUTHOR"
							+ " WHERE book.author_id = author.author_id;");

			while (rs.next()) {
				Vector<Object> current = new Vector<Object>();
				current.add(rs.getInt("book_id"));
				current.add(rs.getString("title"));
				current.add(rs.getString("firstname") + " "
						+ rs.getString("lastname"));
				current.add(rs.getInt("pubyear"));
				current.add(rs.getString("epoche"));
				current.add(rs.getString("genre"));
				current.add(Book.datesRead(rs.getDate("startread"),
						rs.getDate("finishread")));

				result.add(current);
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}
		logger.debug(String.format("Loaded table represetation for %d books",
				result.size()));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getAuthorsForTable()
	 */
	@Override
	public Vector<Vector<Object>> getAuthorsForTable() {
		Statement stmt;
		Vector<Vector<Object>> result = new Vector<Vector<Object>>();

		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT author_id, firstname, lastname, born, died, country FROM AUTHOR;");

			while (rs.next()) {
				Vector<Object> current = new Vector<Object>();
				current.add(rs.getInt("author_id"));
				current.add(rs.getString("firstname"));
				current.add(rs.getString("lastname"));
				current.add(rs.getInt("born"));
				current.add(rs.getInt("died"));
				current.add(rs.getString("country"));
				result.add(current);
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}
		logger.debug(String.format("Loaded table represetation for %d authors",
				result.size()));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#getQuotesForTable()
	 */
	@Override
	public Vector<Vector<Object>> getQuotesForTable() {
		Statement stmt;
		Vector<Vector<Object>> result = new Vector<Vector<Object>>();

		try {
			stmt = conn.createStatement();

			// Execute the query
			ResultSet rs = stmt
					.executeQuery("SELECT quote_id, quote.quotetext, book.title,"
							+ "author.firstname, author.lastname"
							+ " FROM QUOTE, BOOK, AUTHOR"
							+ " WHERE quote.book_id = book.book_id"
							+ " AND author.author_id = book.author_id;");

			while (rs.next()) {

				Vector<Object> current = new Vector<Object>();
				current.add(rs.getInt("quote_id"));
				current.add(rs.getString("quotetext"));
				current.add(rs.getString("title"));
				current.add(rs.getString("firstname") + " "
						+ rs.getString("lastname"));

				result.add(current);
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}
		logger.debug(String.format("Loaded table represetation for %d quotes",
				result.size()));
		return result;
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
		session.save(book);
		tx.commit();
		session.close();
		return book.getId();

		// PreparedStatement stmt;
		// int rowsChanged = 0;
		// int id = -1;
		//
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
		// stmt = conn
		// .prepareStatement(
		// String.format(
		// "INSERT INTO BOOK (authorid, title, comment,"
		// + " epoche, genre, pubyear, startread, finishread)"
		// + " VALUES (%d, '%s', '%s', '%s', '%s', %d, ?, ?);",
		// authorId, title, comment, epoche, genre,
		// published),
		// PreparedStatement.RETURN_GENERATED_KEYS);
		//
		// stmt.setDate(1, sqlStarted);
		// stmt.setDate(2, sqlFinished);
		//
		// logger.debug("Executing query: " + stmt.toString());
		//
		// rowsChanged = stmt.executeUpdate();
		// if (rowsChanged != 0) {
		// logger.debug("Changed " + rowsChanged + " row(s) in DB");
		//
		// ResultSet rs = stmt.getGeneratedKeys();
		// while (rs.next())
		// id = rs.getInt(1);
		// }
		//
		// } catch (SQLException se) {
		// handleSQLException(se);
		// }
		//
		// return id;
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
		session.save(author);
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
		session.save(quote);
		tx.commit();
		session.close();
		return quote.getId();

		// Statement stmt;
		// int result = 0;
		// int id = -1;
		//
		// String text = quote.getText();
		// String comment = quote.getComment();
		// Book book = quote.getBook();
		//
		// try {
		// stmt = conn.createStatement();
		// result = stmt.executeUpdate(String.format(
		// "INSERT INTO QUOTE (quotetext, comment, bookid)"
		// + "VALUES ('%s', '%s', %d);", text, comment,
		// book.getId()));
		//
		// if (result != 0) {
		// // When we inserted something -> get the created ID(s)
		// // (hopefully this is == 1)
		// ResultSet rs = stmt.executeQuery("CALL IDENTITY();");
		// while (rs.next())
		// id = rs.getInt(1);
		// }
		//
		// } catch (SQLException se) {
		// handleSQLException(se);
		// }
		//
		// return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#updateBook(de.senft.jlibre.model.Book)
	 */
	@Override
	public boolean updateBook(Book book) {
		PreparedStatement stmt;
		int rowsChanged = 0;

		int bookId = book.getId();
		String title = book.getTitle();
		String epoche = book.getEpoche();
		String comment = book.getComment();
		String genre = book.getGenre();
		int authorId = book.getAuthor().getId();
		int published = book.getPublicationYear();
		Date started = book.getStartedReading();
		Date finished = book.getFinishedReading();

		java.sql.Date sqlStarted = null;
		java.sql.Date sqlFinished = null;

		if (started != null)
			sqlStarted = new java.sql.Date(started.getTime());

		if (finished != null)
			sqlFinished = new java.sql.Date(finished.getTime());

		try {
			stmt = conn.prepareStatement(String.format(
					"UPDATE Book SET authorid = %d, title = '%s',"
							+ " pubyear = %d, startread = ?, finishread = ?,"
							+ " comment = '%s', epoche = '%s', genre = '%s'"
							+ " WHERE id = %d;", authorId, title, published,
					comment, epoche, genre, bookId));

			if (sqlStarted == null)
				stmt.setNull(1, java.sql.Types.DATE);
			else
				stmt.setDate(1, sqlStarted);

			if (sqlFinished == null)
				stmt.setNull(2, java.sql.Types.DATE);
			else
				stmt.setDate(2, sqlFinished);

			rowsChanged = stmt.executeUpdate();

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return rowsChanged != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.senft.jlibre.io.DBHandler#updateAuthor(de.senft.jlibre.model.Author)
	 */
	@Override
	public boolean updateAuthor(Author author) {
		Statement stmt;
		int result = 0;

		try {
			stmt = conn.createStatement();
			result = stmt
					.executeUpdate(String.format(
							"UPDATE Author SET firstname = '%s',"
									+ "lastname = '%s', country = '%s', born = %d, died = %d"
									+ "WHERE id = %d;", author.getFirstname(),
							author.getLastname(), author.getCountry(),
							author.getBorn(), author.getDied(), author.getId()));

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return result != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.senft.jlibre.io.DBHandler#updateQuote(de.senft.jlibre.model.Quote)
	 */
	@Override
	public boolean updateQuote(Quote quote) {
		Statement stmt;
		int result = 0;

		try {
			stmt = conn.createStatement();
			result = stmt.executeUpdate(String.format(
					"UPDATE Quote SET quotetext = '%s',"
							+ "comment = '%s', bookid= %d" + "WHERE id = %d ",
					quote.getText(), quote.getComment(), quote.getBook()
							.getId(), quote.getId()));

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return result != 0;
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