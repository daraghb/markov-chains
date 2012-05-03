package de.zustandsforschung.markov;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.zustandsforschung.markov.model.Dictionary;
import de.zustandsforschung.markov.model.Tokens;
import de.zustandsforschung.markov.random.DeterministicRandomGenerator;

public class MarkovChainTest {

	private MarkovChain markovChain;
	private Dictionary dictionary;

	@Before
	public void setUp() {
		dictionary = new Dictionary();
		markovChain = new MarkovChainImpl(dictionary);
	}

	@Test
	public void testNextSingle() {
		markovChain.setRandomGenerator(new DeterministicRandomGenerator(0.7));
		markovChain.addTokens(new Tokens("only"));
		assertEquals(null, markovChain.next(new Tokens("only")));
	}

	@Test
	public void testNextTwo() {
		markovChain.setRandomGenerator(new DeterministicRandomGenerator(0.7));
		markovChain.addTokens(new Tokens("first", "second"));
		assertEquals("second", markovChain.next(new Tokens("first")));
	}

	@Test
	public void testNext50Percent() {
		markovChain.setRandomGenerator(new DeterministicRandomGenerator(0.7));
		markovChain.addTokens(new Tokens("one", "two", "one", "three"));
		assertEquals("three", markovChain.next(new Tokens("one")));
	}

	@Test
	public void testNext66Percent() {
		markovChain.setRandomGenerator(new DeterministicRandomGenerator(0.7));
		markovChain.addTokens(new Tokens("one", "two", "one", "two", "one",
				"three"));
		assertEquals("three", markovChain.next(new Tokens("one")));
	}

	@Test
	public void testNext50PercentOrder2() {
		markovChain.setRandomGenerator(new DeterministicRandomGenerator(0.7));
		markovChain.setOrder(2);
		markovChain.addTokens(new Tokens("one", "two", "one", "two", "one",
				"two", "three"));
		assertEquals("three", markovChain.next(new Tokens("one", "two")));
	}

	@Test
	public void testProbability100Percent() {
		markovChain.addTokens(new Tokens("first", "second"));
		assertEquals(1.0, dictionary.probability("second", "first"), 0.0);
	}

	@Test
	public void testProbability50Percent() {
		markovChain.addTokens(new Tokens("one", "two", "one", "three"));
		assertEquals(0.5, dictionary.probability("two", "one"), 0.0);
	}

	@Test
	public void testProbability66Percent() {
		markovChain.addTokens(new Tokens("one", "two", "one", "two", "one",
				"three"));
		assertEquals(0.66, dictionary.probability("two", "one"), 0.1);
	}

	@Test
	public void testProbabilityWithTokenizer() {
		markovChain.addTokens("one two one two one three");
		assertEquals(0.66, dictionary.probability("two", "one"), 0.1);
	}

	@Test
	public void testProbability100PercentOrder2() {
		markovChain.setOrder(2);
		markovChain.addTokens(new Tokens("first", "second", "third"));
		assertEquals(1.0, dictionary.probability("third", "first", "second"),
				0.0);
	}

	@Test
	public void testAddTokensWithoutClear() {
		markovChain.setRandomGenerator(new DeterministicRandomGenerator(0.7));
		markovChain.addTokens(new Tokens("first"));
		markovChain.addTokens(new Tokens("second"));
		assertEquals("second", markovChain.next(new Tokens("first")));
	}

	@Test
	public void testAddTokensWithClear() {
		markovChain.setRandomGenerator(new DeterministicRandomGenerator(0.7));
		markovChain.addTokens(new Tokens("first"));
		markovChain.clearPreviousToken();
		markovChain.addTokens(new Tokens("second"));
		assertEquals(null, markovChain.next(new Tokens("first")));
	}

}
