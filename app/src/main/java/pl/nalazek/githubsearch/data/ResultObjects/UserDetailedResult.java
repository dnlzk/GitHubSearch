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

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import pl.nalazek.githubsearch.data.ExchangeType;

public class UserDetailedResult extends DetailedResult {

    public final static String TYPE = "UserDetailedResult";

    private String userName;
    private int followers;
    private int stars;
    private Bitmap avatarImage;
    private String login;


    /**
     * @param userName Name of user
     * @param followers Count of followers
     * @param stars Count of stars
     * @param avatarImage Avatar image
     * @param exchangeType Type of Exchange
     */
    public UserDetailedResult(String userName,
                              String login,
                              int followers,
                              int stars,
                              Bitmap avatarImage,
                              ExchangeType exchangeType,
                              int id) {

        super(exchangeType, id);
        this.userName = userName;
        this.followers = followers;
        this.stars = stars;
        this.avatarImage = avatarImage;
        this.login = login;
    }


    /**
     * @param userName Name of user
     * @param followers Count of followers
     * @param stars Count of stars
     * @param avatarImage Avatar image
     */
    public UserDetailedResult(String userName,
                              String login,
                              int followers,
                              int stars,
                              @Nullable Bitmap avatarImage,
                              int id) {

        super(ExchangeType.USER_DETAILED, id);
        this.userName = userName;
        this.followers = followers;
        this.stars = stars;
        this.avatarImage = avatarImage;
        this.login = login;
    }


    public String getUserName() {
        return userName;
    }

    public int getFollowers() {
        return followers;
    }

    public int getStars() {
        return stars;
    }

    public Bitmap getAvatarImage() {
        return avatarImage;
    }

    public String getLogin() { return login;}

    @Override
    public String getResultType() {
        return TYPE;
    }
}
