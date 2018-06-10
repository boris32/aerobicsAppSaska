package com.example.boris.myandroidapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Boris on 6/2/2018.
 */

public class AddNewCustomer extends Dialog{

    private Context context;
    private IsAbleToRefresh refresher;
    private int monthIdToAddCustomerTo;

    //Widgets:
    private Button btnAddSelected;
    private Button btnCreateNew;
    private Spinner spnSelectCustomer;

    //Dialogs
    CreateCustomerDialog createCustomerDialog;

    //Utility:
    DataManager dm;
    HashMap<Integer, Customer> customerMap;
    ListsUtility lu;


    public AddNewCustomer(@NonNull Context context, int monthId, IsAbleToRefresh refresher) {
        super(context);
        this.context = context;
        this.refresher = refresher;
        this.monthIdToAddCustomerTo = monthId;
        dm = new DataManager(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_customer_options);

        btnAddSelected = (Button)findViewById(R.id.buttonAddSelected);
        btnCreateNew = (Button)findViewById(R.id.buttonCreateNewCustomerAcc);
        spnSelectCustomer = (Spinner)findViewById(R.id.spinerPickCustomerToAdd);

        lu = new ListsUtility();
        customerMap = new HashMap<Integer, Customer>();

        //Populate spinner:
        ArrayList<String> list = lu.getAllExistingCustomers(dm, spnSelectCustomer, customerMap);
        final ArrayAdapter adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item, list);
        spnSelectCustomer.setAdapter(adapter);

        if (list.isEmpty()) {
            btnAddSelected.setEnabled(false);
            spnSelectCustomer.setEnabled(false);
        }
        else
            updateAddButtonLabel();


        spnSelectCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateAddButtonLabel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAddSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: fee hardcoded, get a configurable value
                dm.addExistingCustomerToBusinessMonth(monthIdToAddCustomerTo, customerMap.get(spnSelectCustomer.getSelectedItemPosition()).getCustomerId(), 1000);
                dismiss();
                refresher.refresh();
            }
        });

        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCustomerDialog = new CreateCustomerDialog(context, refresher, monthIdToAddCustomerTo, new IsAbleToDismiss() {
                    @Override
                    public void dismissMe() {
                        dismiss();
                    }
                });
                createCustomerDialog.show();
            }
        });

    }

    protected void updateAddButtonLabel () {
        btnAddSelected.setText("Add: " + spnSelectCustomer.getSelectedItem());
    }
}
