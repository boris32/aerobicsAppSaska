package com.example.boris.myandroidapp;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ErrorDialog extends Dialog {

    private Context context;

    //Widgets:
    private TextView tv_ErrorMessage;
    private Button btn_Ok;


    public ErrorDialog(@NonNull Context context, String errorMessage) {
        super(context);
        this.context = context;
        String message = errorMessage;
        setContentView(R.layout.error_dialog);
        setCanceledOnTouchOutside(false);

        tv_ErrorMessage = findViewById(R.id.errorDialogErrorMessage);
        tv_ErrorMessage.setText(errorMessage);
        btn_Ok = findViewById(R.id.errorDialogOkButton);
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }






}
