package com.company;

import java.util.Scanner;

public class Menu {

    public void printMainMenu() {
        String menuChoice;
        Scanner sc = new Scanner(System.in);
        FileOperations fileOperations = new FileOperations();
        TableOperations tableOperations = new TableOperations();
        CatalogOperations catalogOperations = new CatalogOperations();

        do {
            System.out.println("****Menu****");
            System.out.println("open <file name>");
            System.out.println("close");
            System.out.println("import <filename>");
            System.out.println("showtables");
            System.out.println("insert <filename>");
            System.out.println("describe <filename>");
            System.out.println("delete <table name> <search column> <search value>");
            System.out.println("update <table name> <search column> <search value> <target value>");
            System.out.println("select <table name> <search column> <search value>");
            System.out.println("count <table name> <search column> <search value>");
            System.out.println("exit");
            System.out.println("\nChoose a command:");

            menuChoice = sc.nextLine();
            String[] arrOfString = menuChoice.split(" ");

            switch (arrOfString[0]) {
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
                case "exit":
                    break;
                default:
                    System.out.println("Invalid command");
            }
        } while(!menuChoice.equalsIgnoreCase("exit"));
    }
}
