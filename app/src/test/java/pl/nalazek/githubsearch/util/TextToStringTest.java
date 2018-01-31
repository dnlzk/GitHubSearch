package pl.nalazek.githubsearch.util;
import com.google.common.base.Charsets;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
public class TextToStringTest {

    private final static String FILE = "testInputs/test.txt";
    private final static String FILE_UTF8 = "testInputs/test_UTF-8.txt";
    private String WORD = "test";

    @Test
    public void givenCharsetwhenReadThenStringIsWord() throws Exception {
        TextToString.printCurrentDirectory();
        String test = TextToString.read(FILE, Charsets.UTF_16);
        assertThat("Test word fault", test, is(WORD));
    }

    @Test
    public void whenReadThenStringIsWord() throws Exception {
        TextToString.printCurrentDirectory();
        String test = TextToString.read(FILE_UTF8);
        assertThat("Test word fault", test, is(WORD));
    }

}