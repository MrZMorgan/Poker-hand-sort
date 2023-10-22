public class Card {
    private CardRating rating;
    private CardSuit suit;

    public Card(
        final CardRating rating,
        final CardSuit suit
    ) {
        this.rating = rating;
        this.suit = suit;
    }

    public CardRating getRating() {
        return rating;
    }

    public CardSuit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return "Card{" +
            "rating=" + rating +
            ", suit=" + suit +
            '}';
    }
}
