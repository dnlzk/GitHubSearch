package pl.nalazek.githubsearch.data;

/**
 * This enum hold types of exchanges between host and client
 * <li>{@link #USER_SEARCH} used when searching for users</li>
 * <li>{@link #USER_PAGE} used when requesting a page from a user search</li>
 * <li>{@link #USER_DETAILED} used when requesting for detailed info about user</li>
 * <li>{@link #USER_DETAILED_STARS} used when requesting user starred urls</li>
 * <li>{@link #USER_DETAILED_AVATAR} used when requesting user avatar</li>
 * <li>{@link #REPOS_SEARCH} used when searching for repositories</li>
 * <li>{@link #REPOS_PAGE} used when requesting a page from a repository search</li>

 * @author Daniel Nalazek
 */
public enum ExchangeType { USER_SEARCH, REPOS_SEARCH, USER_PAGE, REPOS_PAGE, USER_DETAILED, USER_DETAILED_STARS, USER_DETAILED_AVATAR
}
