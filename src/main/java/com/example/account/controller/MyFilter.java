package com.example.prodem.controller;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter(urlPatterns = "/hello-servlet")
public class MyFilter implements Filter {
    public static final String secretKey = "Cpt.Sura-Boy.jwt-secret@IJSE2020-12-22"; // replace with your own secret key


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // filter başlatıldığında yapılacak işlemler
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // filtreleme işlemleri
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean tokenResult = false;
        try {
            String authHeader = httpServletRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                isJwtTokenValid(authHeader);


            }

        } catch (ExpiredJwtException exp) {
            exp.printStackTrace();
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.getWriter().write("Tokenin vaxti bitib!");
            return;
        }


        // Pass the request and response to the next filter in the chain
        chain.doFilter(request, response);


        // sonraki filtreye geçmek için
    }

    private boolean isJwtTokenValid(String jwtToken) {
        String token = jwtToken.replaceAll("(Basic)|(Bearer)", "").trim();

        byte[] secretBytes = secretKey.getBytes();
        String base64Secret = Base64.getEncoder().encodeToString(secretBytes);
        Claims claims = Jwts.parser()
                .setSigningKey(base64Secret)
                .parseClaimsJws(token)
                .getBody();

        LocalDateTime localDateTime = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (localDateTime.isBefore(LocalDateTime.now())) {
            return false;
        }
        System.out.println(claims.getSubject());
        return true;
    }

    @Override
    public void destroy() {
    }
}