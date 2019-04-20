package com.project.flights;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.project.R;

/**
 * This class creates a custom dialog which displays the author of the activity and information to help
 * the user oh how to use tha application
 */
public class DialogHelpInformation extends Dialog {

    public DialogHelpInformation(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flights_help_dialog);
    }
}
