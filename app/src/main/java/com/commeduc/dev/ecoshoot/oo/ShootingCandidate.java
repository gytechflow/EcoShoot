package com.commeduc.dev.ecoshoot.oo;

/**
 * Created by LDN on 9/5/17.
 */

import com.commeduc.dev.ecoshoot.DataContainer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ShootingCandidate {

    private String firstname, lastname, birthdate, bitrhplace, gender, schoolClass, schoolMatricule, matricule,schoolYear;

    public ShootingCandidate() {
        this.firstname = "";
        this.lastname = "";
        this.birthdate = "";
        this.bitrhplace = "";
        this.gender = "";
        this.schoolClass = "";
        this.schoolMatricule = "";
        this.matricule = "";
        this.schoolYear = "";
    }

    public ShootingCandidate( String [] values) throws IncompleteDataException {
        if(values.length >= 9 ){
            this.firstname = values[0];
            this.lastname = values[1];
            this.birthdate = values[2];
            this.bitrhplace = values[3];
            this.gender = values[4];
            this.schoolClass = values[5];
            this.schoolMatricule = values[6];
            this.matricule = values[7];
            this.schoolYear = values[8];

            if(values.length == 10 ){

            }

            if(values.length == 10 ){

            }

        }
        else {
            throw new IncompleteDataException("Some data are missing");
        }
    }

    public ShootingCandidate(String firstname, String lastname, String schoolClass, String schoolMatricule) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.schoolClass = schoolClass;
        this.schoolMatricule = schoolMatricule;
    }

    public ShootingCandidate(String firstname, String lastname, String birthdate,
                             String bitrhplace, String gender, String schoolClass, String schoolMatricule,
                             String matricule, String schoolYear) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.bitrhplace = bitrhplace;
        this.gender = gender;
        this.schoolClass = schoolClass;
        this.schoolMatricule = schoolMatricule;
        this.matricule = matricule;
        this.schoolYear = schoolYear;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBitrhplace() {
        return bitrhplace;
    }

    public void setBitrhplace(String bitrhplace) {
        this.bitrhplace = bitrhplace;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public String getSchoolMatricule() {
        return schoolMatricule;
    }

    public void setSchoolMatricule(String schoolMatricule) {
        this.schoolMatricule = schoolMatricule;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null ){
            return false;
        }
        ShootingCandidate toCompare = (ShootingCandidate)obj;
        if(getMatricule().equalsIgnoreCase(toCompare.getMatricule())){
            return true;
        }
        return false;
    }

    public boolean isComplete() {
        return DataContainer.getStorageFileNameFor(this).exists();
    }

    @Override
    public String toString() {
        return "ShootingCandidate{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", bitrhplace='" + bitrhplace + '\'' +
                ", gender='" + gender + '\'' +
                ", schoolClass='" + schoolClass + '\'' +
                ", schoolMatricule='" + schoolMatricule + '\'' +
                ", matricule='" + matricule + '\'' +
                '}';
    }

    public String toDsiplayString() {
        return   firstname +" , " + lastname +
                "\n" + birthdate +" >>> " + schoolMatricule;
    }
}
