package pl.nalazek.githubsearch.userdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.nalazek.githubsearch.Injection;
import pl.nalazek.githubsearch.R;
import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;
import pl.nalazek.githubsearch.data.ResultObjects.UserSearchResult;



public class UserDetailedActivity extends AppCompatActivity implements UserDetailedContract.Showable {

    private UserDetailsListAdapter userDetailsListAdapter;
    private UserDetailedContract.UserActionListener userActionListener;
    private LinearLayout progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        userActionListener = new UserDetailsPresenter(Injection.provideGitHubRepository(), this);

        setContentView(R.layout.activity_user_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configureViewElements();

        handleIntent(getIntent());
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    public void showResults(List<? extends Result> resultsArray) {
        progressBar.setVisibility(View.INVISIBLE);
        userDetailsListAdapter.clear();
        userDetailsListAdapter.addAll(resultsArray);
    }



    @Override
    public void showError(String error) {
        userDetailsListAdapter.clear();
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void showBusy() {
        userDetailsListAdapter.clear();
        progressBar.setVisibility(View.VISIBLE);
    }




    private void configureViewElements() {

        progressBar = (LinearLayout) findViewById(R.id.include_progress);
        userDetailsListAdapter = new UserDetailsListAdapter(
                this,
                R.layout.item_user_detailed,
                new ArrayList<Result>());

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(userDetailsListAdapter);
    }



    private void handleIntent(Intent intent) {

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            SearchResult searchResult = intent.getParcelableExtra(UserSearchResult.PARCELABLE_TAG);
            userActionListener.requestDetailedData(searchResult);
        }
    }
}
