package scraper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Parser implements IParser {
    public String getPageText(String url) throws IOException {
        Document doc;
        String text;
        try {
            doc = Jsoup.connect(url).get();
            text = doc.text();
        } catch (IOException e) {
            throw e;
        }
        return text;
    }
    
}