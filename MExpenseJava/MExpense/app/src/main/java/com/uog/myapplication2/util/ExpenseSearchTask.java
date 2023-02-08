package com.uog.myapplication2.util;

import android.content.Context;
import android.os.AsyncTask;

import com.uog.myapplication2.database.DatabaseHelper;
import com.uog.myapplication2.database.Expenses;
import com.uog.myapplication2.database.Trip;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ExpenseSearchTask extends AsyncTask< String, Integer, List<Expenses>> {
    public interface ExpenseSearchListener{
        public void result(List<Expenses> expensesList);
    }

    private Context context;
    private ExpenseSearchListener listener;
    public void setListener(ExpenseSearchListener listener){this.listener =listener;}

    public ExpenseSearchTask(Context context){this.context =context;}

    @Override
    protected List<Expenses> doInBackground(String... strings) {
        List<Expenses> expensesList =new ArrayList<>();
        DatabaseHelper databaseHelper =new DatabaseHelper(context);
        try {
            expensesList = databaseHelper.searchExpenses( Integer.parseInt(strings[0]));
            listener.result(expensesList);
        } catch (Exception e) { e.printStackTrace(); }
        return expensesList;
    }

//    @Override
//    protected void onPostExecute(List<Expenses> expensesList){
//
//    }
}