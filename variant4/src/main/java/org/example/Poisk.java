package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Poisk {
    public static void search(Scanner sc) {
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

            System.out.print("Введите подстроку для нахождения: ");
            String Num2 = sc.nextLine();
            String printSQL = "SELECT * FROM " + tbName;
            try (PreparedStatement psInsert = connection.prepareStatement(printSQL)) {
                ResultSet rs = psInsert.executeQuery();
                while (rs.next()) {
                    String updateSQL = "UPDATE " + tbName + " SET OkonPod = ? WHERE Str1 = ?";
                    try (PreparedStatement psUpdate = connection.prepareStatement(updateSQL)) {
                        String num = rs.getString("Str1");
                        boolean Poisk1 = rs.getString("Str1").contains(Num2);
                        boolean Poisk2 = rs.getString("Str2").contains(Num2);
                        boolean P1 = rs.getString("Str1").endsWith(Num2), P2 = rs.getString("Str2").endsWith(Num2);
                        String end;
                        if (Poisk1 || Poisk2) {
                            if (Poisk1 && Poisk2){
                                end = "Обе строки содержат подстроку " + Num2;
                                if (P1) end = end + ",\nпервая строка заканчивается ей";
                                if (P2) end = end + ",\nвторая строка заканчивается ей";
                            } else {
                                if (Poisk1) { end = "Только первая строка содержит подстроку " + Num2; if (P1) end = end + "\nи заканчивается ей";
                                } else { end = "Только вторая строка содержит подстроку " + Num2; if (P2) end = end + "\nи заканчивается ей"; }
                            }
                        } else { end = "Строки не содержат подстроку " + Num2; }

                        psUpdate.setString(1, end);
                        psUpdate.setString(2, num);
                        int rowsAffected = psUpdate.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Поиск подстроки '" + Num2 + "' успешно проведён");
                        } else {
                            System.out.println("Подстрока не найдена.");
                        }
                    } catch (SQLException e) { System.out.println("Ошибка : " + e); }
                }
            }
        } catch (SQLException e) { System.out.println("Ошибка при выводе строк из базы данных: " + e); }
    }
}

