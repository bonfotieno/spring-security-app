package com.bonnieapps.springsecurityapp.security;

/**
* THis enum states user permission
*
* */

public enum ApplicationUserPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    private final String permission;

    ApplicationUserPermission(String permission){
        //if you didn't know, Enum constructor is called by its elements; when calling the elements you are actually calling the constructor
        this.permission = permission;
    }

    public String getPermission() {
        // this returns the values of the elements
        return permission;
    }
}
