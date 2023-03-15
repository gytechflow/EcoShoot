package com.commeduc.dev.ecoshoot;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.commeduc.dev.ecoshoot.oo.ShootingCandidate;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *  Created by JPurple on 14/12/2022
 *
 * A {@link ListProvider} that gets the various lists from teh local storage from the {@link DataContainer#ECOSHOOTING_DIRECTORY}
 */
public class StorageUtil implements ListProvider {

    /**
     *  to get location to the {@link DataContainer#ECOSHOOTING_DIRECTORY}
     * @return location where the concerned files will be found
     */
    private File getStorageLocation(){
        File storageDir;
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            // For KitKat and higher versions, use the external storage directory
            storageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM),
                    "ecoshooting");
            System.out.println("dossier trouvé");
        } else {
            // For older versions, use the external cache directory
            storageDir = new File(Environment.getExternalStorageDirectory() +
                            File.separator + "ecoshooting");
            System.out.println("dossier trouvé");

        }
        if (!storageDir.exists()) {
            // Create the directory if it doesn't exist
            storageDir.mkdirs();
            System.out.println("dossier crée");

        }
        return storageDir;
    }


    @Override
    public List<ShootingCandidate> getStudentsList(@NonNull String schoolName) {
        File location= getStorageLocation();

        List<ShootingCandidate> candidates = new ArrayList<>();
        //todo get students' list
        return candidates;
    }

    @Override
    public List<String> getSchoolsList() {

        // Obtenir l'emplacement du dossier "ecoshooting"
        File location = getStorageLocation();

        // Obtenir la liste des dossiers contenus dans "ecoshooting"
        File[] directories = location.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });

        // Convertir la liste de dossiers en une liste de noms de dossiers
        List<String> schoolsList = new ArrayList<>();
        for (File directory : directories) {
            schoolsList.add(directory.getName());
        }

        // Retourner la liste des dossiers
        return schoolsList;

        /* File location= getStorageLocation();

        //todo get and return schools list
        return null; */
    }
}
