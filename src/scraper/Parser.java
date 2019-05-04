package scraper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Parser {
    public Document readUrl(String s) throws IOException {
        Document doc;
        try {
            doc = Jsoup.connect(s).get();
            // String title = doc.title();
            // System.out.println(title);
            // Element body = doc.body();
            // for (Element headline : body.select("a[href]"))
            //     System.out.println(headline.text());
        } catch (IOException e) {
            throw e;
        }
        return doc;
    }
    
}