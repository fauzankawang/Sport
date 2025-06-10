package com.example.sport.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SportResponse {
    @SerializedName("sports")
    private List<Sport> sports;

    public List<Sport> getSports() {
        return sports;
    }
}