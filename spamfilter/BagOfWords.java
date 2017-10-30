package spamfilter;

// SWAMI KARUPPASWAMI THUNNAI

import java.util.ArrayList;
import java.util.HashSet;

class BagOfWords
{
	private HashSet<String> linkingWordsList = null;
	private String message = null;  // The message itself to be processed
	private ArrayList<String> bagOfWords = null;  // The final bag of words will be stored here
	
	/**
	 * A constructor to load the linking words into the HashSet - linkingWordsList
	 * @param linkingWordsLoc This contains the location of the linking words
	 * */
	public BagOfWords()
	{
		this.linkingWordsList = new HashSet<String>();
		this.bagOfWords = new ArrayList<String>();
		for(String linkingWord : LinkingWords.en)
                {
                    this.linkingWordsList.add(linkingWord);
                }
	}

	/**
	 * A setter to set the message
	 * @param message The actual message to be processed.
	 */
	void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * A getter to get the message
	 * @return message to be processed
	 */
	String getMessage()
	{
		return this.message;
	}

	/**
	 * This method is used to remove the linking words in the String to get a pure Bag of Words
	 * @throws NullPointerException this will be thrown if the input message is null
	 */
	void processMessage() throws NullPointerException
	{
		String[] words = this.message.split(" ");
		for(String word : words)
		{
			// If the word is not a linking word, then add the word to bag of words
			if(!this.linkingWordsList.contains(word.toLowerCase())) this.bagOfWords.add(word);
		}		
	}

	/**
	 * This method is used to return the bag of words
	 * @return Will return the bag of words
	 */
	ArrayList<String> getBagOfWords()
	{
		return this.bagOfWords;
	}

}
	
