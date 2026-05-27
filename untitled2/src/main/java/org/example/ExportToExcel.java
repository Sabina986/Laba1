package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExportToExcel {

    public static void exportTableToExcel(String filepath) {

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
            String printAll = "SELECT * FROM " + tbName;
            try (PreparedStatement psPrint = connection.prepareStatement(printAll); ResultSet rs = psPrint.executeQuery()) {
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("Stroki");
                Row row = sheet.createRow(0);
                row.createCell(0).setCellValue("Первая строка");
                row.createCell(1).setCellValue("Вторая строка");
                row.createCell(2).setCellValue("Размер перв/стр");
                row.createCell(3).setCellValue("Размер втор/стр");
                row.createCell(4).setCellValue("Сумма строк");
                row.createCell(5).setCellValue("Результат сравнения");


                int rowUbdex = 1;
                System.out.printf("\n%-50s | %-50s | %-12s | %-12s | %-100s | %-25s |\n", "Первая строка", "Вторая строка", "Размер 1 стр",
                        "Размер 2 стр", "Сумма строк", "Результат сравнения");
                while (rs.next()) {
                    Row row1 = sheet.createRow(rowUbdex++);
                    row1.createCell(0).setCellValue(rs.getString("Stroka1")); String Str1 = rs.getString("Stroka1");
                    row1.createCell(1).setCellValue(rs.getString("Stroka2")); String Str2 = rs.getString("Stroka2");
                    row1.createCell(2).setCellValue(rs.getInt("Raz1")); String Raz1 = rs.getString("Raz1");
                    row1.createCell(3).setCellValue(rs.getInt("Raz2")); String Raz2 = rs.getString("Raz2");
                    row1.createCell(4).setCellValue(rs.getString("SUMsum")); String SUMsum = rs.getString("SUMsum");
                    row1.createCell(5).setCellValue(rs.getString("Res")); String Res = rs.getString("Res");
                    System.out.printf("%-50s | %-50s | %-12s | %-12s | %-100s | %-25s |\n", Str1, Str2, Raz1, Raz2, SUMsum, Res);
                }
                System.out.print("\n");
                int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
                for (int i = 0; i < columnCount; i++) {
                    sheet.autoSizeColumn(i);
                }
                try (FileOutputStream fos = new FileOutputStream(filepath)) {
                    wb.write(fos);
                } catch (IOException e) {
                    System.out.println("Ошибка при записи Excel-файла: " + e);
                } finally {
                    wb.close();
                    System.out.println("Данные успешно экспортированы в Excel-файл: " + filepath);
                }
            } catch (SQLException e) {
                System.out.println("Ошибка при экспорте данных: " + e);
            }
        } catch (IOException | SQLException e) {
            System.out.println("Ошибка при закрытии Excel-файла: " + e);
        }
    }
}


