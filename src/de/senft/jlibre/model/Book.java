package de.senft.jlibre.model;

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

	public static final int NOT_READ = -1;
	public static final int STARTED_READING = 0;
	public static final int FINISHED_READING = 1;

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

	public String getTitle() {
		return title;
	}

	public Date getStartedReading() {
		return startedReading;
	}

	public Date getFinishedReading() {
		return finishedReading;
	}

	public Author getAuthor() {
		return author;
	}

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	public void setStartedReading(Date startedReading) {
		this.startedReading = startedReading;
	}

	public void setFinishedReading(Date finishedReading) {
		this.finishedReading = finishedReading;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEpoche() {
		return epoche;
	}

	public void setEpoche(String epoche) {
		this.epoche = epoche;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * Checks if this book was read.
	 * 
	 * @return <ul>
	 *         <li><b>-1</b> if neither startedReading nor finishedReading has
	 *         been set</li>
	 *         <li><b>0</b> if startedReading has been set</li>
	 *         <li><b>1</b> if finishedReading, has been set</li>
	 */
	public int isRead() {
		if (startedReading == null)
			return NOT_READ;

		if (finishedReading == null)
			return STARTED_READING;

		return FINISHED_READING;
	}

	public static int datesRead(Date started, Date finished) {
		if (started == null)
			return NOT_READ;

		if (finished == null)
			return STARTED_READING;

		return FINISHED_READING;
	}
}
