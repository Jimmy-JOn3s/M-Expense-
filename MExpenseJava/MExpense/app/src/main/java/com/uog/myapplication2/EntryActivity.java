package com.uog.myapplication2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.uog.myapplication2.database.Trip;
import com.uog.myapplication2.util.Constants;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EntryActivity extends AppCompatActivity {


    public static final String NAME = "name";
    public static final String DESTINATION = "destination";
    public static final String DATE = "date";
    public static final String RISK_ASSESSMENT = "riskAssessment";
    public static final String DESCRIPTION = "description";

    public static final String ID = "id";
    public static final String VALUE1 ="value1";
    public static final String VALUE2 ="value2";
    public static final String VALUE3 ="value3";
    public static final String NUM1 ="num1";
    public static final String NUM2 ="num2";

    private EditText txtName;
    private EditText txtDestination;
    private TextView txtDate;
    private Button btnDateChooser;
    private RadioButton rbtnYes;
    private RadioButton rbtnNo;
    private EditText txtDescription;
    private Button btnNext;
    Date selectedDate;
    Trip trip =null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName =findViewById(R.id.txtName);
        txtDestination =findViewById(R.id.txtDestination);
        txtDate =findViewById(R.id.txtDate);
        btnDateChooser =findViewById(R.id.btnDateChooser);
        rbtnYes =findViewById(R.id.rbtnYes);
        rbtnNo =findViewById(R.id.rbtnNo);
        txtDescription =findViewById(R.id.txtDescription);
        btnNext =findViewById(R.id.btnNext);

        btnDateChooser.setOnClickListener(new View.OnClickListener() {
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
                        txtDate.setText("Date: " + dateStr);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =txtName.getText().toString();
                String destination =txtDestination.getText().toString();
                //String date = selectedDate;
                String riskAssessment = rbtnYes.isChecked()? "Yes" : "No";
                String description =txtDescription.getText().toString();
                boolean result =isValid( name, destination, selectedDate !=null? selectedDate.toString() : null );
                if( result ){
                    Intent intent = new Intent( getApplicationContext(), DetailActivity.class );
                    intent.putExtra(EntryActivity.NAME, name);
                    intent.putExtra(EntryActivity.DESTINATION, destination);
                    intent.putExtra(EntryActivity.DATE, selectedDate.getTime());
                    intent.putExtra(EntryActivity.RISK_ASSESSMENT, riskAssessment);
                    intent.putExtra(EntryActivity.DESCRIPTION, description);

                    // for updating
                    intent.putExtra(EntryActivity.ID, trip !=null? trip.getId() : 0);
                    intent.putExtra(EntryActivity.VALUE1, trip !=null? trip.getValue1() : "");
                    intent.putExtra(EntryActivity.VALUE2, trip !=null? trip.getValue2() : "");
                    intent.putExtra(EntryActivity.VALUE3, trip !=null? trip.getValue3() : "");
                    intent.putExtra(EntryActivity.NUM1, trip !=null? trip.getNum1() : 0);
                    intent.putExtra(EntryActivity.NUM2, trip !=null? trip.getNum2() : 0);
                    startActivity(intent);
                }
            }
        });
        checkUpdate();
    }

    private boolean isValid(String name, String destination, String date){

        if(name ==null || name.trim().isEmpty() ){
            Toast.makeText(this, "Please enter the name", Toast.LENGTH_SHORT).show();
            txtName.requestFocus();
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
            txtDestination.requestFocus();
            return false;
        }

        if( date ==null || date.trim().isEmpty() ){
            Snackbar snackbar = Snackbar.make( txtDate, "Please choose the date", Snackbar.LENGTH_LONG);
            snackbar.show();
            btnDateChooser.requestFocus();
            return false;
        }

        return true;
    }

    private void checkUpdate(){
        Bundle bundle = getIntent().getExtras();
        if( bundle !=null ){
            trip =new Trip(
                    bundle.getInt(EntryActivity.ID),
                    bundle.getString(EntryActivity.NAME),
                    bundle.getString(EntryActivity.DESTINATION),
                    bundle.getLong(EntryActivity.DATE),
                    bundle.getBoolean(EntryActivity.RISK_ASSESSMENT),
                    bundle.getString(EntryActivity.DESCRIPTION),

                    bundle.getString(EntryActivity.VALUE1),
                    bundle.getString(EntryActivity.VALUE2),
                    bundle.getString(EntryActivity.VALUE3),
                    bundle.getDouble(EntryActivity.NUM1, 0),
                    bundle.getDouble(EntryActivity.NUM2, 0)
            );

            txtName.setText(trip.getName());
            txtDestination.setText(trip.getDestination());
            if( trip.isRiskAssessment() )
                rbtnYes.setChecked(true);
            else
                rbtnNo.setChecked(true);

            txtDescription.setText(trip.getDescription());
            selectedDate =new Date( trip.getDate() );
            String dateStr = new SimpleDateFormat(Constants.DATE_FORMAT).format(selectedDate);
            txtDate.setText("Date: " + dateStr);
        }
    }
}