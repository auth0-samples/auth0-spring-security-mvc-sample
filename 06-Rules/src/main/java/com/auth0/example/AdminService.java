package com.auth0.example;

import com.auth0.spring.security.mvc.Auth0UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Demonstration of method level Role based authorization
 * Only an authenticated and authorized User with Admin
 * rights can access this resource.
 *
 * Also demonstrates how to retrieve the UserDetails object
 * representing the Authentication's principal from within
 * a service
 *
 */
@Service
public class AdminService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public boolean ensureAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Auth0UserDetails currentUser = (Auth0UserDetails) authentication.getPrincipal();
        logger.info("Current user accessed Admin secured resource: " + currentUser.getUsername());
        // add extra info here for retrieval of userMetadata, appMetadata and extraInfo
        final Map<String, Object> userMetadata = currentUser.getUserMetadata();
        final Map<String, Object> appMetadata = currentUser.getAppMetadata();
        final Map<String, Object> extraInfo = currentUser.getExtraInfo();
        return true;
    }
}


