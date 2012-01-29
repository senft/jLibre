package de.wulfheide.model;

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

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(120);
		sb.append(firstname).append(" ").append(lastname);
		return sb.toString();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the quotes
	 */
	public HashSet<Quote> getQuotes() {
		HashSet<Quote> quotes = new HashSet<Quote>();

		for (Book b : books) {
			quotes.addAll(b.getQuotes());
		}

		return quotes;
	}

	/**
	 * @return the books
	 */
	public HashSet<Book> getBooks() {
		return books;
	}

	/**
	 * @param book
	 * @return
	 * @see java.util.HashSet#add(java.lang.Object)
	 */
	public boolean addBook(Book book) {
		return books.add(book);
	}

	/**
	 * @param book
	 * @return
	 * @see java.util.HashSet#remove(java.lang.Object)
	 */
	public boolean removeBook(Book book) {
		return books.remove(book);
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the birth
	 */
	public int getBorn() {
		return born;
	}

	/**
	 * @param birth the birth to set
	 */
	public void setBorn(int birth) {
		this.born = birth;
	}

	/**
	 * @return the death
	 */
	public int getDied() {
		return died;
	}

	/**
	 * @param death the death to set
	 */
	public void setDied(int death) {
		this.died = death;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
}
