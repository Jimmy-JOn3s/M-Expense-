package com.uog.myapplication2.util;

import android.content.Context;
import android.os.AsyncTask;

import com.uog.myapplication2.database.DatabaseHelper;
import com.uog.myapplication2.database.Trip;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TripSearchTask extends AsyncTask< String, Integer, List<Trip>> {
    public interface TripSearchListener{
        public void result(List<Trip> tripList);
    }

    private Context context;
    private TripSearchListener listener;
    public void setListener(TripSearchListener listener){this.listener =listener;}

    public TripSearchTask(Context context){this.context =context;}

    @Override
    protected List<Trip> doInBackground(String... strings) {
        List<Trip> tripList =new ArrayList<>();
        DatabaseHelper databaseHelper =new DatabaseHelper(context);
        try {
            tripList =
                    strings.length==1? databaseHelper.searchTrip(strings[0])
                    : databaseHelper.searchTrip(strings[0], strings[1], new Date(Date.parse(strings[2])) );
            listener.result(tripList);
        } catch (Exception e) { e.printStackTrace(); }
        return tripList;
    }

//    @Override
//    protected void onPostExecute(List<Trip> tripList){
//
//    }
}