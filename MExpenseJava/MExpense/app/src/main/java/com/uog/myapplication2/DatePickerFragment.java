package com.uog.myapplication2;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Date;


public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {

    interface DateChangeListener{
        void onSelected( int year, int month, int day);
    }

    private DateChangeListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Date d = new Date();
        int year = d.getYear() + 1900;
        int month = d.getMonth();
        int day = d.getDate();
       return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        listener.onSelected(year, month, day);
    }

    public void setListener(DateChangeListener listener){
        this.listener =listener;
    }
}
