package com.example.sport.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sport.databinding.ItemSportBinding;
import com.example.sport.model.Sport;
import java.util.List;

public class SportAdapter extends ListAdapter<Sport, SportAdapter.SportViewHolder> {
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Sport sport);
    }

    public SportAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Sport> DIFF_CALLBACK = new DiffUtil.ItemCallback<Sport>() {
        @Override
        public boolean areItemsTheSame(@NonNull Sport oldItem, @NonNull Sport newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Sport oldItem, @NonNull Sport newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getImageUrl().equals(newItem.getImageUrl()) &&
                    oldItem.isFavorite() == newItem.isFavorite();
        }
    };

    @NonNull
    @Override
    public SportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSportBinding binding = ItemSportBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new SportViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SportViewHolder holder, int position) {
        Sport sport = getItem(position);
        holder.bind(sport);
    }

    public void submitData(List<Sport> sports) {
        submitList(sports);
    }

    class SportViewHolder extends RecyclerView.ViewHolder {
        private final ItemSportBinding binding;

        SportViewHolder(ItemSportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        void bind(Sport sport) {
            binding.tvSportName.setText(sport.getName());
            Glide.with(binding.getRoot())
                    .load(sport.getImageUrl())
                    .into(binding.ivSport);
        }
    }
}