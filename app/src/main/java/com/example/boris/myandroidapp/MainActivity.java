package com.example.boris.myandroidapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Context context = MainActivity.this;
    protected ListView listMonths;
    protected ListView listCustomers;
    private ListsUtility listUtil = new ListsUtility();
    private Button addNewMonth;
    private MonthAndYearPicker monthPicker;
    private AddNewCustomer addNewCustomerDialog;

    //Values
    public int currentlyDisplayedMonthId;

    //Widgets on Add Month Popup:
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Button btnOk;
    private Button btnCancel;
    private Button btnAddCustomer;

    //Caches
    //Loaded at screen creation
    //TODO: update upon month list changed (such as month added)
    HashMap<Integer, Integer> monthsIdMap = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> customerIdMap = new HashMap<Integer, Integer>();

    DataManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dm = new DataManager(this);
        addNewMonth = (Button)findViewById(R.id.addNewMonth);

        btnAddCustomer = (Button)findViewById(R.id.buttonAddNewCustomer);

        //MONTHS LIST:
        listMonths = (ListView) findViewById(R.id.MonthsList);
        populateMonthsList();

        //TODO: Get rid of this workaround (the if part)
        if (listMonths.getCount() > 1)
            currentlyDisplayedMonthId = monthsIdMap.get(0);

        //CUSTOMERS LIST:
        listCustomers = (ListView) findViewById(R.id.CustomerList);
        populateCustomersList(currentlyDisplayedMonthId);//(monthsIdMap.get(0));

        //Clicking the months list:
        listMonths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentlyDisplayedMonthId = monthsIdMap.get(i);
                populateCustomersList(currentlyDisplayedMonthId);
            }
        });

        //Add New Month:
        addNewMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthPicker = new MonthAndYearPicker(MainActivity.this, new IsAbleToRefresh() {
                    @Override
                    public void refresh() {
                        populateMonthsList();
                        currentlyDisplayedMonthId = monthsIdMap.get(0);
                        populateCustomersList(currentlyDisplayedMonthId);


                        //populateMonthsList();
                        //populateCustomersList(currentlyDisplayedMonthId);
                    }
                });
                monthPicker.show();
            }
        });

        //Add New Customer
        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCustomerDialog = new AddNewCustomer(context, currentlyDisplayedMonthId, new IsAbleToRefresh() {
                    @Override
                    public void refresh() {
                        //populateMonthsList();
                        populateCustomersList(currentlyDisplayedMonthId);
                    }
                });
                addNewCustomerDialog.show();
            }
        });


    }


    public void populateMonthsList() {
        ArrayList<String> list = listUtil.getMonthsList(dm, listMonths, monthsIdMap);

        //TODO: get rid of this workaround
        if (list.isEmpty())
            list.add("...");
        if (list.size() > 1) {
            list.remove("...");
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listMonths.setAdapter(adapter);
    }

    public void populateCustomersList(int index) {
        ArrayList<String> listCustomer = listUtil.getCustomersList(dm, listCustomers, index);
        final ArrayAdapter customerAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listCustomer);
        listCustomers.setAdapter(customerAdapter);
    }


}
