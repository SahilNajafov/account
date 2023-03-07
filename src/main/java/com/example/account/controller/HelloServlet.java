package com.example.account;

import com.example.account.model.Account;
import com.example.account.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/hello-servlet")
public class HelloServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accId = request.getParameter("accId");
        System.out.println(accId);

        Integer id= Integer.valueOf(accId);
        AccountRepository accountRepository = new AccountRepository();
        Account accountById = accountRepository.getAccountById(id);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(accountById);


        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(jsonStr);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


        // Request body'sini oku ve JSON stringini al
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String jsonStr = sb.toString();
        System.out.println(jsonStr);


        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(jsonStr, Account.class);

        AccountRepository accountRepository= new AccountRepository();
        Account accountInDb= accountRepository.saveAccount(account);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(accountInDb));
        // ...
    }

    public void destroy() {
    }


}