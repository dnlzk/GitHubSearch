package pl.nalazek.githubsearch;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import pl.nalazek.githubsearch.data.GitHubRepositoryTest;
import pl.nalazek.githubsearch.search.SearchPresenterTest;
import pl.nalazek.githubsearch.userdetails.UserDetailsPresenterTest;

/**
 * @author Daniel Nalazek
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GitHubRepositoryTest.class,
        SearchPresenterTest.class,
        UserDetailsPresenterTest.class})
public class MockUnitTestSuite {
}
