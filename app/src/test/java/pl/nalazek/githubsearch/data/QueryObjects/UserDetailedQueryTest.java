package pl.nalazek.githubsearch.data.QueryObjects;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URL;
import java.util.Arrays;

import pl.nalazek.githubsearch.data.ExchangeType;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Daniel Nalazek
 */
@RunWith(value = Parameterized.class)
public class UserDetailedQueryTest {

    @Parameterized.Parameters
    public static Iterable<ExchangeType> testData() {
        return Arrays.asList(ExchangeType.values());
    }

    @Parameterized.Parameter
    public ExchangeType eType;

    public UserDetailedQuery userDetailedQuery;
    public URL url;



    @Before
    public void beforeConstructor() throws Exception {
        url = new URL("http://a.com");
        userDetailedQuery = new UserDetailedQuery(url, eType);
    }



    @Test
    public void whenGetQueryTypeThenType() throws Exception {
        assertThat("getQueryType method fault", userDetailedQuery.getQueryType(), is("Detailed"));
    }



    @Test
    public void whenGetExchangeTypeThenExchangeType() throws Exception {
        assertThat("getExchangeType method fault", userDetailedQuery.getExchangeType(), is(eType));
    }



    @Test
    public void whenGetURLThenURL() throws Exception {
        assertThat("getURL method fault", userDetailedQuery.getURL(), is(url));
    }
}