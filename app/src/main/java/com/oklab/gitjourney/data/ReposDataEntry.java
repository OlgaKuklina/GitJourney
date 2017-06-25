package com.oklab.gitjourney.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by olgakuklina on 2017-01-16.
 */

public class ReposDataEntry implements Parcelable {

    public static final Creator<ReposDataEntry> CREATOR = new Creator<ReposDataEntry>() {
        @Override
        public ReposDataEntry createFromParcel(Parcel in) {
            return new ReposDataEntry(in);
        }

        @Override
        public ReposDataEntry[] newArray(int size) {
            return new ReposDataEntry[size];
        }
    };
    private final String title;
    private final String owner;
    private final boolean privacy;
    private final String description;
    private final String language;
    private final int stars;
    private final int forks;

    public ReposDataEntry(String title, String ownerName, boolean privacy, String description, String language, int stars, int forks) {
        this.title = title;
        this.owner = ownerName;
        this.privacy = privacy;
        this.description = description;
        this.language = language;
        this.stars = stars;
        this.forks = forks;
    }

    protected ReposDataEntry(Parcel in) {
        title = in.readString();
        owner = in.readString();
        privacy = in.readByte() != 0;
        description = in.readString();
        language = in.readString();
        stars = in.readInt();
        forks = in.readInt();
    }

    public boolean isPrivate() {
        return privacy;
    }

    public String getTitle() {
        return title;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public int getStars() {
        return stars;
    }

    public int getForks() {
        return forks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(owner);
        parcel.writeInt((byte) (privacy ? 1 : 0));
        parcel.writeString(description);
        parcel.writeString(language);
        parcel.writeInt(stars);
        parcel.writeInt(forks);
    }
}
