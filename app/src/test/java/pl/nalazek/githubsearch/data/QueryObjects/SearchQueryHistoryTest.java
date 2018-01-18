package pl.nalazek.githubsearch.data.QueryObjects;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Observable;
import java.util.Observer;

import pl.nalazek.githubsearch.data.ResponsePackage;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchQueryHistoryTest {

    SearchQueryHistory history;
    String keyword = "sjfosjf";
    String emptyKeyword = "";

    @Mock
    ResponsePackage responsePackage;

    @Before
    public void beforeConstructor() {
        history = new SearchQueryHistory();
    }

    @Test
    public void whenNewInstanceThenHistorySizeIsZero() throws Exception {
        assertThat("History size fault", history.getHistorySize(), is(0));
    }

    @Test
    public void whenNewPairPutThenHistorySizeIsOne() throws Exception {
        history.put(keyword,responsePackage);
        assertThat("History size fault", history.getHistorySize(), is(1));
    }

    @Test
    public void givenNewPairWhenGetKeywordThenSameResponsePackage() throws Exception {
        history.put(keyword,responsePackage);
        assertThat("Returning value fault", history.get(keyword), is(responsePackage));
    }

    @Test
    public void givenNewPairWhenGetLastResponseThenSameResponsePackage() throws Exception {
        history.put(keyword,responsePackage);
        assertThat("Returning value fault", history.getLastResponse(), is(responsePackage));
    }

    @Test
    public void givenNewPairWhenIsInHistoryKeywordThenTrue() throws Exception {
        history.put(keyword,responsePackage);
        assertThat("Returning value fault", history.isKeywordInHistory(keyword), is(true));
    }

    @Test
    public void givenNewPairWhenIsInHistoryUnknownKeywordThenFalse() throws Exception {
        history.put(keyword,responsePackage);
        assertThat("Returning value fault", history.isKeywordInHistory("test123"), is(false));
    }

    @Test
    public void givenObserverWhenNewPairAddedThenUpdateCalled() throws Exception {
        Observer observer = Mockito.spy( new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                ;
            }
        });
        history.addObserver(observer);
        history.put(keyword,responsePackage);
        Mockito.verify(observer).update(history,keyword);
    }

    @Test
    public void whenNewPairWithEmptyStringAddedThenSameResponsePackage() throws Exception {
        history.put(emptyKeyword,responsePackage);
        assertThat("Returning value fault", history.get(emptyKeyword), is(responsePackage));
    }

    @Test(expected = NullPointerException.class)
    public void whenNewPairWithNullStringAddedThenNullPointer() throws Exception {
        history.put(null,responsePackage);
    }

    @Test(expected = NullPointerException.class)
    public void whenNewPairWithNullResponseAddedThenSameResponsePackage() throws Exception {
        responsePackage = null;
        history.put(keyword,responsePackage);
    }
}