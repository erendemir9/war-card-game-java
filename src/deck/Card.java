package deck;

public class Card {
	private String suit;   // Clubs, Hearts, Spades, Diamonds
	private int rank;      // 2-14 (where 14 = Ace)
	// private boolean isFaceUp;

	public Card(String suit, int rank) {
		this.suit = suit;
		this.rank = rank;

	}

	public String getSuit() {
		return this.suit;
	}

	public int getRank() {
		return this.rank;
	}

	
	public void printCardInfo() {
		System.out.println(getRank()+ " of " +getSuit());
	}

	@Override
	public String toString() {
		return rank + " of " + suit;
	}
}
