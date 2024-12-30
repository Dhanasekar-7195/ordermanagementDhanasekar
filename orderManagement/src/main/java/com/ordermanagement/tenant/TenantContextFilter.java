package com.ordermanagement.tenant;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

@WebFilter(urlPatterns = "/api/*")

public class TenantContextFilter extends OncePerRequestFilter {
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String[] pathParts = path.split("/");

        String tenantId = (pathParts.length > 2) ? pathParts[2] : "public";

        if (tenantId != null) {
            TenantContext.setCurrentTenant(tenantId);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing tenant ID");
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear(); 
        }
    }


}
