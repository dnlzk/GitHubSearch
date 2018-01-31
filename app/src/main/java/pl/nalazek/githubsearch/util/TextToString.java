package pl.nalazek.githubsearch.util;

import com.google.common.io.CharStreams;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author Daniel Nalazek
 */

public class TextToString {

    public static String read(String filename) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(filename);
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String read(String filename, Charset charset) throws IOException {
        FileInputStream inputStream = new FileInputStream(filename);
        return CharStreams.toString(new InputStreamReader(inputStream, charset));
    }

    public static void printCurrentDirectory() {
        System.out.println(System.getProperty("user.dir"));
    }
}
