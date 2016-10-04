package com.expanded_dealership;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Transactions represents a transaction or sales record when an employee sells a vehicle
 *  @author Darren Rambaud (d_r273)
 *  @version 10/3/2016
 */
public class Transactions implements Serializable {
    private final static char HEADER_CHAR = '*';
    private final static int FIELD_LEN = 20,
                             NUM_HEADER_CHARS = 102;

    private int customer_id,
                employee_id;
    private String VIN;
    private Date sale_date;
    private float final_sale_price;
    private ArrayList<Transactions> transactions = new ArrayList<>();

    Transactions () {
    }

    /**
     * getCustomerID is a simple getter
     * @return customer_id, an int
     */
    int getCustomerID () {
        return customer_id;
    }

    /**
     * getEmployeeID is a getter
     * @return employee_id, an int
     */
    int getEmployeeID () {
        return employee_id;
    }

    /**
     * getVIN is a getter
     * @return VIN, an int
     */
    String getVIN () {
        return VIN;
    }

    /**
     * getSaleDate is getter
     * @return sale_date, a Date object
     */
    Date getSaleDate () {
        return sale_date;
    }

    /**
     * getFinalSalePrice is a getter
     * @return final_sale_price, a float
     */
    float getFinalSalePrice () {
        return final_sale_price;
    }

