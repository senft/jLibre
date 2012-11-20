package de.senft.jlibre.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity()
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
	private Set<Quote> quotes = new HashSet<Quote>();

	public static final byte NOT_READ = -1;
	public static final byte STARTED_READING = 0;
	public static final byte FINISHED_READING = 1;

	public Book() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id", unique = true)
	public int getId() {
		return id;
	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	@Column(name = "startread", nullable = true)
	public Date getStartedReading() {
		return startedReading;
	}

	@Column(name = "finishread", nullable = true)
	public Date getFinishedReading() {
		return finishedReading;
	}

	@Column(name = "pubyear")
	public int getPublicationYear() {
		return publicationYear;
	}

	@Column(name = "comment")
	public String getComment() {
		return comment;
	}

	@Column(name = "epoche")
	public String getEpoche() {
		return epoche;
	}

	@Column(name = "genre")
	public String getGenre() {
		return genre;
	}

	@JoinColumn(name = "author_id")
	@OneToOne()
	public Author getAuthor() {
		return author;
	}

	// TODO: Maybe there is a way, to do this lazy
	@OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
	public Set<Quote> getQuotes() {
		return quotes;
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

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setEpoche(String epoche) {
		this.epoche = epoche;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setQuotes(Set<Quote> quotes) {
		this.quotes = quotes;
	}

	public boolean addQuote(Quote q) {
		return quotes.add(q);
	}

	/**
	 * Checks if this book was read.
	 * 
	 * @return <ul>
	 *         <li>{@link #NOT_READ} if neither startedReading nor
	 *         finishedReading has been set</li>
	 *         <li>{@link #STARTED_READING} if startedReading has been set</li>
	 *         <li>{@link #finishedReading} if finishedReading, has been set</li>
	 */
	@Transient
	public int isRead() {
		// TODO: maybe make this a @GeneratedValue
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

	public String toString() {
		StringBuilder sb = new StringBuilder(200);
		sb.append(author.toString()).append(": ");
		sb.append(title);
		sb.append(", ").append(publicationYear);
		return sb.toString();
	}
}
