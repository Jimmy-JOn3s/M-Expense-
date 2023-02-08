package com.uog.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.itemNext){
            Toast.makeText(this, "Next menu selected", Toast.LENGTH_SHORT).show();
            return true;
        }else if(item.getItemId() ==R.id.itemExit){
//            finish();
            Toast.makeText(this, "Exit menu selected", Toast.LENGTH_SHORT).show();
            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

}