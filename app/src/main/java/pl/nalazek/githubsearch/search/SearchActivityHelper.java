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

package pl.nalazek.githubsearch.search;

import android.app.SearchManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.UserSearchResult;
import pl.nalazek.githubsearch.userdetails.UserDetailedActivity;

/**
 * Helper class for search activity
 */
public class SearchActivityHelper {

    private final SearchActivity searchActivity;



    SearchActivityHelper(@NonNull SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }



    void configureSearchView(@NonNull SearchView searchView, @NonNull SearchManager searchManager) {

        searchView.setSearchableInfo(searchManager.getSearchableInfo(searchActivity.getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String keyword) {
                if(keyword.length() > 0)
                    createAndStartSearchActivity(keyword, false);
                return true;
            }



            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 0)
                    createAndStartSearchActivity(newText, true);
                else
                    stopSearch();
                return true;
            }



            private void stopSearch() {
                Intent intent = createSearchIntent();
                intent.putExtra(SearchPresenter.STOP_SEARCH, true);
                searchActivity.startActivity(intent);
            }



            private void createAndStartSearchActivity(String keyword, boolean checkInHistory) {
                Intent intent = createSearchIntent();
                intent.putExtra(SearchManager.QUERY, keyword);
                intent.putExtra(SearchPresenter.CHECK_IN_HISTORY, checkInHistory);
                searchActivity.startActivity(intent);
            }



            @NonNull
            private Intent createSearchIntent() {
                Intent intent = new Intent(searchActivity, SearchActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                return intent;
            }
        });
    }



    void configureListView(@NonNull ListView listView,
                           @NonNull final SearchResultListAdapter searchResultListAdapter) {

        listView.setAdapter(searchResultListAdapter);
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                Result result = searchResultListAdapter.getItem(position);
                if(result.getResultType().equals(UserSearchResult.TYPE))
                    createAndStartUserDetailedActivity((UserSearchResult)result);
            }



            private void createAndStartUserDetailedActivity(UserSearchResult result) {
                Intent intent = createDetailedIntent();
                intent.putExtra(UserSearchResult.PARCELABLE_TAG, result);
                searchActivity.startActivity(intent);
            }



            @NonNull
            private Intent createDetailedIntent() {
                Intent intent = new Intent(searchActivity, UserDetailedActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                return intent;
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
    }

}
