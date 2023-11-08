package com.swiftselect.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HelperClass {

    public String getTokenFromHttpRequest(HttpServletRequest request) {
        // Get the bearer token from the http request
        String bearerToken = request.getHeader("Authorization");

        // Extract only the Token excluding the prefix "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
