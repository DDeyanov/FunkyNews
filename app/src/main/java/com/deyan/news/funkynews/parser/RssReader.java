package com.deyan.news.funkynews.parser;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Deyan on 16/09/2014.
 */
public class RssReader {

    public static RssFeed read(URL url) throws SAXException, IOException {

        return read(url.openStream());

    }

    public static RssFeed read(InputStream inputStream) throws SAXException, IOException {

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlReader = parser.getXMLReader();

            RssHandler handler = new RssHandler();
            InputSource input = new InputSource(inputStream);

            xmlReader.setContentHandler(handler);
            xmlReader.parse(input);

            return handler.getResult();

        } catch (ParserConfigurationException e) {
            throw new SAXException();
        }
    }
}
