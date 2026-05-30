package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Podstrok {
    public static void podstr(Scanner sc) {
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

            int Num2;
            while (true) {
                System.out.print("Введите индекс для подстроки: ");
                if (sc.hasNextInt()) {
                    Num2 = sc.nextInt();
                    break; // Корректный ввод
                } else {
                    System.out.println("Ошибка! Введённое значение не является целым или числом\n");
                    sc.next(); // Очистка неверного ввода
                }
            } sc.nextLine();

            String printSQL = "SELECT * FROM " + tbName;
            try (PreparedStatement psInsert = connection.prepareStatement(printSQL)) {
                ResultSet rs = psInsert.executeQuery();
                while (rs.next()) {
                    String updateSQL = "UPDATE " + tbName + " SET Pod1 = ?, Pod2 = ? WHERE Str1 = ?";
                    try (PreparedStatement psUpdate = connection.prepareStatement(updateSQL)) {
                        String num = rs.getString("Str1");
                        String Prov1 = rs.getString("Str1").substring(Num2);
                        String Prov2 = rs.getString("Str2").substring(Num2);
                        psUpdate.setString(1, Prov1);
                        psUpdate.setString(2, Prov2);
                        psUpdate.setString(3, num);
                        int rowsAffected = psUpdate.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Подстрока" + Prov1 + "успешно найдена");
                            System.out.println("Подстрока" + Prov2 + "успешно найдена\n");
                        }  else {
                            System.out.println("Подстрока не найдена.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Ошибка : " + e);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выводе строк из базы данных: " + e);

        }
    }
}

