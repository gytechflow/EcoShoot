package com.commeduc.dev.ecoshoot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;


public class ClassListActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        File ecoshootingDirectory = DataContainer.ECOSHOOTING_DIRECTORY;
        File institutionDirectory = new File(ecoshootingDirectory, DataContainer.getShootimgInstitution() );

        String [] shootingClasses = institutionDirectory.list(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File current, String name) {

                        File intern = new File(current, name);
                        if( intern.isDirectory() ){
                            return new File(intern, DataContainer.STUDENT_FILE).exists();
                        }

                        return false;
                    }
                }
        );

        listView = (ListView) findViewById(R.id.list1);
        String[] values = {};
        if(shootingClasses != null){
            values = shootingClasses;
        }

        final String [] finalValues = values;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_class_item, R.id.className, finalValues );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedClass = finalValues[position];
                DataContainer.setShootimgClass( selectedClass );

                Intent intent = new Intent(ClassListActivity.this, MyCameraActivity.class);
                startActivity(intent);
            }
        });
        }
}
