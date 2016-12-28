package pl.nalazek.githubsearch;


import java.util.List;

/**
 * This class represents a deserialized JSON response for user search
 * Due to one way parsing only getters are provided.
 * @author Daniel Nalazek
 */
public class UserSearchResult extends JsonObject {
    private Integer totalCount;
    private Boolean incompleteResults;
    private List<Item> items = null;

    public Integer getTotalCount() {
        return totalCount;
    }

    public Boolean getIncompleteResults() {
        return incompleteResults;
    }

    public List<Item> getItems() {
        return items;
    }


    public class Item {

        private String login;
        private Integer id;
        private String avatarUrl;
        private String gravatarId;
        private String url;
        private String htmlUrl;
        private String followersUrl;
        private String followingUrl;
        private String gistsUrl;
        private String starredUrl;
        private String subscriptionsUrl;
        private String organizationsUrl;
        private String reposUrl;
        private String eventsUrl;
        private String receivedEventsUrl;
        private String type;
        private Boolean siteAdmin;
        private Double score;

        public String getLogin() {
            return login;
        }

        public Integer getId() {
            return id;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getGravatarId() {
            return gravatarId;
        }

        public String getUrl() {
            return url;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public String getFollowersUrl() {
            return followersUrl;
        }

        public String getFollowingUrl() {
            return followingUrl;
        }

        public String getGistsUrl() {
            return gistsUrl;
        }

        public String getStarredUrl() {
            return starredUrl;
        }

        public String getSubscriptionsUrl() {
            return subscriptionsUrl;
        }

        public String getOrganizationsUrl() {
            return organizationsUrl;
        }

        public String getReposUrl() {
            return reposUrl;
        }

        public String getEventsUrl() {
            return eventsUrl;
        }

        public String getReceivedEventsUrl() {
            return receivedEventsUrl;
        }

        public String getType() {
            return type;
        }

        public Boolean getSiteAdmin() {
            return siteAdmin;
        }

        public Double getScore() {
            return score;
        }

    }

}
