package pl.nalazek.githubsearch.data.ResultObjects;

import android.os.Parcel;
import android.os.Parcelable;
import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * @author Daniel Nalazek
 */

public class RepoSearchResult extends SearchResult {

    private String repoURL;

    public RepoSearchResult(String title, String description, String repoURL, ExchangeType exchangeType) {
        super(title, description, exchangeType);
        this.repoURL = repoURL;
    }

    public static final Parcelable.Creator<RepoSearchResult> CREATOR = new Parcelable.Creator<RepoSearchResult>() {
        public RepoSearchResult createFromParcel(Parcel in) {
            String[] parcelData = new String[3];
            in.readStringArray(parcelData);
            return buildFromParcelData(parcelData);
        }

        public RepoSearchResult[] newArray(int size) {
            return new RepoSearchResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                getTitle(),
                getDescription(),
                this.repoURL,
        });
    }

    private static RepoSearchResult buildFromParcelData(String[] parcelData) {
        return new RepoSearchResult(
                parcelData[0],
                parcelData[1],
                parcelData[2],
                ExchangeType.REPOS_SEARCH
        );
    }
}
