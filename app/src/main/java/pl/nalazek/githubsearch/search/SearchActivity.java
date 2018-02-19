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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.nalazek.githubsearch.Injection;
import pl.nalazek.githubsearch.R;
import pl.nalazek.githubsearch.data.ResultObjects.Result;



public class SearchActivity extends AppCompatActivity implements SearchContract.Showable {

    private SearchResultListAdapter searchResultListAdapter;
    private SearchContract.UserActionListener userActionListener;
    private SearchActivityHelper helper;
    private LinearLayout progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        helper = new SearchActivityHelper(this);
        userActionListener = new SearchPresenter(Injection.provideGitHubRepository(), this);

        setContentView(R.layout.activity_main);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        progressBar = (LinearLayout) findViewById(R.id.include_progress);
        searchResultListAdapter = new SearchResultListAdapter(this, R.layout.item_user_repo, new ArrayList<Result>());

        configureToolbars();
        configureResultsListView();

        handleIntent(getIntent());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflateMenu(menu);
        configureSearch(menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            //TODO Settings
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        userActionListener.finish();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    public void showResults(List<? extends Result> resultsArray) {
        hideBusy();
        searchResultListAdapter.clear();
        searchResultListAdapter.addAll(resultsArray);
    }



    @Override
    public void showError(String error) {
        hideBusy();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void showBusy() {
        progressBar.setVisibility(View.VISIBLE);
    }



    @Override
    public void hideBusy() {
        progressBar.setVisibility(View.INVISIBLE);
    }




    private void inflateMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_scrolling, menu);
    }



    private void configureToolbars() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setScrimsShown(true);
    }



    private void configureResultsListView() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        helper.configureListView(listView, searchResultListAdapter);
    }



    private void configureSearch(Menu menu) {

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        helper.configureSearchView(searchView, searchManager);
    }



    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            boolean stop = intent.getBooleanExtra(SearchPresenter.STOP_SEARCH, false);
            handleSearchAction(intent, stop);
        }
    }



    private void handleSearchAction(Intent intent, boolean stop) {

        if(stop) {
            userActionListener.stopSearch();
        }
        else {
            String query = intent.getStringExtra(SearchManager.QUERY);
            boolean checkInHistory = intent.getBooleanExtra(SearchPresenter.CHECK_IN_HISTORY, false);
            userActionListener.requestSearch(query, checkInHistory);
        }
    }
}
