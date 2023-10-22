import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PokerHand implements Comparable<PokerHand> {

    private final List<Card> cards;
    private final HandType handType;

    public PokerHand(final String cardsInput) {
        final var preparedCard = cardsInput.split(" ");

        if (preparedCard.length != 5) {
            throw new IllegalArgumentException("Hand must contains 5 cards");
        }

        this.cards = Arrays
            .stream(cardsInput
            .split(" "))
            .map(it ->
                new Card(
                    CardRating.getByValue(it.charAt(0)),
                    CardSuit.getByValue(it.charAt(1))
                ))
            .sorted(Comparator.comparingInt(card -> card.getRating().getValue()))
            .collect(Collectors.toList());

        this.handType = HandType.getHandType(this);
    }

    public List<Card> getCards() {
        return cards;
    }

    public HandType getHandType() {
        return handType;
    }

    public Map<CardRating, List<CardRating>> getCardsRatingsCount() {
        final var cardsRatings = getCardsRatings();

        return cardsRatings
            .stream()
            .distinct()
            .collect(Collectors.toMap(
                Function.identity(),
                it -> cardsRatings.stream().filter(rating -> rating == it).collect(Collectors.toList()))
            );
    }

    public List<CardRating> getCardsRatings() {
        return cards
            .stream()
            .map(Card::getRating)
            .collect(Collectors.toList());
    }

    public List<CardSuit> getCardsSuits() {
        return cards
            .stream()
            .map(Card::getSuit)
            .collect(Collectors.toList());
    }


    @Override
    public String toString() {
        return "PokerHand{" +
            "\n firstCard=" + cards.get(0) +
            "\n, secondCard=" + cards.get(1) +
            "\n, thirdCard=" + cards.get(2) +
            "\n, fourthCard=" + cards.get(3) +
            "\n, fifthCard=" + cards.get(4) +
            "\n}";
    }

    @Override
    public int compareTo(final PokerHand comparable) {
        final var thisHandType = HandType.getHandType(this);
        final var comparableHandType = HandType.getHandType(comparable);

        if (thisHandType.getRating() > comparableHandType.getRating()) {
            return 1;
        } else if (thisHandType.getRating() < comparableHandType.getRating()) {
            return -1;
        }

        else return HandType.compareSameHands(this, comparable);
    }
}
