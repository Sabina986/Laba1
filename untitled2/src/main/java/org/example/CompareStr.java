package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CompareStr {
    public static void compareByNum(Scanner sc) {
        try (Connection connection = MysqlConfig.getConnection()) {
            String dbName = MysqlConfig.getDatabaseName();
            if (dbName == null || dbName.isEmpty()) {
                System.out.println("Ошибка! Сначала создайте базу данных!");
                return;
            }
            try (PreparedStatement psUse = connection.prepareStatement("USE " + dbName)) {
                psUse.executeUpdate();
            }
            String tbName = MysqlConfig.getTable();
            if (tbName == null || tbName.isEmpty()) {
                System.out.println("Ошибка! Сначала создайте таблицу в базе данных!");
                return;
            }
            String checkTableSQL = "SHOW TABLES LIKE ?";
            try (PreparedStatement psCheck = connection.prepareStatement(checkTableSQL)) {
                psCheck.setString(1, tbName);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Таблицы '" + tbName + "' не существует. Сначала создайте таблицу");
                        return;
                    }
                }
            }
            String printSQL = "SELECT * FROM " + tbName;
            try (PreparedStatement psInsert = connection.prepareStatement(printSQL)) {
                ResultSet rs = psInsert.executeQuery();
                while (rs.next()) {
                    String updateSQL = "UPDATE " + tbName + " SET Res = ? WHERE Stroka1 = ?";
                    try (PreparedStatement psUpdate = connection.prepareStatement(updateSQL)) {
                        String one = rs.getString("Stroka1");
                        String two = rs.getString("Stroka2");
                        String ravn = "Строки одинаковы"; String nerav = "Строки не одинаковы, ";
                        String bol = "первая строка длиннее второй"; String men = "вторая строка длиннее первой";
                        String rav = "строки равной длины";
                        boolean Res1 = one.equals(two); int Razm1 = rs.getString("Stroka1").length();
                        int Razm2 = rs.getString("Stroka2").length();
                        System.out.println("\nРезультат сравнения строк  '" + one + "' и '" + two);
                        if (!Res1){
                            System.out.print(nerav);
                            if (Razm1 > Razm2 || Razm1 < Razm2){
                                if (Razm1 > Razm2){
                                    System.out.print(bol + "\n");
                                    psUpdate.setString(1, nerav+bol);
                                } else {
                                    System.out.print(men + "\n");
                                    psUpdate.setString(1, nerav+men);
                                }
                            } else {
                                System.out.print(rav + "\n");
                                psUpdate.setString(1, nerav+rav);
                            }
                            psUpdate.setString(2, one);
                        }
                        else {
                            System.out.println(ravn + "\n");
                            psUpdate.setString(1, ravn);
                            psUpdate.setString(2, one);
                        }
                        int rowsAffected = psUpdate.executeUpdate();
                        if (rowsAffected == 0) {System.out.println("Строки не найдены.");}
                    } catch (SQLException e) {
                        System.out.println("Ошибка при подсчёте длин строк: " + e);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выводе строк из базы данных: " + e);
        }
    }
}

