/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.cookie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>Cookie database operation class.</p>
 * Created in Dec 18, 2015 6:30:59 PM.
 */
final class SQLHelper extends SQLiteOpenHelper implements Field {

    private static final String DB_COOKIE_NAME = "_kalle_cookies_db.db";
    private static final int DB_COOKIE_VERSION = 3;

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            " " + URL + " TEXT, " + NAME + " TEXT, " + VALUE + " TEXT, " + COMMENT + " TEXT, " + COMMENT_URL + " TEXT, " + DISCARD + " TEXT," +
            " " + DOMAIN + " TEXT, " + EXPIRY + " INTEGER, " + PATH + " TEXT, " + PORT_LIST + " TEXT, " + SECURE + " TEXT, " + VERSION + " INTEGER)";
    private static final String SQL_CREATE_UNIQUE_INDEX = "CREATE UNIQUE INDEX COOKIE_UNIQUE_INDEX ON COOKIES_TABLE(\"" + NAME + "\", \"" + DOMAIN + "\", \"" + PATH + "\")";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    SQLHelper(Context context) {
        super(context.getApplicationContext(), DB_COOKIE_NAME, null, DB_COOKIE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(SQL_CREATE_TABLE);
            db.execSQL(SQL_CREATE_UNIQUE_INDEX);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            db.beginTransaction();
            try {
                db.execSQL(SQL_DELETE_TABLE);
                db.execSQL(SQL_CREATE_TABLE);
                db.execSQL(SQL_CREATE_UNIQUE_INDEX);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}
