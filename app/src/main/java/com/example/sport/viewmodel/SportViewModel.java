package com.example.sport.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.sport.model.Sport;
import com.example.sport.repository.SportRepository;
import java.util.List;

public class SportViewModel extends AndroidViewModel {
    private final SportRepository repository;

    public SportViewModel(Application application) {
        super(application);
        repository = new SportRepository(application);
    }

    public LiveData<List<Sport>> getSports() {
        return repository.getAllSports();
    }

    public LiveData<List<Sport>> getFavoriteSports() {
        return repository.getFavoriteSports();
    }

    public void insert(Sport sport) {
        repository.insert(sport);
    }

    public void deleteSport(Sport sport) {
        repository.delete(sport);
    }

    public void updateFavorite(Sport sport) {
        sport.setFavorite(!sport.isFavorite());
        repository.update(sport);
    }

    public LiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    public LiveData<String> getErrorMessage() {
        return repository.getErrorMessage();
    }

    public void fetchSports() {
        repository.fetchSportsFromServer();
    }
}