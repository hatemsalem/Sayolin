package com.zobonapp.db;

/**
 * Created by hasalem on 11/26/2017.
 */

public class DbSchema
{
    public static final int VERSION=1;
    public static final String SCHEMA_NAME="zobonapp.db";
    public static final class BusinessEntityTable
    {
        public static final String NAME="businessentity";
        public static final class Cols
        {
            public static final String ID ="id";
            public static final String DEFAULT_CONTACT="contactId";
            public static final String URI="uri";
            public static final String NAME="name";
            public static final String DESC="desc";
            public static final String AR_NAME="arName";
            public static final String AR_DESC="arDesc";
            public static final String EN_NAME="enName";
            public static final String EN_DESC="enDesc";
            public static final String FAVORITE="favorite";
            public static final String OFFERS="offers";
            public static final String SEARCH_TEXT="searchText";
        }
        public static String getCreateStatement()
        {
            StringBuilder statement=new StringBuilder("Create Table ");
            statement.append(NAME)
                    .append("( _id integer primary key autoincrement, ")
                    .append(Cols.ID).append(" UNIQUE NOT NULL, ")
                    .append(Cols.DEFAULT_CONTACT).append(", ")
                    .append(Cols.AR_NAME).append(", ")
                    .append(Cols.AR_DESC).append(", ")
                    .append(Cols.EN_NAME).append(", ")
                    .append(Cols.EN_DESC).append(", ")
                    .append(Cols.OFFERS).append(", ")
                    .append(Cols.SEARCH_TEXT).append(", ")
                    .append(Cols.FAVORITE).append(")");
            return statement.toString();
        }
    }
    public static final class CategoryTable
    {
        public static final String NAME="category";
        public static final class Cols
        {
            public static final String ID ="id";
            public static final String NAME="name";
            public static final String AR_NAME="arName";
            public static final String EN_NAME="enName";
            public static final String SEARCH_TEXT="searchText";
            public static final String TYPE="type";
            public static final String OFFERS="offers";
            public static final String ENTITIES="entities";

        }
        public static String getCreateStatement()
        {
            StringBuilder statement=new StringBuilder("Create Table ");
            statement.append(NAME)
                    .append("( _id integer primary key autoincrement, ")
                    .append(Cols.ID).append(" UNIQUE NOT NULL, ")
                    .append(Cols.AR_NAME).append(", ")
                    .append(Cols.EN_NAME).append(", ")
                    .append(Cols.TYPE).append(", ")
                    .append(Cols.SEARCH_TEXT).append(") ");
            return statement.toString();
        }
    }

    public static final class ItemCategoryTable
    {
        public static final String NAME="itemCategory";
        public static final class Cols
        {
            public static final String CATEGORY_ID="categoryId";
            public static final String ITEM_ID="itemId";
        }
        public static String getCreateStatement()
        {
            StringBuilder statement=new StringBuilder("Create Table ");
            statement.append(NAME)
                    .append("( _id integer primary key autoincrement, ")
                    .append(Cols.CATEGORY_ID).append(" NOT NULL, ")
                    .append(Cols.ITEM_ID).append(" NOT NULL) ");
            return statement.toString();
        }
    }

    public static final class MenutTable
    {
        public static final String NAME="menu";
        public static final class Cols
        {
            public static final String ID="id";
            public static final String ITEM_ID="itemId";
            public static final String NAME="name";
            public static final String DESC="desc";
            public static final String AR_NAME="arName";
            public static final String AR_DESC="arDesc";
            public static final String EN_NAME="enName";
            public static final String EN_DESC="enDesc";
            public static final String SEARCH_TEXT="searchText";
        }
        public static String getCreateStatement()
        {
            StringBuilder statement=new StringBuilder("Create Table ");
            statement.append(NAME)
                    .append("( _id integer primary key autoincrement, ")
                    .append(Cols.ID).append(" UNIQUE NOT NULL, ")
                    .append(Cols.ITEM_ID).append(", ")
                    .append(Cols.AR_NAME).append(", ")
                    .append(Cols.EN_NAME).append(", ")
                    .append(Cols.AR_DESC).append(", ")
                    .append(Cols.EN_DESC).append(", ")
                    .append(Cols.SEARCH_TEXT).append(") ");
            return statement.toString();
        }
    }

    public static final class OfferTable
    {
        public static final String NAME="offer";
        public static final class Cols
        {
            public static final String ID="id";
            public static final String ITEM_ID="itemId";
            public static final String NAME="name";
            public static final String DESC="desc";
            public static final String AR_NAME="arName";
            public static final String AR_DESC="arDesc";
            public static final String EN_NAME="enName";
            public static final String EN_DESC="enDesc";
            public static final String START_DATE="startDate";
            public static final String END_DATE="endDate";
            public static final String SEARCH_TEXT="searchText";
        }
        public static String getCreateStatement()
        {
            StringBuilder statement=new StringBuilder("Create Table ");
            statement.append(NAME)
                    .append("( _id integer primary key autoincrement, ")
                    .append(Cols.ID).append(" UNIQUE NOT NULL, ")
                    .append(Cols.ITEM_ID).append(", ")
                    .append(Cols.AR_NAME).append(", ")
                    .append(Cols.EN_NAME).append(", ")
                    .append(Cols.AR_DESC).append(", ")
                    .append(Cols.EN_DESC).append(", ")
                    .append(Cols.START_DATE).append(", ")
                    .append(Cols.END_DATE).append(", ")
                    .append(Cols.SEARCH_TEXT).append(") ");
            return statement.toString();
        }
    }
    public static final class ContactTable
    {
        public static final String NAME="contact";
        public static final class Cols
        {
            public static final String ID ="id";
            public static final String ITEM_ID="itemId";
            public static final String URI="uri";
        }
        public static String getCreateStatement()
        {
            StringBuilder statement=new StringBuilder("Create Table ");
            statement.append(NAME)
                    .append("( _id integer primary key autoincrement, ")
                    .append(Cols.ID).append(", ")
                    .append(Cols.ITEM_ID).append(", ")
                    .append(Cols.URI).append(") ");
            return statement.toString();
        }
    }

}
