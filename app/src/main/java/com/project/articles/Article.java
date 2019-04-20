package com.project.articles;

/**
 * This class models an article
 */
public class Article {
    private String id;
    private String webUrl;
    private String snippet;
    private String imageUrl;
    private String headline;
    private boolean offline;

    /**
     * Class constructor which creates a new Article object
     * @param id
     * @param webUrl
     * @param headline
     * @param imageUrl
     * @param snippet
     */
    public Article(String id, String webUrl, String headline, String imageUrl, String snippet) {
        this.id = id;
        this.webUrl = webUrl;
        this.headline = headline;
        this.snippet = snippet;
        this.imageUrl = imageUrl;
        this.offline = false;
    }


    /**
     * Getter web url
     * @return
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Getter for snippet
     * @return
     */
    public String getSnippet() {
        return snippet;
    }


    /**
     * Getter for image url
     * @return
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Getter for headline
     * @return
     */
    public String getHeadline() {
        return headline;
    }

    /**
     * Getter for article id
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Setter which specifies whether article is offline or not
     * @param offline
     */
    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    /**
     * Getter for is offline
     * @return
     */
    public boolean isOffline() {
        return offline;
    }
}
