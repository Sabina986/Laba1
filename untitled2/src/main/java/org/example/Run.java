package org.example;

import java.util.Scanner;

public class Run {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n1. Вывести все таблицы из MySQL\n" +
                    "2. Создать таблицу в БД в MySQL\n" +
                    "3. Ввести две строки с клавиатуры, результат сохранить в MySQL с выводом в консоль\n" +
                    "4. Подсчитать размер ранее введённых строк, результат сохранить в MySQL с выводом в консоль\n" +
                    "5. Объединить две строки в единое целое, результат сохранить в MySQL с выводом в консоль\n" +
                    "6. Сравнить две раннее введённые строки, результат сохранить в MySQL с выводом в консоль\n" +
                    "7. Сохранить все данные из MySQL в Excel и вывести на экран\n" + "8. Выход\n");
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
                    break;
                case "4":
                    CountRaz.countAll(sc);
                    break;
                case "5":
                    SumStr.sumByNum(sc);
                    break;
                case "6":
                    CompareStr.compareByNum(sc);
                    break;
                case "7":
                    System.out.print("Введите путь для сохранения Excel-файла: ");
                    String filepath = sc.nextLine();
                    ExportToExcel.exportTableToExcel(filepath);
                    break;
                case"8":
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

