import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        final var hands = new ArrayList<PokerHand>();

        hands.add(new PokerHand("2S 3S 4S 5S 6S"));
        hands.add(new PokerHand("TC 4H 7D KC 2S"));
        Collections.sort(hands);

        System.out.println(hands);
    }
}
