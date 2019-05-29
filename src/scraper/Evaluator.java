package scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

public class Evaluator implements IEvaluator {

    @Override
    public Integer getWordCount(String text, String word) {
        Pattern pattern = Pattern.compile(word);
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find())
            count++;
        return count;
    }

    @Override
    public Integer getImpression(String text, String word) {

        return null;
    }
    private List<String> getSentenceWords(String text, Integer index){
        ArrayList<String> sentenceWords = new ArrayList<String>();
        Integer punctuation = getNextPunctuation(text,0);
        Integer nextPunctuation = getNextPunctuation(text, punctuation+1);
        while(nextPunctuation<index){
            punctuation=nextPunctuation;
            nextPunctuation=text.indexOf(".", punctuation+1);
        }
        if (punctuation != -1 && nextPunctuation != -1){
            String sentence = text.substring(punctuation+1, nextPunctuation);
            //sentenceWords = sentence.split(" ");//TODO: NEED A REGEX FOR COMMA OR WHITESPACE
        }
        return sentenceWords;
    }
    private Integer getNextPunctuation(String text, Integer index){
        Integer nextPeriod = text.indexOf(".", index+1);
        Integer nextQuestionMark = text.indexOf("?", index+1);
        Integer nextExclamationPoint = text.indexOf("!", index+1);
        if (nextPeriod==-1&&nextExclamationPoint==-1&&nextQuestionMark==-1){
            return -1;
        }
        nextPeriod = nextPeriod >= 0 ? nextPeriod : Integer.MAX_VALUE;
        nextQuestionMark = nextQuestionMark >= 0 ? nextQuestionMark : Integer.MAX_VALUE;
        nextExclamationPoint = nextExclamationPoint >= 0 ? nextExclamationPoint : Integer.MAX_VALUE;
        return Math.min(Math.min(nextPeriod,nextQuestionMark),nextExclamationPoint);
    }
}
