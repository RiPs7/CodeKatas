package main;

import main.checkout.Checkout;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MainTest {

    @Test
    public void testTotals () {
        assertThat(price(""), is(0.0));
        assertThat(price("A"), is(50.0));
        assertThat(price("AB"), is(80.0));
        assertThat(price("CDBA"), is(115.0));

        assertThat(price("AA"), is(100.0));
        assertThat(price("AAA"), is(130.0));
        assertThat(price("AAAA"), is(180.0));
        assertThat(price("AAAAA"), is(230.0));
        assertThat(price("AAAAAA"), is(260.0));

        assertThat(price("AAAB"), is(160.0));
        assertThat(price("AAABB"), is(175.0));
        assertThat(price("AAABBD"), is(190.0));
        assertThat(price("DABABA"), is(190.0));
    }

    @Test
    public void testIncremental () {
        final Checkout checkout = new Checkout();

        assertThat(checkout.getTotal(), is(0.0));

        checkout.scan("A");
        assertThat(checkout.getTotal(), is(50.0));

        checkout.scan("B");
        assertThat(checkout.getTotal(), is(80.0));

        checkout.scan("A");
        assertThat(checkout.getTotal(), is(130.0));

        checkout.scan("A");
        assertThat(checkout.getTotal(), is(160.0));

        checkout.scan("B");
        assertThat(checkout.getTotal(), is(175.0));
    }

    private double price (String goods) {
        final Checkout checkout = new Checkout();
        if ("".equals(goods)) {
            return 0;
        }
        Arrays.stream(goods.split("")).forEach(checkout::scan);
        return checkout.getTotal();
    }

}
