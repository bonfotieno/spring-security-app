package com.bonnieapps.springsecurityapp.security;


import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.bonnieapps.springsecurityapp.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
    ADMINTRANEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){

        // this function gets set of all ApplicationUserPermissions loop through them one by one with map function and
        // creates object SimpleGrantedAuthority then creates a set again (SimpleGrantedAuthority is just an arraylist object)
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(
                new SimpleGrantedAuthority("ROLE_"+this.name())// this.name() is the name of the element that will
        );                                                          // be referred to e.g. ADMIN.getGrantedAuthorities() this=ADMIN

        return permissions;
        // what will be returned is something like [ROLE_ADMINTRANEE, student:read, course:read]
    }
}
