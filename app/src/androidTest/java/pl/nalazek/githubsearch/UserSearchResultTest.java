package pl.nalazek.githubsearch;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.nalazek.githubsearch.data.ResultObjects.UserSearchResult;

import static org.junit.Assert.assertTrue;

/**
 * @author Daniel Nalazek
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class UserSearchResultTest {

    private UserSearchResult userSearchResult;
    private String name = "John";
    private String description = "evaluate description";
    private String exampleUserUrl = "http://u.pl";
    private String exampleStarredUrl = "http://s.pl";
    private String exampleAvatarUrl = "http://a.pl";
    private Parcel parcel;


    @Before
    public void beforeConstructor() {
        userSearchResult = new UserSearchResult(
                name,
                description,
                exampleUserUrl,
                exampleStarredUrl,
                exampleAvatarUrl);

        parcel = Parcel.obtain();
    }



    @Test
    public void givenParcelFromUserSearchResultWhenRecreatedThenValuesEqual() throws Exception {
        userSearchResult.writeToParcel(parcel,0);
        parcel.setDataPosition(0);
        UserSearchResult fromParcel = UserSearchResult.CREATOR.createFromParcel(parcel);
        assertTrue("Parcel creation fault", userSearchResult.equals(fromParcel));
    }



    @After
    public void after() {
        parcel.recycle();
    }
}
