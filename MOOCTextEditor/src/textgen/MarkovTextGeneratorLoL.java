package textgen;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

/** 
 * An implementation of the MTG interface that uses a list of lists.
 * @author UC San Diego Intermediate Programming MOOC team 
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

	// The list of words with their next words
	private List<ListNode> wordList; 
	
	// The starting "word"
	private String starter;
	
	// The random number generator
	private Random rnGenerator;
	
	public MarkovTextGeneratorLoL(Random generator)
	{
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}
	
	
	/** Train the generator by adding the sourceText */
	@Override
	public void train(String sourceText)
	{
		String[] words = sourceText.split("[\\s]+");
		
		// If the wordList is empty, set starter to be the first word in the text:
		if (wordList.isEmpty()) {
			starter = words[0];
		}
		
		// set "prevWord" to be starter
		String prevWord = starter;
		
		// for each word "w" in the source text starting at the second word
		for (int i = 1; i < words.length; i++) {
			String w = words[i];
			
			// check to see if "prevWord" is already a node in the list
			ListNode node = null;
			for (ListNode n : wordList) {
				if (n.getWord().equals(prevWord)) {
					node = n;
					break;
				}
			}
			
			if (node != null) {
				// if "prevWord" is a node in the list, add "w" as a nextWord to the "prevWord" node
				node.addNextWord(w);
			} else {
				// else, add a node to the list with "prevWord" as the node's word
				ListNode newNode = new ListNode(prevWord);
				wordList.add(newNode);
				// add "w" as a nextWord to the "prevWord" node
				newNode.addNextWord(w);
			}
			
			// set "prevWord" = "w"
			prevWord = w;
		}
		
		// add 'starter' as the next word for the last word in the text, similar to the original implementation
		ListNode node = null;
		for (ListNode n : wordList) {
			if (n.getWord().equals(prevWord)) {
				node = n;
				break;
			}
		}

		if (node != null) {
			// if "prevWord" is a node in the list, add "w" as a nextWord to the "prevWord" node
			node.addNextWord(starter);
		} else {
			// else, add a node to the list with "prevWord" as the node's word
			ListNode newNode = new ListNode(prevWord);
			wordList.add(newNode);
			// add "w" as a nextWord to the "prevWord" node
			newNode.addNextWord(starter);
		}
	}

	
	/** 
	 * Generate the number of words requested.
	 */
	@Override
	public String generateText(int numWords) {

		if (wordList.isEmpty()) {
        	return "Error: wordList is empty. Please train the generator first.";
    	}

		if (numWords == 0) {
			return "";
		}
	    // set "currWord" to be the starter word
		String currWord = starter;

		// set "output" to be ""
		String output = "";

		// add "currWord" to output
		output += currWord;

		// while you need more words
		while (numWords > 1) {

			// find the "node" corresponding to "currWord" in the list
			ListNode node = null;
			for (ListNode n : wordList) {
				if (n.getWord().equals(currWord)) {
					node = n;
					break;
				}
			}

			// select a random word "w" from the "wordList" for "node"
			String w = node.getRandomNextWord(rnGenerator);

			// add "w" to the "output"
			output += " " + w;

			// set "currWord" to be "w"
			currWord = w;

			// increment number of words added to the list
			numWords--;
		}	
		// return the "output"
		return output;

	}
	
	
	// Can be helpful for debugging
	@Override
	public String toString()
	{
		String toReturn = "";
		for (ListNode n : wordList)
		{
			toReturn += n.toString();
		}
		return toReturn;
	}
	
	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText)
	{
		// clear the wordList
		wordList.clear();

		// clear the starter
		starter = "";

		// train the generator on the new source text
		train(sourceText);
	}
	
	// Add any private helper methods you need here.
	
	/**
	 * Returns a list of all the words in the source text.
	 */
	private List<String> getWords(String sourceText) {
		List<String> words = new ArrayList<>();
		String[] tokens = sourceText.split("\\s+");
		for (String token : tokens) {
			// remove all non-alphanumeric characters
			String word = token.replaceAll("[^a-zA-Z0-9']", ""); 
			if (!word.isEmpty()) {
				words.add(word);
			}
		}
		return words;
	}
	
	/**
	 * Returns a list of all the unique words in the source text.
	 */
	private Set<String> getUniqueWords(String sourceText) {
		Set<String> uniqueWords = new HashSet<>();
		List<String> words = getWords(sourceText);
		for (String word : words) {
			uniqueWords.add(word);
		}
		return uniqueWords;
	}
	
	/**
	 * Returns a list of all the sentences in the source text.
	 */
	private List<String> getSentences(String sourceText) {
		List<String> sentences = new ArrayList<>();
		String[] tokens = sourceText.split("(?<=[.!?])\\s+");
		for (String token : tokens) {
			String sentence = token.trim();
			if (!sentence.isEmpty()) {
				sentences.add(sentence);
			}
		}
		return sentences;
	}
	
	/**
	 * Returns a list of all the words that come after the given word in the source text.
	 */
	private List<String> getWordsThatFollow(String word, String sourceText) {
		List<String> wordsThatFollow = new ArrayList<>();
		List<String> words = getWords(sourceText);
		for (int i = 0; i < words.size() - 1; i++) {
			String currWord = words.get(i);
			String nextWord = words.get(i + 1);
			if (currWord.equals(word)) {
				wordsThatFollow.add(nextWord);
			}
		}
		return wordsThatFollow;
	}
	
	/**
	 * This is a minimal set of tests.  Note that it can be difficult
	 * to test methods/classes with randomized behavior.   
	 * @param args
	 */
	public static void main(String[] args)
	{
		// feed the generator a fixed random value for repeatable behavior
		MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
		String textString = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
		//String textString = "hi there hi Leo";
		System.out.println("\nprinting textString: ");
		System.out.println(textString);
		gen.train(textString);
		System.out.println("\nprinting gen: ");
		System.out.println(gen);
		System.out.println("\nprinting generateText(20): ");
		System.out.println(gen.generateText(20));
		String textString2 = "You say yes, I say no, "+
				"You say stop, and I say go, go, go, "+
				"Oh no. You say goodbye and I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"I say high, you say low, "+
				"You say why, and I say I don't know. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"Why, why, why, why, why, why, "+
				"Do you say goodbye. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"You say yes, I say no, "+
				"You say stop and I say go, go, go. "+
				"Oh, oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello,";
		System.out.println("\nprinting textString2: ");
		System.out.println(textString2);
		gen.retrain(textString2);
		System.out.println("\nprinting gen: ");
		System.out.println(gen);
		System.out.println("\nprinting generateText(40): ");
		System.out.println(gen.generateText(40));

		// testing helper methods for debugging
		System.out.println("\nTesting helper methods for debugging");
		System.out.println("\nTesting getSentences()");
		System.out.println(gen.getSentences(textString2));
		System.out.println("\nTesting getWordsThatFollow() with the word \"you\"");
		System.out.println(gen.getWordsThatFollow("you", textString2));
		System.out.println("\nTesting getUniqueWords()");
		System.out.println(gen.getUniqueWords(textString2));
		System.out.println("\nTesting getWords()");
		System.out.println(gen.getWords(textString2));
	}

}

/** Links a word to the next words in the list 
 * You should use this class in your implementation. */
class ListNode
{
    // The word that is linking to the next words
	private String word;
	
	// The next words that could follow it
	private List<String> nextWords;
	
	ListNode(String word)
	{
		this.word = word;
		nextWords = new LinkedList<String>();
	}
	
	public String getWord()
	{
		return word;
	}

	public void addNextWord(String nextWord)
	{
		nextWords.add(nextWord);
	}
	
	public String getRandomNextWord(Random generator)
	{
		// select a random word from the "wordList" for "node"
		int index = generator.nextInt(nextWords.size());
		return nextWords.get(index);
	}

	public String toString()
	{
		String toReturn = word + ": ";
		for (String s : nextWords) {
			toReturn += s + "->";
		}
		toReturn += "\n";
		return toReturn;
	}
	
}


