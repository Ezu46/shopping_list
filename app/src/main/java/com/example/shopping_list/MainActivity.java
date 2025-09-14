package com.example.shopping_list;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.shopping_list.data.model.ShoppingList;
import com.example.shopping_list.ui.adapter.ShoppingListAdapter;
import com.example.shopping_list.ItemsActivity;
import android.content.Intent;
import com.example.shopping_list.ui.viewmodel.ShoppingListViewModel;

public class MainActivity extends AppCompatActivity implements ShoppingListAdapter.OnItemClickListener {

    private ShoppingListViewModel viewModel;
private ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListAdapter(this, viewModel, this);
        recyclerView.setAdapter(adapter);
        viewModel.getAllLists().observe(this, adapter::submitList);

        findViewById(R.id.fabAddList).setOnClickListener(v -> showAddListDialog());
    }

    private void showAddListDialog() {
        final EditText input = new EditText(this);
        input.setHint(R.string.list_name_hint);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_list)
                .setView(input)
                .setPositiveButton(R.string.create, (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        viewModel.insert(new ShoppingList(name));
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onItemClick(@NonNull ShoppingList list) {
        Intent intent = new Intent(this, ItemsActivity.class);
        intent.putExtra("listId", list.id);
        intent.putExtra("listName", list.name);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(@NonNull ShoppingList list) {
        showEditListDialog(list);
    }

    private void showEditListDialog(ShoppingList list) {
        final EditText input = new EditText(this);
        input.setText(list.name);
        new AlertDialog.Builder(this)
                .setTitle(R.string.rename_list)
                .setView(input)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty() && !newName.equals(list.name)) {
                        list.name = newName;
                        viewModel.updateList(list);
                    }
                })
                .setNeutralButton(R.string.delete, (d, w) -> {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.delete_list_title)
                            .setMessage(R.string.delete_list_msg)
                            .setPositiveButton(R.string.delete, (dd, ww) -> viewModel.delete(list))
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}