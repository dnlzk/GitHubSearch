package pl.nalazek.githubsearch.util;
import com.google.common.base.Charsets;

import org.junit.Test;

import java.io.FileNotFoundException;

import pl.nalazek.githubsearch.data.DataPaths;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
public class TextToStringTest {

    private final static String FILE = DataPaths.JSON_FILE_PATH + "test.txt";
    private final static String FILE_UTF8 = DataPaths.JSON_FILE_PATH + "test_UTF-8.txt";
    private String WORD = "test";

    @Test
    public void givenCharsetwhenReadThenStringIsWord() throws Exception {
        TextToString.printCurrentDirectory();
        String test;

        try {
            test = TextToString.read(FILE, Charsets.UTF_16);
        }
        catch(FileNotFoundException e) {
            test = TextToString.getCurrentDirectory();
            e.printStackTrace();
        }
        assertThat("Test word fault", test, is(WORD));
    }

    @Test
    public void whenReadThenStringIsWord() throws Exception {
        TextToString.printCurrentDirectory();
        String test;
        try {
            test = TextToString.read(FILE_UTF8);
        }
        catch(FileNotFoundException e) {
            test = TextToString.getCurrentDirectory();
            e.printStackTrace();
        }
        assertThat("Test word fault", test, is(WORD));
    }

}