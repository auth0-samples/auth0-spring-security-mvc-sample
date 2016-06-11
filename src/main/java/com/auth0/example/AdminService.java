package com.auth0.example;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Demonstration of method level Role based authorization
 * Only an authenticated and authorized User with Admin
 * rights can access this resource.
 */
@Service
public class AdminService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean ensureAdmin() {
        return true;
    }
}


