package com.example.shopping_list.data.model;

import androidx.room.ColumnInfo;

public class ListCount {
    @ColumnInfo(name = "listId")
    public long listId;

    @ColumnInfo(name = "cnt")
    public int cnt;
}
