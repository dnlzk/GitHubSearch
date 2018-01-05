package pl.nalazek.githubsearch.userdetails;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.nalazek.githubsearch.R;
import pl.nalazek.githubsearch.Showable;
import pl.nalazek.githubsearch.data.ExchangeType;
import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.UserDetailedResult;

/**
 * @author Daniel Nalazek
 */

public class UserDetailsListAdapter extends ArrayAdapter<Result> implements Showable {

    private View progressBarLayout;
    private TextView userName;
    private TextView stars;
    private TextView followers;
    private ImageView avatarImage;

    public UserDetailsListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Result> objects, View progressBarLayout) {
        super(context, resource, objects);
        this.progressBarLayout = progressBarLayout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Result result = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_detailed, parent, false);

        userName = (TextView) convertView.findViewById(R.id.textUserName);
        stars = (TextView) convertView.findViewById(R.id.textStars);
        followers = (TextView) convertView.findViewById(R.id.textFollowers);
        avatarImage = (ImageView) convertView.findViewById(R.id.imageViewAvatar);

        //TODO solve null pointer exception
        if(result.getType() == ExchangeType.USER_EXPAND) {
            UserDetailedResult userDetailedResult = (UserDetailedResult) result;
            userName.setText(userDetailedResult.getUserName());
            stars.setText(String.valueOf(userDetailedResult.getStars()));
            followers.setText(String.valueOf(userDetailedResult.getFollowers()));
            //TODO set image
        }
        return convertView;
    }

    @Override
    public void showResults(ArrayList<? extends Result> resultsArray) {
        clear();
        progressBarLayout.setVisibility(View.GONE);
        addAll(resultsArray);
    }

    @Override
    public void showError(String error) {
        clear();
        progressBarLayout.setVisibility(View.GONE);
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBusy() {
        clear();
        progressBarLayout.setVisibility(View.VISIBLE);
    }
}
