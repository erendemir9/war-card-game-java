package deck;

import java.util.ArrayList;
import java.util.List;

public class CardCatalog {
	private List<Card> cardList;

	public CardCatalog() {
		this.cardList = new ArrayList<>();
		populateDefaultCards();
	}

	private void populateDefaultCards() {
		String[] suits = { "Clubs", "Hearts", "Spades", "Diamonds" };
		for (int rank = 2; rank <= 14; rank++) {
			for (String suit : suits) {
				cardList.add(new Card(suit, rank));
			}
		}
	}

	public List<Card> getDefaultCards() {
		return this.cardList;
	}

	public void printCardList() {
		for (Card c : cardList) {
			System.out.println(c);
		}
	}
}