package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PrintTable {
    public static void printAllStudents(Scanner sc) {

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
                System.out.printf("%-50s | %-50s | %-12s | %-12s | %-100s | %-25s |\n", "Первая строка", "Вторая строка", "Размер 1 стр",
                        "Размер 2 стр", "Сумма строк", "Результат сравнения");
                while (rs.next()) {
                    String Str1 = rs.getString("Stroka1");
                    String Str2 = rs.getString("Stroka2");
                    String Raz1 = rs.getString("Raz1");
                    String Raz2 = rs.getString("Raz2");
                    String SUMsum = rs.getString("SUMsum");
                    String Res = rs.getString("Res");
                    System.out.printf("%-50s | %-50s | %-12s | %-12s | %-100s | %-25s |\n", Str1, Str2, Raz1, Raz2, SUMsum, Res);
                }
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при выводе данных из базы данных: " + e);

        }
    }
}

