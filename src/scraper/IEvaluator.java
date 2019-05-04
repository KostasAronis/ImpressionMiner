package scraper;

import org.jsoup.nodes.Document;

import models.Word;

public interface IEvaluator {
	Integer getWordCount(Document doc, String word);

	Integer getImpression(Document doc, String word);
}