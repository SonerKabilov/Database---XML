package com.company;

import java.util.Scanner;

public class Menu {

    public void printMainMenu() {
        String menuChoice;
        Scanner sc = new Scanner(System.in);
        FileOperations fileOperations = new FileOperations();
        TableOperations tableOperations = new TableOperations();
        CatalogOperations catalogOperations = new CatalogOperations();
        PrintMenuOptions menuOptions = new PrintMenuOptions();

        do {
            System.out.println("\nChoose a command /type 'help' for more information/:");

            menuChoice = sc.nextLine();
            String[] arrOfString = menuChoice.split(" ");

            switch (arrOfString[0]) {
                case "help":
                    menuOptions.help();
                    break;
                case "open":
                    fileOperations.openFile(arrOfString[1]);
                    break;
                case "import":
                    catalogOperations.importTable(arrOfString[1]);
                    break;
                case "showtables":
                    catalogOperations.showTables();
                    break;
                case "insert":
                    tableOperations.insertNode(arrOfString[1]);
                    break;
                case "describe":
                    tableOperations.describe(arrOfString[1]);
                    break;
                case "delete":
                    tableOperations.deleteColumn(arrOfString[1], arrOfString[2], arrOfString[3]);
                    break;
                case "update":
                    tableOperations.updateColumn(arrOfString[1], arrOfString[2], arrOfString[3], arrOfString[4]);
                    break;
                case "select":
                    tableOperations.selectNode(arrOfString[1],arrOfString[2],arrOfString[3]);
                    break;
                case "count":
                    tableOperations.count(arrOfString[1],arrOfString[2],arrOfString[3]);
                    break;
                case "addcolumn":
                    tableOperations.addColumn(arrOfString[1],arrOfString[2],arrOfString[3]);
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Invalid command");
            }
        } while(!menuChoice.equalsIgnoreCase("exit"));
    }
}
