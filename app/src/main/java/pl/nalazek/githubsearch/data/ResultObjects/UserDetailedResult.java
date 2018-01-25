package pl.nalazek.githubsearch.data.ResultObjects;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * @author Daniel Nalazek
 */
public class UserDetailedResult extends DetailedResult {

    public final static String TYPE = "UserDetailedResult";

    private String userName;
    private int followers;
    private int stars;
    private byte[] avatarImage;


    /**
     * @param userName Name of user
     * @param followers Count of followers
     * @param stars Count of stars
     * @param avatarImage Avatar image
     * @param exchangeType Type of Exchange
     */
    public UserDetailedResult(String userName, int followers, int stars, byte[] avatarImage, ExchangeType exchangeType) {
        super(exchangeType);
        this.userName = userName;
        this.followers = followers;
        this.stars = stars;
        this.avatarImage = avatarImage;
    }

    /**
     * @param userName Name of user
     * @param followers Count of followers
     * @param stars Count of stars
     * @param avatarImage Avatar image
     */
    public UserDetailedResult(String userName, int followers, int stars, byte[] avatarImage) {
        super(ExchangeType.USER_DETAILED);
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

    @Override
    public String getResultType() {
        return TYPE;
    }
}
