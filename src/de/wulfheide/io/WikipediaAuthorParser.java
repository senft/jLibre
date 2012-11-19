package de.wulfheide.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.tidy.Tidy;

import de.wulfheide.model.Author;

public class WikipediaAuthorParser {

	protected static Logger logger = Logger.getLogger("WikiParser");

	public Author getAuthor(String firstname, String lastname) {
		Author author = new Author();
		Document document;

		String url = getWikiURL(firstname, lastname);

		try {
			logger.debug("Fetching " + url);
			document = new Tidy().parseDOM(new URL(url).openStream(), null);
		} catch (MalformedURLException e) {
			logger.error("Malformed URL, couldn't fetch " + url);
			return null;
		} catch (IOException e) {
			logger.error("IO error, couldn't fetch " + url);
			return null;
		}

		try {
			author.setBorn(getBirthdate(document));
		} catch (FetchException e) {
			logger.error("Couldn't fetch birthdate");
			return null;
		}

		try {
			author.setDied(getDeathdate(document));
		} catch (FetchException e) {
			logger.error("Couldn't fetch birthdate");
			return null;
		}

		return author;
	}

	public String getWikiURL(String firstname, String lastname) {
		firstname = firstname.replace(" ", "_");
		lastname = lastname.replace(" ", "_");
		String name = firstname + "_" + lastname;
		return "https://en.wikipedia.org/wiki/" + name;
	}

	private int getBirthdate(Document document) throws FetchException {
		int fetchedDate = getDateFromXPath(document, "//span[@class='bday']");

		if (fetchedDate == -1)
			throw new FetchException();

		logger.debug("Fetched deathdate " + fetchedDate);
		return fetchedDate;
	}

	private int getDeathdate(Document document) throws FetchException {
		int fetchedDate = getDateFromXPath(document,
				"//span[@class='dday deathdate']");

		if (fetchedDate == -1)
			throw new FetchException();

		logger.debug("Fetched deathdate " + fetchedDate);
		return fetchedDate;
	}

	private int getDateFromXPath(Document document, String sXpath) {
		// TODO Add error handling
		int date;
		String fetchedDate = "";
		Node nodeDate;
		XPath xpath = XPathFactory.newInstance().newXPath();

		try {
			nodeDate = (Node) xpath.compile(sXpath).evaluate(document,
					XPathConstants.NODE);

			if (nodeDate.hasChildNodes())
				fetchedDate = nodeDate.getFirstChild().getNodeValue();

		} catch (XPathExpressionException e) {
			logger.error("XPathExpressionException " + e.getMessage());
			logger.error(e.getStackTrace());
			return -1;
		}

		// TODO Might be better to parse the date with DateFormat
		try {
			date = Integer.valueOf(fetchedDate.substring(0, 4));
		} catch (NumberFormatException e) {
			date = -1;
		}
		return date;
	}
}
