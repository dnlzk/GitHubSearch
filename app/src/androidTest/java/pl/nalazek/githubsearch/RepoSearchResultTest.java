package pl.nalazek.githubsearch;

import android.os.Parcel;
import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.nalazek.githubsearch.data.ResultObjects.RepoSearchResult;

import static org.junit.Assert.assertTrue;

/**
 * @author Daniel Nalazek
 */
@MediumTest
public class RepoSearchResultTest {


    private RepoSearchResult repoSearchResult;
    private String name = "John";
    private String description = "evaluate description";
    private String exampleRepoUrl = "http://u.pl";
    private Parcel parcel;

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
