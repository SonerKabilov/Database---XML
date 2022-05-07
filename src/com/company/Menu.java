package com.company;

import java.util.Scanner;

public class Menu {

    public void printMainMenu() {
        String menuChoice;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("****Menu****");
            System.out.println("open <file name>");
            System.out.println("close");
            System.out.println("add <filename>");
            System.out.println("describe <filename>");
            System.out.println("delete <table name> <search column> <search value>");
            System.out.println("exit");
            System.out.println("\nChoose a command:");
            menuChoice = sc.nextLine();
            String[] arrOfString = menuChoice.split(" ");
        } while(!menuChoice.equalsIgnoreCase("exit"));
    }
}
