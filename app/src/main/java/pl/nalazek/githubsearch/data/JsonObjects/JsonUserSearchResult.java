package pl.nalazek.githubsearch.data.JsonObjects;


import java.util.List;

/**
 * This class represents a deserialized JSON response for user search
 * @author Daniel Nalazek
 */
public class JsonUserSearchResult extends JsonObject {
    private Integer totalCount;
    private Boolean incompleteResults;
    private List<Item> items = null;

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public void setIncompleteResults(Boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

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

        public void setLogin(String login) {
            this.login = login;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public void setGravatarId(String gravatarId) {
            this.gravatarId = gravatarId;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        public void setFollowersUrl(String followersUrl) {
            this.followersUrl = followersUrl;
        }

        public void setFollowingUrl(String followingUrl) {
            this.followingUrl = followingUrl;
        }

        public void setGistsUrl(String gistsUrl) {
            this.gistsUrl = gistsUrl;
        }

        public void setStarredUrl(String starredUrl) {
            this.starredUrl = starredUrl;
        }

        public void setSubscriptionsUrl(String subscriptionsUrl) {
            this.subscriptionsUrl = subscriptionsUrl;
        }

        public void setOrganizationsUrl(String organizationsUrl) {
            this.organizationsUrl = organizationsUrl;
        }

        public void setReposUrl(String reposUrl) {
            this.reposUrl = reposUrl;
        }

        public void setEventsUrl(String eventsUrl) {
            this.eventsUrl = eventsUrl;
        }

        public void setReceivedEventsUrl(String receivedEventsUrl) {
            this.receivedEventsUrl = receivedEventsUrl;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setSiteAdmin(Boolean siteAdmin) {
            this.siteAdmin = siteAdmin;
        }

        public void setScore(Double score) {
            this.score = score;
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
