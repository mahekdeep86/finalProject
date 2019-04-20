package com.project.news;

/**
 * This class models a news article
 */
public class NewsArticle {
    public static final String TABLE_NAME = "NewsArticle";
    public static final String UUID = "uuid";
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String FULL_TITLE = "fullTitle";
    public static final String SECTION_TITLE = "sectionTitle";
    public static final String PUBLISHED_ON = "publishedOn";
    public static final String MAIN_IMAGE = "mainImage";
    private String uuid;
    private String url;
    private String title;
    private String fullTitle;
    private String sectionTitle;
    private String publishedOn;
    private String mainImage;

    /**
     * Parameterized class constructor
     * @param uuid
     * @param url
     * @param title
     * @param fullTitle
     * @param sectionTitle
     * @param publishedOn
     * @param mainImage
     */
    public NewsArticle(String uuid, String url, String title, String fullTitle, String sectionTitle, String publishedOn, String mainImage) {
        this.uuid = uuid;
        this.url = url;
        this.title = title;
        this.fullTitle = fullTitle;
        this.sectionTitle = sectionTitle;
        this.publishedOn = publishedOn;
        this.mainImage = mainImage;
    }

    /**
     * Getter for news article url
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Getter for title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for news article uuid
     * @return
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Getter for section title
     * @return
     */
    public String getSectionTitle() {
        return sectionTitle;
    }

    /**
     * Getter for published date of the date
     * @return
     */
    public String getPublishedOn() {
        return publishedOn;
    }

    /**
     * Getter for the main image url of the news article
     * @return
     */
    public String getMainImage() {
        return mainImage;
    }

    /**
     * Getter for full title of the news article
     * @return
     */
    public String getFullTitle() {
        return fullTitle;
    }

    @Override
    public String toString() {
        return "Url: " + url + "title: " + title + "fullTitle: ";
    }
}
