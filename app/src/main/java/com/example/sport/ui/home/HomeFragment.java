package com.example.sport.ui.home;

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
import com.example.sport.databinding.FragmentHomeBinding;
import com.example.sport.model.Sport;
import com.example.sport.viewmodel.SportViewModel;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private SportViewModel viewModel;
    private SportAdapter adapter;
    private SportAdapter secondAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerViews();
        setupViewModel();
        observeViewModel();

        binding.btnRetry.setOnClickListener(v -> viewModel.fetchSports());
    }

    private void setupRecyclerViews() {
        adapter = new SportAdapter(sport -> {
            Intent intent = new Intent(requireActivity(), DetailActivity.class);
            intent.putExtra("SPORT", sport);
            startActivity(intent);
        });
        binding.recyclerViewSports.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewSports.setAdapter(adapter);

        secondAdapter = new SportAdapter(sport -> {
            Intent intent = new Intent(requireActivity(), DetailActivity.class);
            intent.putExtra("SPORT", sport);
            startActivity(intent);
        });
        binding.recyclerViewSportsSecond.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewSportsSecond.setAdapter(secondAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SportViewModel.class);
        viewModel.fetchSports();
    }

    private void observeViewModel() {
        viewModel.getSports().observe(getViewLifecycleOwner(), sports -> {
            if (sports != null && !sports.isEmpty()) {
                binding.contentLayout.setVisibility(View.VISIBLE);
                binding.errorLayout.setVisibility(View.GONE);

                adapter.submitData(sports.subList(0, Math.min(5, sports.size())));

                if (sports.size() > 5) {
                    secondAdapter.submitData(sports.subList(5, sports.size()));
                    binding.recyclerViewSportsSecond.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerViewSportsSecond.setVisibility(View.GONE);
                }
            } else {
                binding.contentLayout.setVisibility(View.GONE);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.contentLayout.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            binding.errorLayout.setVisibility(View.GONE);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                binding.errorLayout.setVisibility(View.VISIBLE);
                binding.contentLayout.setVisibility(View.GONE);
                binding.tvErrorMessage.setText(error);
            } else {
                binding.errorLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}