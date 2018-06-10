package com.example.boris.myandroidapp;

import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Boris on 5/27/2018.
 */

public class ListsUtility {
    public ArrayList<String> getAllExistingCustomers (DataManager dm, Spinner allCustomers, HashMap<Integer, Customer> customersMap) {
        final ArrayList<String> list = new ArrayList<String>();
        Cursor c = dm.getAllExistingCustomersCursor();
        int i = 0;

        while (c.moveToNext()) {
            customersMap.put(i++, new Customer(c.getString(0), c.getString(1), Integer.parseInt(c.getString(2)), Integer.parseInt(c.getString(3))));
            list.add(c.getString(0) + " " + c.getString(1));
        }

        return list;
    }

    public ArrayList<String> getMonthsList(DataManager dm, ListView listMonths, HashMap<Integer, Integer> monthsIdMap) {
        Cursor c = dm.getAllBusinessMonthRecords();

        final ArrayList<String> list = new ArrayList<String>();
        int i = 0;
        while (c.moveToNext()) {
            list.add(c.getString(0) + " " + c.getString(1) + "(" + c.getString(2) + ")");
            monthsIdMap.put(c.getCount()-1-i, Integer.parseInt(c.getString(2)));
            i++;
        }


        Collections.reverse(list);
        //list.add(0, "+ Add New...");

        return list;
    }

    public ArrayList<String> getCustomersList(DataManager dm, ListView listCustomers, int clickedItemIndex) {

        Cursor c = dm.getCustomersForMonth(clickedItemIndex);

        final ArrayList<String> listCustomer = new ArrayList<String>();
        //int i = 0;
        while (c.moveToNext()) {
            listCustomer.add(c.getString(0) + " " + c.getString(1) + "(paid: " + c.getString(2) + ")");
            //customersIdMap.put(c.getCount()-1-i, Integer.parseInt(c.getString(2)));
            //i++;
        }

        return listCustomer;
    }
}
