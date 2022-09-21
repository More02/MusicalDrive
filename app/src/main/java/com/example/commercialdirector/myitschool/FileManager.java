package com.example.commercialdirector.myitschool;


import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.example.commercialdirector.myitschool.fragments.ProfileFragment;

public class FileManager extends ListActivity{
    private List<String> directoryEntries = new ArrayList<String>();
    private File currentDirectory = new File("/sdcard");

    private static final String FILENAME = "File";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.main);

        browseTo(new File("/sdcard"));
    }


    private void upOneLevel(){
        if(this.currentDirectory.getParent() != null) {
            this.browseTo(this.currentDirectory.getParentFile());
        }
    }


    private void browseTo(final File aDirectory){

        if (aDirectory.isDirectory()){

            this.currentDirectory = aDirectory;
            fill(aDirectory.listFiles());


            TextView titleManager = (TextView) findViewById(R.id.titleManager);
            titleManager.setText(aDirectory.getAbsolutePath());
        } else {

            OnClickListener okButtonListener = new OnClickListener(){
                public void onClick(DialogInterface arg0, int arg1) {

//                    Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("file://" + aDirectory.getAbsolutePath()));
//
//                    startActivity(i);
                    Intent i = new Intent();
                    i.putExtra(FILENAME,aDirectory.toString());
                    setResult(RESULT_OK, i);
                    finish();
                }
            };

            OnClickListener cancelButtonListener = new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            };


            new AlertDialog.Builder(this)
                    .setTitle("Подтверждение")
                    .setMessage("Хотите загрузить выбранный файл "+ aDirectory.getName() + "?")
                    .setPositiveButton("Да", okButtonListener)
                    .setNegativeButton("Нет", cancelButtonListener)
                    .show();
        }
    }

    private void fill(File[] files) {

        this.directoryEntries.clear();

        if (this.currentDirectory.getParent() != null)
            this.directoryEntries.add("..");


        for (File file : files) {
            this.directoryEntries.add(file.getAbsolutePath());
        }


        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, R.layout.row, this.directoryEntries);
        this.setListAdapter(directoryList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        int selectionRowID = position;
        String selectedFileString = this.directoryEntries.get(selectionRowID);


        if(selectedFileString.equals("..")){
            this.upOneLevel();
        } else {

            File clickedFile = null;
            clickedFile = new File(selectedFileString);
            if (clickedFile != null) {
                this.browseTo(clickedFile);

            }
        }
    }
}
