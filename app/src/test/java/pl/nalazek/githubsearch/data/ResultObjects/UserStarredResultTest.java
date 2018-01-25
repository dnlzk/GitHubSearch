package pl.nalazek.githubsearch.data.ResultObjects;

import org.junit.Before;
import org.junit.Test;

import pl.nalazek.githubsearch.data.ExchangeType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
public class UserStarredResultTest {

    UserStarredResult userStarredResult;
    int id = 546;
    String url = "http://dfsdf.com";

    @Before
    public void before() {
        userStarredResult = new UserStarredResult(id, url);
    }


    @Test
    public void whenGetResultTypeThenResultType() throws Exception {
        assertThat("Result type fault", userStarredResult.getResultType(), is(UserStarredResult.TYPE));
    }

    @Test
    public void whenGetExchangeTypeThenUserStarredExchange() throws Exception {
        assertThat("Exchange type fault", userStarredResult.getExchangeType(), is(ExchangeType.USER_DETAILED_STARS));
    }



}