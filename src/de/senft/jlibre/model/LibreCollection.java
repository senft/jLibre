package de.senft.jlibre.model;

import java.util.List;

import de.senft.jlibre.io.DBHandler;
import de.senft.jlibre.io.HSQLHibernateHandler;

public class LibreCollection {

	protected DBHandler dbHandler;

	private List<Author> authors;
	private List<Book> books;
	private List<Quote> quotes;

	public LibreCollection() {
		dbHandler = HSQLHibernateHandler.getInstance();

		authors = dbHandler.getAuthors();
		books = dbHandler.getBooks();
		quotes = dbHandler.getQuotes();
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public List<Quote> getQuotes() {
		return quotes;
	}

	public void setQuotes(List<Quote> quotes) {
		this.quotes = quotes;
	}

}
