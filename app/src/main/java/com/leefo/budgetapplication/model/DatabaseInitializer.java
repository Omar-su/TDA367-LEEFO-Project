package com.leefo.budgetapplication.model;

import android.content.Context;

public class DatabaseInitializer {

    public static void InitializeDatabase(Context context)
    {
        DataBaseManager.initialize(context);
    }
}
