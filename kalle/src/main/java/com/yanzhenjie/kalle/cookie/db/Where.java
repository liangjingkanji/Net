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

import java.util.Iterator;
import java.util.List;

/**
 * Created in Dec 19, 2015 4:16:24 PM.
 */
public class Where {

    private StringBuilder mBuilder;

    private Where(Builder builder) {
        this.mBuilder = builder.mBuilder;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return mBuilder.toString();
    }

    public enum Options {

        EQUAL(" = "), NO_EQUAL(" != "), BIGGER(" > "), SMALLER(" < ");

        private String value;

        Options(String value) {
            this.value = value;
        }
    }

    public static class Builder {
        private StringBuilder mBuilder;

        private Builder() {
            mBuilder = new StringBuilder();
        }

        public Builder append(Object row) {
            mBuilder.append(row);
            return this;
        }

        public Builder set(String row) {
            mBuilder.delete(0, mBuilder.length()).append(row);
            return this;
        }

        public Builder isNull(CharSequence columnName) {
            mBuilder.append("\"").append(columnName).append("\"").append(" IS ").append("NULL");
            return this;
        }

        private Builder add(CharSequence columnName, Options op) {
            mBuilder.append("\"").append(columnName).append("\"").append(op.value);
            return this;
        }

        public Builder add(CharSequence columnName, Options op, Object value) {
            add(columnName, op).append("'").append(value).append("'");
            return this;
        }

        public <T> Builder in(CharSequence columnName, List<T> values) {
            mBuilder.append(columnName).append(" IN ").append("(");
            StringBuilder sb = new StringBuilder();
            Iterator<?> it = values.iterator();
            if (it.hasNext()) {
                sb.append("'").append(it.next()).append("'");
                while (it.hasNext()) {
                    sb.append(", '").append(it.next()).append("'");
                }
            }
            mBuilder.append(sb).append(")");
            return this;
        }

        public Builder and() {
            if (mBuilder.length() > 0) mBuilder.append(" AND ");
            return this;
        }

        public Builder and(CharSequence columnName, Options op, Object value) {
            return and().add(columnName, op, value);
        }

        public Builder andNull(CharSequence columnName) {
            return and().isNull(columnName);
        }

        public Builder and(Where where) {
            return and().append(where);
        }

        public Builder or() {
            if (mBuilder.length() > 0)
                mBuilder.append(" OR ");
            return this;
        }

        public Builder or(CharSequence columnName, Options op, Object value) {
            return or().add(columnName, op, value);
        }

        public Builder orNull(CharSequence columnName) {
            return or().isNull(columnName);
        }

        public Builder or(Where where) {
            return or().append(where);
        }

        public Builder bracket() {
            return insert(0, "(").append(')');
        }

        public Builder insert(int index, CharSequence s) {
            mBuilder.insert(index, s);
            return this;
        }

        public Where build() {
            return new Where(this);
        }
    }
}
