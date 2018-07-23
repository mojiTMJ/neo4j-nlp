package com.graphaware.nlp.parser.procedure;

import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;

public class ADAFHandler extends ToXMLContentHandler {

    Map<String, String> descMap = new HashMap<>();
    private StringBuilder sb = new StringBuilder();

    private String currentClass = null;
    private int currentP = 0;
    private String action = null;


    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        super.startElement(uri, localName, qName, atts);
        System.out.println("start el");
        System.out.println(qName);
        if (qName.equals("p")) {
//            System.out.println("p");
            if (currentClass == null) {
                return;
            }
        }

        currentP++;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("end el");
        System.out.println(qName);
        if (qName.equals("p") && currentP == 1 && currentClass != null) {
            descMap.put(currentClass, sb.toString().replaceAll("  ", " "));
            sb = new StringBuilder();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        System.out.println(ch);
        String content = new String(ch).trim();
        if (content. matches("\\*[0-9]+\\*")) {
            String cl = content.replaceAll("\\*", "");
            currentClass = cl;
            currentP = 0;
        } else {
            if (currentClass != null) {
                if (currentP == 1) {
                    sb.append(content);
                    sb.append(" ");
                }
            }
        }
    }

    public void out() {
        System.out.println("class|description");
        for (String s : descMap.keySet()) {
            System.out.println(s + "|" + descMap.get(s));
        }
    }
}
