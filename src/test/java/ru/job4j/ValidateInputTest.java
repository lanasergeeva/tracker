package ru.job4j;

import org.junit.Test;
import ru.job4j.tracker.interfaces.Input;
import ru.job4j.tracker.interfaces.Output;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ValidateInputTest {

    @Test
    public void whenInvalidInput() {
        Output out = new StubOutput();
        Input in = new StubInput(
                new String[]{"one", "1"}
        );
        ValidateInput input = new ValidateInput(out, in);
        int selected = input.askInt("Enter menu:");
        System.out.println(out.toString());
        assertThat(selected, is(1));
        assertThat(out.toString(), is(
                "Please enter validate data again." + System.lineSeparator()
        ));
    }

    @Test
    public void whenInvalidInputTrue() {
        Output out = new StubOutput();
        Input in = new StubInput(
                new String[]{"1"}
        );
        ValidateInput input = new ValidateInput(out, in);
        int selected = input.askInt("Enter menu:");
        assertThat(selected, is(1));
    }

    @Test
    public void whenInvalidInputMany() {
        Output out = new StubOutput();
        Input in = new StubInput(
                new String[]{"1", "2", "3"}
        );
        ValidateInput input = new ValidateInput(out, in);
        int selected = input.askInt("Enter menu:");
        System.out.println(out.toString());
        assertThat(selected, is(1));
    }

    @Test
    public void whenInvalidInputNegativ() {
        Output out = new StubOutput();
        Input in = new StubInput(
                new String[]{"-1"}
        );
        ValidateInput input = new ValidateInput(out, in);
        int selected = input.askInt("Enter menu:");
        System.out.println(out.toString());
        assertThat(selected, is(-1));
    }
}