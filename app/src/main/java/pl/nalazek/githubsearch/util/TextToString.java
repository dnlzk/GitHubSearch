/**
 *  Copyright 2018 Daniel Nalazek

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package pl.nalazek.githubsearch.util;

import com.google.common.io.CharStreams;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

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



    public static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }
}
