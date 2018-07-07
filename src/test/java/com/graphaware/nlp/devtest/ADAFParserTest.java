package com.graphaware.nlp.devtest;

import com.graphaware.common.util.Pair;
import com.graphaware.nlp.parser.procedure.ADAFHandler;
import com.graphaware.nlp.parser.procedure.CustomHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.junit.Test;
import org.xml.sax.ContentHandler;

import java.net.URL;
import java.util.*;

public class ADAFParserTest {

    @Test
    public void testParsingAdaf() throws Exception {
        AutoDetectParser parser = new AutoDetectParser();
        ContentHandler contentHandler = new BodyContentHandler(new CustomHandler());
        Metadata metadata = new Metadata();
        parser.parse(getClass().getClassLoader().getResourceAsStream("ADAF_full.pdf"), contentHandler, metadata);

//        System.out.println(contentHandler.toString());

        String c = contentHandler.toString();
        c = c.replaceAll("<p>", "<p>\n").replaceAll("&amp;", "&");

        List<String> lines = Arrays.asList(c.split("\n"));
        List<Pair> pairs = new ArrayList<>();
        Map<String, String> classmap = new HashMap<>();
        lines.forEach(l -> {
            if (l.contains("—")) {
                String[] parts = l.split("—");
                if (parts[0].trim().matches("[A-Z& ]*")) {
                    String function = parts[0].trim();
                    String[] classesNumbers = parts[1].split("\\[");
                    List<String> clNs = new ArrayList<>();
                    if (classesNumbers.length > 1) {
                        String cls = classesNumbers[1].split("\\]")[0];
                        if (cls.contains(", ")) {
                            List<String> sp = Arrays.asList(cls.split(", "));
                            for (String sps : sp) {
                                if (sps.contains("–")) {
                                    clNs.addAll(Arrays.asList(sps.split("–")));
                                } else {
                                    clNs.add(sps);
                                }
                            }
                        } else {
                            List<String> sp = Arrays.asList(cls.split("–"));
                            for (String sps : sp) {
                                if (sps.contains("–")) {
                                    clNs.addAll(Arrays.asList(sps.split("–")));
                                } else {
                                    clNs.add(sps);
                                }
                            }
                        }
//                        System.out.println(clNs);
                    }
                    String activity = parts[1].split(",")[0].trim();
                    pairs.add(new Pair(function, activity));
                    classmap.put(function + "__" + activity, StringUtils.join(clNs, ";"));
                }

            }
        });

        System.out.println("function,activity,classes");
        pairs.forEach(pair -> {
            System.out.println(pair.first() + "," + pair.second() + "," + classmap.get(pair.first() + "__" + pair.second()));
        });


    }

    @Test
    public void testParsingAdaf2() throws Exception {
        AutoDetectParser parser = new AutoDetectParser();
        ADAFHandler contentHandler = new ADAFHandler();
        Metadata metadata = new Metadata();
        parser.parse(getClass().getClassLoader().getResourceAsStream("ADAF_full.pdf"), contentHandler, metadata);

        contentHandler.out();

//        System.out.println(contentHandler.toString());
    }
}
