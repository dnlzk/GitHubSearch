package pl.nalazek.githubsearch.data.QueryObjects;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(value = Parameterized.class)
public class SearchQueryWhitespacesTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Parameterized.Parameters
    public static Iterable<Object> testData() {
        return Arrays.asList(new Object[] {
                "    ", "", " ", "\t", "\r", "\n"
        });
    }

    @Parameterized.Parameter()
    public static String keyword;


    public SearchQuery searchQuery;


    @Test
    public void whenNewSearchQueryInstanceGivenWhitespaceKeywordThenException()  throws  WhitespaceKeywordException {

        for(SearchQuery.SearchScope scope : SearchQuery.SearchScope.values())
        {
            exception.expect(WhitespaceKeywordException.class);
            searchQuery = new SearchQuery(keyword, scope);
        }
    }
}