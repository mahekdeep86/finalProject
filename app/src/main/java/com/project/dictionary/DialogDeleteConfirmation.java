package com.project.dictionary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.project.R;

/**
 * Creates a dialog for user to confirm before deleting a search term
 */
public class DialogDeleteConfirmation  extends Dialog implements
        android.view.View.OnClickListener{

    Button buttonYes;
    Button buttonNo;
    OnDeleteListener onDeleteListener;

    public interface OnDeleteListener{
        void deleteDefinition();
    }

    public DialogDeleteConfirmation(Context context, OnDeleteListener onDeleteListener) {
        super(context);
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_delete);
        buttonYes = findViewById(R.id.btn_yes);
        buttonNo = findViewById(R.id.btn_no);
        buttonYes.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                if (onDeleteListener != null){
                    onDeleteListener.deleteDefinition();
                }
                dismiss();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
