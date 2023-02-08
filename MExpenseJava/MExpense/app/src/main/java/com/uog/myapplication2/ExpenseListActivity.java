package com.uog.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uog.myapplication2.adapter.ExpenseListAdapter;
import com.uog.myapplication2.database.DatabaseHelper;
import com.uog.myapplication2.database.Expenses;
import com.uog.myapplication2.util.ExpenseSearchTask;
import java.util.ArrayList;
import java.util.List;


public class ExpenseListActivity extends AppCompatActivity {
    private List<Expenses> expensesList =new ArrayList<>();
    private FloatingActionButton fabExpense;
    private RecyclerView recyclerViewExpense;
    private ExpenseListAdapter adapter;
    private Integer tripId =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabExpense =findViewById(R.id.fabExpense);
        recyclerViewExpense =findViewById(R.id.recyclerViewExpense);
        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();
        if( bundle !=null ){
            tripId =bundle.getInt(EntryActivity.ID, 0);
        }

        if( tripId ==0){
            Toast.makeText(getBaseContext(), "Please select the trip", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionsRegister();
        expenseSearch( );
    }

    private void actionsRegister(){

        fabExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExpenseEntryActivity.class);
                intent.putExtra(EntryActivity.ID, tripId);
                startActivity( intent );
            }
        });

        adapter =new ExpenseListAdapter(expensesList);
        adapter.setOnItemClickListener(new ExpenseListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, long id) {
                removeExpense(position);
            }
        });
        recyclerViewExpense.setAdapter(adapter);
    }

    private void expenseSearch(){
        ExpenseSearchTask searchTask =new ExpenseSearchTask(this);
        searchTask.setListener(new ExpenseSearchTask.ExpenseSearchListener() {
            @Override
            public void result(List<Expenses> expensesList) {
                showExpenseListResult( expensesList );
            }
        });
        searchTask.execute( String.valueOf(tripId) );
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == SEARCH_CRITERIA_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                String name = data.getStringExtra(EntryActivity.NAME);
//                String destination = data.getStringExtra(EntryActivity.DESTINATION);
//                long date = data.getLongExtra(EntryActivity.DATE, 0);
//                advanceSearch( name, destination, new Date(date) );
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}