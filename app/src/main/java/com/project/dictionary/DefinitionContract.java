package com.project.dictionary;


import android.provider.BaseColumns;

/**
 * This class contains the column names of the definitions table in sqlite database
 */
public class DefinitionContract {
    private DefinitionContract() {}
    public static class DefinitionEntry implements BaseColumns {
        public static final String TABLE_NAME = "definitions";
        public static final String COLUMN_ID = "definition_id";
        public static final String COLUMN_HEAD_WORD = "headWord";
        public static final String COLUMN_PRONUNCIATION = "pronunciation";
        public static final String COLUMN_FUNCTIONAL = "functional";
        public static final String COLUMN_DEFINITIONS = "definitions";
    }
}
