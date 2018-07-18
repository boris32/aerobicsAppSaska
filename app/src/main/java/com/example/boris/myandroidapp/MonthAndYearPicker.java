package com.example.boris.myandroidapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Boris on 5/27/2018.
 */

public class MonthAndYearPicker extends Dialog {

    Context context;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Button btnOk;
    private Button btnCancel;

    private String currentlySelectedMonth;
    private String currentlySelectedYear;

    private DataManager dm;
    private IsAbleToRefresh refresher;

    public MonthAndYearPicker(@NonNull Context context, IsAbleToRefresh refresher) {
        super(context);
        this.context = context;
        this.refresher = refresher;
        dm = new DataManager(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month_year_picker);

        monthSpinner = (Spinner)findViewById(R.id.spinnerMonth);
        yearSpinner = (Spinner)findViewById(R.id.spinnerYear);
        btnOk = (Button)findViewById(R.id.buttonMonthDialogOk);
        btnCancel = (Button)findViewById(R.id.buttonMonthDialogCancel);

        //ArrayList arraySpinner = new ArrayList();

        String[] arraySpinner = new String[] {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };

        //"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"

        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        //adapter.setDropDownViewResource(android.R.layout.simple_spin‌​ner_dropdown_item);
        monthSpinner.setAdapter(adapterMonth);


        /*String[] arraySpinnerYear = new String[] {
                "2018", "2019", "2020", "2021", "2022"
        };*/
        String[] arraySpinnerYear = new String[3];
        for (int i = 0; i < 3; i++)
            arraySpinnerYear[i] = Integer.toString(Calendar.getInstance().get(Calendar.YEAR) + i);

        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, arraySpinnerYear);
        //adapter.setDropDownViewResource(android.R.layout.simple_spin‌​ner_dropdown_item);
        yearSpinner.setAdapter(adapterYear);

        currentlySelectedMonth=monthSpinner.getSelectedItem().toString();
        currentlySelectedYear=yearSpinner.getSelectedItem().toString();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dm.addNewMonthEntry(getCurrentlySelectedMonth(), getCurrentlySelectedYear());
                dismiss();
                refresher.refresh();
            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentlySelectedMonth=monthSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentlySelectedYear=yearSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private String getCurrentlySelectedMonth () {
        return currentlySelectedMonth;
    }
    private String getCurrentlySelectedYear () {
        return currentlySelectedYear;
    }

}
