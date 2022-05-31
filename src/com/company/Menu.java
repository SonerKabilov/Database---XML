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
                    try {
                        fileOperations.openFile(arrOfString[1]);
                    } catch (RuntimeException e){
                        System.out.println("Enter the file name after the command");
                    }
                    break;
                case "save":
                    fileOperations.saveFile();
                    break;
                case "saveas":
                    try {
                        fileOperations.saveAs(arrOfString[1]);
                    } catch (RuntimeException e) {
                        System.out.println("Enter the file name after the command");
                    }
                    break;
                case "close":
                    fileOperations.close();
                    break;
                case "import":
                    try {
                        catalogOperations.importTable(arrOfString[1], fileOperations.getFile());
                    } catch (RuntimeException e) {
                        System.out.println("Enter the file name after the command");
                    }
                    break;
                case "showtables":
                    catalogOperations.showTables();
                    break;
                case "insert":
                    try {
                        tableOperations.insertNode(arrOfString[1], fileOperations.getFile());
                    } catch (RuntimeException e) {
                        System.out.println("Enter the file name after the command");
                    }
                    break;
                case "describe":
                    try {
                        tableOperations.describe(arrOfString[1], fileOperations.getFile());
                    } catch (RuntimeException e) {
                        System.out.println("Enter the file name after the command");
                    }
                    break;
                case "delete":
                    try {
                        tableOperations.deleteColumn(arrOfString[1], arrOfString[2], arrOfString[3], fileOperations.getFile());
                    } catch (RuntimeException e) {
                        System.out.println("Enter the file name, column name and column value after the command");
                    }
                    break;
                case "update":
                    try {
                        tableOperations.updateColumn(arrOfString[1], arrOfString[2], arrOfString[3], arrOfString[4], fileOperations.getFile());
                    } catch (RuntimeException e) {
                        System.out.println("Enter the file name, column name and old column value and new column value after the command");
                    }
                    break;
                case "select":
                    try {
                        tableOperations.selectNode(arrOfString[1],arrOfString[2],arrOfString[3], fileOperations.getFile());
                    } catch (RuntimeException e) {
                        System.out.println("Enter the file name, column name and column value after the command");
                    }
                    break;
                case "count":
                    try {
                        tableOperations.count(arrOfString[1],arrOfString[2],arrOfString[3], fileOperations.getFile());
                    } catch (RuntimeException e) {
                        System.out.println("Enter the file name, column name and column value after the command");
                    }
                    break;
                case "addcolumn":
                    try {
                        tableOperations.addColumn(arrOfString[1],arrOfString[2],arrOfString[3], fileOperations.getFile());
                    } catch (RuntimeException e) {
                        System.out.println("Enter the file name, column name and column type after the command");
                    }
                    break;
                case "print":
                    try {
                        tableOperations.print(arrOfString[1], fileOperations.getFile());
                    } catch (RuntimeException e) {
                        System.out.println("Enter the file name after the command");
                    }
                    break;
                case "rename":
                    try {
                        fileOperations.renameFile(arrOfString[1], arrOfString[2]);
                    } catch (RuntimeException e) {
                        System.out.println("Enter the old file name and the new file name after the command");
                    }
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
