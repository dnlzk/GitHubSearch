package pl.nalazek.githubsearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class ScrollingActivity extends AppCompatActivity {

    CustomListAdapter customListAdapter;
    private ProgressBarManager progressBarManager;
    private SearchAgent searchAgent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchAgent = SearchAgent.getInstance();
        setContentView(R.layout.activity_scrolling);
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
            searchAgent.searchForPhrase(query, customListAdapter, progressBarManager);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            //TODO Enter page
            //showResult(data);
        }
    }

    private void configureToolbars() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setScrimsShown(true);
    }


    private void configureListViewAndProgressBar() {
        customListAdapter = new CustomListAdapter(this, R.layout.item_user_repo, new ArrayList<SearchResult>());
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(customListAdapter);
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
        LinearLayout progressBar = (LinearLayout) findViewById(R.id.include_progress);
        progressBarManager = new ProgressBarManager(progressBar);
        SearchAgent.getQueryHistory().addObserver(progressBarManager);
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
                    startNewSearchAction(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 0)
                    startNewSearchAction(newText);
                return true;
            }

            private void startNewSearchAction(String query) {
                Intent intent = new Intent(ScrollingActivity.this, ScrollingActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
            }
        });
    }

}
