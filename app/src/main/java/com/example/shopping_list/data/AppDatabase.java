package com.example.shopping_list.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.shopping_list.data.dao.ShoppingDao;
import com.example.shopping_list.data.model.ShoppingItem;
import com.example.shopping_list.data.model.ShoppingList;

@Database(
    entities = {ShoppingList.class, ShoppingItem.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShoppingDao shoppingDao();
    
    private static volatile AppDatabase INSTANCE;
    
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "shopping_database"
                        )
                        .fallbackToDestructiveMigration()
                        .build();
                }
            }
        }
        return INSTANCE;
    }
}
