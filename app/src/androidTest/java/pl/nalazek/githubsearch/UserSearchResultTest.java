package pl.nalazek.githubsearch;

import android.os.Parcel;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.nalazek.githubsearch.data.ResultObjects.UserSearchResult;

import static org.junit.Assert.assertTrue;

/**
 * @author Daniel Nalazek
 */

@MediumTest
public class UserSearchResultTest {

    UserSearchResult userSearchResult;
    String name = "John";
    String description = "evaluate description";
    String exampleUserUrl = "http://u.pl";
    String exampleStarredUrl = "http://s.pl";
    String exampleAvatarUrl = "http://a.pl";
    Parcel parcel;

    @Before
    public void beforeConstructor() {
        userSearchResult = new UserSearchResult(name, description, exampleUserUrl, exampleStarredUrl, exampleAvatarUrl);
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
