package com.uog.myapplication2.util;

import android.content.Context;
import android.os.AsyncTask;

import com.uog.myapplication2.database.DatabaseHelper;
import com.uog.myapplication2.database.Expenses;
import com.uog.myapplication2.database.TripExportData;

import java.util.ArrayList;
import java.util.List;

public class TripDataExportTask extends AsyncTask< String, Integer, List<TripExportData>> {
    public interface TripDataExportListener{
        public void result(List<TripExportData> tripExportData);
    }

    private Context context;
    private TripDataExportListener listener;
    public void setListener(TripDataExportListener listener){this.listener =listener;}

    public TripDataExportTask(Context context){this.context =context;}

    @Override
    protected List<TripExportData> doInBackground(String... strings) {
        List<TripExportData> tripExportData =new ArrayList<>();
        DatabaseHelper databaseHelper =new DatabaseHelper(context);
        try {
            tripExportData = databaseHelper.exportTrip( );
            listener.result(tripExportData);
        } catch (Exception e) { e.printStackTrace(); }
        return tripExportData;
    }

//    @Override
//    protected void onPostExecute(List<Expenses> expensesList){
//
//    }
}