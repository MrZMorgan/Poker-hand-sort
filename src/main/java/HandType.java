import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public enum HandType {
    HIGH_CARD(1),
    PAIR(2),
    TWO_PAIRS(3),
    THREE_OF_A_KIND(4),
    STRAIGHT(5),
    FLUSH(6),
    FULL_HOUSE(7),
    FOUR_OF_A_KIND(8),
    STRAIGHT_FLUSH(9),
    ROYAL_FLASH(10);

    private final int rating;

    HandType(final int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public static int compareSameHands(final PokerHand first, final PokerHand second) {
        assert first.getHandType() == second.getHandType();
        final var handType = first.getHandType();

        switch (handType) {
            case ROYAL_FLASH:     return 0;
            case STRAIGHT_FLUSH:
            case FLUSH:
            case STRAIGHT:
                return compareHighCard(first, second);

            case FOUR_OF_A_KIND:  return compareForOfFourOfAKind(first, second);
            case FULL_HOUSE:      return compareFullHouses(first, second);
            case THREE_OF_A_KIND: return compareForOfThreeOfAKind(first, second);
            case TWO_PAIRS:       return compareTwoPairs(first, second);
            case PAIR:            return comparePair(first, second);
        }

        return compareHighCard(first, second);
    }

    public static HandType getHandType(final PokerHand hand) {
        if (isRolayFlush(hand))    return ROYAL_FLASH;
        if (isStraightFlush(hand)) return STRAIGHT_FLUSH;
        if (isFourOfAKind(hand))   return FOUR_OF_A_KIND;
        if (isFullHouse(hand))     return FULL_HOUSE;
        if (isFlush(hand))         return FLUSH;
        if (isStraight(hand))      return STRAIGHT;
        if (isThreeOfAKind(hand))  return THREE_OF_A_KIND;
        if (isTwoPair(hand))       return TWO_PAIRS;
        if (isPair(hand))          return PAIR;

        return HIGH_CARD;
    }

    private static int comparePair(final PokerHand first, final PokerHand second) {
        int twoComparison = compareTwoSameCard(first, second);

        if (twoComparison != 0) {
            return twoComparison;
        }

        return Integer.compare(
            maxCardValueOutOfNumberCombination(first, 2),
            maxCardValueOutOfNumberCombination(second, 2)
        );
    }

    private static int compareTwoPairs(final PokerHand first,final PokerHand second) {
        final var firstPairsValues = getTwoPairsValues(first);
        final var secondPairsValues = getTwoPairsValues(second);

        final var pairsNotSame = !firstPairsValues.containsAll(secondPairsValues);

        if (pairsNotSame) {
            for (int i = 0; i < 5; i++) {
                final var result = Integer.compare(firstPairsValues.get(i), secondPairsValues.get(i));

                if (result != 0) {
                    return result;
                }
            }
        }

        return Integer.compare(
            maxCardValueOutOfNumberCombination(first, 2),
            maxCardValueOutOfNumberCombination(second, 2)
        );
    }

    private static List<Integer> getTwoPairsValues(final PokerHand hand) {
        return hand
            .getCardsRatingsCount()
            .values()
            .stream()
            .filter(cardRatings -> cardRatings.size() == 2)
            .flatMap(it -> it.stream().map(CardRating::getValue).distinct())
            .collect(Collectors.toList());
    }

    private static int compareForOfThreeOfAKind(final PokerHand first, final PokerHand second) {
        int threeComparison = compareThreeSameCard(first, second);

        if (threeComparison != 0) {
            return threeComparison;
        }

        return compareHighCard(first, second);
    }

    private static int maxCardValueOutOfNumberCombination(final PokerHand hand, int combinationCardCount) {
        return hand.getCardsRatingsCount()
            .entrySet()
            .stream()
            .filter(it -> it.getValue().size() != combinationCardCount)
            .flatMap(it -> it.getValue().stream())
            .max(Comparator.comparingInt(CardRating::getValue))
            .get()
            .getValue();
    }


    private static int compareFullHouses(final PokerHand first, final PokerHand second) {
        int threeComparison = compareThreeSameCard(first, second);
        
        if (threeComparison != 0) {
            return threeComparison;
        }

        return Integer.compare(
            getMaxOfNumberCombination(first, 2),
            getMaxOfNumberCombination(second, 2)
        );
    }

    private static int compareTwoSameCard(final PokerHand first, final PokerHand second) {
        return Integer.compare(
            getMaxOfNumberCombination(first, 2),
            getMaxOfNumberCombination(second, 2)
        );
    }

    private static int compareThreeSameCard(final PokerHand first, final PokerHand second) {
        return Integer.compare(
            getMaxOfNumberCombination(first, 3),
            getMaxOfNumberCombination(second, 3)
        );
    }

    private static int compareForOfFourOfAKind(final PokerHand first, final PokerHand second) {
        return Integer.compare(
            getMaxOfNumberCombination(first, 4),
            getMaxOfNumberCombination(second, 4)
        );
    }

    private static int getMaxOfNumberCombination(final PokerHand hand, int combinationCardCount) {

        return hand.getCardsRatingsCount()
            .entrySet()
            .stream()
            .filter(it -> it.getValue().size() == combinationCardCount)
            .findFirst()
            .get()
            .getValue()
            .stream().max(Comparator.comparingInt(CardRating::getValue))
            .get()
            .getValue();
    }

    private static int compareHighCard(final PokerHand first, final PokerHand second) {
        final var firstValues = first
            .getCardsRatings().stream()
            .map(CardRating::getValue)
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());

        final var secondValues = second
            .getCardsRatings().stream()
            .map(CardRating::getValue)
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());

        for (int i = 0; i < 5; i++) {
            final var result = Integer.compare(firstValues.get(i), secondValues.get(i));

            if (result != 0) {
                return result;
            }
        }

        return 0;
    }

    private static boolean isPair(final PokerHand hand) {
        return hand
            .getCardsRatingsCount()
            .values()
            .stream()
            .filter(it -> it.size() == 2)
            .count() == 1;
    }

    private static boolean isTwoPair(final PokerHand hand) {
        return hand
            .getCardsRatingsCount()
            .values()
            .stream()
            .filter(it -> it.size() == 2)
            .count() == 2;
    }

    private static boolean isThreeOfAKind(final PokerHand hand) {

        return hand
            .getCardsRatingsCount()
            .values()
            .stream()
            .anyMatch(it -> it.size() == 3);
    }

    private static boolean isStraight(final PokerHand hand) {
        final var handRatings = hand
            .getCardsRatingsCount()
            .keySet()
            .stream()
            .sorted()
            .collect(Collectors.toList());

        if (handRatings.size() != 5) return false;

        for (int i = 0; i < handRatings.size() - 1; i++) {
            if ((handRatings.get(i + 1).getValue() - handRatings.get(i).getValue()) != 1) {
                return false;
            }
        }

        return true;
    }

    private static boolean isFlush(final PokerHand hand) {
        return hand.getCardsRatingsCount().size() == 5
            && new HashSet<>(hand.getCardsSuits()).size() == 1;
    }


    private static boolean isFullHouse(final PokerHand hand) {
        return hand.getCardsRatingsCount().size() == 2
            && hand.getCardsRatingsCount().values().stream().anyMatch(it -> it.size() == 3);
    }


    private static boolean isFourOfAKind(final PokerHand hand) {

        return hand.getCardsRatingsCount().values()
            .stream()
            .anyMatch(it -> it.size() == 4);
    }

    private static boolean isStraightFlush(final PokerHand hand) {
        boolean suitsCorrespond = new HashSet<>(hand.getCardsSuits()).size() == 1;
        if (!suitsCorrespond) return false;

        return isStraight(hand);
    }

    private static boolean isRolayFlush(final PokerHand hand) {
        boolean suitsCorrespond = new HashSet<>(hand.getCardsSuits()).size() == 1;

        if (!suitsCorrespond) return false;

        return hand
            .getCardsRatingsCount()
            .keySet()
            .containsAll(List.of(
                CardRating.ACE,
                CardRating.KING,
                CardRating.QUEEN,
                CardRating.JACK,
                CardRating.TEN));
    }
}
