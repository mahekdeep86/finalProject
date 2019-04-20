package com.project.articles;


import android.provider.BaseColumns;

/**
 * This class defines the contents of the article table
 */
public class ArticleContract {
    /**
     * To prevent someone from accidentally instantiating the contract class, make the constructor private.
     */
    private ArticleContract() {}

    //Inner class that defines the table contents
    public static class ArticleEntry implements BaseColumns {
        public static final String TABLE_NAME = "articles";
        public static final String ID = "id";
        public static final String COLUMN_NAME_WEB_URL = "webUrl";
        public static final String COLUMN_NAME_SNIPPET = "snippet";
        public static final String COLUMN_NAME_LEAD_IMAGE_URL = "imageUrl";
        public static final String COLUMN_NAME_HEADLINE = "headline";
    }
}
