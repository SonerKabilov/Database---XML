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
                case "save":
                    fileOperations.saveFile();
                    break;
                case "saveas":
                    fileOperations.saveAs(arrOfString[1]);
                    break;
                case "close":
                    fileOperations.close();
                    break;
                case "import":
                    catalogOperations.importTable(arrOfString[1], fileOperations.getFile());
                    break;
                case "showtables":
                    catalogOperations.showTables();
                    break;
                case "insert":
                    tableOperations.insertNode(arrOfString[1], fileOperations.getFile());
                    break;
                case "describe":
                    tableOperations.describe(arrOfString[1], fileOperations.getFile());
                    break;
                case "delete":
                    tableOperations.deleteColumn(arrOfString[1], arrOfString[2], arrOfString[3], fileOperations.getFile());
                    break;
                case "update":
                    tableOperations.updateColumn(arrOfString[1], arrOfString[2], arrOfString[3], arrOfString[4], fileOperations.getFile());
                    break;
                case "select":
                    tableOperations.selectNode(arrOfString[1],arrOfString[2],arrOfString[3], fileOperations.getFile());
                    break;
                case "count":
                    tableOperations.count(arrOfString[1],arrOfString[2],arrOfString[3], fileOperations.getFile());
                    break;
                case "addcolumn":
                    tableOperations.addColumn(arrOfString[1],arrOfString[2],arrOfString[3], fileOperations.getFile());
                    break;
                case "print":
                    tableOperations.print(arrOfString[1], fileOperations.getFile());
                    break;
                case "exit":
                    fileOperations.exitProgram();
                    break;
                default:
                    System.out.println("Invalid command");
            }
        } while(!menuChoice.equalsIgnoreCase("exit"));
    }
}
