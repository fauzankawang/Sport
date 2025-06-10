package com.example.sport.repository;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.sport.api.ApiClient;
import com.example.sport.api.ApiService;
import com.example.sport.db.SportDao;
import com.example.sport.db.SportDatabase;
import com.example.sport.model.Sport;
import com.example.sport.model.SportResponse;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportRepository {
    private SportDao sportDao;
    private ApiService apiService;
    private ExecutorService executorService;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SportRepository(Application application) {
        SportDatabase db = SportDatabase.getDatabase(application);
        sportDao = db.sportDao();
        apiService = ApiClient.getClient().create(ApiService.class);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Sport>> getAllSports() {
        return sportDao.getAllSports();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void insert(Sport sport) {
        executorService.execute(() -> sportDao.insert(sport));
    }

    public void update(Sport sport) {
        executorService.execute(() -> sportDao.update(sport));
    }

    public void delete(Sport sport) {
        executorService.execute(() -> sportDao.delete(sport));
    }

    public LiveData<List<Sport>> getFavoriteSports() {
        return sportDao.getFavoriteSports();
    }

    public void fetchSportsFromServer() {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        apiService.getAllSports().enqueue(new Callback<SportResponse>() {
            @Override
            public void onResponse(Call<SportResponse> call, Response<SportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Sport> sports = response.body().getSports();
                    Log.d("SportRepository", "Data olahraga yang diterima: " + (sports != null ? sports.size() : 0));

                    executorService.execute(() -> {
                        try {
                            sportDao.deleteAll();
                            for (Sport newSport : sports) {
                                Sport existingSport = sportDao.getSportById(newSport.getId());
                                if (existingSport != null) {
                                    newSport.setFavorite(existingSport.isFavorite());
                                }
                                Log.d("SportRepository", "Memproses olahraga: " + newSport.getName());
                            }
                            sportDao.insertAll(sports);
                            Log.d("SportRepository", "Update database berhasil");
                        } catch (Exception e) {
                            Log.e("SportRepository", "Error database: " + e.getMessage());
                            errorMessage.postValue("Error database: " + e.getMessage());
                        }
                    });
                    errorMessage.setValue(null);
                } else {
                    String error = "Error: " + response.code() + " " + response.message();
                    Log.e("SportRepository", error);
                    errorMessage.setValue(error);
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<SportResponse> call, Throwable t) {
                String error = "Error jaringan: " + t.getMessage();
                Log.e("SportRepository", error, t);
                errorMessage.setValue(error);
                isLoading.setValue(false);
            }
        });
    }
}