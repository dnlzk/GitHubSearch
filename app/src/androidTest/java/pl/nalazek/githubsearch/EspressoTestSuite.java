package pl.nalazek.githubsearch;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import pl.nalazek.githubsearch.search.SearchActivityTest;
import pl.nalazek.githubsearch.search.SearchActivityToUserDetailedActivityUserInputRecordTest;
import pl.nalazek.githubsearch.search.SearchActivityWhitespaceRecordTest;

/**
 * Entry point to all Espresso tests
 * @author Daniel Nalazek
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SearchActivityTest.class,
        SearchActivityWhitespaceRecordTest.class,
        SearchActivityToUserDetailedActivityUserInputRecordTest.class})
public class EspressoTestSuite {
}
