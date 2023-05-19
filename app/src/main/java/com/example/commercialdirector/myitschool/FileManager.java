package com.example.commercialdirector.myitschool;


import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.example.commercialdirector.myitschool.fragments.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FileManager extends ListActivity {
    private final List<String> directoryEntries = new ArrayList<String>();
    private File currentDirectory = new File(android.os.Environment.getExternalStorageDirectory().getPath());

    private static final String FILENAME = "File";
    private int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        browseTo(new File(Environment.getExternalStorageDirectory().getPath()));
    }

    private void upOneLevel() {
        if (this.currentDirectory.getParent() != null) {
            this.browseTo(Objects.requireNonNull(this.currentDirectory.getParentFile()));
        }
    }


    private void browseTo(@NonNull final File aDirectory) {

        if (aDirectory.isDirectory()) {
            this.currentDirectory = aDirectory;
            File[] files = aDirectory.listFiles(pathname -> {
                if (pathname.getName().endsWith(".mp3") || pathname.getName().endsWith(".ogg") || pathname.getName().endsWith(".WAV") || pathname.getName().endsWith(".AIFF") || pathname.getName().endsWith(".APE") || pathname.getName().endsWith(".FLAC") || pathname.isDirectory()) {
                    return true;
                } else return false;
            });
            fill(files);
            TextView titleManager = (TextView) findViewById(R.id.titleManager);
            titleManager.setText(aDirectory.getAbsolutePath());
        } else {

            OnClickListener okButtonListener = (arg0, arg1) -> {
                Intent i = new Intent();
                i.putExtra(FILENAME, aDirectory.toString());
                setResult(RESULT_OK, i);
                finish();
            };

            OnClickListener cancelButtonListener = (arg0, arg1) -> {
            };

            new AlertDialog.Builder(this).setTitle("Подтверждение").setMessage("Хотите загрузить выбранный файл " + aDirectory.getName() + "?").setPositiveButton("Да", okButtonListener).setNegativeButton("Нет", cancelButtonListener).show();
        }
    }

    private void fill(File[] files) {

        this.directoryEntries.clear();
        if (this.currentDirectory.getParent() != null) this.directoryEntries.add("..");

        for (File file : files) {
            this.directoryEntries.add(file.getAbsolutePath());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, R.layout.row, this.directoryEntries);
        this.setListAdapter(directoryList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String selectedFileString = this.directoryEntries.get(position);

        if (selectedFileString.equals("..")) {
            this.upOneLevel();
        } else {
            File clickedFile = null;
            clickedFile = new File(selectedFileString);
            this.browseTo(clickedFile);
        }
    }
}
