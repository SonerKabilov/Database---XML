package com.company;

import java.util.Scanner;

public class Menu {

    public void printMainMenu() {
        String menuChoice;
        Scanner sc = new Scanner(System.in);
        FileOperations fileOperations = new FileOperations();
        TableOperations tableOperations = new TableOperations();

        do {
            System.out.println("****Menu****");
            System.out.println("open <file name>");
            System.out.println("close");
            System.out.println("insert <filename>");
            System.out.println("describe <filename>");
            System.out.println("delete <table name> <search column> <search value>");
            System.out.println("exit");
            System.out.println("\nChoose a command:");

            menuChoice = sc.nextLine();
            String[] arrOfString = menuChoice.split(" ");

            switch (arrOfString[0]) {
                case "open":
                    fileOperations.openFile(arrOfString[1]);
                    break;
                case "insert":
                    tableOperations.insertNode(arrOfString[1]);
                    break;
                case "describe":
                    tableOperations.describe(arrOfString[1]);
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Invalid command");
            }
        } while(!menuChoice.equalsIgnoreCase("exit"));
    }
}
