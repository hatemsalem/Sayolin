package com.zobonapp.manager;

import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.domain.Category;
import com.zobonapp.domain.Contact;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by hasalem on 11/26/2017.
 */

public interface DataManager
{
   void updateBusinessEntities(List<BusinessEntity> entities);
   void updateItemCategoryRelations(HashMap<UUID,Vector<UUID>> relation);
   void updateBusinessItem(BusinessEntity entity);
   void updateCategories(List<Category> categories);
   void insertContacts(List<Contact> contacts);
   void updateEntityTags(List<Category> tags);
   List<BusinessEntity> findBusinessEntitiesForPage(int offset,int limit,String searchQuery,String categoryId);
   List<BusinessEntity> findFavoriteEntitiesForPage(int offset,int limit,String searchQuery,String categoryId);
   List<Category> findCategoriesForPage(int type,int offset,int limit);
   List<Contact> findContactsForItem(String itemId);
}
