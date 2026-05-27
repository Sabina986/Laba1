package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Proverka {
    public static void insertNum(Scanner sc) {

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

            int NUM;
            //Ввод числа с проверкой
            while (true) {
                System.out.print("Введите число: ");
                if (sc.hasNextInt()) {
                    NUM = sc.nextInt();
                    break; // Корректный ввод
                } else {
                    System.out.println("Ошибка! Введённое значение не является целым или числом\n");
                    sc.next(); // Очистка неверного ввода
                }
            }
            String Str1 = "Да"; String Str2 = "Нет";
            String insertSQL = "INSERT INTO " + tbName + " (Chislo, Chet) VALUES (?, ?)";
            try (PreparedStatement psInsert = connection.prepareStatement(insertSQL)) {
                psInsert.setInt(1, NUM);
                if (NUM % 2 == 0) { psInsert.setString(2, Str1);
                    System.out.println("Число " + NUM + " - чётное\n"); }
                else { psInsert.setString(2, Str2);
                    System.out.println("Число " + NUM + " - нечётное\n"); }
                psInsert.executeUpdate();
            }

            int Yn;
            while (true) {
                System.out.println("Ввести другое число? \n1. Да      2. Нет");
                if (sc.hasNextInt()) {
                    Yn = sc.nextInt();
                    if (Yn == 1) { Proverka.insertNum(sc); break; }
                    if (Yn == 2){  sc.nextLine(); break; }
                    else {
                        System.out.println("Ошибка! Введите 1 или 2\n");
                    }
                }
                else {
                    System.out.println("Ошибка! Введите число\n");
                    sc.next(); // Очистка неверного ввода
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении строк в базу данных: " + e);
        }
    }
}

