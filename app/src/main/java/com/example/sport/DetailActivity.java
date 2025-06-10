package com.example.sport;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.sport.databinding.ActivityDetailBinding;
import com.example.sport.model.Sport;
import com.example.sport.viewmodel.SportViewModel;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private SportViewModel viewModel;
    private Sport sport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detail Olahraga");
        }

        viewModel = new ViewModelProvider(this).get(SportViewModel.class);

        sport = getIntent().getParcelableExtra("SPORT");
        if (sport != null) {
            binding.tvDetailName.setText(sport.getName());
            binding.tvDetailDesc.setText(sport.getDescription());

            Glide.with(this)
                    .load(sport.getImageUrl())
                    .into(binding.ivDetailThumb);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, AddEditActivity.class);
            intent.putExtra("SPORT", sport);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            showDeleteConfirmation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Olahraga")
                .setMessage("Apakah Anda yakin ingin menghapus olahraga ini?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    viewModel.deleteSport(sport);
                    finish();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}