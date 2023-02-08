package com.uog.myapplication2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.uog.myapplication2.database.DatabaseHelper;
import com.uog.myapplication2.database.Trip;
import com.uog.myapplication2.util.Constants;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ExpenseEntryActivity extends AppCompatActivity {

    private EditText txtExpenseType;
    private EditText txtExpenseAmount;
    private TextView txtExpenseDate;
    private Button btnExpenseDateChooser;
    private EditText txtExpenseComment;
    private Button btnExpenseSave;
    private Date selectedDate;
    private Integer tripId =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtExpenseType =findViewById(R.id.txtExpenseType);
        txtExpenseAmount =findViewById(R.id.txtExpenseAmount);
        txtExpenseDate =findViewById(R.id.txtExpenseDate);
        btnExpenseDateChooser =findViewById(R.id.btnExpenseDateChooser);
        txtExpenseComment =findViewById(R.id.txtExpenseComment);
        btnExpenseSave =findViewById(R.id.btnExpenseSave);

        btnExpenseDateChooser.setOnClickListener(new View.OnClickListener() {
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
                        txtExpenseDate.setText("Date: " + dateStr);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnExpenseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type =txtExpenseType.getText().toString();
                String amount =txtExpenseAmount.getText().toString();
                String comment =txtExpenseComment.getText().toString();
                boolean result =isValid( type, amount, selectedDate !=null? selectedDate.toString() : null );
                if( result ){
                    DatabaseHelper database =new DatabaseHelper(getBaseContext());
                    long dbResult =0;
                    dbResult =database.saveExpense(tripId, type,Double.parseDouble(amount), selectedDate.getTime(), comment,
                            "", "","");

                    database.close();
                    if( dbResult >0 ){
                        Toast.makeText(getBaseContext(), "Expense has been saved!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if( bundle !=null ){
            tripId =bundle.getInt(EntryActivity.ID, 0);
        }

        if( tripId ==0){
            Toast.makeText(getBaseContext(), "Please select the trip", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean isValid(String type, String amount, String date){

        if( type ==null || type.trim().isEmpty() ){
            Toast.makeText(this, "Please enter the expense type", Toast.LENGTH_SHORT).show();
            txtExpenseType.requestFocus();
            return false;
        }

        if( amount ==null || amount.trim().isEmpty() ){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please enter the amount")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
            txtExpenseAmount.requestFocus();
            return false;
        }

        if( date ==null || date.trim().isEmpty() ){
            Snackbar snackbar = Snackbar.make( txtExpenseDate, "Please choose the date", Snackbar.LENGTH_LONG);
            snackbar.show();
            btnExpenseDateChooser.requestFocus();
            return false;
        }

        return true;
    }
}