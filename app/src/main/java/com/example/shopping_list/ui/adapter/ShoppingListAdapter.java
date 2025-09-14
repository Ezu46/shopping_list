package com.example.shopping_list.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping_list.R;
import com.example.shopping_list.data.model.ShoppingList;

public class ShoppingListAdapter extends ListAdapter<ShoppingList, ShoppingListAdapter.ListViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ShoppingList list);
        void onItemLongClick(ShoppingList list);
    }

    private final OnItemClickListener listener;

    public ShoppingListAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ShoppingList list = getItem(position);
        holder.bind(list);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvCount;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvListName);
            tvCount = itemView.findViewById(R.id.tvItemCount);
            itemView.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(pos));
                }
            });
            itemView.setOnLongClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(getItem(pos));
                    return true;
                }
                return false;
            });
        }

        void bind(ShoppingList list) {
            tvName.setText(list.name);
            // tvCount can show 0 items for now; we'll update later with actual count
            tvCount.setText(itemView.getContext().getString(R.string.item_count_format, 0));
        }
    }

    private static final DiffUtil.ItemCallback<ShoppingList> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
            return oldItem.name.equals(newItem.name);
        }
    };
}
