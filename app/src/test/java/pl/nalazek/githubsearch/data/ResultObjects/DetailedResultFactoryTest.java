package pl.nalazek.githubsearch.data.ResultObjects;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InvalidObjectException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Charsets;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import pl.nalazek.githubsearch.data.*;
import pl.nalazek.githubsearch.data.QueryObjects.*;
import pl.nalazek.githubsearch.util.TextToString;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class DetailedResultFactoryTest {

    private static final String JSON_FILE_STARRED_PAGE_FIRST_LINKHEADER = "testInputs/starFirstPage.linkhead";
    private static final String JSON_FILE_STARRED_PAGE_FIRST = "testInputs/starFirstPage.json";
    private static final String JSON_FILE_STARRED_PAGE_LAST_LINKHEADER = "testInputs/starLastPage.linkhead";
    private static final String JSON_FILE_STARRED_PAGE_LAST = "testInputs/starLastPage.json";
    private static final String JSON_FILE_STARRED_PAGE_ONEBUTLAST_LINKHEADER = "testInputs/starOneButLastPage.linkhead";
    private static final String JSON_FILE_STARRED_PAGE_SINGLETON = "testInputs/starSingleton.json";


    @Mock
    ResponseBody bodyUser;
    @Mock
    ResponseBody bodyStarsFirstPage;
    @Mock
    Headers headersStarsFirstPage;
    @Mock
    ResponseBody bodyStarsLastPage;
    @Mock
    Headers headersStarsLastPage;
    @Mock
    ResponseBody bodyStarsSingleton;
    @Mock
    Headers headersStarsSingleton;
    @Mock
    Headers headersStarsOneButLastPage;
    @Mock
    ResponseBody bodyAvatarImage;
    @Mock
    Headers headers;


    @Rule
    public ExpectedException exception = ExpectedException.none();

    DetailedResultFactory detailedResultFactory;
    String name = "Jason Rudolph";
    String followers = "332";
    ResponsePackage responsePackage;



    @Before
    public void before() throws Exception {

        detailedResultFactory = new DetailedResultFactory();
        responsePackage = new ResponsePackage(UserDetailedQuery.TYPE);
        setupMocks();
    }



    @Test
    public void givenResponsePackageWithoutResponsesWhenMakeResultsThenEmptyList() throws Exception {
        List<DetailedResult> list = detailedResultFactory.makeResults(responsePackage);
        assertTrue("Results empty list fault", list.isEmpty());
    }



    @Test(expected = InvalidObjectException.class)
    public void givenResponsePackageWithSearchQueryTypeWhenMakeResultsThenThrowInvalidObjectException() throws Exception {
        mockResponsePackageWithSearchQueryType();
        detailedResultFactory.makeResults(responsePackage);
    }



    @Test(expected = InvalidJsonObjectException.class)
    public void givenResponsePackageWithUserDetailedStarsResponseWhenMakeResultsThenException() throws Exception {
        addToResponsePackageFirstPageLastPageStarsResponses();
        detailedResultFactory.makeResults(responsePackage);
    }



    @Test
    public void givenResponsePackageWithUserDetailedStarsNotLastNotFirstPageResponseWhenMakeResultsThenException() throws Exception {

        exception.expect(InvalidJsonObjectException.class);
        exception.expectMessage(containsString("first and/or last"));

        addToResponsePackageFirstPageNotLastPageStarsResponses();
        detailedResultFactory.makeResults(responsePackage);
    }



    @Test
    public void givenResponsePackageWithAvatarResponseWhenMakeResultsThenException() throws Exception {

        exception.expect(InvalidJsonObjectException.class);
        exception.expectMessage(containsString("Not found"));

        addToResponsePackageAvatarResponse();
        detailedResultFactory.makeResults(responsePackage);
    }



    @Test(expected = InvalidJsonObjectException.class)
    public void givenResponsePackageWithDetailedUserResponseWhenMakeResultsThenException() throws Exception {
        addToResponsePackageUserDetailedResponse();
        detailedResultFactory.makeResults(responsePackage);
    }



    @Test
    public void givenResponsePackageWithStarsAndAvatarResponseWhenMakeResultsThenException() throws Exception {

        exception.expect(InvalidJsonObjectException.class);
        exception.expectMessage(containsString("Not found"));

        addToResponsePackageFirstPageLastPageStarsResponses();
        addToResponsePackageAvatarResponse();

        detailedResultFactory.makeResults(responsePackage);
    }


    @Test
    public void givenResponsePackageWithStarsAndAvatarAndUserResponseWhenMakeResultsThenResultSizeOne() throws Exception {

        addToResponsePackageFirstPageLastPageStarsResponses();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        List<DetailedResult> results = detailedResultFactory.makeResults(responsePackage);
        assertThat("Result size fault", results.size(), is(1));
    }


    @Test
    public void givenResponsePackageWithStarsAndAvatarAndUserResponseWhenMakeResultsThenTypeUserDetailedResult() throws Exception {

        addToResponsePackageFirstPageLastPageStarsResponses();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        List<DetailedResult> results = detailedResultFactory.makeResults(responsePackage);
        assertTrue("Result type fault", results.get(0) instanceof UserDetailedResult);
    }


    @Test
    public void givenResponsePackageWithStarsIncompleteOnlyFirstPageAndAvatarAndUserResponseWhenMakeResultsThenException() throws Exception {

        exception.expect(InvalidJsonObjectException.class);
        exception.expectMessage(containsString("First or last page of stars exchange missing"));

        addToResponsePackageFirstPageStarsResponse();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        detailedResultFactory.makeResults(responsePackage);
    }


    @Test
    public void givenResponsePackageWithStarsIncompleteOnlyLastPageAndAvatarAndUserResponseWhenMakeResultsThenException() throws Exception {

        exception.expect(InvalidJsonObjectException.class);
        exception.expectMessage(containsString("First or last page of stars exchange missing"));

        addToResponsePackageLastPageStarsResponse();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        detailedResultFactory.makeResults(responsePackage);
    }


    @Test
    public void givenResponsePackageWithStarsOnlyFirstPageAndAvatarAndUserResponseWhenMakeResultsThenCorrectResponse() throws Exception {

        addToResponsePackageSingletonPageStarsResponse();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        List<DetailedResult> results = detailedResultFactory.makeResults(responsePackage);
        UserDetailedResult result = (UserDetailedResult) results.get(0);

        assertThat("Result size fault", results.size(), is(1));
        assertThat("Result stars fault", result.getStars(), is(6));
        assertThat("Result name fault", result.getUserName(), is(name));
        assertThat("Result followers fault", result.getFollowers(), is(Integer.valueOf(followers)));
        assertThat("Result exchange type fault", result.getExchangeType(), is(ExchangeType.USER_DETAILED));
        assertTrue("Result avatar fault", result.getAvatarImage() == null);
        assertThat("Result type fault", result.getResultType(), is(UserDetailedResult.TYPE));
    }




    @Test
    public void givenResponsePackageWithStarsAndAvatarAndUserResponseWhenMakeResultsThenStarsCount() throws Exception {

        addToResponsePackageFirstPageLastPageStarsResponses();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        UserDetailedResult result = (UserDetailedResult) detailedResultFactory.makeResults(responsePackage).get(0);
        assertThat("Result stars count fault", result.getStars(), is(130));
    }


    @Test
    public void givenResponsePackageWithStarsAndAvatarAndUserResponseWhenMakeResultsThenExchangeType() throws Exception {

        addToResponsePackageFirstPageLastPageStarsResponses();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        UserDetailedResult result = (UserDetailedResult) detailedResultFactory.makeResults(responsePackage).get(0);
        assertThat("Result exchange type fault", result.getExchangeType(), is(ExchangeType.USER_DETAILED));
    }


    @Test
    public void givenResponsePackageWithStarsAndAvatarAndUserResponseWhenMakeResultsThenFollowersCount() throws Exception {

        addToResponsePackageFirstPageLastPageStarsResponses();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        UserDetailedResult result = (UserDetailedResult) detailedResultFactory.makeResults(responsePackage).get(0);
        assertThat("Result followers fault", result.getFollowers(), is(Integer.valueOf(followers)));
    }


    @Test
    public void givenResponsePackageWithStarsAndAvatarAndUserResponseWhenMakeResultsThenName() throws Exception {

        addToResponsePackageFirstPageLastPageStarsResponses();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        UserDetailedResult result = (UserDetailedResult) detailedResultFactory.makeResults(responsePackage).get(0);
        assertThat("Result name fault", result.getUserName(), is(name));
    }


    @Test
    public void givenResponsePackageWithStarsAndAvatarAndUserResponseWhenMakeResultsThenAvatarNull() throws Exception {

        addToResponsePackageFirstPageLastPageStarsResponses();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        UserDetailedResult result = (UserDetailedResult) detailedResultFactory.makeResults(responsePackage).get(0);
        assertTrue("Result avatar null fault", result.getAvatarImage() == null);
    }


    @Test
    public void givenResponsePackageWithStarsAndAvatarAndUserResponseWhenMakeResultThenResultType() throws Exception {

        addToResponsePackageFirstPageLastPageStarsResponses();
        addToResponsePackageAvatarResponse();
        addToResponsePackageUserDetailedResponse();

        UserDetailedResult result = (UserDetailedResult) detailedResultFactory.makeResults(responsePackage).get(0);
        assertThat("Result type fault", result.getResultType(), is(UserDetailedResult.TYPE));
    }


    @Test
    public void givenResponsePartitionedUserDetailedWhenMakeResultsThenArrayLenghtOne() throws Exception {
        DetailedResult[] detailedResults = givenResponsePartionedUserDetailedWhenMakeResults();
        assertThat("Results array lenght fault", detailedResults.length, is(1));
    }



    @Test
    public void givenResponsePartitionedUserDetailedWhenMakeResultsThenResultClassTypeIsUserDetailedResult() throws Exception {
        DetailedResult[] detailedResults = givenResponsePartionedUserDetailedWhenMakeResults();
        assertTrue("Results class type fault", detailedResults[0] instanceof UserDetailedResult);
    }



    @Test
    public void givenResponsePartitionedUserDetailedWhenMakeResultsAndGetNameThenName() throws Exception {
        UserDetailedResult userDetailedResult = givenResponsePartionedUserDetailedWhenMakeResultsGetFirstResult();
        assertThat("Results name fault", userDetailedResult.getUserName(), is(name));
    }



    @Test
    public void givenResponsePartitionedUserDetailedWhenMakeResultsAndGetFollowersThenFollowers() throws Exception {
        UserDetailedResult userDetailedResult = givenResponsePartionedUserDetailedWhenMakeResultsGetFirstResult();
        assertThat("Followers fault", userDetailedResult.getFollowers(), is(Integer.valueOf(followers)));
    }



    @Test
    public void givenResponsePartitionedUserDetailedWhenMakeResultsAndGetAvatarThenNull() throws Exception {
        UserDetailedResult userDetailedResult = givenResponsePartionedUserDetailedWhenMakeResultsGetFirstResult();
        assertTrue("Avatar null fault", userDetailedResult.getAvatarImage() == null);
    }



    @Test
    public void givenResponsePartitionedUserDetailedWhenMakeResultsAndGetStarsThenZero() throws Exception {
        UserDetailedResult userDetailedResult = givenResponsePartionedUserDetailedWhenMakeResultsGetFirstResult();
        assertThat("Stars count fault", userDetailedResult.getStars(), is(-1));
    }



    @Test(expected = InvalidJsonObjectException.class)
    public void givenResponsePartitionedSearchWhenMakeResultsThenException() throws Exception {
        ResponsePartitioned responsePartitioned = new ResponsePartitioned(headers, bodyUser, QueryTask.STATE_SUCCESS, ExchangeType.USER_SEARCH);
        detailedResultFactory.makeResults(responsePartitioned);
    }



    @Test
    public void givenResponsePartionedAvatarWhenMakeResultsThenResultAndTypeEmpty() throws Exception {
        ResponsePartitioned responsePartitioned = new ResponsePartitioned(headersStarsLastPage, bodyStarsLastPage, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_AVATAR);
        DetailedResult[] result = detailedResultFactory.makeResults(responsePartitioned);
        assertThat("Avatar result error", result[0].getResultType(), is(DetailedResult.TYPE));
    }


    @Test
    public void givenResponsePartionedStarsWhenMakeResultsThenEmptyArray() throws Exception {
        ResponsePartitioned responsePartitioned = new ResponsePartitioned(headersStarsLastPage, bodyStarsLastPage, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_STARS);
        DetailedResult[] result = detailedResultFactory.makeResults(responsePartitioned);
        assertThat("Avatar result error", result.length, is(10));
    }





    private void setupMocks() throws Exception{
        when(bodyUser.string()).thenReturn("{\n" +
                "  \"login\": \"jasonrudolph\",\n" +
                "  \"id\": 2988,\n" +
                "  \"avatar_url\": \"https://avatars3.githubusercontent.com/u/2988?v=4\",\n" +
                "  \"gravatar_id\": \"\",\n" +
                "  \"url\": \"https://api.github.com/users/jasonrudolph\",\n" +
                "  \"html_url\": \"https://github.com/jasonrudolph\",\n" +
                "  \"followers_url\": \"https://api.github.com/users/jasonrudolph/followers\",\n" +
                "  \"following_url\": \"https://api.github.com/users/jasonrudolph/following{/other_user}\",\n" +
                "  \"gists_url\": \"https://api.github.com/users/jasonrudolph/gists{/gist_id}\",\n" +
                "  \"starred_url\": \"https://api.github.com/users/jasonrudolph/starred{/owner}{/repo}\",\n" +
                "  \"subscriptions_url\": \"https://api.github.com/users/jasonrudolph/subscriptions\",\n" +
                "  \"organizations_url\": \"https://api.github.com/users/jasonrudolph/orgs\",\n" +
                "  \"repos_url\": \"https://api.github.com/users/jasonrudolph/repos\",\n" +
                "  \"events_url\": \"https://api.github.com/users/jasonrudolph/events{/privacy}\",\n" +
                "  \"received_events_url\": \"https://api.github.com/users/jasonrudolph/received_events\",\n" +
                "  \"type\": \"User\",\n" +
                "  \"site_admin\": true,\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"company\": null,\n" +
                "  \"blog\": \"http://jasonrudolph.com\",\n" +
                "  \"location\": \"Durham, NC\",\n" +
                "  \"email\": null,\n" +
                "  \"hireable\": null,\n" +
                "  \"bio\": null,\n" +
                "  \"public_repos\": 49,\n" +
                "  \"public_gists\": 27,\n" +
                "  \"followers\": " + followers + ",\n" +
                "  \"following\": 0,\n" +
                "  \"created_at\": \"2008-03-13T15:02:53Z\",\n" +
                "  \"updated_at\": \"2018-01-01T15:12:27Z\"\n" +
                "}");

        when(headersStarsFirstPage.get("Link")).thenReturn(TextToString.read(JSON_FILE_STARRED_PAGE_FIRST_LINKHEADER, Charsets.UTF_16));
        when(bodyStarsFirstPage.string()).thenReturn(TextToString.read(JSON_FILE_STARRED_PAGE_FIRST, Charsets.UTF_16));
        when(headersStarsLastPage.get("Link")).thenReturn(TextToString.read(JSON_FILE_STARRED_PAGE_LAST_LINKHEADER, Charsets.UTF_16));
        when(bodyStarsLastPage.string()).thenReturn(TextToString.read(JSON_FILE_STARRED_PAGE_LAST, Charsets.UTF_16));
        when(headersStarsOneButLastPage.get("Link")).thenReturn(TextToString.read(JSON_FILE_STARRED_PAGE_ONEBUTLAST_LINKHEADER, Charsets.UTF_16));
        when(headersStarsSingleton.get("Link")).thenReturn(null);
        when(bodyStarsSingleton.string()).thenReturn(TextToString.read(JSON_FILE_STARRED_PAGE_SINGLETON, Charsets.UTF_16));

    }



    private void mockResponsePackageWithSearchQueryType() {
        responsePackage = mock(ResponsePackage.class);
        when(responsePackage.getQueryType()).thenReturn(SearchQuery.TYPE);
        when(responsePackage.isEmpty()).thenReturn(false);
    }


    private void addToResponsePackageFirstPageLastPageStarsResponses() {
        responsePackage.addResponses(Arrays.asList(
                new ResponsePartitioned(headersStarsFirstPage, bodyStarsFirstPage, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_STARS),
                new ResponsePartitioned(headersStarsLastPage, bodyStarsLastPage, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_STARS)
                )
        );
    }



    private void addToResponsePackageFirstPageNotLastPageStarsResponses() {
        responsePackage.addResponses(Arrays.asList(
                new ResponsePartitioned(headersStarsFirstPage, bodyStarsFirstPage, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_STARS),
                new ResponsePartitioned(headersStarsOneButLastPage, bodyStarsLastPage, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_STARS)
                )
        );
    }



    private void addToResponsePackageSingletonPageStarsResponse() {
        responsePackage.addResponses(Collections.singletonList(
                new ResponsePartitioned(headersStarsSingleton, bodyStarsSingleton, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_STARS)
                )
        );
    }



    private void addToResponsePackageAvatarResponse() {
        responsePackage.addResponses(Collections.singletonList(
                new ResponsePartitioned(headersStarsLastPage, bodyStarsLastPage, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_AVATAR)
                )
        );
    }



    private void addToResponsePackageUserDetailedResponse() {
        responsePackage.addResponses(Collections.singletonList(
                new ResponsePartitioned(headers, bodyUser, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED)
                )
        );
    }



    private void addToResponsePackageLastPageStarsResponse() {
        responsePackage.addResponses(Collections.singletonList(
                new ResponsePartitioned(headersStarsLastPage, bodyStarsLastPage, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_STARS)
                )
        );
    }



    private void addToResponsePackageFirstPageStarsResponse() {
        responsePackage.addResponses(Collections.singletonList(
                new ResponsePartitioned(headersStarsFirstPage, bodyStarsFirstPage, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_STARS)
                )
        );
    }



    private DetailedResult[] givenResponsePartionedUserDetailedWhenMakeResults() throws Exception {
        ResponsePartitioned responsePartitioned = new ResponsePartitioned(headers, bodyUser, QueryTask.STATE_SUCCESS ,ExchangeType.USER_DETAILED);
        return detailedResultFactory.makeResults(responsePartitioned);
    }



    private UserDetailedResult givenResponsePartionedUserDetailedWhenMakeResultsGetFirstResult() throws Exception{
        DetailedResult[] detailedResults = givenResponsePartionedUserDetailedWhenMakeResults();
        return (UserDetailedResult) detailedResults[0];
    }

}