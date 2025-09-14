package com.example.shopping_list.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "shopping_items",
    foreignKeys = @ForeignKey(
        entity = ShoppingList.class,
        parentColumns = "id",
        childColumns = "listId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("listId")}
)
public class ShoppingItem {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public long listId;
    
    @NonNull
    public String name;
    
    public int quantity;
    public boolean purchased;
    
    public ShoppingItem(long listId, @NonNull String name, int quantity) {
        this.listId = listId;
        this.name = name;
        this.quantity = quantity;
        this.purchased = false;
    }
}
