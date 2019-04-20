package com.project.dictionary;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.project.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedDefinitionsFragment extends Fragment {
    ListView listViewItems;
    AdapterDefinitions adapterDefinitions;

    public SavedDefinitionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offline_definitions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewItems = view.findViewById(R.id.listViewArticles);
        adapterDefinitions = new AdapterDefinitions(getActivity());
        listViewItems.setAdapter(adapterDefinitions);
        readOfflineDefinitions();
    }

    /**
     * This function reads definition from local sqlite database
     */
    private void readOfflineDefinitions() {
        SavedDefinitionsDatabaseHelper databaseHelper = new SavedDefinitionsDatabaseHelper(getActivity());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(DefinitionContract.DefinitionEntry.TABLE_NAME,null,null,null,null,null,null);

        ArrayList<Definition> definitionsList = new ArrayList<>();
        while(cursor.moveToNext()) {
            int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(DefinitionContract.DefinitionEntry.COLUMN_ID));
            String headWord = cursor.getString(cursor.getColumnIndexOrThrow(DefinitionContract.DefinitionEntry.COLUMN_HEAD_WORD));
            String pronunciation = cursor.getString(cursor.getColumnIndexOrThrow(DefinitionContract.DefinitionEntry.COLUMN_PRONUNCIATION));
            String functional = cursor.getString(cursor.getColumnIndexOrThrow(DefinitionContract.DefinitionEntry.COLUMN_FUNCTIONAL));
            String definitions = cursor.getString(cursor.getColumnIndexOrThrow(DefinitionContract.DefinitionEntry.COLUMN_DEFINITIONS));
            Definition definition = new Definition(itemId, headWord, pronunciation, functional, definitions);
            definitionsList.add(definition);
        }
        cursor.close();
        adapterDefinitions.addItems(definitionsList);
        adapterDefinitions.notifyDataSetChanged();
    }
}
