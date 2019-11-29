import main.Rack;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MainTest {

    @Test
    public void testSortingBallsWithBinaryTreeRack () {
        insertElementsToRackAndAssert(
            new Rack(),
            true,
            20,
            Collections.singletonList(20),
            10,
            Arrays.asList(10, 20),
            30,
            Arrays.asList(10, 20, 30));
    }

    @Test
    public void testSortingCharactersWithBinaryTreeRack () {
        insertElementsToRackAndAssert(
            new Rack(),
            false,
            "When not studying nuclear physics, Bambi likes to play beach volleyball.",
        "BWaaaaabbbcccdeeeeeghhhiiiiklllllllmnnnnooopprsssstttuuvyyyy");
    }

    private void insertElementsToRackAndAssert (Rack rack, boolean incrementally, Object... objects) {
        if (incrementally) {
            for (int i = 0; i < objects.length; i += 2) {
                rack.add(objects[i]);
                assertThat(rack.order(), is(objects[i + 1]));
            }
        } else {
            String sentence = (String) objects[0];
            for (int i = 0; i < sentence.length(); i++) {
                char c = sentence.charAt(i);
                if (!Character.isAlphabetic(c)) {
                    continue;
                }
                rack.add(c);
            }
            assertThat(rack.order().stream().map(Object::toString).reduce("", String::concat), is(objects[1]));
        }
    }

}