    /**
     * setCustomerID is a setter
     * @param customer_id, is an int
     */
    void setCustomerID (int customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * setEmployeeID is a setter
     * @param employee_id, an int
     */
    void setEmployeeID (int employee_id) {
        this.employee_id = employee_id;
    }

    /**
     * setVIN is a setter
     * @param VIN, a String
     */
    void setVIN (String VIN) {
        this.VIN = VIN;
    }

    /**
     * setSaleDate is a setter
     * @param sale_date, a Date object
     */
    void setSaleDate (Date sale_date) {
        this.sale_date = sale_date;
    }

    /**
     * setFinalSalePrice is a setter
     * @param final_sale_price, a float
     */
    void setFinalSalePrice (float final_sale_price) {
        this.final_sale_price = final_sale_price;
    }

    /**
     * printSeparatorLine prints HEADER_CHARS to serve as a defacto border
     */
    void printSeparatorLine() {
        System.out.print("\n");
        for (int i = 0; i < NUM_HEADER_CHARS; ++i){
            System.out.print(HEADER_CHAR);
        }
        System.out.print("\n");
    }

    /**
     * printHeaderLine prints header information to screen
     */
    void printHeaderLine () {
        System.out.print(""+HEADER_CHAR+" ");
        System.out.printf("%-"+(FIELD_LEN - 7)+"s", "CUSTOMER ID#");
        System.out.printf("%-"+(FIELD_LEN - 9)+"s", "VIN NUMBER");
        System.out.printf("%-"+(FIELD_LEN + 10)+"s", "SALE DATE");
        System.out.printf("%-"+(FIELD_LEN)+"s", "FINAL SALE PRICE");
        System.out.printf("%-"+(FIELD_LEN + 4)+"s "+HEADER_CHAR+"", "SOLD BY (EMP ID#)");
    }

    /**
     * printSpecificTransaction prints one object's attributes to the screen based upon the index given by y
     * @param y, an int, represents the index at which the object is found at
     */
    void printSpecificTransaction (int y) {
        System.out.print("\n* ");
        System.out.printf("%-"+(FIELD_LEN - 7)+"s", transactions.get(y).getCustomerID());
        System.out.printf("%-"+(FIELD_LEN - 9)+"s", transactions.get(y).getVIN());
        System.out.printf("%-"+(FIELD_LEN + 10)+"s", transactions.get(y).getSaleDate());
        System.out.printf("$%-"+(FIELD_LEN)+".2f", transactions.get(y).getFinalSalePrice());
        System.out.printf("%-"+(FIELD_LEN + 3)+"s "+HEADER_CHAR+"", transactions.get(y).getEmployeeID());

    }

    /**
     * importTransactionData deserializes an ArrayList object and imports it to the local ArrayList
     * @return a boolean, true == success, false == unsuccessful
     */
    boolean importTransactionData () {
        ArrayList<Transactions> temp;
        boolean success = true;
        try {
            FileInputStream fis = new FileInputStream("TransactionData.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (ArrayList<Transactions>) ois.readObject() ;
            transactions = temp;
        }
        catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    /**
     * exportTransactionData serializes the ArrayList and stores in a file
     * @return a boolean, true if successful
     */
    boolean exportTransactionData () {
        boolean success = true;
        try {
            FileOutputStream fos = new FileOutputStream("TransactionData.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(transactions);
            fos.close();
        }
        catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    /**
     * sellVehicle allows an employee to initiate a sales transaction. Requires there to be at least 1 Vehicle,
     * 1 Customer, and 1 Employee to initiate a transacion
     * @param inv represents the Vehicle object
     * @param user_db, represents where User objects are stored
     */
    void sellVehicle (Vehicle inv, User user_db) {
        if (inv.isEmpty()) {
            System.out.println("ERROR: There are no vehicles in the inventory");
            return;
        }
        if (user_db.isEmpty()) {
            System.out.println ("ERROR: There are no users in the database");
            return;
        }
        if (!user_db.anyCustInDB()) {
            System.out.println("ERROR: There are no customers in the database");
            return;
        }
        if (!user_db.anyEmpInDB()) {
            System.out.println("ERROR: There are no employees in the database to facilitate a sale");
            return;
        }

        Scanner sc = new Scanner(System.in);

        System.out.print("Before continuing with sale, please enter your employee ID number: ");
        int search;
        boolean i;
        Transactions temp = new Transactions();

        do {
            search = sc.nextInt();
            if (user_db.returnInstanceTypeEmp(search) instanceof Employee) {
                i = false;
            }
            else {
                System.out.print("The provided ID number is not a valid employee ID, try again: ");
                i = true;
            }
        } while (i);
        temp.setEmployeeID(search);

        System.out.print("Enter the customer's ID: ");
        do {
            search = sc.nextInt();
            if (user_db.returnInstanceTypeCust(search) instanceof Customer) {
                i = false;
            }
            else {
                System.out.print("The provided ID number is not a valid customer ID, try again: ");
                i = true;
            }
        } while (i);
        temp.setCustomerID(search);

        System.out.println("Which vehicle type are you selling? ");
        System.out.println("1. PASSENGER\n" +
                "2. MOTORCYCLE\n" +
                "3. TRUCK\n" +
                "Please select an option above: ");
        int yy;
        yy = sc.nextInt();
        switch (yy) {
            case 1:
                String passenger;
                System.out.println("Please enter the vehicle's VIN: ");
                do {
                    sc.nextLine();
                    passenger = sc.nextLine();
                    if (inv.doesVehicleExistObj(passenger) instanceof Passenger) {
                        i = false;
                    }
                    else {
                        System.out.println("The VIN does not exist in the database. Try again.");
                        i = true;
                    }
                } while (i);
                temp.setVIN(passenger);
                inv.quickDelVIN(passenger);
                break;
            case 2:
                String motorcycle;
                System.out.println("Please enter the vehicle's VIN: ");
                do {
                    motorcycle = sc.nextLine();
                    if (inv.doesVehicleExistObj(motorcycle) instanceof Motorcycle) {
                        i = false;
                    }
                    else {
                        System.out.println("The VIN does not exist in the database. Try again.");
                        i = true;
                    }
                } while (i);
                temp.setVIN(motorcycle);
                inv.quickDelVIN(motorcycle);
                break;
            case 3:
                String truck;
                System.out.println("Please enter the vehicle's VIN: ");
                do {
                    truck = sc.nextLine();
                    if (inv.doesVehicleExistObj(truck) instanceof Trucks) {
                        i = false;
                    }
                    else {
                        System.out.println("The VIN does not exist in the database. Try again.");
                        i = true;
                    }
                } while (i);
                temp.setVIN(truck);
                inv.quickDelVIN(truck);
                break;
        }

        Date now = new Date();
        temp.setSaleDate(now);

        float sale;
        System.out.println("Please enter the final sale price of the vehicle: ");
        sale = sc.nextFloat();
        temp.setFinalSalePrice(sale);
        transactions.add(temp);

        System.out.println("Success: sales transaction successfully completed!");
    }

    /**
     * showCompletedSaleTransactions prints to the screen the ArrayList contents
     */
    void showCompletedSaleTransactions () {
        printSeparatorLine();
        printHeaderLine();
        if (transactions.isEmpty()) {
            System.out.print("\n*                    ~ No sales transactions have been completed ~                 *");
            printSeparatorLine();
            return;
        }
        for (int i = 0; i < transactions.size(); ++i) {
            printSpecificTransaction(i);
        }
        printSeparatorLine();
    }
}