package com.example.yuanzhan.monashff.Constants;

import android.provider.BaseColumns;

/**
 * Created by YuanZhan on 24/04/2017.
 */

public class DBStructure {

    public static abstract class tableEntry implements BaseColumns {
        public static final String TABLE_NAME = "CountryLang";
        public static final String COLUMN_COUNTRY_ABBR = "ID";
        public static final String COLUMN_COUNTRY = "Country";
        public static final String COLUMN_LANGUAGE = "Language";
    }

}
