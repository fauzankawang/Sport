package com.example.sport.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "sports")
public class Sport implements Parcelable {
    @PrimaryKey
    @NonNull
    @SerializedName("idSport")
    private String id;

    @SerializedName("strSport")
    private String name;

    @SerializedName("strSportThumb")
    private String imageUrl;

    @SerializedName("strSportDescription")
    private String description;

    private boolean isFavorite;

    public Sport(@NonNull String id, String name, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.isFavorite = false;
    }

    // Getter dan setter yang sudah ada
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    // Tambahan getter dan setter
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Parcelable implementation
    protected Sport(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        isFavorite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    public static final Creator<Sport> CREATOR = new Creator<Sport>() {
        @Override
        public Sport createFromParcel(Parcel in) {
            return new Sport(in);
        }

        @Override
        public Sport[] newArray(int size) {
            return new Sport[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}