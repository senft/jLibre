package de.senft.jlibre.model;

import java.util.HashSet;

public class Author {
	private int id = -1;
	private String firstname;
	private String lastname;
	private String country;
	private int born;
	private int died;

	private HashSet<Book> books = null;

	public Author(String theFirstname, String theLastname) {
		firstname = theFirstname;
		lastname = theLastname;

		books = new HashSet<Book>();
	}

	public Author(int theId, String theFirstname, String theLastname) {
		id = theId;
		firstname = theFirstname;
		lastname = theLastname;

		books = new HashSet<Book>();
	}

	public Author() {
		books = new HashSet<Book>();
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(120);
		sb.append(firstname).append(" ").append(lastname);
		return sb.toString();
	}

	public int getId() {
		return id;
	}

	public HashSet<Quote> getQuotes() {
		HashSet<Quote> quotes = new HashSet<Quote>();

		for (Book b : books) {
			quotes.addAll(b.getQuotes());
		}

		return quotes;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public boolean addBook(Book book) {
		return books.add(book);
	}

	public boolean removeBook(Book book) {
		return books.remove(book);
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getBorn() {
		return born;
	}

	public void setBorn(int birth) {
		this.born = birth;
	}

	public int getDied() {
		return died;
	}

	public void setDied(int death) {
		this.died = death;
	}

	public void setId(int id) {
		this.id = id;
	}
}