/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
package spamfilter;
 
import java.util.ArrayList;
import java.util.HashMap;
 
/**
 *
 * @author Visweswaran Nagasivam
 */
public class SpamFilter {
    // ======================= TRAINING DATA =====================
    private ArrayList<String> spamMessages = null;
    private ArrayList<String> hamMessages = null;
    private Integer totalSpamMessages;
    private Integer totalHamMessages;
    private HashMap<String, Double> spamProbabilityForWord = null;
    private HashMap<String, Double> hamProbabilityForWord = null;
   
    /**
     * A constructor to load the spam words and ham words
     * @param spamMessages could be anything spam words / financial words/ marketing words which is applied to filter
     * @param hamMessages  the list of good words
     */
    public SpamFilter(ArrayList<String> spamMessages, ArrayList<String> hamMessages)
    {
        this.spamMessages = spamMessages;
        this.hamMessages = hamMessages;
        this.totalHamMessages = this.hamMessages.size();
        this.totalSpamMessages = this.spamMessages.size();
        this.spamProbabilityForWord = new HashMap<String, Double>();
        this.hamProbabilityForWord = new HashMap<String, Double>();
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
     * A method which is used to learn from your messages
     * @throws java.lang.ArithmeticException will throw either spam / ham training data is empty
     */
    public void learn() throws ArithmeticException
    {
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
    }
   
    private void calculateProbabilityAndUpdate(String word)
    {
        /*
        calculation of p(word/spam)
        */
        int countOfWordInSpam = getWordCountInSpam(word);
        Double pOfWordSpam = (double) countOfWordInSpam /this.totalSpamMessages;
        this.updateSpamProbability(word, pOfWordSpam);
        System.out.println("p("+word+"/spam) = "+pOfWordSpam);
        int countOfWordInHam = getWordCountInHam(word);
        Double pOfWordHam = (double) countOfWordInHam /this.totalHamMessages;
        this.updateHamProbability(word, pOfWordHam);
        System.out.println("p("+word+"/ham) = "+pOfWordHam);
    }
   
    /**
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
   
    /**
     * This method is used to update the spam table which is nothing but a hash map
     * e.g spam table
     * ================================
     * word          ||  probability
     * ================================
     * million       || 0.7
     * ================================
     * @param word The spam word
     * @param probability The probability of the spam word
     */
    private void updateSpamProbability(String word, Double probability)
    {
        if(this.spamProbabilityForWord.containsKey(word))
        {
            this.spamProbabilityForWord.remove(word);
            this.spamProbabilityForWord.put(word, probability);
        }
        else this.spamProbabilityForWord.put(word, probability);
    }
   
    /**
     * This method is used to update the ham table which is nothing but a hash map
     * e.g ham table
     * ================================
     * word          ||  probability
     * ================================
     * bank          || 0.7
     * ================================
     * @param word The spam word
     * @param probability The probability of the spam word
     * */
    private void updateHamProbability(String word, Double probability)
    {
        if(this.hamProbabilityForWord.containsKey(word))
        {
            this.hamProbabilityForWord.remove(word);
            this.hamProbabilityForWord.put(word, probability);
        }
        else this.hamProbabilityForWord.put(word, probability);
    }
    
    /**
     * @param message The input message for which you need to get the probability
     * @return spam probability which ranges between 0.0 to 1.0
     */
    public Double getSpamProbabilityForMessage(String message)
    {
        BagOfWords words = new BagOfWords();
        words.setMessage(message);
        words.processMessage();
        ArrayList<String> wordsInMessage = words.getBagOfWords();
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