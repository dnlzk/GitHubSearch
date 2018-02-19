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

package pl.nalazek.githubsearch.data.ResultObjects;

import android.os.Parcel;
import android.os.Parcelable;

import pl.nalazek.githubsearch.data.ExchangeType;


public class UserSearchResult extends SearchResult {

    public final static String PARCELABLE_TAG = "userSearchResult";
    public final static String TYPE = "UserSearchResult";

    private String userURL;
    private String starredURL;
    private String avatarURL;


    /**
     * @param title Name to show on list
     * @param description Description of the result
     * @param userURL URL of user page
     * @param starredURL URL of user stars
     * @param avatarURL URL of users avatar image4
     */
    public UserSearchResult(String title,
                            String description,
                            String userURL,
                            String starredURL,
                            String avatarURL,
                            int id) {

        super(title, description, ExchangeType.USER_SEARCH, id);
        this.userURL = userURL;
        this.starredURL = starredURL;
        this.avatarURL = avatarURL;
    }



    public String getUserURL() {
        return userURL;
    }



    public String getStarredURL() { return starredURL; }



    public String getAvatarURL() { return avatarURL; }



    public static final Parcelable.Creator<UserSearchResult> CREATOR =
            new Parcelable.Creator<UserSearchResult>() {

        public UserSearchResult createFromParcel(Parcel in) {
            String[] parcelData = new String[6];
            in.readStringArray(parcelData);
            return buildFromParcelData(parcelData);
        }

        public UserSearchResult[] newArray(int size) {
            return new UserSearchResult[size];
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
                userURL,
                starredURL,
                avatarURL,
                String.valueOf(getId())
        });
    }



    private static UserSearchResult buildFromParcelData(String[] parcelData) {
        return new UserSearchResult(
                parcelData[0],
                parcelData[1],
                parcelData[2],
                parcelData[3],
                parcelData[4],
                Integer.valueOf(parcelData[5])
        );
    }



    @Override
    public String getResultType() {
        return TYPE;
    }



    @Override
    public int hashCode() {
        int result = 15;
        result = 37 * result + userURL.hashCode();
        result = 37 * result + avatarURL.hashCode();
        result = 37 * result + starredURL.hashCode();
        result = 37 * result + getTitle().hashCode();
        result = 37 * result + getDescription().hashCode();
        result = 37 * result + getExchangeType().hashCode();
        result *= getId();
        return result;
    }



    @Override
    public boolean equals(Object o) {
        return o instanceof UserSearchResult &&
                userURL.equals( ((UserSearchResult)o).getUserURL() ) &&
                avatarURL.equals( ((UserSearchResult)o).getAvatarURL() ) &&
                starredURL.equals( ((UserSearchResult)o).getStarredURL() ) &&
                getTitle().equals( ((UserSearchResult)o).getTitle() ) &&
                getDescription().equals( ((UserSearchResult)o).getDescription() ) &&
                getExchangeType().equals( ((UserSearchResult)o).getExchangeType() ) &&
                getId() == ((UserSearchResult)o).getId();
    }
}
