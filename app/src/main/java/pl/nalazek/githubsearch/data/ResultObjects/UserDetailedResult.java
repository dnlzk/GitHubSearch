package pl.nalazek.githubsearch.data.ResultObjects;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * @author Daniel Nalazek
 */

public class UserDetailedResult extends Result {



    private String userName;
    private int followers;
    private int stars;
    private byte[] avatarImage;

    public UserDetailedResult(String userName, int followers, int stars, byte[] avatarImage, ExchangeType exchangeType) {
        super(exchangeType);
        this.userName = userName;
        this.followers = followers;
        this.stars = stars;
        this.avatarImage = avatarImage;
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

    public byte[] getAvatarImage() {
        return avatarImage;
    }
}
