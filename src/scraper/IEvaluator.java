package scraper;

public interface IEvaluator {
	Integer getWordCount(String doc, String word);

	Integer getImpression(String doc, String word);
}