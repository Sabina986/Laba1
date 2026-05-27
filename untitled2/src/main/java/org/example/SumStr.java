package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SumStr {
    public static void sumByNum(Scanner sc) {
        try (Connection connection = MysqlConfig.getConnection()) {
            String dbName = MysqlConfig.getDatabaseName();
            if (dbName == null || dbName.isEmpty()) {
                System.out.println("Ошибка! Сначала создайте/подключитесь к базе данных!");
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
                    String updateSQL = "UPDATE " + tbName + " SET SUMsum = ? WHERE Stroka1 = ?";
                    try (PreparedStatement psUpdate = connection.prepareStatement(updateSQL)) {
                        String field2 = rs.getString("Stroka1") + rs.getString("Stroka2");
                        String num = rs.getString("Stroka1");
                        psUpdate.setString(1, field2);
                        psUpdate.setString(2, num);
                        int rowsAffected = psUpdate.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Строки '" + rs.getString("Stroka1") + "' и '" + rs.getString("Stroka2") + "' успешно объединены");
                        }  else {
                            System.out.println("Строки не найдены.");
                        }
                        System.out.println("Результат объединения: " + field2 + "\n");
                    } catch (SQLException e) {
                        System.out.println("Ошибка при объединении строк: " + e);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выводе строк из базы данных: " + e);

        }
    }
}

