package com.project.dictionary;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.R;

import java.util.ArrayList;

public class AdapterDefinitions extends BaseAdapter {
    ArrayList<Definition> items;
    Context context;
    public AdapterDefinitions(Context context){
        this.context = context;
        this.items = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItems(ArrayList<Definition> definitions) {
        this.items = definitions;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // if convertView is null inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.cell_definition, parent, false);
        }
        // get current news item to be displayed
        final Definition currentDefinition = (Definition) getItem(position);

        //initialize text views to display the definitions
        final TextView headWordTextView = convertView.findViewById(R.id.headWordTextView);
        final TextView pronunciationTextView = convertView.findViewById(R.id.pronunciationTextView);
        final TextView functionalLabelTextView = convertView.findViewById(R.id.functionalLabelTextView);
        final TextView definitionTextView = convertView.findViewById(R.id.definitionTextView);
        final Button saveButton = convertView.findViewById(R.id.saveButton);
        final Button deleteButton = convertView.findViewById(R.id.deleteButton);

        //display the full time of the current item
        headWordTextView.setText(currentDefinition.getHw());
        if (currentDefinition.getPr() != null && !currentDefinition.getPr().equalsIgnoreCase("")){
            pronunciationTextView.setVisibility(View.VISIBLE);
        }
        if (currentDefinition.isOnline()){
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
        }else {
            saveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDefinition(currentDefinition);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDefinition(currentDefinition.getId(), position);
            }
        });

        pronunciationTextView.setText(currentDefinition.getPr());
        functionalLabelTextView.setText(currentDefinition.getFl());
        definitionTextView.setText(currentDefinition.getDef());


        // returns the view for the current row
        return convertView;
    }

    /**
     * Delete definition of a given search term
     * @param currentDefinitionId
     * @param position
     */
    private void deleteDefinition(final int currentDefinitionId, final int position) {
        DialogDeleteConfirmation dialogDeleteConfirmation = new DialogDeleteConfirmation(context, new DialogDeleteConfirmation.OnDeleteListener() {
            @Override
            public void deleteDefinition() {
                String selection = DefinitionContract.DefinitionEntry.COLUMN_ID + " = ?";
                //specify the id of the item to be deleted
                String[] selectionArgs = { currentDefinitionId + ""};
                // Execute the delete statement
                int deletedRows = new SavedDefinitionsDatabaseHelper(context).getReadableDatabase().delete(DefinitionContract.DefinitionEntry.TABLE_NAME, selection, selectionArgs);
                items.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "The definition has been deleted", Toast.LENGTH_LONG).show();
            }
        });
        dialogDeleteConfirmation.show();

    }

    /**
     * Saves the a single definition of a given search term
     * @param currentDefinition
     */
    private void saveDefinition(Definition currentDefinition) {
        //get the database
        SQLiteDatabase database = new SavedDefinitionsDatabaseHelper(context).getWritableDatabase();
        //create a ContentValues to hold the values
        ContentValues values = new ContentValues();
        values.put(DefinitionContract.DefinitionEntry.COLUMN_HEAD_WORD, currentDefinition.getHw());
        values.put(DefinitionContract.DefinitionEntry.COLUMN_PRONUNCIATION, currentDefinition.getPr());
        values.put(DefinitionContract.DefinitionEntry.COLUMN_FUNCTIONAL, currentDefinition.getFl());
        values.put(DefinitionContract.DefinitionEntry.COLUMN_DEFINITIONS, currentDefinition.getDef());

        //insert the article record to the database
        long newRowId = database.insert(DefinitionContract.DefinitionEntry.TABLE_NAME, null, values);
        Toast.makeText(context, "The definition has been saved", Toast.LENGTH_LONG).show();
    }
}
