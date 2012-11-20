package de.senft.jlibre.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "AUTHOR")
public class Author implements Serializable {

	private int id = 0;
	private String firstname;
	private String lastname;
	private String country;
	private int born;
	private int died;

	private Set<Book> books = new HashSet<Book>();;

	public Author() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "firstname", nullable = false)
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(name = "lastname", nullable = false)
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Transient
	public HashSet<Quote> getQuotes() {
		HashSet<Quote> quotes = new HashSet<Quote>();

		for (Book b : books) {
			quotes.addAll(b.getQuotes());
		}

		return quotes;
	}

	@Transient
	public Set<Book> getBooks() {
		return books;
	}

	public boolean addBook(Book book) {
		return books.add(book);
	}

	public boolean removeBook(Book book) {
		return books.remove(book);
	}

	@Column(name = "country")
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

	public String toString() {
		StringBuilder sb = new StringBuilder(120);
		sb.append(firstname).append(" ").append(lastname);
		return sb.toString();
	}
}