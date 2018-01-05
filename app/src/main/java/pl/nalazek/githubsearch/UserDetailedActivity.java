package pl.nalazek.githubsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pl.nalazek.githubsearch.QueryObjects.Query;
import pl.nalazek.githubsearch.QueryObjects.QueryBuilder;
import pl.nalazek.githubsearch.ResultObjects.Result;

public class UserDetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String userUrl = getIntent().getStringExtra("userUrl");
        String starredUrl = getIntent().getStringExtra("starredUrl");
        starredUrl = starredUrl.substring(0,starredUrl.indexOf('{'));
        String avatarUrl = getIntent().getStringExtra("avatarUrl");

        LinearLayout progressBar = (LinearLayout) findViewById(R.id.include_progress);
        UserDetailsListAdapter userDetailsListAdapter = new UserDetailsListAdapter(this, R.layout.item_user_detailed, new ArrayList<Result>(), progressBar);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(userDetailsListAdapter);

        Query[] queries;
        QueryBuilder queryBuilder = new QueryBuilder();

        userDetailsListAdapter.showBusy();
        try {
            queries = queryBuilder.buildUserDetailedQuerry().build(new URL(userUrl), new URL(starredUrl), null, userDetailsListAdapter);
            QueryTask queryTask = new QueryTask();
            queryTask.execute(queries);
        }
        catch (MalformedURLException e) {
            userDetailsListAdapter.showError(e.getMessage());
        }

    }
}
