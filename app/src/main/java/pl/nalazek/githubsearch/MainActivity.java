package pl.nalazek.githubsearch;

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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import pl.nalazek.githubsearch.ResultObjects.Result;
import pl.nalazek.githubsearch.ResultObjects.SearchResult;
import pl.nalazek.githubsearch.ResultObjects.UserSearchResult;

public class MainActivity extends AppCompatActivity {

    CustomListAdapter customListAdapter;
    private SearchAgent searchAgent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchAgent = SearchAgent.getInstance();
        setContentView(R.layout.activity_main);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        configureToolbars();
        handleIntent(getIntent());
        configureListViewAndProgressBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflateMenu(menu);
        configureSearchEngine(menu);
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

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            boolean checkInHistory = intent.getBooleanExtra(SearchAgent.CHECK_IN_HISTORY, false);
            searchAgent.searchForPhrase(query, checkInHistory, customListAdapter);
        }
    }

    private void configureToolbars() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setScrimsShown(true);
    }


    private void configureListViewAndProgressBar() {
        LinearLayout progressBar = (LinearLayout) findViewById(R.id.include_progress);
        customListAdapter = new CustomListAdapter(this, R.layout.item_user_repo, new ArrayList<Result>(),progressBar);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(customListAdapter);
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                Result result = customListAdapter.getItem(position);

                if(result instanceof UserSearchResult) {
                    Intent intent = new Intent(MainActivity.this, UserDetailedActivity.class);
                    intent.setAction(Intent.ACTION_VIEW);
                    String userUrl = ((UserSearchResult)result).getUserURL();
                    intent.putExtra("userUrl", userUrl);
                    startActivity(intent);
                }
        }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
    }

    private void inflateMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_scrolling, menu);
    }


    private void configureSearchEngine(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 0)
                    startNewSearchAction(query, false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 0)
                    startNewSearchAction(newText, true);
                return true;
            }

            private void startNewSearchAction(String query, boolean checkInHistory) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                intent.putExtra(SearchAgent.CHECK_IN_HISTORY, checkInHistory);
                startActivity(intent);
            }
        });
    }

}
