package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CountRaz {
    public static void countAll(Scanner sc) {
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
                    String updateSQL = "UPDATE " + tbName + " SET Raz1 = ?, Raz2 = ? WHERE Stroka1 = ?";
                    try (PreparedStatement psUpdate = connection.prepareStatement(updateSQL)) {
                        int Raz1 = rs.getString("Stroka1").length();
                        int Raz2 = rs.getString("Stroka2").length();
                        String num = rs.getString("Stroka1");
                        psUpdate.setInt(1, Raz1);
                        psUpdate.setInt(2, Raz2);
                        psUpdate.setString(3, num);
                        int rowsAffected = psUpdate.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Длины строк  '" + num + "' и '" + rs.getString("Stroka2") + "' успешно подсчитаны");
                            System.out.println("Длина строки  '" + num + "' равна " + Raz1 + " символов");
                            System.out.println("Длина строки  '" + rs.getString("Stroka2") + "' равна " + Raz2 + " символов\n");
                        }  else {
                            System.out.println("Строки не найдены.");
                        }
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

