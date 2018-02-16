package pl.nalazek.githubsearch.userdetails;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.Spy;

import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import pl.nalazek.githubsearch.FakeGitHubRepositoryAPI;
import pl.nalazek.githubsearch.Injection;
import pl.nalazek.githubsearch.data.GitHubRepository;
import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;
import pl.nalazek.githubsearch.data.ResultObjects.UserSearchResult;

import static org.mockito.Mockito.timeout;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsPresenterTest {

    @Spy
    UserDetailedContract.Showable showable;
    @Spy
    GitHubRepository repo =  Mockito.spy(Injection.provideGitHubRepository());

    @Captor
    ArgumentCaptor<List<? extends Result>> resultCaptor;
    @Captor
    ArgumentCaptor<String> errorCaptor;

    private UserDetailsPresenter userPresenter;



    @Before
    public void before() {
        userPresenter = new UserDetailsPresenter(repo, showable);
    }



    @Test
    public void whenRequestDetailedDataThenShowableBusyAndShowResult() throws Exception {

        SearchResult searchResult = new UserSearchResult(
                "title",
                "description",
                "userURL",
                "starredURL",
                "avatarURL");

        userPresenter.requestDetailedData(searchResult);

        Mockito.verify(showable, timeout(100)).showBusy();
        Mockito.verify(showable, timeout(100)).showResults(resultCaptor.capture());
    }



    @Test
    public void whenRequestDetailedrDataWithErrorThenShowableBusyAndShowError() throws Exception {

        SearchResult searchResult = new UserSearchResult(
                FakeGitHubRepositoryAPI.GENERATE_ERROR_PHRASE,
                "description",
                "userURL",
                "starredURL",
                "avatarURL");

        userPresenter.requestDetailedData(searchResult);

        Mockito.verify(showable, timeout(100)).showBusy();
        Mockito.verify(showable, timeout(100)).showError(errorCaptor.capture());
    }



}