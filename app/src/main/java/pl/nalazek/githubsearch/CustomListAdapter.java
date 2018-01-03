package pl.nalazek.githubsearch;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.nalazek.githubsearch.ResultObjects.Result;
import pl.nalazek.githubsearch.ResultObjects.SearchResult;

/**
 * This adapter is used to set up the text and view search result list in MainActivity
 * @author Daniel Nalazek
 */
public class CustomListAdapter extends ArrayAdapter<SearchResult> implements Showable {


    public CustomListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<SearchResult> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SearchResult searchResult = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_repo, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView type = (TextView) convertView.findViewById(R.id.type);

        ExchangeType exchangeType = searchResult.getType();

        switch(exchangeType) {
            case REPOS_SEARCH:
                type.setText("R");
                break;
            case USER_SEARCH:
                type.setText("U");
                break;
            default:
                type.setText("?");
                break;

        }
        name.setText(searchResult.getName());
        description.setText(searchResult.getDescription());

        return convertView;
    }

    @Override
    public void showResults(ArrayList<? extends Result> resultsArray) {
        clear();
        ArrayList<SearchResult> searchResults = (ArrayList<SearchResult>) resultsArray;
        addAll(searchResults);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}