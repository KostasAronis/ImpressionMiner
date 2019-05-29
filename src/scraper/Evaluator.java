package scraper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;


class EvaluationWeights{
/*
        The following three resources are used in the evaluation algorithm
        and are provided as an example, they are in no way complete, well weighted
        or even remotely sufficient.
    */
    static Map<String,Double> wordValue = new HashMap<String,Double>()
    {
        {
            put("good", 1.0);
            put("bad", -1.0);
            put("great", 5.0);
            put("terrible", -5.0);
            put("awesome", 10.0);
            put("aweful", -10.0);
        }
    };
    static Map<String,Double> wordΜultiplier=new HashMap<String,Double>()
    {
        {
            put("really", 2.0);
            put("very", 3.0);
            put("not", -1.0);
        }
    };
    static List<String> noiseWords = new ArrayList<String>(){
        {
            add("the");
            add("that");
            add("all");
        }
    };
}

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
    public Double getImpression(String text, String word) {
        Double score=0.0;
        for (String sentence : this.getSentences(text,word)) {
            List<String> sentenceWords=this.getSentenceWords(sentence);
            Double sentenceValue = this.getSentenceValue(sentenceWords, EvaluationWeights.wordValue, EvaluationWeights.wordΜultiplier, EvaluationWeights.noiseWords);
            score+=sentenceValue;
        }
        return score;
    }
    private List<String> getSentences(String text,String word){
        List<String> sentences = new ArrayList<String>();
        String regex = "[^.]* "+word+" [^.]*\\.";
        String string = "As she did so, a most extraordinary thing happened. The bed-clothes gathered themselves together, leapt up suddenly into a sort of peak, and then jumped headlong over the bottom rail.It was exactly as if a hand had clutched them in the centre and flung them aside. Immediately after, .........";
        String subst = "";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find())
            sentences.add(matcher.group());
        return sentences;
    }
    private List<String> getSentenceWords(String sentence){
        String[] words = sentence.split("\\W+"); // seperate the sentence into words
        List<String> sentenceWords = Arrays.asList(words);
        return sentenceWords;
    }

    private Double getSentenceValue(List<String> sentence,
                                    Map<String,Double> wordValue,
                                    Map<String,Double> wordΜultiplier,
                                    List<String> noiseWords){
        Double score = 0.0;
        Double multiplier = 1.0;
        Double wordGap = 0.0;
        for (String word : sentence) {
            if(wordValue.containsKey(word)){
                score += (multiplier/1+wordGap)*wordValue.get(word);
                multiplier=1.0;
                wordGap=0.0;
            } else {
                if(wordΜultiplier.containsKey(word)){
                    multiplier*=wordΜultiplier.get(word);
                } else {
                    if(!noiseWords.contains(word)){
                        wordGap+=1;
                    }
                }
            }
        }
        return score;
    }
}
