/*
 * Copyright © 2018 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.cookie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yanzhenjie.kalle.cookie.Cookie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created in Jan 10, 2016 8:18:28 PM.
 */
public class CookieDao implements Field {

    private SQLHelper mSQLHelper;

    public CookieDao(Context context) {
        this.mSQLHelper = new SQLHelper(context);
    }

    protected final SQLiteDatabase getDateBase() {
        return mSQLHelper.getReadableDatabase();
    }

    protected final void closeDateBase(SQLiteDatabase database) {
        if (database != null && database.isOpen())
            database.close();
    }

    protected final void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    /**
     * Query the number of records.
     */
    public int count() {
        return count("SELECT COUNT(" + ID + ") FROM " + TABLE_NAME);
    }

    /**
     * Query the number of records.
     */
    public int count(String sql) {
        SQLiteDatabase database = getDateBase();
        Cursor cursor = database.rawQuery(sql, null);
        try {
            return cursor.moveToNext() ? cursor.getInt(0) : 0;
        } finally {
            closeCursor(cursor);
            closeDateBase(database);
        }
    }

    /**
     * Delete all cookie.
     */
    public boolean deleteAll() {
        return delete("1=1");
    }

    /**
     * Delete the cookie in the specified list.
     */
    public boolean delete(List<Cookie> cookies) {
        List<Long> idList = new ArrayList<>();
        for (Cookie cookie : cookies) {
            idList.add(cookie.getId());
        }
        Where where = Where.newBuilder().in(ID, idList).build();
        return delete(where.toString());
    }

    /**
     * Delete cookies based on the conditions.
     */
    public boolean delete(String where) {
        SQLiteDatabase database = getDateBase();
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + where;
        database.beginTransaction();
        try {
            database.execSQL(sql);
            database.setTransactionSuccessful();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            database.endTransaction();
            closeDateBase(database);
        }
    }

    /**
     * Query all cookie.
     */
    public List<Cookie> getAll() {
        return getList(null, null, null, null);
    }

    /**
     * Query the cookie list based on the conditions.
     */
    public List<Cookie> getList(String where, String orderBy, String limit, String offset) {
        StringBuilder sqlBuild = new StringBuilder("SELECT ").append("*").append(" FROM ").append(TABLE_NAME);
        if (!TextUtils.isEmpty(where)) {
            sqlBuild.append(" WHERE ");
            sqlBuild.append(where);
        }
        if (!TextUtils.isEmpty(orderBy)) {
            sqlBuild.append(" ORDER BY ");
            sqlBuild.append(orderBy);
        }
        if (!TextUtils.isEmpty(limit)) {
            sqlBuild.append(" LIMIT ");
            sqlBuild.append(limit);

            if (!TextUtils.isEmpty(offset)) {
                sqlBuild.append(" OFFSET ");
                sqlBuild.append(offset);
            }
        }
        return getList(sqlBuild.toString());
    }

    /**
     * Save or set cookies.
     */
    public long replace(Cookie cookie) {
        SQLiteDatabase database = getDateBase();
        database.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(URL, cookie.getUrl());
        values.put(NAME, cookie.getName());
        values.put(VALUE, cookie.getValue());
        values.put(COMMENT, cookie.getComment());
        values.put(COMMENT_URL, cookie.getCommentURL());
        values.put(DISCARD, String.valueOf(cookie.isDiscard()));
        values.put(DOMAIN, cookie.getDomain());
        values.put(EXPIRY, cookie.getExpiry());
        values.put(PATH, cookie.getPath());
        values.put(PORT_LIST, cookie.getPortList());
        values.put(SECURE, String.valueOf(cookie.isSecure()));
        values.put(VERSION, cookie.getVersion());
        try {
            long result = database.replace(TABLE_NAME, null, values);
            database.setTransactionSuccessful();
            return result;
        } catch (Exception e) {
            return -1;
        } finally {
            database.endTransaction();
            closeDateBase(database);
        }
    }

    /**
     * According to the unique index adds or updates a row data.
     */
    public List<Cookie> getList(String querySql) {
        SQLiteDatabase database = getDateBase();
        List<Cookie> cookieList = new ArrayList<>();
        Cursor cursor = database.rawQuery(querySql, null);
        while (cursor.moveToNext()) {
            Cookie cookie = new Cookie();
            cookie.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            cookie.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
            cookie.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            cookie.setValue(cursor.getString(cursor.getColumnIndex(VALUE)));
            cookie.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
            cookie.setCommentURL(cursor.getString(cursor.getColumnIndex(COMMENT_URL)));
            cookie.setDiscard("true".equals(cursor.getString(cursor.getColumnIndex(DISCARD))));
            cookie.setDomain(cursor.getString(cursor.getColumnIndex(DOMAIN)));
            cookie.setExpiry(cursor.getLong(cursor.getColumnIndex(EXPIRY)));
            cookie.setPath(cursor.getString(cursor.getColumnIndex(PATH)));
            cookie.setPortList(cursor.getString(cursor.getColumnIndex(PORT_LIST)));
            cookie.setSecure("true".equals(cursor.getString(cursor.getColumnIndex(SECURE))));
            cookie.setVersion(cursor.getInt(cursor.getColumnIndex(VERSION)));
            cookieList.add(cookie);
        }
        closeCursor(cursor);
        closeDateBase(database);
        return cookieList;
    }
}