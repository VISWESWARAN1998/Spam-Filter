/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
package spamfilter;
 
import java.util.ArrayList;
import java.util.HashMap;
 
/**
 *
 * @author student
 */
public class SpamFilter {
    // ======================= TRAINING DATA =====================
    private ArrayList<String> spamMessages = null;
    private ArrayList<String> hamMessages = null;
    private Integer totalMessags;
    private Integer totalSpamMessages;
    private Integer totalHamMessages;
    private HashMap<String, Double> spamProbabilityForWord = null;
    private HashMap<String, Double> hamProbabilityForWord = null;
   
    public SpamFilter(ArrayList<String> spamMessages, ArrayList<String> hamMessages)
    {
        this.spamMessages = spamMessages;
        this.hamMessages = hamMessages;
        // Total messages =  no. of ham messages + no. of spam messages
        this.totalMessags = this.hamMessages.size() + this.spamMessages.size();
        this.totalHamMessages = this.hamMessages.size();
        this.totalSpamMessages = this.spamMessages.size();
        this.spamProbabilityForWord = new HashMap<String, Double>();
        this.hamProbabilityForWord = new HashMap<String, Double>();
    }
   
    private void resetTotalMsgCount()
    {
        this.totalMessags = this.hamMessages.size() + this.spamMessages.size();
    }
   
    private void resetSpamMsgCount()
    {
        this.totalHamMessages = this.hamMessages.size();
    }
   
    private void resetHamMsgCount()
    {
        this.totalHamMessages = this.hamMessages.size();
    }
   
    /**
     *
     * @throws java.lang.ArithmeticException will throw either spam / ham training data is empty
     */
    void learn() throws ArithmeticException
    {
        resetTotalMsgCount();
        resetSpamMsgCount();
        resetHamMsgCount();
        
        // calculate p( word / spam) and p( word / ham )
        /**
         * p(word/spam) = Count of word / Total Spam messages
         */
        /**
         * p(word/ham) = Count of word / Total Ham messages
         */
        for(String word: this.spamMessages){
            this.calculateProbabilityAndUpdate(word);
        }
        for(String word: this.hamMessages) this.calculateProbabilityAndUpdate(word);
        // At this moment we have learned the spam & ham probability of all words
        System.out.println(this.spamProbabilityForWord);
        System.out.println(this.hamProbabilityForWord);
    }
   
    void calculateProbabilityAndUpdate(String word)
    {
        int countOfWordInSpam = getWordCountInSpam(word);
        System.out.println("Spam: " + word+ " "+countOfWordInSpam);
        Double pOfWordSpam = (double) countOfWordInSpam /this.totalSpamMessages;
        System.out.println("p(spam) = "+pOfWordSpam);
        this.updateSpamProbability(word, pOfWordSpam);
        int countOfWordInHam = getWordCountInHam(word);
        System.out.println("Ham: " + word+ " "+countOfWordInHam);
        Double pOfWordHam = (double) countOfWordInHam /this.totalHamMessages;
        System.out.println("p(ham) = "+pOfWordHam);
        this.updateHamProbability(word, pOfWordHam);
    }
   
    /**
     *
     * @param word The word for which the count is to be identified
     * @return returns the no of times the "word" is present in HamMessages
     */
    private int getWordCountInHam(String word)
    {
        int count = 0;
        for(String wordInHam : this.hamMessages)
        {
            // If the word is present in the ham messages increment the count
            // contains method will help to pattern match
            if(wordInHam.contains(word)) count++;
        }
        return count;
    }
   
    /**
     *
     * @param word The word for which the count is to be identified
     * @return returns the no of times the "word" is present in Spam Messages
     */
    private int getWordCountInSpam(String word)
    {
        int count = 0;
        for(String wordInSpam : this.spamMessages)
        {
            // If the word is present in the ham messages increment the count
            // contains method will help to pattern match
            if(wordInSpam.contains(word)) count++;
        }
        return count;
    }
   
    private void updateSpamProbability(String word, Double probability)
    {
        if(this.spamProbabilityForWord.containsKey(word))
        {
            this.spamProbabilityForWord.remove(word);
            this.spamProbabilityForWord.put(word, probability);
        }
        else this.spamProbabilityForWord.put(word, probability);
    }
   
    private void updateHamProbability(String word, Double probability)
    {
        if(this.hamProbabilityForWord.containsKey(word))
        {
            this.hamProbabilityForWord.remove(word);
            this.hamProbabilityForWord.put(word, probability);
        }
        else this.hamProbabilityForWord.put(word, probability);
    }
   
    public Double getSpamProbabilityForMessage(String message)
    {
        BagOfWords words = new BagOfWords();
        words.setMessage(message);
        words.processMessage();
        ArrayList<String> wordsInMessage = words.getBagOfWords();
        int totalWordsInMsg = wordsInMessage.size();
        ArrayList<Double> hamProbabilities = new ArrayList<Double>();
        ArrayList<Double> spamProbabilities = new ArrayList<Double>();
        double probability = 0.0;
        for(String wordInMessage : wordsInMessage)
        {
            if(this.hamProbabilityForWord.containsKey(wordInMessage.toLowerCase()))
            {
                hamProbabilities.add(this.hamProbabilityForWord.get(wordInMessage.toLowerCase()));
            } else hamProbabilities.add(0.0);
           
            if(this.spamProbabilityForWord.containsKey(wordInMessage))
            {
                spamProbabilities.add(this.spamProbabilityForWord.get(wordInMessage));
            } else spamProbabilities.add(0.0);
        }
        double pOfWByS = 0.0;
        double pOfWByH = 0.0;
        for(Double i : spamProbabilities) pOfWByS+=i;
        for(Double i : hamProbabilities) pOfWByH+=i;
        if((pOfWByS+pOfWByH)!=0.0)
        {
            probability = (double) pOfWByS / (pOfWByS+pOfWByH);
        }
        System.out.println(spamProbabilities);
        System.out.println(hamProbabilities);
        return probability;
    }
}