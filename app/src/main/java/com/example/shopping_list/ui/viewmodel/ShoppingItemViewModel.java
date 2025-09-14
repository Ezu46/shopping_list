package com.example.shopping_list.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shopping_list.data.model.ShoppingItem;
import com.example.shopping_list.data.repository.ShoppingRepository;

import java.util.List;

public class ShoppingItemViewModel extends AndroidViewModel {
    private final ShoppingRepository repository;
    private LiveData<List<ShoppingItem>> itemsForList;

    public ShoppingItemViewModel(@NonNull Application application) {
        super(application);
        repository = new ShoppingRepository(application);
    }

    public LiveData<List<ShoppingItem>> getItemsForList(long listId) {
        if (itemsForList == null) {
            itemsForList = repository.getItemsForList(listId);
        }
        return itemsForList;
    }

    public void insert(ShoppingItem item) {
        repository.insertItem(item);
    }

    public void update(ShoppingItem item) {
        repository.updateItem(item);
    }

    public void delete(ShoppingItem item) {
        repository.deleteItem(item);
    }

    public void clearPurchased(long listId) {
        repository.deletePurchased(listId);
    }
}
