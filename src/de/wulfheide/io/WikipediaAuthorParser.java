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

	// TODO: Try to get a picture with this XPath:
	// /html[@class='client-js']/body[@class='mediawiki ltr sitedir-ltr ns-0
	// ns-subject page-E_T_A_Hoffmann action-view
	// skin-vector']/div[@id='content']/div[@id='bodyContent']/div[@class='mw-content-ltr']

	protected static Logger logger = Logger.getLogger("WikiParser");

	public Author getAuthor(String firstname, String lastname) {
		Document document;
		firstname = firstname.replace(" ", "_");
		lastname = lastname.replace(" ", "_");
		String name = firstname + "_" + lastname;
		String url = "https://en.wikipedia.org/wiki/" + name;

		Author author = null;

		logger.debug("Fetching " + url);

		try {
			document = new Tidy().parseDOM(new URL(url).openStream(), null);

		} catch (MalformedURLException e) {
			logger.error("Malformed URL " + url);
			return null;
		} catch (IOException e) {
			logger.error("Couldn't fetch " + url);
			return null;
		}

		author = new Author();
		author.setBorn(getBirthdate(document));
		author.setDied(getDeathdate(document));

		 getPicture(document);

		return author;
	}

	private void getPicture(Document document) {
		String fetchedPicture = "";
		Node picture;
		XPath xpath = XPathFactory.newInstance().newXPath();

		System.out.println(document.getTextContent());

		// try {
		// picture = (Node) xpath
		// .compile(
		// "//tbody/tr[2]/td/a[@class='image']/img/@src")
		// .evaluate(document, XPathConstants.NODE);
		//
		// if (picture.hasChildNodes())
		// fetchedPicture = picture.getFirstChild().getNodeValue();
		//
		// } catch (XPathExpressionException e) {
		// logger.error("XPathExpressionException " + e.getMessage());
		// logger.error(e.getStackTrace());
		// return;
		// }

		logger.debug("Fetched picture " + fetchedPicture);
	}

	private int getBirthdate(Document document) {
		// TODO Add error handling
		String fetchedBirth = "";
		Node birthdate;
		XPath xpath = XPathFactory.newInstance().newXPath();

		try {
			birthdate = (Node) xpath.compile("//span[@class='bday']").evaluate(
					document, XPathConstants.NODE);

			if (birthdate.hasChildNodes())
				fetchedBirth = birthdate.getFirstChild().getNodeValue();

		} catch (XPathExpressionException e) {
			logger.error("XPathExpressionException " + e.getMessage());
			logger.error(e.getStackTrace());
			return -1;
		}

		logger.debug("Fetched birthdate " + fetchedBirth);
		// TODO Might be better to parse the date with DateFormat
		return Integer.valueOf(fetchedBirth.substring(0, 4));

	}

	private int getDeathdate(Document document) {
		// TODO Add error handling
		String fetchedDeath = "";
		Node deathdate;
		XPath xpath = XPathFactory.newInstance().newXPath();

		try {
			deathdate = (Node) xpath.compile("//span[@class='dday deathdate']")
					.evaluate(document, XPathConstants.NODE);

			if (deathdate.hasChildNodes())
				fetchedDeath = deathdate.getFirstChild().getNodeValue();

		} catch (XPathExpressionException e) {
			logger.error("XPathExpressionException " + e.getMessage());
			logger.error(e.getStackTrace());
			return -1;
		}

		logger.debug("Fetched deathdate " + fetchedDeath);
		// TODO Might be better to parse the date with DateFormat
		return Integer.valueOf(fetchedDeath.substring(0, 4));

	}
}
