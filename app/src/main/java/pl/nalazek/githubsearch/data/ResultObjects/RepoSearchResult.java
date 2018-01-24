package pl.nalazek.githubsearch.data.ResultObjects;

import android.os.Parcel;
import android.os.Parcelable;
import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * @author Daniel Nalazek
 */

public class RepoSearchResult extends SearchResult {

    public final static String TYPE = "RepoSearchResult";

    private String repoURL;

    /**
     * @param title Name to show on list
     * @param description Description of the result
     * @param repoURL URL of repositorium
     * @param exchangeType Type of Exchange
     */
    public RepoSearchResult(String title, String description, String repoURL, ExchangeType exchangeType) {
        super(title, description, exchangeType);
        this.repoURL = repoURL;
    }

    public RepoSearchResult(String title, String description, String repoURL) {
        super(title, description, ExchangeType.REPOS_SEARCH);
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

    public String getRepoURL() {
        return repoURL;
    }

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

    @Override
    public String getResultType() {
        return TYPE;
    }

    private static RepoSearchResult buildFromParcelData(String[] parcelData) {
        return new RepoSearchResult(
                parcelData[0],
                parcelData[1],
                parcelData[2],
                ExchangeType.REPOS_SEARCH
        );
    }

    @Override
    public int hashCode() {
        int result = 15;
        result = 37 * result + repoURL.hashCode();
        result = 37 * result + getTitle().hashCode();
        result = 37 * result + getDescription().hashCode();
        result = 37 * result + getExchangeType().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RepoSearchResult &&
                repoURL.equals( ((RepoSearchResult)o).getRepoURL() ) &&
                getTitle().equals( ((RepoSearchResult)o).getTitle() ) &&
                getDescription().equals( ((RepoSearchResult)o).getDescription() ) &&
                getExchangeType().equals( ((RepoSearchResult)o).getExchangeType() );
    }
}
