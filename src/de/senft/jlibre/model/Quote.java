package de.senft.jlibre.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Quote {
	private int id;
	private String text;
	private String comment;
	private Book book;
	private String pages;

	public Quote() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "quote_id", unique = true)
	public int getId() {
		return id;
	}

	@Column(name = "quotetext")
	public String getText() {
		return text;
	}

	@JoinColumn(name = "book_id")
	@OneToOne()
	public Book getBook() {
		return book;
	}

	@Column(name = "comment")
	public String getComment() {
		return comment;
	}

	@Column(name = "pages")
	public String getPages() {
		return pages;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String toString() {
		return text;
	}

}
