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

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.nalazek.githubsearch.R;
import pl.nalazek.githubsearch.data.ExchangeType;
import pl.nalazek.githubsearch.data.ResultObjects.*;

/**
 * This adapter is used to set up search result list in {@link SearchActivity}
 */
public class SearchResultListAdapter extends ArrayAdapter<Result> {

    private TextView name;
    private TextView description;
    private TextView type;



    public SearchResultListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Result> objects) {
        super(context, resource, objects);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Result result = getItem(position);

        if(result == null)
            return new View(parent.getContext());

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_repo, parent, false);

        setVariables(convertView);
        setResultsToView(result);

        return convertView;
    }




    private void setResultsToView(Result result) {

        ExchangeType exchangeType = result.getExchangeType();
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
    }



    private void setVariables(View convertView) {
        name = (TextView) convertView.findViewById(R.id.name);
        description = (TextView) convertView.findViewById(R.id.description);
        type = (TextView) convertView.findViewById(R.id.type);
    }



    private void setTextForSearchResult(SearchResult searchResult) {
        name.setText(searchResult.getTitle());
        description.setText(searchResult.getDescription());
    }
}