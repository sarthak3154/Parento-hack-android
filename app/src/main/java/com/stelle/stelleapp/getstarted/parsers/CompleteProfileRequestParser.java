package com.stelle.stelleapp.getstarted.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 22-04-2017
 */

public class CompleteProfileRequestParser {

    @SerializedName("studentName")
    @Expose
    private String studentName;
    @SerializedName("standard")
    @Expose
    private String standard;
    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("schoolName")
    @Expose
    private String schoolName;
    @SerializedName("subject")
    @Expose
    private String subject;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
