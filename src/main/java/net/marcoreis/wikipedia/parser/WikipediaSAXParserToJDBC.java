package net.marcoreis.wikipedia.parser;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.marcoreis.wikipedia.dao.DAOPaginaWikipedia;
import net.marcoreis.wikipedia.vo.PaginaWikipedia;

public class WikipediaSAXParserToJDBC extends DefaultHandler {
    private static final int BATCH_SIZE = 10000;
    private static String nomeArquivo = System.getProperty("filename");
    private static Logger logger = LoggerFactory.getLogger(WikipediaSAXParserToJDBC.class);
    private PaginaWikipedia pagina = new PaginaWikipedia();
    private StringBuilder content = new StringBuilder();
    // Formato da data no dump da Wikipedia
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private int paginasIndexadas;
    private DAOPaginaWikipedia daoPaginaWikipedia;
//    private DAOCategoriaWikipedia daoCategoriaWikipedia;
//    private CategoryExtractor extractor;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("page")) {
            pagina.clear();
            content.setLength(0);
        } else if (qName.equals("title")) {
            content.setLength(0);
        } else if (qName.equals("timestamp")) {
            content.setLength(0);
        } else if (qName.equals("username")) {
            content.setLength(0);
        } else if (qName.equals("text")) {
            content.setLength(0);
        } else if (qName.equals("model")) {
            content.setLength(0);
        } else if (qName.equals("format")) {
            content.setLength(0);
        } else if (qName.equals("comment")) {
            content.setLength(0);
        } else if (qName.equals("id")) {
            content.setLength(0);
        } else {
            logger.info("qName: " + qName);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        content.append(String.copyValueOf(ch, start, length));
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("page")) {
            daoPaginaWikipedia.inserir(pagina);
            paginasIndexadas++;
            logAndamento();
        } else if (qName.equals("title")) {
            pagina.setTitle(content.toString());
        } else if (qName.equals("timestamp")) {
            try {
                Date timestamp = sdf.parse(content.toString());
                pagina.setTimeStamp(timestamp);
            } catch (ParseException e) {
                logger.error(e.toString());
            }
        } else if (qName.equals("username")) {
            pagina.setUserName(content.toString());
        } else if (qName.equals("text")) {
            String text = content.toString();
            pagina.setText(text);

//            extractor.extractAndAddText(text);
//            extractor.getCategories().stream()
//                    .forEach(category -> daoCategoriaWikipedia.inserir(new CategoriaWikipedia(category)));
//            extractor.getCategories().clear();

        } else if (qName.equals("model")) {
            pagina.setModel(content.toString());
        } else if (qName.equals("format")) {
            pagina.setFormat(content.toString());
        } else if (qName.equals("comment")) {
            pagina.setComment(content.toString());
        } else if (qName.equals("id")) {
            if (pagina.getId() == null)
                pagina.setId(Long.valueOf(content.toString()));
        }
    }

    private void logAndamento() {
        if (paginasIndexadas % BATCH_SIZE == 0) {
            logger.info("Parcial: " + paginasIndexadas);
        }
    }

    public static void main(String[] args) {
        new WikipediaSAXParserToJDBC().parse();
    }

    public void parse() {
        try {
            logger.info("Inicio");
            daoPaginaWikipedia = new DAOPaginaWikipedia(BATCH_SIZE);
//            daoCategoriaWikipedia = new DAOCategoriaWikipedia(BATCH_SIZE);
//            extractor = new CategoryExtractor();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(nomeArquivo), this);
            daoPaginaWikipedia.close();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
