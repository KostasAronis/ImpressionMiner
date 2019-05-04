package scraper;

import java.io.IOException;

import org.jsoup.nodes.Document;

public interface IParser {
    public Document readUrl(String s) throws IOException;
}