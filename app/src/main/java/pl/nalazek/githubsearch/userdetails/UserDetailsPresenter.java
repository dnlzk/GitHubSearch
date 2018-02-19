/**
 *  Copyright 2018 Daniel Nalazek

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package pl.nalazek.githubsearch.userdetails;


import java.util.List;

import pl.nalazek.githubsearch.data.GitHubRepository;
import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;

/**
 *  This class is a controller responsible for serving requests and callbacks between the view
 *  and the model for detailed data
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
