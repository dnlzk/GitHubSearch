package pl.nalazek.githubsearch.data.ResultObjects;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
public class UserDetailedResultTest {

    String name = "John";
    int stars = 55;
    int followers = 200;
    byte[] image = new byte[5];

    UserDetailedResult userDetailedResult;

    @Before
    public void before() {
        userDetailedResult = new UserDetailedResult(
                name,
                followers,
                stars,
                image);
    }

    @Test
    public void whenGetResultTypeThenType() throws Exception {
        assertThat("Type fault", userDetailedResult.getResultType(), is(UserDetailedResult.TYPE));
    }

    @Test
    public void whenGetUserNameThenName() throws Exception {
        assertThat("Name fault", userDetailedResult.getUserName(), is(name));
    }

    @Test
    public void whenGetFollowersThenFollowers() throws Exception {
        assertThat("Followers count fault", userDetailedResult.getFollowers(), is(followers));
    }

    @Test
    public void whenGetStarsThenStars() throws Exception {
        assertThat("Stars count fault", userDetailedResult.getStars(), is(stars));
    }

    @Test
    public void whenGetAvatarImageThenAvatarImage() throws Exception {
        assertThat("Avatar image fault", userDetailedResult.getAvatarImage(), is(image));
    }

}