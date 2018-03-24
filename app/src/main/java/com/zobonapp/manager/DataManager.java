package com.zobonapp.manager;

import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.domain.Category;
import com.zobonapp.domain.Contact;
import com.zobonapp.domain.Menu;
import com.zobonapp.domain.Offer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by hasalem on 11/26/2017.
 */

public interface DataManager
{
   void populateItemCategoryRelations(HashMap<String,Vector<String>> objects);
   BusinessEntity findBusinessItemById(String id);
   Category findCategoryById(String id);
   void updateBusinessItem(BusinessEntity entity);
   void populateCategories(List<HashMap<String,?>> objects);
   void populateOffers(List<HashMap<String,?>> objects);
   void populateMenus(List<HashMap<String,?>> objects);
   void populateBusinessEntities(List<HashMap<String,?>> objects);
   void populateContacts(List<HashMap<String,?>> objects);
   List<BusinessEntity> findBusinessEntitiesForPage(int offset,int limit,String searchQuery,String categoryId);
   List<BusinessEntity> findFavoriteEntitiesForPage(int offset,int limit,String searchQuery,String categoryId);
   List<Offer> findOffersForPage(int offset,int limit,String searchQuery,String categoryId);
   List<Menu> findMenusForPage(int offset, int limit, String searchQuery, String categoryId);
   List<Category> findCategoriesForPage(int type,int offset,int limit,String searchQuery);
   List<Contact> findContactsForItem(String itemId);
   void deleteItems(List<String> items);
   void deleteCategories(List<String> items);
}