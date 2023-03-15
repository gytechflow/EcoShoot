package com.commeduc.dev.ecoshoot;

import android.support.annotation.NonNull;

import com.commeduc.dev.ecoshoot.oo.ShootingCandidate;

import java.util.List;

/**
 * Created by JPurple on 14/12/2022
 *
 * Defines behaviour for classes that will provide list of students or schools
 */
public interface ListProvider {
    /**
     *  to get the list of students from a specific school
     * @param schoolName name of school to get students from
     * @return list of {@link ShootingCandidate}s
     */
    List<ShootingCandidate> getStudentsList(@NonNull String schoolName);

    /**
     * to get get list of schools
     * @return list of schools
     */
    List<String> getSchoolsList();
}
