package player;

import deck.Card;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
	private String id;
	private String name;
	private List<Card> deck;
	
	public User(String id, String name) {
		this.id = id;
		this.name = name;
		this.deck = new ArrayList<>();
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public List<Card> getDeck() { return deck; }
	public void setDeck(List<Card> deck) { this.deck = deck; }

	public Card getFirstCardOfDeck() {
		return deck.get(0);
	}

	public int getRankOfFirstCard() {
		return deck.get(0).getRank();
	}

	public void printFirstCardOfDeck() {
		deck.get(0).printCardInfo();
	}

	public void addCardToDeck(Card card) {
		deck.add(card);
	}

	public void removeCardFromDeck(Card card) {
		deck.remove(card);
	}
}