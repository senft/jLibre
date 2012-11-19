package de.senft.jlibre.io.wiki;

import java.io.IOException;
import java.net.URL;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.tidy.Tidy;

public class WikipediaAuthorParser {

	protected static Logger logger = Logger
			.getLogger(WikipediaAuthorParser.class);

	private Document document;

	private String firstname;
	private String lastname;

	private String url;

	public WikipediaAuthorParser(String first, String last) throws IOException {
		this.firstname = first;
		this.lastname = last;

		url = getWikiURL();

		logger.debug("Fetching " + url);
		document = new Tidy().parseDOM(new URL(url).openStream(), null);

	}

	public String getWikiURL() {
		String url_firstname = firstname.replace(" ", "_");
		String url_lastname = lastname.replace(" ", "_");
		String name = url_firstname + "_" + url_lastname;
		return "https://en.wikipedia.org/wiki/" + name;
	}

	public int getBirthdate() throws FetchException {
		int fetchedDate = getDateFromXPath(document, "//span[@class='bday']");

		if (fetchedDate == -1)
			throw new FetchException("Couldn't fetch birthdate.");

		logger.debug("Fetched deathdate " + fetchedDate);
		return fetchedDate;
	}

	public int getDeathdate() throws FetchException {
		int fetchedDate = getDateFromXPath(document,
				"//span[@class='dday deathdate']");

		if (fetchedDate == -1)
			throw new FetchException("Couldn't fetch deathdate.");

		logger.debug("Fetched deathdate " + fetchedDate);
		return fetchedDate;
	}

	private int getDateFromXPath(Document document, String sXpath) {
		// TODO Add error handling
		int date = -1;
		String fetchedDate = "";
		Node nodeDate;
		XPath xpath = XPathFactory.newInstance().newXPath();

		try {
			nodeDate = (Node) xpath.compile(sXpath).evaluate(document,
					XPathConstants.NODE);

			if (nodeDate != null && nodeDate.hasChildNodes())
				fetchedDate = nodeDate.getFirstChild().getNodeValue();

		} catch (XPathExpressionException e) {
			logger.error("XPathExpressionException " + e.getMessage());
			logger.error(e.getStackTrace());
			return -1;
		}

		if (!fetchedDate.isEmpty()) {
			// TODO Might be better to parse the date with DateFormat
			try {
				date = Integer.valueOf(fetchedDate.substring(0, 4));
			} catch (NumberFormatException e) {
				logger.error("Got a date, but could not parse it: "
						+ fetchedDate + ".");
			}
		} else {
			logger.error("Fetched nothing.");
		}
		return date;
	}
}
