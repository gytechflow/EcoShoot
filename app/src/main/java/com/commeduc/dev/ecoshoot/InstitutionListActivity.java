package com.commeduc.dev.ecoshoot;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.getExternalStoragePublicDirectory;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;


public class InstitutionListActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_list);

        File ecoshootingDirectory = DataContainer.ECOSHOOTING_DIRECTORY;
        File[] aDirArray = ContextCompat.getExternalFilesDirs(getApplicationContext(), null);

        ecoshootingDirectory = DataContainer.ECOSHOOTING_DIRECTORY;
        //ecoshootingDirectory = new File(aDirArray[1], Environment.DIRECTORY_DCIM);
        ecoshootingDirectory = new File( getExternalStoragePublicDirectory(DIRECTORY_DCIM), "ecoshooting" );
        ecoshootingDirectory = getExternalStoragePublicDirectory(DIRECTORY_DCIM) ;

        //File ecoshootingDirectory = new File( getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ecoshooting");
        //Log.d("BADLUCK", "XX_"+ecoshootingDirectory.getAbsolutePath() );

        if(!ecoshootingDirectory.exists()){

            Log.d("HOSIRUS", "MUST CREATE in "+ getDCIMCamera() );
            ecoshootingDirectory.mkdir();
            Log.d("HOSIRUS", "CREATED : "+ecoshootingDirectory.exists()  );
        }
        else {
            Log.d("HOSIRUS", "EXISTS ALREADY" );
        }


        Log.d("HOSIRUS", ecoshootingDirectory.getAbsolutePath() );

        String [] shootingGroups = ecoshootingDirectory.list(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File current, String name) {

                        File intern = new File(current, name);

                        Log.d("HOSIRUS", "XX_"+current.getAbsolutePath() );

                        //Log.d("BADLUCK", "XX_"+name );
                        if( intern.isDirectory() ){
                            return true;
                        }

                        return false;
                    }
                }
        );



        if (ecoshootingDirectory.exists()) {
            File[] filee = ecoshootingDirectory.listFiles();
            if (filee != null) {
                for (File file : filee) {
                    Log.d("HOSIRUS", "XX_" + file.getAbsolutePath());
                }
            }
        }



        listView = (ListView) findViewById(R.id.institutionList);
        String [] values = {};
        if(shootingGroups != null){
            values = shootingGroups;
        }

        //Log.d("BADLUCK", "VALUES : "+values.length );

        final String [] finalValues = values;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_institution_item, R.id.institutionName, finalValues );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedInstitution = finalValues[position];
                DataContainer.setShootimgInstitution( selectedInstitution );

                Intent intent = new Intent(InstitutionListActivity.this, ClassListActivity.class);
                startActivity(intent);
            }
        });
        }


    String getDCIMCamera() {
        try {
            String[] projection = new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE};

            Cursor cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
            if (cursor != null) {
                cursor.moveToFirst();
                do {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (path.contains("/DCIM/")) {
                        File file = new File(path);
                        path = file.getParent();
                        cursor.close();
                        return path;
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        catch (Exception e) {
        }
        return "";
    }

}
