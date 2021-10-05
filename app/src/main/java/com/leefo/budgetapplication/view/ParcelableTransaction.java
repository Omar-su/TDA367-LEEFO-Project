package com.leefo.budgetapplication.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.leefo.budgetapplication.model.FinancialTransaction;

public class ParcelableTransaction implements Parcelable {

    public FinancialTransaction financialTransaction;

    public ParcelableTransaction(FinancialTransaction financialTransaction) {
        this.financialTransaction = financialTransaction;
    }

    protected ParcelableTransaction(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableTransaction> CREATOR = new Creator<ParcelableTransaction>() {
        @Override
        public ParcelableTransaction createFromParcel(Parcel in) {
            return new ParcelableTransaction(in);
        }

        @Override
        public ParcelableTransaction[] newArray(int size) {
            return new ParcelableTransaction[size];
        }
    };
}
