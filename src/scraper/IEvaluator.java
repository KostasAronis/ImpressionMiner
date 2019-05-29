package scraper;

public interface IEvaluator {
	Integer getWordCount(String doc, String word);

	Double getImpression(String doc, String word);
}