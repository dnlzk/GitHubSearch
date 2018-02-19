package pl.nalazek.githubsearch.data.ResultObjects;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailedResultTest {

    String name = "John";
    int stars = 55;
    int followers = 200;
    int id = 3423;

    @Mock
    Bitmap image;

    UserDetailedResult userDetailedResult;

    @Before
    public void before() {
        userDetailedResult = new UserDetailedResult(
                name,
                followers,
                stars,
                image,
                id);
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

    @Test
    public void whenGetIdThenID() throws Exception {
        assertThat("Id fault", userDetailedResult.getId(), is(id));
    }


}