package com.example.prodem.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet( "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username+" "+ password);
        boolean validCredentials = checkCredentials(username, password);
        if (validCredentials) {
            String token = createJwtToken(username);
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(token);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean checkCredentials(String username, String password) {
        if ("Shaig".equals(username) && "password".equals(password)) {
            return true;
        }
        return false;
    }

    private String createJwtToken(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date expirationDate = new Date(currentTimeMillis + (10 * 1000)); // 5 minutes from now
        byte[] secretBytes = MyFilter.secretKey.getBytes();
        String base64Secret = Base64.getEncoder().encodeToString(secretBytes);

        String jws = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, base64Secret)
                .compact();
        return jws;}


}
