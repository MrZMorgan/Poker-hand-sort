public enum CardRating {
    TWO('2', 2),
    THREE('3', 3),
    FOUR('4', 4),
    FIVE('5', 5),
    SIX('6', 6),
    SEVEN('7', 7),
    EIGHT('8', 8),
    NINE('9', 9),
    TEN('T', 10),
    JACK('J', 11),
    QUEEN('Q', 12),
    KING('K', 13),
    ACE('A', 14);

    private final char designation;
    private final int value;

    CardRating(
        final char designation,
        final int value
    ) {
        this.designation = designation;
        this.value = value;
    }

    public char getDesignation() {
        return designation;
    }

    public int getValue() {
        return value;
    }

    public static CardRating getByValue(final char value) {

        for (final CardRating cardValue : CardRating.values()) {
            if (cardValue.getDesignation() == value) {
                return cardValue;
            }
        }

        throw new IllegalArgumentException(value + " not math any of card rating value");
    }
}
