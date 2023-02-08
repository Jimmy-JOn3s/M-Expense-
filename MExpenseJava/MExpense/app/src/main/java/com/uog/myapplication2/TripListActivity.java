package com.uog.myapplication2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uog.myapplication2.adapter.TripListAdapter;
import com.uog.myapplication2.database.DatabaseHelper;
import com.uog.myapplication2.database.Trip;
import com.uog.myapplication2.util.Constants;
import com.uog.myapplication2.util.TripSearchTask;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TripListActivity extends AppCompatActivity {
    private List<Trip> tripList =new ArrayList<>();
    private EditText txtSearch;
    private ImageButton btnSearch, btnAdvance;
    private FloatingActionButton fabAddExpense, fabUpload;
    private RecyclerView recyclerView;
    private static final int SEARCH_CRITERIA_REQUEST_CODE =1;
    private TripListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        txtSearch =findViewById(R.id.txtSearch);
        btnSearch =findViewById(R.id.btnSearch);
        btnAdvance =findViewById(R.id.btnAdvance);
        fabAddExpense =findViewById(R.id.fabAddExpense);
        fabUpload =findViewById(R.id.fabUpload);
        recyclerView =findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionsRegister();
        basicSearch( txtSearch.getText().toString().trim());
    }

    private void actionsRegister(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicSearch( txtSearch.getText().toString().trim());
            }
        });

        btnAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(TripListActivity.this, AdvanceSearchActivity.class ),SEARCH_CRITERIA_REQUEST_CODE);
            }
        });

        fabAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( getApplicationContext(), EntryActivity.class ) );
            }
        });

        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( getApplicationContext(), TripDataUploadActivity.class ) );
            }
        });

        adapter =new TripListAdapter(tripList);
        adapter.setOnItemClickListener(new TripListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, long id) {
                if( id == R.id.btnTripEdit){
                    updateTrip( position );

                }else if( id == R.id.btnTripRemove ){
                    removeTrip(position);
                }else if( id == R.id.btnTripExpense ){
                    tripExpense(position);
                }else if( id == R.id.btnTripDetail ){
                    tripDetail(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void basicSearch(String keyword){
        TripSearchTask searchTask =new TripSearchTask(this);
        searchTask.setListener(new TripSearchTask.TripSearchListener() {
            @Override
            public void result(List<Trip> trips) {
                showTripListResult( trips );
            }
        });
        searchTask.execute( keyword );
    }

    private void advanceSearch(String name, String destination, Date date){
        TripSearchTask searchTask =new TripSearchTask(this);
        searchTask.setListener(new TripSearchTask.TripSearchListener() {
            @Override
            public void result(List<Trip> trips) {
                showTripListResult( trips );
            }
        });
        searchTask.execute( name, destination, date.toString() );
    }

    private void showTripListResult(List<Trip> trips){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tripList = trips;
                adapter.setTripList(tripList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void updateTrip( int position ){
        Trip trip =tripList.get(position);
        Intent intent = new Intent( getApplicationContext(), EntryActivity.class );
        intent.putExtra(EntryActivity.NAME, trip.getName());
        intent.putExtra(EntryActivity.DESTINATION, trip.getDestination());
        intent.putExtra(EntryActivity.DATE, trip.getDate());
        intent.putExtra(EntryActivity.RISK_ASSESSMENT, trip.isRiskAssessment());
        intent.putExtra(EntryActivity.DESCRIPTION, trip.getDescription());

        // for updating
        intent.putExtra(EntryActivity.ID, trip !=null? trip.getId() : 0);
        intent.putExtra(EntryActivity.VALUE1, trip !=null? trip.getValue1() : "");
        intent.putExtra(EntryActivity.VALUE2, trip !=null? trip.getValue2() : "");
        intent.putExtra(EntryActivity.VALUE3, trip !=null? trip.getValue3() : "");
        intent.putExtra(EntryActivity.NUM1, trip !=null? trip.getNum1() : 0);
        intent.putExtra(EntryActivity.NUM2, trip !=null? trip.getNum2() : 0);
        startActivity(intent);
    }

    private void removeTrip( int position ){
        DatabaseHelper database =new DatabaseHelper(getBaseContext());
        Trip trip =tripList.get(position);
        long result =database.deleteTrip(trip.getId());
        if( result >0 ){
            tripList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(getBaseContext(), trip.getName() +" has been deleted!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(), "Error!, failed to delete the trip "+trip.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void tripExpense(int position){
        Trip trip =tripList.get(position);
        Intent intent = new Intent(getApplicationContext(), ExpenseListActivity.class);
        intent.putExtra(EntryActivity.ID, trip.getId());
        startActivity( intent );
    }

    private void tripDetail(int position){
        Trip trip =tripList.get(position);
        Intent intent = new Intent( getApplicationContext(), TripDetailActivity.class );
        intent.putExtra(EntryActivity.NAME, trip.getName());
        intent.putExtra(EntryActivity.DESTINATION, trip.getDestination());
        intent.putExtra(EntryActivity.DATE, trip.getDate());
        intent.putExtra(EntryActivity.RISK_ASSESSMENT, trip.isRiskAssessment()?"Yes":"No");
        intent.putExtra(EntryActivity.DESCRIPTION, trip.getDescription());

        // for updating
        intent.putExtra(EntryActivity.ID, trip !=null? trip.getId() : 0);
        intent.putExtra(EntryActivity.VALUE1, trip !=null? trip.getValue1() : "");
        intent.putExtra(EntryActivity.VALUE2, trip !=null? trip.getValue2() : "");
        intent.putExtra(EntryActivity.VALUE3, trip !=null? trip.getValue3() : "");
        intent.putExtra(EntryActivity.NUM1, trip !=null? trip.getNum1() : 0);
        intent.putExtra(EntryActivity.NUM2, trip !=null? trip.getNum2() : 0);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SEARCH_CRITERIA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(EntryActivity.NAME);
                String destination = data.getStringExtra(EntryActivity.DESTINATION);
                long date = data.getLongExtra(EntryActivity.DATE, 0);
                advanceSearch( name, destination, new Date(date) );
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}