package de.wulfheide.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Book {
	private int id;
	private String title;
	private String comment;
	private String epoche;
	private String genre;
	private int publicationYear;
	private Date startedReading;
	private Date finishedReading;

	private Author author;
	private Set<Quote> quotes;

	public Book() {
		quotes = new HashSet<Quote>();
	}

	public Book(int theId, String theTitle, Author theAuthor,
			int thePublicationYear, Date theStartedReading,
			Date theFinishedReading, String theComment, String theEpoche,
			String theGenre) {
		this();
		id = theId;
		title = theTitle;
		author = theAuthor;
		publicationYear = thePublicationYear;
		startedReading = theStartedReading;
		finishedReading = theFinishedReading;

		comment = theComment;
		epoche = theEpoche;
		genre = theGenre;
	}

	public boolean addQuote(Quote q) {
		return quotes.add(q);
	}

	public Set<Quote> getQuotes() {
		return quotes;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the startedReading
	 */
	public Date getStartedReading() {
		return startedReading;
	}

	/**
	 * @return the finishedReading
	 */
	public Date getFinishedReading() {
		return finishedReading;
	}

	/**
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * @return the publicationYear
	 */
	public int getPublicationYear() {
		return publicationYear;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(200);
		sb.append(author.toString()).append(": ");
		sb.append(title);
		sb.append(", ").append(publicationYear);
		return sb.toString();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param publicationYear the publicationYear to set
	 */
	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	/**
	 * @param startedReading the startedReading to set
	 */
	public void setStartedReading(Date startedReading) {
		this.startedReading = startedReading;
	}

	/**
	 * @param finishedReading the finishedReading to set
	 */
	public void setFinishedReading(Date finishedReading) {
		this.finishedReading = finishedReading;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the epoche
	 */
	public String getEpoche() {
		return epoche;
	}

	/**
	 * @param epoche the epoche to set
	 */
	public void setEpoche(String epoche) {
		this.epoche = epoche;
	}

	/**
	 * @return the genre
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * @param genre the genre to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}
}
