package pl.nalazek.githubsearch;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.nalazek.githubsearch.ResultObjects.RepoSearchResult;
import pl.nalazek.githubsearch.ResultObjects.Result;
import pl.nalazek.githubsearch.ResultObjects.SearchResult;
import pl.nalazek.githubsearch.ResultObjects.UserSearchResult;

/**
 * This adapter is used to set up the text and view search result list in MainActivity
 * @author Daniel Nalazek
 */
public class SearchResultListAdapter extends ArrayAdapter<Result> implements Showable {

    private View progressBarLayout;
    private TextView name;
    private TextView description;
    private TextView type;

    public SearchResultListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Result> objects, View progressBarLayout) {
        super(context, resource, objects);
        this.progressBarLayout = progressBarLayout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Result result = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_repo, parent, false);

        name = (TextView) convertView.findViewById(R.id.name);
        description = (TextView) convertView.findViewById(R.id.description);
        type = (TextView) convertView.findViewById(R.id.type);

        ExchangeType exchangeType = result.getType();
        SearchResult searchResult;

        switch(exchangeType) {
            case REPOS_SEARCH:
                type.setText("R");
                searchResult = (RepoSearchResult) result;
                setTextForSearchResult(searchResult);
                break;
            case USER_SEARCH:
                type.setText("U");
                searchResult = (UserSearchResult) result;
                setTextForSearchResult(searchResult);
                break;
            default:
                type.setText("?");
                break;
        }


        return convertView;
    }

    @Override
    public void showResults(ArrayList<? extends Result> resultsArray) {
        progressBarLayout.setVisibility(View.GONE);
        clear();
        addAll(resultsArray);
    }

    @Override
    public void showError(String error) {
        progressBarLayout.setVisibility(View.GONE);
        clear();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBusy() {
        clear();
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void setTextForSearchResult(SearchResult searchResult) {
        name.setText(searchResult.getName());
        description.setText(searchResult.getDescription());
    }
}