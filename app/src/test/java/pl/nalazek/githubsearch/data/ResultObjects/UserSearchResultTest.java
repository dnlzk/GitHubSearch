package pl.nalazek.githubsearch.data.ResultObjects;

import org.junit.Before;
import org.junit.Test;

import pl.nalazek.githubsearch.data.ExchangeType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
public class UserSearchResultTest {

    UserSearchResult userSearchResult;
    String name = "John";
    String description = "evaluate description";
    String exampleUserUrl = "http://u.pl";
    String exampleStarredUrl = "http://s.pl";
    String exampleAvatarUrl = "http://a.pl";

    @Before
    public void beforeConstructor() {
        userSearchResult = new UserSearchResult(name, description, exampleUserUrl, exampleStarredUrl, exampleAvatarUrl);
    }

    @Test
    public void whenGetTitleThenName() throws Exception {
        assertThat("Title fault", userSearchResult.getTitle(), is(name));
    }

    @Test
    public void whenGetDescriptionThenDescription() throws Exception {
        assertThat("Description fault", userSearchResult.getDescription(), is(description));
    }

    @Test
    public void whenGetExchangeThenUserSearch() throws Exception {
        assertThat("Exchange type fault", userSearchResult.getExchangeType(), is(ExchangeType.USER_SEARCH));
    }

    @Test
    public void whenGetTypeThenUserSearchType() throws Exception {
        assertThat("Type fault", userSearchResult.getResultType(), is(UserSearchResult.TYPE));
    }

    @Test
    public void whenGetUserURLThenUserUrl() throws Exception {
        assertThat("Type fault", userSearchResult.getUserURL(), is(exampleUserUrl));
    }

    @Test
    public void whenGetAvatarURLThenAvatarUrl() throws Exception {
        assertThat("Type fault", userSearchResult.getAvatarURL(), is(exampleAvatarUrl));
    }

    @Test
    public void whenGetStarredURLThenStarredUrl() throws Exception {
        assertThat("Type fault", userSearchResult.getStarredURL(), is(exampleStarredUrl));
    }

    @Test
    public void givenNewUserSearchResultSameValuesWhenEqualsThenEqual() throws Exception {
        UserSearchResult newUserSearchResult = new UserSearchResult(name, description, exampleUserUrl, exampleStarredUrl, exampleAvatarUrl);
        assertTrue(userSearchResult.equals(newUserSearchResult));
    }

    @Test
    public void givenNewUserSearchResultSameValuesWhenHashCodeEqualityCheckThenEqual() throws Exception {
        UserSearchResult newUserSearchResult = new UserSearchResult(name, description, exampleUserUrl, exampleStarredUrl, exampleAvatarUrl);
        assertTrue(userSearchResult.hashCode() == newUserSearchResult.hashCode());
    }

    @Test
    public void givenNewUserSearchResultDifferentValuesWhenEqualsThenNotEqual() throws Exception {
        UserSearchResult newUserSearchResult = new UserSearchResult(name, description, exampleUserUrl, exampleStarredUrl, "other");
        assertFalse(userSearchResult.equals(newUserSearchResult));
    }

    @Test
    public void givenNewUserSearchResultDifferentValuesWhenHashCodeEqualityCheckThenNotEqual() throws Exception {
        UserSearchResult newUserSearchResult = new UserSearchResult(name, description, exampleUserUrl, "other", exampleAvatarUrl);
        assertFalse(userSearchResult.hashCode() == newUserSearchResult.hashCode());
    }

}