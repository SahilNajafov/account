package com.example.account;

import com.example.account.model.Account;

import java.math.BigDecimal;
import java.sql.*;

public class AccountRepository {


    public Account getAccountById(long id) {
        Account account = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/registration";
            Connection connection = DriverManager.getConnection(url, "postgres", "123SsAaBgcd00!");
            String sql = "SELECT * FROM public.accounts where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Integer id1 = rs.getInt("id");
                String name1 = rs.getString("name");
                BigDecimal balance = rs.getBigDecimal("balance");
                account = new Account(id1, name1, balance);
            }


            preparedStatement.close();
            connection.close();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return account;


    }

    public Account saveAccount(Account newAccount) {

        int id=0;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/registration";
            Connection connection = DriverManager.getConnection(url, "postgres", "123SsAaBgcd00!");
            String sql = "insert into public.accounts values(nextval('account_id'), ?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);//????????????????????
            preparedStatement.setString(1, newAccount.getName());
            preparedStatement.setBigDecimal(2, newAccount.getBalance());
           preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys(); //??????????????????????????????????????

            // Get the ID of the newly inserted row
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return getAccountById(id);


    }
}
