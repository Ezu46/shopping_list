package com.example.shopping_list.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.shopping_list.data.AppDatabase;
import com.example.shopping_list.data.dao.ShoppingDao;
import com.example.shopping_list.data.model.ShoppingItem;
import com.example.shopping_list.data.model.ShoppingList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingRepository {
    private final ShoppingDao shoppingDao;
    private final ExecutorService executorService;
    
    public ShoppingRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        shoppingDao = db.shoppingDao();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    // ShoppingList operations
    public LiveData<List<ShoppingList>> getAllLists() {
        return shoppingDao.getAllLists();
    }
    
    public LiveData<Integer> getItemCount(long listId) {
        return shoppingDao.getItemCount(listId);
    }

    public void insertList(ShoppingList list) {
        executorService.execute(() -> shoppingDao.insertList(list));
    }
    
    public void updateList(ShoppingList list) {
        executorService.execute(() -> shoppingDao.updateList(list));
    }
    
    public void deleteList(ShoppingList list) {
        executorService.execute(() -> shoppingDao.deleteList(list));
    }
    
    // ShoppingItem operations
    public LiveData<List<ShoppingItem>> getItemsForList(long listId) {
        return shoppingDao.getItemsForList(listId);
    }
    
    public void insertItem(ShoppingItem item) {
        executorService.execute(() -> shoppingDao.insertItem(item));
    }
    
    public void updateItem(ShoppingItem item) {
        executorService.execute(() -> shoppingDao.updateItem(item));
    }
    
    public void deleteItem(ShoppingItem item) {
        executorService.execute(() -> shoppingDao.deleteItem(item));
    }

    public void deletePurchased(long listId) {
        executorService.execute(() -> shoppingDao.deletePurchased(listId));
    }
}
