package de.senft.jlibre.model;

public class Quote {
	private int id;
	private String text;
	private String comment;
	private Book book;

	public Quote() {
	}

	public Quote(int theId, String theText, String theComment, Book theBook) {
		id = theId;
		text = theText;
		comment = theComment;
		book = theBook;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String toString() {
		return text;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
