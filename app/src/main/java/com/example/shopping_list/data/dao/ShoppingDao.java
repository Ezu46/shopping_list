package com.example.shopping_list.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shopping_list.data.model.ShoppingItem;
import com.example.shopping_list.data.model.ShoppingList;

import java.util.List;

@Dao
public interface ShoppingDao {
    // ShoppingList operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertList(ShoppingList shoppingList);
    
    @Query("SELECT * FROM shopping_lists ORDER BY name ASC")
    LiveData<List<ShoppingList>> getAllLists();
    
    @Update
    void updateList(ShoppingList shoppingList);

    @Delete
    void deleteList(ShoppingList shoppingList);
    
    // ShoppingItem operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertItem(ShoppingItem item);
    
    @Update
    void updateItem(ShoppingItem item);
    
    @Delete
    void deleteItem(ShoppingItem item);
    
    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY id ASC")
    LiveData<List<ShoppingItem>> getItemsForList(long listId);
    
    @Query("DELETE FROM shopping_items WHERE listId = :listId")
    void deleteItemsForList(long listId);

    @Query("DELETE FROM shopping_items WHERE listId = :listId AND purchased = 1")
    void deletePurchased(long listId);

    @Query("SELECT COUNT(*) FROM shopping_items WHERE listId = :listId")
    LiveData<Integer> getItemCount(long listId);
}
