package pl.nalazek.githubsearch.userdetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.nalazek.githubsearch.R;
import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.UserDetailedResult;

/**
 * This adapter is used to set up detailed result list in {@link UserDetailedActivity}
 * @author Daniel Nalazek
 */
public class UserDetailsListAdapter extends ArrayAdapter<Result> {

    private TextView userName;
    private TextView stars;
    private TextView followers;
    private ImageView avatarImage;



    public UserDetailsListAdapter(@NonNull Context context,
                                  @LayoutRes int resource,
                                  @NonNull List<Result> objects) {

        super(context, resource, objects);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Result result = getItem(position);

        if(result == null)
            return new View(parent.getContext());

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_detailed,
                                                                    parent,
                                                                    false);

        setVariables(convertView);

        UserDetailedResult userDetailedResult = (UserDetailedResult) result;
        setResultsToView(userDetailedResult);

        return convertView;
    }




    private void setVariables(@Nullable View convertView) {

        userName = (TextView) convertView.findViewById(R.id.textUserName);
        stars = (TextView) convertView.findViewById(R.id.textStars);
        followers = (TextView) convertView.findViewById(R.id.textFollowers);
        avatarImage = (ImageView) convertView.findViewById(R.id.imageViewAvatar);
    }



    private void setResultsToView(UserDetailedResult userDetailedResult) {

        userName.setText(userDetailedResult.getUserName());
        stars.setText(String.valueOf(userDetailedResult.getStars()));
        followers.setText(String.valueOf(userDetailedResult.getFollowers()));
        Bitmap bitmap = userDetailedResult.getAvatarImage();

        if(bitmap != null)
            avatarImage.setImageBitmap(bitmap);
    }
}
