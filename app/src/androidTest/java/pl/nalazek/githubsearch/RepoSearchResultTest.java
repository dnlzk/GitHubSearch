package pl.nalazek.githubsearch;

import android.os.Parcel;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.nalazek.githubsearch.data.ResultObjects.RepoSearchResult;

import static org.junit.Assert.assertTrue;

/**
 * @author Daniel Nalazek
 */

@MediumTest
public class RepoSearchResultTest {
    RepoSearchResult repoSearchResult;
    String name = "John";
    String description = "evaluate description";
    String exampleRepoUrl = "http://u.pl";
    Parcel parcel;

    @Before
    public void beforeConstructor() {
        repoSearchResult = new RepoSearchResult(name, description, exampleRepoUrl);
        parcel = Parcel.obtain();
    }

    @Test
    public void givenParcelFromUserSearchResultWhenRecreatedThenValuesEqual() throws Exception {
        repoSearchResult.writeToParcel(parcel,0);
        parcel.setDataPosition(0);
        RepoSearchResult fromParcel = RepoSearchResult.CREATOR.createFromParcel(parcel);
        assertTrue("Parcel creation fault", repoSearchResult.equals(fromParcel));
    }

    @After
    public void after() {
        parcel.recycle();
    }

}
