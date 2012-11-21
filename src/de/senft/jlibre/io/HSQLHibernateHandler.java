package de.senft.jlibre.io;

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

	private HSQLHibernateHandler() {

	}

	public static HSQLHibernateHandler getInstance() {
		if (dbHandler == null) {
			logger.debug("initialising new DBHandler");
			dbHandler = new HSQLHibernateHandler();
		}
		return dbHandler;
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
	public int save(Book book) {
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
	public int save(Author author) {
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
	public int save(Quote quote) {
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
	public void update(Book book) {
		save(book);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.senft.jlibre.io.DBHandler#updateAuthor(de.senft.jlibre.model.Author)
	 */
	@Override
	public void update(Author author) {
		save(author);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.senft.jlibre.io.DBHandler#updateQuote(de.senft.jlibre.model.Quote)
	 */
	@Override
	public void update(Quote quote) {
		save(quote);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#delete(de.senft.jlibre.model.Author)
	 */
	@Override
	public void delete(Author author) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#delete(de.senft.jlibre.model.Book)
	 */
	@Override
	public void delete(Book book) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.senft.jlibre.io.DBHandler#delete(de.senft.jlibre.model.Quote)
	 */
	@Override
	public void delete(Quote quote) {
	}
}