package com.graphaware.nlp.devtest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.*;

public class RAWebTest {

    @Test
    public void testParsingHtmlTables() throws Exception {
        List<String> docs = Arrays.asList("http://www.naa.gov.au/information-management/records-authorities/types-of-records-authorities/Agency-RA/2011/00642501/",
                "http://www.naa.gov.au/information-management/records-authorities/types-of-records-authorities/Agency-RA/2017/00409605/");

        System.out.println("classnumber,description");
        docs.forEach(d -> {
            try {
                parseDoc(d);
            } catch (Exception e) {
                //
            }
        });
    }

    private void parseDoc(String url) throws Exception {
        Document document = Jsoup.connect(url).get();

        String lastH = null;
        Map<String, Set<String>> classMap = new HashMap<>();
        Map<String, String> classDesc = new HashMap<>();

        Elements elements = document.select("*");
        for (Element element : elements) {
            if (element.tagName().equalsIgnoreCase("h3")) {
                lastH = element.text();
            }

            if (element.tagName().equalsIgnoreCase("tr")) {
                Elements cols = element.select("td");
                if (cols.size() > 0) {
                    String classNumber = cols.get(0).text();
                    String desc = cols.get(1).text();
                    classDesc.put(classNumber, desc);
                    if (!classMap.containsKey(lastH)) {
                        classMap.put(lastH, new HashSet<>());
                    }
                    classMap.get(lastH).add(classNumber);
                }
            }
        }

        classDesc.keySet().forEach(k -> {
            System.out.println(k + "," + classDesc.get(k));
        });
    }

}
