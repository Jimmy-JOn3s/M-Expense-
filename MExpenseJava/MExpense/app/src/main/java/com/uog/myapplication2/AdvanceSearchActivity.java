package com.uog.myapplication2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.uog.myapplication2.util.Constants;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AdvanceSearchActivity extends AppCompatActivity {

    private EditText txtSearchName;
    private EditText txtSearchDestination;
    private TextView txtSearchDate;
    private Button btnSearchDateChooser;
    private Button btnAdvanceSearch;
    private Date selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);

        txtSearchName =findViewById(R.id.txtSearchName);
        txtSearchDestination =findViewById(R.id.txtSearchDestination);
        txtSearchDate =findViewById(R.id.txtSearchDate);
        btnSearchDateChooser =findViewById(R.id.btnSearchDateChooser);
        btnAdvanceSearch =findViewById(R.id.btnAdvanceSearch);

        btnSearchDateChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setListener(new DatePickerFragment.DateChangeListener() {
                    @Override
                    public void onSelected(int year, int month, int day) {
                        selectedDate = new Date();
                        selectedDate.setYear(year - 1900);
                        selectedDate.setMonth(month);
                        selectedDate.setDate(day);
                        String dateStr = new SimpleDateFormat(Constants.DATE_FORMAT).format(selectedDate);
                        txtSearchDate.setText("Date: " + dateStr);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnAdvanceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =txtSearchName.getText().toString();
                String destination =txtSearchDestination.getText().toString();

                boolean result =isValid( name, destination, selectedDate !=null? selectedDate.toString() : null );
                if( result ){
                    Intent intent = new Intent();
                    intent.putExtra(EntryActivity.NAME, name);// .php?name=$name
                    intent.putExtra(EntryActivity.DESTINATION, destination);
                    intent.putExtra(EntryActivity.DATE, selectedDate.getTime());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private boolean isValid(String name, String destination, String date){

        if(name ==null || name.trim().isEmpty() ){
            Toast.makeText(this, "Please enter the name", Toast.LENGTH_SHORT).show();
            txtSearchName.requestFocus();
            return false;
        }

        if( destination ==null || destination.trim().isEmpty() ){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please enter the destination")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
            txtSearchDestination.requestFocus();
            return false;
        }

        if( date ==null || date.trim().isEmpty() ){
            Snackbar snackbar = Snackbar.make( txtSearchDate, "Please choose the date", Snackbar.LENGTH_LONG);
            snackbar.show();
            btnSearchDateChooser.requestFocus();
            return false;
        }

        return true;
    }
}