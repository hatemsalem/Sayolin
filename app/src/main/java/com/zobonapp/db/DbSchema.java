package com.zobonapp.db;

/**
 * Created by hasalem on 11/26/2017.
 */

public class DbSchema
{
    public static final int VERSION=1;
    public static final String SCHEMA_NAME="zobonapp.db";
    public static final String CREATE_TEMPLATE="Create Table %s (_id integer primary key autoincrement,%s)";
    public interface L10NCols
    {
        String NAME="name";
        String DESC="desc";

    }
    public interface NameI18NCols
    {
        String AR_NAME="arName";
        String EN_NAME="enName";
    }
    public interface DescI18NCols
    {
        String AR_DESC="arDesc";
        String EN_DESC="enDesc";
    }

    public static class ItemCols implements NameI18NCols,DescI18NCols
    {
        public static String ID ="id";
        public static String FAVORITE="favorite";
        public static String KEY_WORDS ="keywords";
        public static String RANK="rank";
        public static String getItemFields()
        {
            StringBuilder fields=new StringBuilder();
            fields.append(ID).append(" UNIQUE NOT NULL, ")
                    .append(AR_NAME).append(", ")
                    .append(AR_DESC).append(", ")
                    .append(EN_NAME).append(", ")
                    .append(EN_DESC).append(", ")
                    .append(KEY_WORDS).append(", ")
                    .append(RANK).append(" DEFAULT 0, ")
                    .append(FAVORITE).append(" DEFAULT 0 ");
            return fields.toString();
        }

    }


    public static final class BusinessEntityTable
    {
        public static final String NAME="businessentity";
        public static final class Cols extends ItemCols
        {
            public static final String DEFAULT_CONTACT="contactId";
            public static final String URI="uri";
            public static final String OFFERS="offers";
            public static final String WEB_SITE ="web";
            public static final String FB_PAGE="fb";
        }
        public static String getCreateStatement()
        {
            StringBuilder fields=new StringBuilder();
            fields.append(Cols.getItemFields()).append(", ")
                    .append(Cols.DEFAULT_CONTACT).append(", ")
                    .append(Cols.WEB_SITE).append(", ")
                    .append(Cols.FB_PAGE).append(", ")
                    .append(Cols.OFFERS).append(" DEFAULT 0 ");
            return String.format(CREATE_TEMPLATE,NAME,fields);
        }
    }
    public static final class CategoryTable
    {
        public static final String NAME="category";
        public static final class Cols extends ItemCols
        {
            public static final String TYPE="type";
            public static final String OFFERS="offers";
            public static final String ENTITIES="entities";

        }
        public static String getCreateStatement()
        {
            StringBuilder fields=new StringBuilder();
            fields.append(Cols.getItemFields()).append(", ")
                    .append(Cols.TYPE);
            return String.format(CREATE_TEMPLATE,NAME,fields);
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
            StringBuilder fields=new StringBuilder();
            fields.append(Cols.CATEGORY_ID).append(" NOT NULL, ")
                    .append(Cols.ITEM_ID).append(" NOT NULL, ")
                    .append(String.format("UNIQUE(%s,%s) ON CONFLICT REPLACE",Cols.CATEGORY_ID,Cols.ITEM_ID));
            return String.format(CREATE_TEMPLATE,NAME,fields);
        }
    }

    public static final class MenutTable
    {
        public static final String NAME="menu";
        public static final class Cols extends ItemCols
        {
            public static final String ITEM_ID="itemId";
            public static final String START_DATE="startDate";
            public static final String END_DATE="endDate";
            public static final String PAGES="pages";
        }
        public static String getCreateStatement()
        {
            StringBuilder fields=new StringBuilder();
            fields.append(Cols.getItemFields()).append(", ")
                    .append(Cols.ITEM_ID).append(", ")
                    .append(Cols.START_DATE).append(", ")
                    .append(Cols.END_DATE).append(", ")
                    .append(Cols.PAGES);
            return String.format(CREATE_TEMPLATE,NAME,fields);
        }
    }

    public static final class OfferTable
    {
        public static final String NAME="offer";
        public static final class Cols extends ItemCols
        {
            public static final String ENTITY_ID="entityId";
            public static final String START_DATE="startDate";
            public static final String END_DATE="endDate";
            public static final String PAGES="pages";
        }
        public static String getCreateStatement()
        {
            StringBuilder fields=new StringBuilder();
            fields.append(Cols.getItemFields()).append(", ")
                    .append(Cols.ENTITY_ID).append(", ")
                    .append(Cols.START_DATE).append(", ")
                    .append(Cols.END_DATE).append(", ")
                    .append(Cols.PAGES);
            return String.format(CREATE_TEMPLATE,NAME,fields);
        }
    }
    public static final class ContactTable
    {
        public static final String NAME="contact";
        public static final class Cols implements NameI18NCols
        {
            public static final String ID ="id";
            public static final String ENTITY_ID="entityId";
            public static final String URI="uri";
        }
        public static String getCreateStatement()
        {
            StringBuilder fields=new StringBuilder();
            fields.append(Cols.ID).append(" UNIQUE NOT NULL, ")
                .append(Cols.ENTITY_ID).append(", ")
                .append(Cols.AR_NAME).append(", ")
                .append(Cols.EN_NAME).append(", ")
                .append(Cols.URI);
            return String.format(CREATE_TEMPLATE,NAME,fields);
        }
    }

}
