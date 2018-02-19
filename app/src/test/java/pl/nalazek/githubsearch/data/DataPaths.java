package pl.nalazek.githubsearch.data;

/**
 * @author Daniel Nalazek
 */
public interface DataPaths {

    String JSON_FILE_PATH = "src/test/resources/testInputs/";

    String JSON_FILE_SEARCHUSER = "searchResultUsers.json";
    String JSON_FILE_SEARCHUSER_HEADER = "searchResultUsers.head";

    String JSON_FILE_SEARCHREPOS = "searchResultRepos.json";
    String JSON_FILE_SEARCHREPOS_HEADER = "searchResultRepos.head";

    String JSON_FILE_DETAILEDUSER = "userDetailedSingleton.json";

    String JSON_FILE_STARRED = "starSingleton.json";

    String JSON_FILE_STARRED_PAGE_FIRST_LINKHEADER = "starFirstPage.linkhead";
    String JSON_FILE_STARRED_PAGE_FIRST_HEADER = "starFirstPage.head";
    String JSON_FILE_STARRED_PAGE_FIRST = "starFirstPage.json";

    String JSON_FILE_STARRED_PAGE_LAST_LINKHEADER = "starLastPage.linkhead";
    String JSON_FILE_STARRED_PAGE_LAST_HEADER = "starLastPage.head";
    String JSON_FILE_STARRED_PAGE_LAST = "starLastPage.json";

    String JSON_FILE_STARRED_PAGE_ONEBUTLAST_LINKHEADER = "starOneButLastPage.linkhead";
    String JSON_FILE_STARRED_PAGE_ONEBUTLAST_HEADER = "starOneButLastPage.head";

    String JSON_FILE_AVATAR = "avatar.stream";
    String JSON_FILE_AVATAR_HEADER = "avatar.head";
}
