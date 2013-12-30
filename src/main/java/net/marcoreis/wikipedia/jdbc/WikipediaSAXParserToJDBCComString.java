package net.marcoreis.wikipedia.jdbc;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.marcoreis.wikipedia.vo.PaginaWikipedia;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WikipediaSAXParserToJDBCComString extends DefaultHandler {
    private static String nomeArquivo = System.getProperty("user.home")
	    + "/dados/ptwiki-20130817-pages-articles-multistream.xml";
    private static Logger logger = Logger
	    .getLogger(WikipediaSAXParserToJDBCComString.class);
    private PaginaWikipedia pagina;
    private String content;
    // Formato da data no dump da Wikipedia
    private SimpleDateFormat sdf = new SimpleDateFormat(
	    "yyyy-MM-dd'T'HH:mm:ss'Z'");
    private int paginasIndexadas;
    private DAOPaginaWikipedia dao = new DAOPaginaWikipedia();

    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	if (qName.equals("page")) {
	    pagina = new PaginaWikipedia();
	    content = "";
	} else if (qName.equals("title")) {
	    content = "";
	} else if (qName.equals("timestamp")) {
	    content = "";
	} else if (qName.equals("username")) {
	    content = "";
	} else if (qName.equals("text")) {
	    content = "";
	} else if (qName.equals("model")) {
	    content = "";
	} else if (qName.equals("format")) {
	    content = "";
	} else if (qName.equals("comment")) {
	    content = "";
	} else if (qName.equals("id")) {
	    content = "";
	}
    }

    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	content += (String.copyValueOf(ch, start, length).trim());
    }

    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	if (qName.equals("page")) {
	    dao.inserir(pagina);
	    paginasIndexadas++;
	    // pagina = null;
	    logAndamento();
	} else if (qName.equals("title")) {
	    pagina.setTitle(content);
	} else if (qName.equals("timestamp")) {
	    try {
		Date timestamp = sdf.parse(content);
		pagina.setTimeStamp(timestamp);
	    } catch (ParseException e) {
		logger.error(e);
	    }
	} else if (qName.equals("username")) {
	    pagina.setUserName(content);
	} else if (qName.equals("text")) {
	    pagina.setText(content);
	} else if (qName.equals("model")) {
	    pagina.setModel(content);
	} else if (qName.equals("format")) {
	    pagina.setFormat(content);
	} else if (qName.equals("comment")) {
	    pagina.setComment(content);
	} else if (qName.equals("id")) {
	    if (pagina.getId() == null)
		pagina.setId(new Long(content));
	}
    }

    private void logAndamento() {
	if (paginasIndexadas % 10000 == 0) {
	    logger.info("Parcial: " + paginasIndexadas);
	}
    }

    public static void main(String[] args) {
	new WikipediaSAXParserToJDBCComString().parse();
    }

    public void parse() {
	try {
	    logger.info("Inicio");
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    factory.setNamespaceAware(true);
	    SAXParser parser = factory.newSAXParser();
	    parser.parse(new File(nomeArquivo), this);
	} catch (Exception e) {
	    logger.error(e);
	}
    }
}
