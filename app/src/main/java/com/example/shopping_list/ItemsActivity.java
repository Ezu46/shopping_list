package com.example.shopping_list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping_list.data.model.ShoppingItem;
import com.example.shopping_list.ui.adapter.ShoppingItemAdapter;
import com.example.shopping_list.ui.viewmodel.ShoppingItemViewModel;

public class ItemsActivity extends AppCompatActivity implements ShoppingItemAdapter.OnItemInteractionListener {

    private long listId;
    private String listName;

    private ShoppingItemAdapter adapter;
    private ShoppingItemViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        listId = getIntent().getLongExtra("listId", -1);
        listName = getIntent().getStringExtra("listName");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(listName);
        }

        RecyclerView rv = findViewById(R.id.rvItems);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingItemAdapter(this);
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ShoppingItemViewModel.class);
        viewModel.getItemsForList(listId).observe(this, adapter::submitList);

        findViewById(R.id.fabAddItem).setOnClickListener(v -> showAddItemDialog());

        // swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ShoppingItem item = adapter.getCurrentList().get(viewHolder.getBindingAdapterPosition());
                viewModel.delete(item);
            }
        }).attachToRecyclerView(rv);
    }

    private void showAddItemDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
        EditText etName = dialogView.findViewById(R.id.etItemName);
        EditText etQty = dialogView.findViewById(R.id.etQuantity);
        etQty.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_item)
                .setView(dialogView)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String qtyStr = etQty.getText().toString().trim();
                    int qty = qtyStr.isEmpty() ? 1 : Integer.parseInt(qtyStr);
                    if (!name.isEmpty()) {
                        viewModel.insert(new ShoppingItem(listId, name, qty));
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onPurchasedToggled(ShoppingItem item, boolean purchased) {
        item.purchased = purchased;
        viewModel.update(item);
    }

    @Override
    public void onQuantityChanged(ShoppingItem item) {
        viewModel.update(item);
    }

    @Override
    public void onItemLongPress(ShoppingItem item) {
        showEditItemDialog(item);
    }

    private void showEditItemDialog(ShoppingItem item) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
        EditText etName = dialogView.findViewById(R.id.etItemName);
        EditText etQty = dialogView.findViewById(R.id.etQuantity);
        etQty.setInputType(InputType.TYPE_CLASS_NUMBER);

        etName.setText(item.name);
        etQty.setText(String.valueOf(item.quantity));

        new AlertDialog.Builder(this)
                .setTitle(R.string.edit_item)
                .setView(dialogView)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String newName = etName.getText().toString().trim();
                    String qtyStr = etQty.getText().toString().trim();
                    int newQty = qtyStr.isEmpty() ? 1 : Integer.parseInt(qtyStr);
                    boolean changed = false;
                    if (!newName.isEmpty() && !newName.equals(item.name)) {
                        item.name = newName;
                        changed = true;
                    }
                    if (newQty != item.quantity) {
                        item.quantity = newQty;
                        changed = true;
                    }
                    if (changed) {
                        viewModel.update(item);
                    }
                })
                .setNeutralButton(R.string.delete, (dialog, which) -> viewModel.delete(item))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
