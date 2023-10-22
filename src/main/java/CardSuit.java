public enum CardSuit {
    SPADES('S'),
    HEART('H'),
    DIAMONDS('D'),
    CLUBS('C');

    private final char value;

    CardSuit(final char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public static CardSuit getByValue(final char value) {

        for (final CardSuit suit : CardSuit.values()) {
            if (suit.getValue() == value) {
                return suit;
            }
        }

        throw new IllegalArgumentException(value + " not math any of card suit value");
    }
}
