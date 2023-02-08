package com.uog.myapplication2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uog.myapplication2.adapter.ExpenseListAdapter;
import com.uog.myapplication2.database.DatabaseHelper;
import com.uog.myapplication2.database.Expenses;
import com.uog.myapplication2.database.Trip;
import com.uog.myapplication2.util.Constants;
import com.uog.myapplication2.util.ExpenseSearchTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripDetailActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtDestination;
    private TextView txtDate;
    private TextView txtRiskAssessment;
    private TextView txtDescription;
    private RecyclerView recyclerViewExpense;
    private ExpenseListAdapter adapter;

    private String name;
    private String destination;
    private long date;
    private String riskAssessment;
    private String description;
    private String value1;
    private String value2;
    private String value3;
    private Double num1;
    private Double num2;
    private Integer id =0;
    private List<Expenses> expensesList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName =findViewById(R.id.txtName);
        txtDestination =findViewById(R.id.txtDestination);
        txtDate =findViewById(R.id.txtDate);
        txtRiskAssessment =findViewById(R.id.txtRiskAssessment);
        txtDescription =findViewById(R.id.txtDescription);
        recyclerViewExpense =findViewById(R.id.recyclerViewExpense);
        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(this));

        adapter =new ExpenseListAdapter(expensesList);
        adapter.setOnItemClickListener(new ExpenseListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, long id) {
                removeExpense(position);
            }
        });
        recyclerViewExpense.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        if( bundle !=null ){
            name = bundle.getString(EntryActivity.NAME);
            destination = bundle.getString(EntryActivity.DESTINATION);
            date = bundle.getLong(EntryActivity.DATE);
            riskAssessment = bundle.getString(EntryActivity.RISK_ASSESSMENT);
            description = bundle.getString(EntryActivity.DESCRIPTION);

            txtName.setText(name);
            txtDestination.setText(destination);
            txtRiskAssessment.setText(riskAssessment);
            txtDescription.setText(description);
            Date selectedDate =new Date( date );
            String dateStr = new SimpleDateFormat(Constants.DATE_FORMAT).format(selectedDate);
            txtDate.setText("Date: " + dateStr);

            try {
                id =bundle.getInt(EntryActivity.ID, 0);
                value1 =bundle.getString(EntryActivity.VALUE1);
                value2 =bundle.getString(EntryActivity.VALUE2);
                value3 =bundle.getString(EntryActivity.VALUE3);
                num1 =bundle.getDouble(EntryActivity.NUM1, 0);
                num2 =bundle.getDouble(EntryActivity.NUM2, 0);
            }catch (Exception e){}

        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        expenseSearch( );
    }
    private void expenseSearch(){
        ExpenseSearchTask searchTask =new ExpenseSearchTask(this);
        searchTask.setListener(new ExpenseSearchTask.ExpenseSearchListener() {
            @Override
            public void result(List<Expenses> expensesList) {
                showExpenseListResult( expensesList );
            }
        });
        searchTask.execute( String.valueOf(id) );
    }

    private void showExpenseListResult(List<Expenses> expenses){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                expensesList = expenses;
                adapter.setTripList(expensesList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void removeExpense( int position ){
        DatabaseHelper database =new DatabaseHelper(getBaseContext());
        Expenses expenses =expensesList.get(position);
        long result =database.deleteExpense(expenses.getId());
        if( result >0 ){
            expensesList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(getBaseContext(), expenses.getExpenseType() +" has been deleted!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(), "Error!, failed to delete the trip "+expenses.getExpenseType(), Toast.LENGTH_SHORT).show();
        }
    }
}