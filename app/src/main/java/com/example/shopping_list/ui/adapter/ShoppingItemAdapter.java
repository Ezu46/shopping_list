package com.example.shopping_list.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping_list.R;
import com.example.shopping_list.data.model.ShoppingItem;

public class ShoppingItemAdapter extends ListAdapter<ShoppingItem, ShoppingItemAdapter.ItemViewHolder> {

    public interface OnItemInteractionListener {
        void onPurchasedToggled(@NonNull ShoppingItem item, boolean purchased);
        void onItemLongPress(@NonNull ShoppingItem item);
        void onQuantityChanged(@NonNull ShoppingItem item); // called after + / -
    }

    private final OnItemInteractionListener listener;

    public ShoppingItemAdapter(OnItemInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox cbPurchased;
        private final TextView tvName;
        private final TextView tvQty;
        private final android.widget.Button btnPlus;
        private final android.widget.Button btnMinus;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cbPurchased = itemView.findViewById(R.id.cbPurchased);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQty = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }

        void bind(ShoppingItem item) {
            tvName.setText(item.name);
            tvQty.setText(String.valueOf(item.quantity));
            cbPurchased.setOnCheckedChangeListener(null);
            cbPurchased.setChecked(item.purchased);
            cbPurchased.setOnCheckedChangeListener((buttonView, isChecked) -> {
                listener.onPurchasedToggled(item, isChecked);
            });

            // quantity display
            tvQty.setText(String.valueOf(item.quantity));
            btnPlus.setOnClickListener(v -> {
                item.quantity += 1;
                tvQty.setText(String.valueOf(item.quantity));
                listener.onQuantityChanged(item);
            });
            btnMinus.setOnClickListener(v -> {
                if (item.quantity > 1) {
                    item.quantity -= 1;
                    tvQty.setText(String.valueOf(item.quantity));
                    listener.onQuantityChanged(item);
                }
            });

            itemView.setOnLongClickListener(v -> {
                listener.onItemLongPress(item);
                return true;
            });
        }
    }

    private static final DiffUtil.ItemCallback<ShoppingItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
            return oldItem.name.equals(newItem.name) && oldItem.quantity == newItem.quantity && oldItem.purchased == newItem.purchased;
        }
    };
}
