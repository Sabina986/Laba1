package org.example;

import java.util.Scanner;

public class Run {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n1. Вывести все таблицы из MySQL\n" + "2. Создать таблицу в БД в MySQL\n" +
                    "3. Возвращение подстроки по индексам, результат сохранить в MySQL с последующим выводом в консоль\n" +
                    "4. Перевод строк в верхний и нижний регистр, результат сохранить в MySQL с последующим выводом в консоль\n" +
                    "5. Поиск подстроки и определение окончания подстроки, результат сохранить в MySQL с последующим выводом в консоль\n" +
                    "6. Сохранить все данные из MySQL в Excel и вывести на экран\n" + "7. Выход\n");
            System.out.print("Выберите пункт меню: ");
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    CreateDB.createDatabase(sc);
                    break;
                case "2":
                    CreateTB.createTable(sc);
                    break;
                case "3":
                    NewStr.insertStr(sc);
                    Podstrok.podstr(sc);
                    break;
                case "4":
                    Case.update(sc);
                    break;
                case "5":
                    Poisk.search(sc);
                    break;
                case "6":
                    System.out.print("Введите путь для сохранения Excel-файла: ");
                    String filepath = sc.nextLine();
                    ExportToExcel.exportTableToExcel(filepath);
                    break;
                case "7":
                    exit = true;
                    System.out.println("Выход из программы");
                    break;
                default:
                    System.out.println("Неверное значение пункта меню! Попробуйте снова.");
            }
        }
        MysqlConfig.shutdown();
        sc.close();
    }
}
