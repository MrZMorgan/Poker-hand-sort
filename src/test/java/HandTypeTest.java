import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HandTypeTest {

    @ParameterizedTest
    @MethodSource("shouldGetHandTypeTest")
    void getHandTypeTest(final HandTypeTestEntry testEntry) {
        Assertions.assertEquals(testEntry.expected, testEntry.data.getHandType());
    }

    @Test
    void testHighCardCompare() {
        final var highCart1 = new PokerHand("TC 4H 7D KC 2S");
        final var highCart2 = new PokerHand("TC 4H 7D KC 2S");
        final var highCart3 = new PokerHand("TC 3H 8C KD 6H");
        Assertions.assertEquals(highCart1.compareTo(highCart2), 0);
        Assertions.assertEquals(highCart1.compareTo(highCart3), -1);
        Assertions.assertEquals(highCart3.compareTo(highCart1), 1);
    }

    @Test
    void testPairCompare() {
        final var pair1 = new PokerHand("KC KH 7D 2C 5S");
        final var pair2 = new PokerHand("QC QH 7D 2C 5S");
        final var pair3 = new PokerHand("KC KH 8D 2C 5S");
        final var pair4 = new PokerHand("KC KH 8S 2C 5S");
        Assertions.assertEquals(pair1.compareTo(pair2), 1);
        Assertions.assertEquals(pair1.compareTo(pair3), -1);
        Assertions.assertEquals(pair3.compareTo(pair4), 0);
    }

    @Test
    void testTwoPairsCompare() {
        final var twoPairs1 = new PokerHand("KC KH 7D 7C 5S");
        final var twoPairs2 = new PokerHand("KC KH 8D 8C 5S");
        final var twoPairs3 = new PokerHand("KC KH 7S 7D 5S");
        final var twoPairs4 = new PokerHand("KC KH 7S 7D 6S");
        Assertions.assertEquals(twoPairs1.compareTo(twoPairs3), 0);
        Assertions.assertEquals(twoPairs1.compareTo(twoPairs4), -1);
        Assertions.assertEquals(twoPairs2.compareTo(twoPairs3), 1);
    }

    @Test
    void testThreeOfAKindCompare() {
        final var threeOfAKind1 = new PokerHand("KC KH KD 7C 5S");
        final var threeOfAKind2 = new PokerHand("KS KH KD 7C 5S");
        final var threeOfAKind3 = new PokerHand("QC QH QD 7H 5D");
        final var threeOfAKind4 = new PokerHand("KS KH KD 7D 6H");

        Assertions.assertEquals(threeOfAKind1.compareTo(threeOfAKind2), 0);
        Assertions.assertEquals(threeOfAKind1.compareTo(threeOfAKind3), 1);
        Assertions.assertEquals(threeOfAKind3.compareTo(threeOfAKind2), -1);
        Assertions.assertEquals(threeOfAKind4.compareTo(threeOfAKind1), 1);
    }

    @Test
    void testStraightCompare() {
        final var straight1 = new PokerHand("3C 4H 5D 6C 7S");
        final var straight2 = new PokerHand("3D 4C 5H 6S 7H");
        final var straight3 = new PokerHand("4C 5H 6D 7C 8S");

        Assertions.assertEquals(straight1.compareTo(straight2), 0);
        Assertions.assertEquals(straight1.compareTo(straight3), -1);
        Assertions.assertEquals(straight3.compareTo(straight1), 1);
    }

    @Test
    void testFlushCompare() {
        final var flush1 = new PokerHand("KC QC 9C 8C 2C");
        final var flush2 = new PokerHand("KD QD 9D 8D 2D");
        final var flush3 = new PokerHand("AC KC QC JC TC");

        Assertions.assertEquals(flush1.compareTo(flush2), 0);
        Assertions.assertEquals(flush1.compareTo(flush3), -1);
        Assertions.assertEquals(flush3.compareTo(flush2), 1);
    }

    @Test
    void testFullHouseCompare() {
        final var fullHouse1 = new PokerHand("KC KH KD 7C 7C");
        final var fullHouse2 = new PokerHand("QC QH QD 7C 7C");
        final var fullHouse3 = new PokerHand("KC KH KD 8C 8C");
        final var fullHouse4 = new PokerHand("KC KH KD 7D 7H");

        Assertions.assertEquals(fullHouse1.compareTo(fullHouse2), 1);
        Assertions.assertEquals(fullHouse1.compareTo(fullHouse3), -1);
        Assertions.assertEquals(fullHouse1.compareTo(fullHouse4), 0);
        Assertions.assertEquals(fullHouse3.compareTo(fullHouse1), 1);
    }

    @Test
    void testForOfKindCompare() {
        final var fourOfAKind1 = new PokerHand("6S 6D 6H 6C KS");
        final var fourOfAKind2 = new PokerHand("4S 4D 4H 4C KD");

        Assertions.assertEquals(fourOfAKind1.compareTo(fourOfAKind2), 1);
        Assertions.assertEquals(fourOfAKind2.compareTo(fourOfAKind1), -1);
    }

    @Test
    void testStraightFlashCompare() {
        final var straightFlash1 = new PokerHand("2S 3S 4S 5S 6S");
        final var straightFlash2 = new PokerHand("3S 4S 5S 6S 7S");
        final var straightFlash3 = new PokerHand("2D 3D 4D 5D 6D");

        Assertions.assertEquals(straightFlash1.compareTo(straightFlash2), -1);
        Assertions.assertEquals(straightFlash1.compareTo(straightFlash3), 0);
        Assertions.assertEquals(straightFlash2.compareTo(straightFlash1), 1);
    }


    @Test
    void shouldSortByHand() {
        final var expected = testData().stream().map(it -> it.data).collect(Collectors.toList());
        final var actual = new ArrayList<>(List.copyOf(expected));

        Collections.shuffle(actual);

        Assertions.assertNotEquals(expected, actual);

        Collections.sort(actual);

        Assertions.assertEquals(expected, actual);
    }

    private static Stream<HandTypeTestEntry> shouldGetHandTypeTest() {
        return testData().stream();
    }

    private static List<HandTypeTestEntry> testData() {
        return List.of(
            new HandTypeTestEntry(HandType.HIGH_CARD, new PokerHand("TC 4H 7D KC 2S")),
            new HandTypeTestEntry(HandType.PAIR, new PokerHand("KC KH 7D 2C 5S")),
            new HandTypeTestEntry(HandType.TWO_PAIRS, new PokerHand("KC KH 7D 7C 5S")),
            new HandTypeTestEntry(HandType.THREE_OF_A_KIND, new PokerHand("KC KH KD 7C 5S")),
            new HandTypeTestEntry(HandType.STRAIGHT, new PokerHand("3C 4H 5D 6C 7S")),
            new HandTypeTestEntry(HandType.FLUSH, new PokerHand("KC QC 9C 8C 2C")),
            new HandTypeTestEntry(HandType.FULL_HOUSE, new PokerHand("KC KH KD 7C 7C")),
            new HandTypeTestEntry(HandType.FOUR_OF_A_KIND, new PokerHand("6S 6D 6H 6C KS")),
            new HandTypeTestEntry(HandType.STRAIGHT_FLUSH, new PokerHand("2S 3S 4S 5S 6S")),
            new HandTypeTestEntry(HandType.ROYAL_FLASH, new PokerHand("TS JS QS KS AS"))
        );
    }

    private static class HandTypeTestEntry {
        private final HandType expected;
        private final PokerHand data;

        public HandTypeTestEntry(final HandType expected, final PokerHand data) {
            this.expected = expected;
            this.data = data;
        }
    }
}