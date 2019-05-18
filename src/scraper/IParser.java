package scraper;

import java.io.IOException;

public interface IParser {
    public String getPageText(String s) throws IOException;
}