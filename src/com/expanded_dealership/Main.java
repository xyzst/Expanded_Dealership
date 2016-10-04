package com.expanded_dealership;

import java.util.Scanner;

/**
 *  The program represents an expanded dealership concept and uses some object oriented programming techniques
 *  to simulate an inventory management system.
 *
 *  @author Darren Rambaud (d_r273)
 *  @version 10/3/2016
 */
public class Main {

    private static final int SHOW_EXISTING_CAR_RECORDS = 1,
                             ADD_NEW_CAR = 2,
                             DELETE_CAR = 3,
                             SEARCH_FOR_CAR_VIN = 4,
                             SHOW_LIST_CARS_RANGE = 5,
                             SHOW_USERS = 6,
                             ADD_USER = 7,
                             UPDATE_USER = 8,
                             SELL_VEHICLE = 9,
                             SHOW_SALE_TRANS = 10,
                             EXIT_PROGRAM = 11,
                             VIN_LIMIT = 5;

    private static Main UI = new Main ();
    private static Vehicle inv = new Vehicle ();
    private static User user_db = new User ();
    private static Transactions transaction_db = new Transactions ();

    /**
     * The main method of this class handles displaying the menu to the user and branches off into other classes
     * to handle the various functions.
     * @param args is not utilized since the program does not require any command line arguments
     */
    public static void main(String[] args) {
        System.out.print("Importing data ...");
        if (inv.importInvData() && user_db.importUserData() && transaction_db.importTransactionData()) {
            System.out.print("DONE!");
        }
        else {
            System.out.println("ERROR! (see stack trace)");
        }

        Scanner sc = new Scanner(System.in);
        int  option,
             outcome;

        do {
            UI.displayMenu();
            option = UI.sanitizeUserInput(SHOW_EXISTING_CAR_RECORDS, EXIT_PROGRAM);
            switch (option) {
                case SHOW_EXISTING_CAR_RECORDS:
                    System.out.println("Proceeding to display vehicle records ...");
                    inv.showAllExistingRecords();
                    break;
                case ADD_NEW_CAR:
                    int vehicle;
                    System.out.println("Now adding a new vehicle to the datebase ...");
                    System.out.println("Please specify the type of the vehicle below: ");
                    System.out.println("1. CAR\n" +
                                       "2. TRUCK\n" +
                                       "3. MOTORCYCLE");
                    vehicle = UI.sanitizeUserInput(1,3);
                    inv.addNewVehicle(vehicle, VIN_LIMIT);
                    break;
                case DELETE_CAR:
                    outcome = inv.delVehiclebyVIN(VIN_LIMIT);
                    System.out.println("Proceeding to delete a vehicle from the inventory...");
                    if (outcome == -1) {
                        System.err.println("The database is currently empty, now returning to the main menu...");
                    }
                    else if (outcome == -2) {
                        System.out.println("\nVehicle not found in the inventory list.");
                    }
                    else {
                        System.out.println("\nVehicle has been successfully removed!");
                    }
                    break;
                case SEARCH_FOR_CAR_VIN:
                    outcome = inv.searchViaVIN(VIN_LIMIT);

                    if (outcome != -1) {
                        System.out.print("\nVEHICLE FOUND!");
                        inv.printSeparatorLine();
                        inv.printHeaderLine();
                        inv.printSpecificVehicle(outcome);
                        inv.printSeparatorLine();
                    }
                    else {
                        System.out.println("\nSorry, there are no vehicles associated with this VIN.");
                    }
                    break;
                case SHOW_LIST_CARS_RANGE:
                    if (inv.isEmpty()) {
                        System.err.println("ERROR: The database is currently empty.");
                        break;
                    }
                    int lower,
                        higher;
                    boolean invalid_range,
                            invalid = false;

                    System.out.println("Please select a vehicle type below:");
                    System.out.print("1. MOTORCYCLE\n" +
                                     "2. PASSENGER\n" +
                                     "3. TRUCK\n" +
                                     "Select an option above: ");
                    int choice;

                    do {
                        choice = sc.nextInt();
                        if (choice < 1 && choice > 3) {
                            System.out.println("ERROR: You have entered an invalid choice. Try again: ");
                            invalid = true;
                        }
                    } while (invalid);

                    do {
                        System.out.println("\nPlease enter the MINIMUM price threshold: ");
                        sc.nextLine(); // consume newline char
                        lower = Integer.parseInt(sc.nextLine());

                        System.out.println("\nPlease enter the MAXIMUM price threshold: ");
                        higher = Integer.parseInt(sc.nextLine());

                        if (lower < 0 || higher < 0) {
                            invalid_range = true;
                            System.out.println("\nYou have entered negative values for the price threshold(s), " +
                                               "please try again ...");
                        }
                        else if (lower > higher || higher < lower){
                            System.out.println("\nYou have entered an invalid range, please try again...");
                            invalid_range = true;
                        }
                        else {
                            invalid_range = false;
                        }

                    }while (invalid_range);
                    inv.showVehiclesWithinPriceRange(lower, higher, choice);
                    break;
                case SHOW_USERS:
                    user_db.showListofUsers();
                    break;
                case ADD_USER:
                    user_db.addNewUser();
                    break;
                case UPDATE_USER:
                    if (user_db.isEmpty()) {
                        System.out.println("There are no users in the database to update. Returning to main menu");
                        break;
                    }
                    System.out.println("Now proceeding to update a user ...");
                    user_db.showListofUsers();
                    user_db.updateUser();
                    break;
                case SELL_VEHICLE:
                    transaction_db.sellVehicle(inv, user_db);
                    break;
                case SHOW_SALE_TRANS:
                    System.out.println("Transaction history:");
                    transaction_db.showCompletedSaleTransactions();
                    break;
                case EXIT_PROGRAM:
                    System.out.print("Exporting data ...");
                     if (inv.exportInvData() && user_db.exportUserData() && transaction_db.exportTransactionData()) {
                         System.out.println("DONE!");
                     }
                     else {
                         System.out.println("ERROR! (see stack trace)");
                     }
                    break;
            }
            if (option != EXIT_PROGRAM) {
                UI.pressEnter2Continue();
            }
            else {
                System.out.println("Goodbye!");
            }
        } while (option != EXIT_PROGRAM);
    }

