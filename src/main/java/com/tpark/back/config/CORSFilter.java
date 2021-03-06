package com.tpark.back.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class CORSFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(CORSFilter.class);

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("------CORSFilter------------");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
     
        if ("OPTIONS".equals(request.getMethod())) {
	    response.setStatus(HttpServletResponse.SC_OK);
	} else {
	    chain.doFilter(request, response);
	}
    }

}
