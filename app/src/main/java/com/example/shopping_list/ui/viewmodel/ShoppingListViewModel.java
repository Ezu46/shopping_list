package com.example.shopping_list.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shopping_list.data.model.ShoppingList;
import com.example.shopping_list.data.repository.ShoppingRepository;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {
    private final ShoppingRepository repository;
    private final LiveData<List<ShoppingList>> allLists;

    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        repository = new ShoppingRepository(application);
        allLists = repository.getAllLists();
    }

    public LiveData<List<ShoppingList>> getAllLists() {
        return allLists;
    }

    public void insert(ShoppingList list) {
        repository.insertList(list);
    }

    public void delete(ShoppingList list) {
        repository.deleteList(list);
    }
}