    /**
     * sanitizeUserInput method guarantees certain input is sanitized before being passed to another method of a different
     * class
     * @param lower_limit specifies the lower threshold
     * @param upper_limit specifies the upper threshold
     * @return an integer, indicating the sanitized input
     */
    private int sanitizeUserInput (int lower_limit, int upper_limit) {
        Scanner sc = new Scanner(System.in);
        int selection = sc.nextInt();

        while (selection < lower_limit || selection > upper_limit) {
            System.out.println("You have entered an invalid option (\""+selection+"\"). Please try again...");
            System.out.print("Select an option between "+lower_limit+" and "+upper_limit+": ");
            selection = sc.nextInt();
        }

        return selection;
    }

    /**
     * displayMenu takes no arguments and merely displays the menu options to the user via System.out.print
     */
    private void displayMenu(){
        System.out.println("\nWelcome to the Main Menu!");
        System.out.print("\n     "+SHOW_EXISTING_CAR_RECORDS+". Show all existing car records in the database (in any order)." +
                         "\n     "+ADD_NEW_CAR+". Add a new vehicle record to the database." +
                         "\n     "+DELETE_CAR+". Delete a vehicle record from a database." +
                         "\n     "+SEARCH_FOR_CAR_VIN+". Search for a vehicle (given its VIN)." +
                         "\n     "+SHOW_LIST_CARS_RANGE+". Show a list of vehicles within a given price range." +
                         "\n     "+SHOW_USERS+". Show list of users in the database." +
                         "\n     "+ADD_USER+". Add a new user to the database." +
                         "\n     "+UPDATE_USER+". Update user info (given their ID)." +
                         "\n     "+SELL_VEHICLE+". Sell a vehicle." +
                         "\n     "+SHOW_SALE_TRANS+". Show a list of completed sale transactions." +
                         "\n     "+EXIT_PROGRAM+". Exit program.\n"+
                         "\nPlease select an option between "+SHOW_EXISTING_CAR_RECORDS+" and "+EXIT_PROGRAM+": ");
    }

    /**
     * pressEnter2Continue allows the user to pause the screen before loading up the menu or cluttering the interface
     */
    private void pressEnter2Continue() {
        Scanner advance = new Scanner(System.in);
        System.out.print("\nPress \"ENTER\" to advance to the main menu ...");
        advance.nextLine();
    }
}