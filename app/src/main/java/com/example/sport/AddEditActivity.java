package com.example.sport;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.sport.adapter.SportAdapter;
import com.example.sport.api.ApiClient;
import com.example.sport.api.ApiService;
import com.example.sport.databinding.ActivityAddEditBinding;
import com.example.sport.model.Sport;
import com.example.sport.model.SportResponse;
import com.example.sport.viewmodel.SportViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import java.util.stream.Collectors;

public class AddEditActivity extends AppCompatActivity {
    private ActivityAddEditBinding binding;
    private SportViewModel viewModel;
    private SportAdapter adapter;
    private ApiService apiService;
    private final Handler searchHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tambah Olahraga");
        }

        viewModel = new ViewModelProvider(this).get(SportViewModel.class);
        apiService = ApiClient.getClient().create(ApiService.class);

        setupRecyclerView();
        setupSearchView();
        loadAllSports();
    }

    private void setupRecyclerView() {
        adapter = new SportAdapter(sport -> {
            viewModel.insert(sport);
            Toast.makeText(this, "Olahraga berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            finish();
        });
        binding.rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSearchResults.setAdapter(adapter);
    }

    private void setupSearchView() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchHandler.removeCallbacksAndMessages(null);
                searchHandler.postDelayed(() -> {
                    String query = s.toString().trim();
                    if (query.isEmpty()) {
                        loadAllSports();
                    } else {
                        searchSports(query);
                    }
                }, 300);
            }
        });
    }

    private void loadAllSports() {
        binding.progressBar.setVisibility(View.VISIBLE);
        apiService.getAllSports().enqueue(new Callback<SportResponse>() {
            @Override
            public void onResponse(Call<SportResponse> call, Response<SportResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.submitData(response.body().getSports());
                } else {
                    Toast.makeText(AddEditActivity.this,
                            "Gagal memuat data olahraga", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SportResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AddEditActivity.this,
                        "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchSports(String query) {
        binding.progressBar.setVisibility(View.VISIBLE);
        apiService.getAllSports().enqueue(new Callback<SportResponse>() {
            @Override
            public void onResponse(Call<SportResponse> call, Response<SportResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Sport> filteredSports = response.body().getSports().stream()
                            .filter(sport -> sport.getName().toLowerCase()
                                    .contains(query.toLowerCase()))
                            .collect(Collectors.toList());

                    adapter.submitData(filteredSports);
                    if (filteredSports.isEmpty()) {
                        Toast.makeText(AddEditActivity.this,
                                "Olahraga tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SportResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AddEditActivity.this,
                        "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}