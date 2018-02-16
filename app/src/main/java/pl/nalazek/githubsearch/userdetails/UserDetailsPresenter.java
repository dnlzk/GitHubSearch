package pl.nalazek.githubsearch.userdetails;

import java.util.List;

import pl.nalazek.githubsearch.data.GitHubRepository;
import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;

/**
 *  This class is a controller responsible for serving requests and callbacks between the view
 *  and the model for detailed data
 * @author Daniel Nalazek
 */
public class UserDetailsPresenter implements UserDetailedContract.UserActionListener {


    private UserDetailedContract.Showable showable;
    private GitHubRepository gitHubRepository;



    public UserDetailsPresenter(GitHubRepository gitHubRepository,
                                UserDetailedContract.Showable showable) {
        this.gitHubRepository = gitHubRepository;
        this.showable = showable;
    }



    @Override
    public void requestDetailedData(SearchResult searchResult) {

        showable.showBusy();
        gitHubRepository.requestDetailedData(searchResult,
                                            new GitHubRepository.DetailedResultsCallback() {

            @Override
            public void onDetailedDataResultReady(List<? extends Result> result) {
                showable.showResults(result);
            }

            @Override
            public void onError(String message) {
                showable.showError(message);
            }
        });
    }
}
