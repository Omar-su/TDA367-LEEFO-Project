package com.leefo.budgetapplication.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.leefo.budgetapplication.model.Category;

public class ParcelableCategory implements Parcelable {

    public Category category;

    public ParcelableCategory(Category category) {
        this.category = category;
    }

    public ParcelableCategory(Parcel in) {
    }

    public static final Creator<ParcelableCategory> CREATOR = new Creator<ParcelableCategory>() {
        @Override
        public ParcelableCategory createFromParcel(Parcel in) {
            return new ParcelableCategory(in);
        }

        @Override
        public ParcelableCategory[] newArray(int size) {
            return new ParcelableCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
