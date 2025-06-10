package com.example.sport.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.sport.DetailActivity;
import com.example.sport.adapter.SportAdapter;
import com.example.sport.databinding.FragmentFavoriteBinding;
import com.example.sport.model.Sport;
import com.example.sport.viewmodel.SportViewModel;

public class FavoriteFragment extends Fragment {
    private FragmentFavoriteBinding binding;
    private SportViewModel sportViewModel;
    private SportAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupViewModel();
        observeFavorites();
    }

    private void setupRecyclerView() {
        adapter = new SportAdapter(sport -> {
            Intent intent = new Intent(requireActivity(), DetailActivity.class);
            intent.putExtra("SPORT", sport);
            startActivity(intent);
        });

        binding.recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewFavorites.setAdapter(adapter);
    }

    private void setupViewModel() {
        sportViewModel = new ViewModelProvider(this).get(SportViewModel.class);
    }

    private void observeFavorites() {
        sportViewModel.getFavoriteSports().observe(getViewLifecycleOwner(), sports -> {
            if (sports != null && !sports.isEmpty()) {
                adapter.submitData(sports);
                binding.recyclerViewFavorites.setVisibility(View.VISIBLE);
                binding.tvNoFavorites.setVisibility(View.GONE);
            } else {
                binding.recyclerViewFavorites.setVisibility(View.GONE);
                binding.tvNoFavorites.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}