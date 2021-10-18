package com.leefo.budgetapplication.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.leefo.budgetapplication.model.FinancialTransaction;

/**
 * class that holds one FinancialTransaction
 * implements Parcelable
 *
 * used by fragments needing to send a FinancialTransaction with Bundle to another fragment
 * Bundle can't take any type of object. But bundle can take any Parcelable objects. That's why this class exist.
 *
 * @author Emelie Edberg
 */
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
