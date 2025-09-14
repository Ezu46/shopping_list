package com.example.shopping_list.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_lists")
public class ShoppingList {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    @NonNull
    public String name;
    
    public ShoppingList(@NonNull String name) {
        this.name = name;
    }
}
