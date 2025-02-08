package com.example.BankDemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://dpg-cuj6qm23esus73fnqma0-a.oregon-postgres.render.com:5432/bank_4yjc?ssl=true&sslmode=require";
        String user = "bank_4yjc_user";
        String password = "riTAzYSJqNOTTDHPIC15vOLYRdjbVMcP";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
