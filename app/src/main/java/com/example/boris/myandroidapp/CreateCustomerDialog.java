package com.example.boris.myandroidapp;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Boris on 6/4/2018.
 */

public class CreateCustomerDialog extends Dialog {


    Context context;
    IsAbleToRefresh refresher;
    IsAbleToDismiss dismissable;
    int monthId;

    //Widgets:
    EditText fname;
    EditText lname;
    EditText fee;
    Button btnCreate;
    Button btnCancel;

    //Utility:
    DataManager dm;



    public CreateCustomerDialog(@NonNull Context context, final IsAbleToRefresh refresher, final int monthId, final IsAbleToDismiss dismissable) {
        super(context);
        this.context = context;
        setContentView(R.layout.create_new_customer);
        this.refresher = refresher;
        dm = new DataManager(context);
        this.monthId = monthId;
        this.dismissable = dismissable;

        //TODO: Add validation to the edit fields
        fname = (EditText) findViewById(R.id.editFirstName);
        lname = (EditText) findViewById(R.id.editLastName);
        fee = (EditText) findViewById(R.id.editUsualFee);
        btnCreate = (Button)findViewById(R.id.btnCreateCustomer);
        btnCancel = (Button)findViewById(R.id.btnCancelCustomer);

        //Listeners:
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dm.createNewCustomer(fname.getText().toString().trim(), lname.getText().toString().trim(), fee.getText().toString().trim());
                int cid = Integer.parseInt(dm.getLatestCreatedCustomer());
                //TODO: Fee hard-coded, needs to be a configurable value
                dm.addExistingCustomerToBusinessMonth(monthId, cid, 1000);
                dismissable.dismissMe();
                refresher.refresh();
                dismiss();
            }
        });
    }


}
