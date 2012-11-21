package de.senft.jlibre.model;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

	public String[] getCommonEpoches() {
		Set<String> epoches = new TreeSet<String>();
		for (Book b : books) {
			String epoche = b.getEpoche();
			if (epoche != null && !epoche.isEmpty())
				epoches.add(b.getEpoche());
		}
		return epoches.toArray(new String[0]);
	};

	public String[] getCommonGenres() {
		Set<String> genres = new TreeSet<String>();
		for (Book b : books) {
			String genre = b.getGenre();
			if (genre != null && !genre.isEmpty())
				genres.add(b.getGenre());
		}
		return genres.toArray(new String[0]);
	};

}
