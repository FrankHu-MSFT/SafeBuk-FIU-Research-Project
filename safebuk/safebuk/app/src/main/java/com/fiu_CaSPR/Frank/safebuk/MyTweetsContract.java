package com.fiu_CaSPR.Frank.safebuk;

/**
 * Created by ivan.minev on 21.1.2015 г..
 */
public final class MyTweetsContract
{
    public static abstract class MyTweets
    {
        public static final String TABLE_NAME = "My Tweets";

        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_NAME = "Name";

        public static final String COLUMN_USERNAME = "Username";

        public static final String COLUMN_TIME = "Time";

        public static final String COLUMN_TEXT = "Text";
    }

    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA_SEP = ", ";

    public static final String CREATE_STATEMENT = "CREATE TABLE " + MyTweets.TABLE_NAME +
            " (" + MyTweets.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
            MyTweets.TABLE_NAME + TEXT_TYPE + COMMA_SEP +
            MyTweets.COLUMN_USERNAME + TEXT_TYPE + COMMA_SEP +
            MyTweets.COLUMN_TIME + TEXT_TYPE + COMMA_SEP +
            MyTweets.COLUMN_TEXT + TEXT_TYPE + " )";

    public static final String DELETE_STATEMENT = "DROP TABLE IF EXISTS " + MyTweets.TABLE_NAME;
}
