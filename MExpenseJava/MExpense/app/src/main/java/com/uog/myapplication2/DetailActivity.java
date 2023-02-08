package com.uog.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uog.myapplication2.database.DatabaseHelper;
import com.uog.myapplication2.database.Trip;
import com.uog.myapplication2.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtDestination;
    private TextView txtDate;
    private TextView txtRiskAssessment;
    private TextView txtDescription;

    String name;
    String destination;
    long date;
    String riskAssessment;
    String description;

    private String value1;
    private String value2;
    private String value3;
    private Double num1;
    private Double num2;
    private Integer id =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtName =findViewById(R.id.txtName);
        txtDestination =findViewById(R.id.txtDestination);
        txtDate =findViewById(R.id.txtDate);
        txtRiskAssessment =findViewById(R.id.txtRiskAssessment);
        txtDescription =findViewById(R.id.txtDescription);

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

        Button btnBack =findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnSave =findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper database =new DatabaseHelper(getBaseContext());
                long result =0;
                if( id ==0) {
                    result = database.saveTrip(name, destination, date, riskAssessment.equalsIgnoreCase("Yes") ? 1 : 0, description,
                            null, null, null, null, null);
                }else{
                    Trip trip =new Trip(
                            id,
                            name,
                            destination,
                            date,
                            riskAssessment.equalsIgnoreCase("Yes"),
                            description,
                            value1,
                            value2,
                            value3,
                            num1 ==0? null : num1,
                            num2 ==0? null : num2
                    );
                    result =database.updateTrip(trip);
                }
                database.close();
                if( result >0 ){
                    Toast.makeText(getBaseContext(), "Trip information has been saved!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}