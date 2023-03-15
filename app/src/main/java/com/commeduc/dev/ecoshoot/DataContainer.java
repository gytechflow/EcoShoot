package com.commeduc.dev.ecoshoot;

import android.app.Application;
import android.inputmethodservice.Keyboard;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.commeduc.dev.ecoshoot.oo.IncompleteDataException;
import com.commeduc.dev.ecoshoot.oo.ShootingCandidate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fondation on 9/7/17.
 */

public class DataContainer {

    //public static final File ECOSHOOTING_DIRECTORY = new File(Environment.getExternalStorageDirectory(),"ecoshooting");
    //public static final File ECOSHOOTING_DIRECTORY = new File( getContext().getFilesDir(), "ecoshooting");
    public static final File ECOSHOOTING_DIRECTORY = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"ecoshooting");

    //public static final File SHOOTING_DIRECTORYX = new File( ECOSHOOTING_DIRECTORY,"shooting");

    //public static final File CONFIG_FILE = new File(ECOSHOOTING_DIRECTORY,"config.txt");

    public static final String STUDENT_FILE = "students.csv";
    private static final java.lang.String ELEMENT_SEPARATOR = ",";


    private static String SHOOTIMG_INSTITUTION = "";
    private static String SHOOTIMG_CLASS = "";

    public static String getShootimgInstitution() {
        return SHOOTIMG_INSTITUTION;
    }

    public static void setShootimgInstitution(String shootimgInstitution) {
        SHOOTIMG_INSTITUTION = shootimgInstitution;
    }

    public static void setShootimgClass(String shootimgClass) {
        SHOOTIMG_CLASS = shootimgClass;
    }

    public static String getShootimgClass() {
        return SHOOTIMG_CLASS;
    }



    public static ShootingCandidate getFirstCandidateOfClass( String selectedClass, boolean complete) {

        File institutionDirectory = new File(DataContainer.ECOSHOOTING_DIRECTORY, DataContainer.getShootimgInstitution() );
        File shootingClassFolder = new File(institutionDirectory, selectedClass );
        List<ShootingCandidate> candidates = getCandidatesFromFile(selectedClass);

        Log.d("THE_KING", "La Classe : "+ shootingClassFolder.getAbsolutePath() );
        Log.d("THE_KING", "Nombre de candidats : "+ candidates.size() );

        for(ShootingCandidate actual : candidates ){
            if( ! actual.isComplete() ){
                return actual;
            }
        }

        // Else return the first one.
        if(complete){
            return candidates.get(0);
        }

        return null;
    }

    public static int getFirstCandidateIndexOfClass( String selectedClass) {

        File institutionDirectory = new File(DataContainer.ECOSHOOTING_DIRECTORY, DataContainer.getShootimgInstitution() );
        File shootingClassFolder = new File(institutionDirectory, selectedClass );
        List<ShootingCandidate> candidates = getCandidatesFromFile(selectedClass);

        for(ShootingCandidate actual : candidates ){
            File photo = new File(shootingClassFolder, actual.getSchoolMatricule()+".png");
            if( ! photo.exists()){
                return candidates.indexOf(actual);
            }
        }

        return 0;
    }

    /**
     *
     * @param selectedClass
     * @return
     */
    public static List<ShootingCandidate> getCandidatesFromFile( String selectedClass) {

        File institutionDirectory = new File(DataContainer.ECOSHOOTING_DIRECTORY, DataContainer.getShootimgInstitution() );
        File shootingClassFolder = new File(institutionDirectory, selectedClass );
        File studentClassFile = new File(shootingClassFolder, STUDENT_FILE);

        List<ShootingCandidate> candidates = new ArrayList<ShootingCandidate>();
        FileInputStream fis = null;

        try {

            fis = new FileInputStream(studentClassFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] RowData = line.split(ELEMENT_SEPARATOR);

                try {

                    Log.d("THE_KING", "ROWDATA : "+ RowData );
                    Log.d("THE_KING", "ROWDATA LENGTH : "+ RowData.length );

                    ShootingCandidate sCandidate = new ShootingCandidate(RowData);

                    candidates.add(sCandidate);

                } catch (IncompleteDataException e) {
                    Log.e("ecoShoot", "Exception reading "+ selectedClass );
                }

            }

        } catch (FileNotFoundException e) {
            Log.d( "COMMESSAGE", "ERROR : "+e.getMessage() );
        } catch (IOException ex) {
            Log.d( "COMMESSAGE", "ERROR : "+ex.getMessage() );
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Log.d( "COMMESSAGE", "ERROR : "+ex.getMessage() );
            }
        }

        return candidates;

    }


    public static ShootingCandidate getPreviousCandidateOfClass( ShootingCandidate current) {

        File institutionDirectory = new File(DataContainer.ECOSHOOTING_DIRECTORY, DataContainer.getShootimgInstitution() );
        File shootingClassFolder = new File(institutionDirectory, getShootimgClass() );
        List<ShootingCandidate> candidates = getCandidatesFromFile(getShootimgClass());

        Log.d("THE_KING", "La Classe : "+ shootingClassFolder.getAbsolutePath() );
        Log.d("THE_KING", "Nombre de candidats : "+ candidates.size() );

        for(ShootingCandidate actual : candidates ){

            if(actual.equals(current) ){

                int index = candidates.indexOf(actual);
                if( index == 0 ){
                    return candidates.get( candidates.size()-1 );
                }
                else {
                    return candidates.get( index - 1 );
                }

                /*
                File photo = DataContainer.getStorageFileNameFor(current);
                if( ! photo.exists()){
                    return actual;
                }
                else {
                    int index = candidates.indexOf(actual);
                    if( index == 0 ){
                        return candidates.get( candidates.size()-1 );
                    }
                    else {
                        return candidates.get( index - 1 );
                    }
                }
                */
            }
        }

        return null;
    }

    /**
     *
     * @param current
     * @return
     */
    public static ShootingCandidate getNextCandidateOfClass( ShootingCandidate current) {

        File institutionDirectory = new File(DataContainer.ECOSHOOTING_DIRECTORY, DataContainer.getShootimgInstitution() );
        File shootingClassFolder = new File(institutionDirectory, getShootimgClass() );
        List<ShootingCandidate> candidates = getCandidatesFromFile(getShootimgClass());

        Log.d("THE_KING", "La Classe : "+ shootingClassFolder.getAbsolutePath() );
        Log.d("THE_KING", "Nombre de candidats : "+ candidates.size() );

        for(ShootingCandidate actual : candidates ){

            if(actual.equals(current) ){

                int index = candidates.indexOf(actual);
                if( index == candidates.size()-1 ){
                    return candidates.get( 0 );
                }
                else {
                    return candidates.get( index + 1 );
                }

                /*
                File photo = DataContainer.getStorageFileNameFor(current);
                if( ! photo.exists()){
                    return actual;
                }
                else {
                    int index = candidates.indexOf(actual);
                    if( index == candidates.size() -1 ){
                        return candidates.get( 0 );
                    }
                    else {
                        return candidates.get( index +1 );
                    }
                }
                */
            }
        }

        return null;
    }


    /**
     *
     * @param selectedClass
     * @return
     */
    public static boolean isClassComplete( String selectedClass) {

        List<ShootingCandidate> candidates = getCandidatesFromFile(selectedClass);
        for(ShootingCandidate candidate : candidates){
            if(!candidate.isComplete()){
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the folder in which the picture should be stored for a specified ShootingCandidate
     * @param current
     * @return
     */
    public static File getStorageFileNameFor( ShootingCandidate current) {

        File institutionDirectory = new File(DataContainer.ECOSHOOTING_DIRECTORY, DataContainer.getShootimgInstitution());
        File shootingClassFolder = new File(institutionDirectory, getShootimgClass());

        return new File(shootingClassFolder, current.getSchoolMatricule()+".jpg");

    }


}
