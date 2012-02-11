package de.wulfheide.persistency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import de.wulfheide.model.Author;
import de.wulfheide.model.Book;
import de.wulfheide.model.Quote;

public class DBHandler {
	/*
	 * TODO: Refactor. Change all 'CALL IDENTITIY()' to PreparedStatements with
	 * getGeneratedKeys()
	 */

	private static DBHandler dbHandler;
	private static Logger logger = Logger.getLogger("DBHandler");

	private Connection conn;

	private DBHandler() {
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

	public static DBHandler getInstance() {
		if (dbHandler == null) {
			logger.debug("initialising new DBHandler");
			dbHandler = new DBHandler();
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
	public Vector<Vector<Object>> getBooksForTable() {
		Statement stmt;
		Vector<Vector<Object>> result = new Vector<Vector<Object>>();

		try {
			stmt = conn.createStatement();

			// Execute the query
			ResultSet rs = stmt
					.executeQuery("SELECT book.id, book.title, author.firstname,"
							+ " author.lastname, book.pubyear, book.genre, book.epoche,"
							+ " book.startread, book.finishread"
							+ " FROM BOOK, AUTHOR"
							+ " WHERE book.authorid = author.id;");

			while (rs.next()) {
				Vector<Object> current = new Vector<Object>();
				current.add(rs.getInt("id"));
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
	public Vector<Vector<Object>> getAuthorsForTable() {
		Statement stmt;
		Vector<Vector<Object>> result = new Vector<Vector<Object>>();

		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT id, firstname, lastname, born, died, country FROM AUTHOR;");

			while (rs.next()) {
				Vector<Object> current = new Vector<Object>();
				current.add(rs.getInt("id"));
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
	public Vector<Vector<Object>> getQuotesForTable() {
		Statement stmt;
		Vector<Vector<Object>> result = new Vector<Vector<Object>>();

		try {
			stmt = conn.createStatement();

			// Execute the query
			ResultSet rs = stmt
					.executeQuery("SELECT quote.id, quote.quotetext, book.title,"
							+ "author.firstname, author.lastname"
							+ " FROM QUOTE, BOOK, AUTHOR"
							+ " WHERE quote.bookid = book.id"
							+ " AND author.id = book.authorid;");

			while (rs.next()) {

				Vector<Object> current = new Vector<Object>();
				current.add(rs.getInt("id"));
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

	public Object[][] getBooksForComboBox() {
		Statement stmt;
		ArrayList<Object[]> result = new ArrayList<Object[]>();

		try {
			stmt = conn.createStatement();

			// Execute the query
			ResultSet rs = stmt
					.executeQuery("SELECT book.id, book.title, book.pubyear, author.firstname, author.lastname FROM BOOK, AUTHOR WHERE book.authorid = author.id;;");

			while (rs.next()) {
				int id = rs.getInt("id");
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

	public Object[][] getAuthorsForComboBox() {
		Statement stmt;
		ArrayList<Object[]> result = new ArrayList<Object[]>();

		try {
			stmt = conn.createStatement();

			// Execute the query
			ResultSet rs = stmt
					.executeQuery("SELECT id, firstname, lastname FROM AUTHOR;");

			while (rs.next()) {
				result.add(new Object[] {
						rs.getInt("id"),
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

	/**
	 * Fetches all necessary data from the DB to create a
	 * {@link de.wulfheide.model.Book Book} object, containing all data
	 * displayed on an info panel.
	 * 
	 * @param id the books id
	 * @return an {@link de.wulfheide.model.Book Book} object containing all the
	 *         data
	 */
	public Book getBook(int id) {
		Statement stmt;
		Book book = null;

		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt
					.executeQuery(String
							.format("SELECT book.id, book.authorid, book.title, book.startread, "
									+ "book.finishread, book.comment, book.epoche, book.genre, "
									+ "author.firstname, author.lastname, book.pubyear "
									+ "FROM BOOK, AUTHOR "
									+ "WHERE author.id = book.authorid "
									+ "AND book.id=%d;", id));

			while (rs.next()) {
				book = new Book(id, rs.getString("title"), new Author(
						rs.getInt("authorid"), rs.getString("firstname"),
						rs.getString("lastname")), rs.getInt("pubyear"),
						rs.getDate("startread"), rs.getDate("finishread"),
						rs.getString("comment"), rs.getString("epoche"),
						rs.getString("genre"));
			}

			// Get quotes from this book
			rs = stmt.executeQuery(String.format(
					"SELECT id, quotetext FROM QUOTE WHERE bookid=%d;", id));

			while (rs.next()) {
				Quote quote = new Quote();
				quote.setId(rs.getInt("id"));
				quote.setText(rs.getString("quotetext"));
				book.addQuote(quote);
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return book;
	}

	/**
	 * Fetches all necessary data from the DB to create an
	 * {@link de.wulfheide.model.Author Author} object, containing all data
	 * displayed on an info panel.
	 * 
	 * @param id the authors id
	 * @return an {@link de.wulfheide.model.Author Author} object containing all
	 *         the data
	 */
	public Author getAuthor(int id) {
		Statement stmt;
		Author author = null;

		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(String.format(
					"SELECT id, firstname, lastname, country, born, died "
							+ "FROM AUTHOR " + "WHERE id=%d;", id));

			while (rs.next()) {
				author = new Author(id, rs.getString("firstname"),
						rs.getString("lastname"));
				author.setCountry(rs.getString("country"));
				author.setBorn(rs.getInt("born"));
				author.setDied(rs.getInt("died"));
			}

			// Get books by this author
			rs = stmt.executeQuery(String.format("SELECT id, title, pubyear "
					+ "FROM BOOK WHERE authorid=%d", id));

			while (rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt("id"));
				book.setTitle(rs.getString("title"));
				book.setPublicationYear(rs.getInt("pubyear"));
				author.addBook(book);

				// Get quotes of this author
				ResultSet rsQuotes = stmt.executeQuery(String.format(
						"SELECT quotetext FROM QUOTE" + " WHERE bookid = %d;",
						book.getId()));

				while (rsQuotes.next()) {
					Quote quote = new Quote();
					quote.setText(rsQuotes.getString("quotetext"));
					quote.setBook(book);
					book.addQuote(quote);
				}
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return author;
	}

	/**
	 * Fetches all necessary data from the DB to create a
	 * {@link de.wulfheide.model.Quote Quote} object, containing all data
	 * displayed on an info panel.
	 * 
	 * @param id the books id
	 * @return an {@link de.wulfheide.model.Book Book} object containing all the
	 *         data
	 */
	public Quote getQuote(int id) {
		Statement stmt;
		Quote quote = null;
		int bookId = -1;

		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(String.format(
					"SELECT id, quotetext, comment, bookid FROM QUOTE "
							+ "WHERE id = %d;", id));

			while (rs.next()) {
				quote = new Quote();
				quote.setId(id);
				quote.setText(rs.getString("quotetext"));
				quote.setComment(rs.getString("comment"));
				bookId = rs.getInt("bookid");
			}

			if (bookId == -1) // SQLException probably is misleading here
				throw new SQLException();

			// // Get book of this quote
			rs = stmt.executeQuery(String.format(
					"SELECT book.id, book.title, book.pubyear, author.firstname,"
							+ "author.lastname FROM BOOK, AUTHOR"
							+ " WHERE book.id = %d"
							+ " AND book.authorid = author.id", bookId));

			while (rs.next()) {
				Book book = new Book();
				Author author = new Author();

				author.setFirstname(rs.getString("firstname"));
				author.setLastname(rs.getString("lastname"));

				book.setId(rs.getInt("id"));
				book.setTitle(rs.getString("title"));
				book.setPublicationYear(rs.getInt("pubyear"));
				book.setAuthor(author);
				quote.setBook(book);
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return quote;
	}

	/**
	 * Stores an {@link de.wulfheide.model.Book Book} in the DB and returns the
	 * id (created by the DB) for this quote.
	 * 
	 * @param book the quote to store
	 * @return the id of this book (-1 if something went wrong)
	 */
	public int makeBook(Book book) {
		PreparedStatement stmt;
		int rowsChanged = 0;
		int id = -1;

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
			stmt = conn
					.prepareStatement(
							String.format(
									"INSERT INTO BOOK (authorid, title, comment,"
											+ " epoche, genre, pubyear, startread, finishread)"
											+ " VALUES (%d, '%s', '%s', '%s', '%s', %d, ?, ?);",
									authorId, title, comment, epoche, genre,
									published),
							PreparedStatement.RETURN_GENERATED_KEYS);

			stmt.setDate(1, sqlStarted);
			stmt.setDate(2, sqlFinished);

			logger.debug("Executing query: " + stmt.toString());

			// TODO: get real id here
			rowsChanged = stmt.executeUpdate();
			if (rowsChanged != 0) {
				logger.debug("Changed " + rowsChanged + " row(s) in DB");

				ResultSet rs = stmt.getGeneratedKeys();
				while (rs.next())
					id = rs.getInt(1);
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return id;
	}

	/**
	 * Stores an {@link de.wulfheide.model.Author Author} in the DB and returns
	 * the id (created by the DB) for this author.
	 * 
	 * @param author the author to store
	 * @return the id of this author (-1 if someting went wrong)
	 */
	public int makeAuthor(Author author) {
		Statement stmt;
		int result = 0;
		int id = -1;

		String firstname = author.getFirstname();
		String lastname = author.getLastname();
		int born = author.getBorn();
		int died = author.getDied();
		String country = author.getCountry();

		try {
			stmt = conn.createStatement();
			result = stmt.executeUpdate(String.format(
					"INSERT INTO AUTHOR (firstname, lastname, born, died, country)"
							+ "VALUES ('%s', '%s', %d, %d, '%s');", firstname,
					lastname, born, died, country));

			if (result != 0) {
				// When we inserted something -> get the created ID(s)
				// (hopefully this is == 1)
				ResultSet rs = stmt.executeQuery("CALL IDENTITY();");
				while (rs.next())
					id = rs.getInt(1);
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return id;
	}

	/**
	 * Stores an {@link de.wulfheide.model.Quote Quote} in the DB, and returns
	 * the id (created by the DB) for this quote.
	 * 
	 * @param quote the quote to store
	 * @return the id of this quote (-1 if someting went wrong)
	 */
	public int makeQuote(Quote quote) {
		Statement stmt;
		int result = 0;
		int id = -1;

		String text = quote.getText();
		String comment = quote.getComment();
		Book book = quote.getBook();

		try {
			stmt = conn.createStatement();
			result = stmt.executeUpdate(String.format(
					"INSERT INTO QUOTE (quotetext, comment, bookid)"
							+ "VALUES ('%s', '%s', %d);", text, comment,
					book.getId()));

			if (result != 0) {
				// When we inserted something -> get the created ID(s)
				// (hopefully this is == 1)
				ResultSet rs = stmt.executeQuery("CALL IDENTITY();");
				while (rs.next())
					id = rs.getInt(1);
			}

		} catch (SQLException se) {
			handleSQLException(se);
		}

		return id;
	}

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

	public boolean delete(Author[] authors) {
		boolean result = true;
		for (int i = 0; i < authors.length; i++) {
			if (!delete(authors[i]))
				result = false;
		}
		return result;
	}

	public boolean delete(Book[] books) {
		boolean result = true;
		for (int i = 0; i < books.length; i++) {
			if (!delete(books[i]))
				result = false;
		}
		return result;
	}

	public boolean delete(Quote[] quotes) {
		boolean result = true;
		for (int i = 0; i < quotes.length; i++) {
			if (!delete(quotes[i]))
				result = false;
		}
		return result;
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