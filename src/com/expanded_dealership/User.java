package com.expanded_dealership;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 *  User class represents the 2 users or actors for this dealership, specifically the Customer and Employee
 *  @author Darren Rambaud (d_r273)
 *  @version 10/3/2016
 */
public class User implements Serializable{
    private final static int SIZE = 5,
                             FIELD_LEN = 20,
                             NUM_HEADER_CHARS = 102;
    private final static char HEADER_CHAR = '*';

    private int id_no;
    private String first_name,
                   last_name;
    private ArrayList<User> user_db = new ArrayList<>(SIZE);

    User () {
    }

    /**
     * setIDNO is a setter for attribute id_no
     * @param id_no, an int
     */
    void setIDNO (int id_no) {
        this.id_no = id_no;
    }

    /**
     * setFirstName is a  setter for first_name
     * @param first_name, a String
     */
    void setFirstName (String first_name) {
        this.first_name = first_name;
    }

    /**
     * setLastName is a setter for last_name
     * @param last_name, a String
     */
    void setLastName (String last_name) {
        this.last_name = last_name;
    }

    /**
     * getID_NO is a getter for id_no
     * @return id_no, an int
     */
    int getID_NO () {
        return id_no;
    }

    /**
     * getFirstName is a getter for first_name
     * @return first_name, a String
     */
    String getFirstName () {
        return first_name;
    }

    /**
     * getLastName is a getter for last_name
     * @return last_name
     */
    String getLastName () {
        return last_name;
    }

    /**
     * importUserData imports a serialized object from a file
     * @return boolean, true == success, false == not successful
     */
    boolean importUserData () {
        ArrayList<User> temp;
        boolean success = true;
        try {
            FileInputStream fis = new FileInputStream("UserData.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (ArrayList<User>) ois.readObject() ;
            user_db = temp;
        }
        catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    /**
     * exportUserData exports the ArrayList to a serialized object and stores in a file
     * @return a boolean, true == successful, otherwise unsuccessful
     */
    boolean exportUserData () {
        boolean success = true;
        try {
            FileOutputStream fos = new FileOutputStream("UserData.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user_db);
            fos.close();
        }
        catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    /**
     * showListofUsers prints to the screen the attributes of each object in the ArrayList
     */
    void showListofUsers () {
        printSeparatorLine();
        printHeaderLine();
        if (user_db.isEmpty()) {
            System.out.print("\n"+HEADER_CHAR+"             ~ The user database is empty ~                    "+HEADER_CHAR+"");
        }
        else {
            for (int i = 0; i < user_db.size(); ++i) {
                printSpecificUser(i);
            }
        }
        printSeparatorLine();
    }

    /**
     * printHeaderLine prints the basic headers for easy reading
     */
    private void printHeaderLine () {
        System.out.print(""+HEADER_CHAR+" ");
        System.out.printf("%-"+(FIELD_LEN - 10)+"s", "ID NUMBER");
        System.out.printf("%-"+(FIELD_LEN - 8)+"s", "FIRST NAME");
        System.out.printf("%-"+(FIELD_LEN - 8)+"s", "LAST NAME");
        System.out.printf("%-"+(FIELD_LEN + 44)+"s "+HEADER_CHAR+"", "ATTRIBUTES");
    }

    /**
     * printSeparatorLine prints HEADER_CHAR for NUM_HEADER_CHARS
     */
    private void printSeparatorLine() {
        System.out.print("\n");
        for (int i = 0; i < NUM_HEADER_CHARS; ++i){
            System.out.print(HEADER_CHAR);
        }
        System.out.print("\n");
    }

    /**
     * printSpecificUser prints attributes of a single object in the ArrayList
     * @param y, an int represents the index
     */
    private void printSpecificUser (int y) {
        System.out.print("\n* ");
        System.out.printf("%-"+(FIELD_LEN - 10)+"s", user_db.get(y).getID_NO());
        System.out.printf("%-"+(FIELD_LEN - 8)+"s", user_db.get(y).getFirstName());
        System.out.printf("%-"+(FIELD_LEN - 8)+"s", user_db.get(y).getLastName());
        if (user_db.get(y) instanceof Employee) {
            User myObjEmp = user_db.get(y);
            Employee mySubEmp = (Employee) myObjEmp;
            System.out.printf("(EMPLOYEE) Monthly Salary: $%-"+(FIELD_LEN - 8)+".2f" ,mySubEmp.getMonthlySalary());
            System.out.printf("%-"+(FIELD_LEN + 5)+"s"+HEADER_CHAR+"" ,"Bank Acc #: "+mySubEmp.getDirectDepNo()+"");
        }

        if (user_db.get(y) instanceof Customer) {
            User myObjCust = user_db.get(y);
            Customer mySubCust = (Customer) myObjCust;
            System.out.printf("%-"+(FIELD_LEN + 15)+"s" ,"(CUSTOMER) Phone #: "+mySubCust.getPhoneNumber()+"; ");
            System.out.printf("%-"+(FIELD_LEN + 10)+"s"+HEADER_CHAR+"" ,"DL #: "+mySubCust.getStateAbbr()+"-"+mySubCust.getDL()+"");
        }
    }

    /**
     * anyEmpInDB determines whether or not there is an instance of Employee in the ArrayList
     * @returns a boolean, true if Employee exists, false otherwise
     */
    boolean anyEmpInDB () {
        for (User i : user_db) {
            if (i instanceof Employee) {
                return true;
            }
        }
        return false;
    }

    /**
     * anyCustInDB determines whether or not there is an instance of Customer in ArrayList
     * @return a boolean, true if Customer exists, false otherwise
     */
    boolean anyCustInDB () {
        for (User i : user_db) {
            if (i instanceof Customer) {
                return true;
            }
        }
        return false;
    }

    /**
     * addNewUser allows the user to either add an instance of Customer or Employee to the Array List with associated
     * attributes
     */
    void addNewUser () {
        Scanner in = new Scanner(System.in);
        Pattern z = Pattern.compile("^[0-9]+$");
        int query;
        String input;
        boolean invalid,
                onlyNumeric;

        System.out.println("Which type of user would you like to add?");
        do {
            System.out.println("1. CUSTOMER\n" +
                               "2. EMPLOYEE");
            System.out.print("Select an option between 1 and 2: ");
            query = in.nextInt();
            in.nextLine();

            if (query < 1 || query > 2) {
                System.out.println("ERROR: You have selected an invalid option. Please try again");
                invalid = true;
            }
            else {
                invalid = false;
            }
        } while (invalid);

        switch (query) {
            case 1:
                Customer temp_obj = new Customer();

                do {
                    System.out.print("Please enter the ID NUMBER of the new customer: ");
                    input = in.nextLine();

                    onlyNumeric = z.matcher(input).find();
                    if (!onlyNumeric) {
                        System.out.println("ERROR: You have entered non-numeric characters. Please try again.");
                        invalid = true;
                    }
                    else if (input.length() != 5) {
                        System.out.println("ERROR: The ID Number you have entered is too long or too short. Please " +
                                           "try again.");
                        invalid = true;
                    }
                    else if (quickIDSearch(Integer.parseInt(input))) {
                        System.out.println("ERROR: The ID Number already exists in the database! Please try again.");
                        invalid = true;
                    }
                    else {
                        invalid = false;
                    }
                } while (invalid);
                temp_obj.setIDNO(Integer.parseInt(input));

                System.out.print("Enter the FIRST NAME of the customer: ");
                input = in.nextLine();
                temp_obj.setFirstName(input);

                System.out.print("Enter the LAST NAME of the customer: ");
                input = in.nextLine();
                temp_obj.setLastName(input);

                do {
                    System.out.print("Enter the PHONE NUMBER of the customer (AAA-BBB-CCCC or AAABBBCCCC): ");
                        input = in.nextLine();
                        if (!input.matches("^(\\d{3}-?\\d{3}-?\\d{4})$")) {
                            System.out.println("ERROR: Bad input detected. Please check your input and try again.");
                            invalid = true;
                        }
                        else {
                            invalid = false;
                        }
                    } while (invalid);
                    temp_obj.setPhoneNumber(input);

                    System.out.println("Now entering the customers driver's license information ...");
                    do {
                        System.out.print("Please enter the 2 character state abbreviation of the customer's driver's" +
                                " license (eg, Texas == 'TX'): ");
                        input = in.nextLine();
                        if (input.length() != 2) {
                            System.out.print("ERROR: Invalid state abbreviation. Please try again");
                            invalid = true;
                    }
                    else {
                        invalid = false;
                    }
                } while (invalid);
                temp_obj.setStateAbbr(input);

                System.out.print("Please enter the driver's license number of the customer: ");
                query = in.nextInt();
                temp_obj.setDL(query);

                user_db.add(temp_obj);
                System.out.println("SUCCESS: New customer has been added to the database!");
                break;
            case 2:
                Employee emp_temp_obj = new Employee();

                do {
                    System.out.print("Please enter the ID NUMBER of the new employee: ");
                    input = in.nextLine();

                    onlyNumeric = z.matcher(input).find();
                    if (!onlyNumeric) {
                        System.out.println("ERROR: You have entered non-numeric characters. Please try again.");
                        invalid = true;
                    }
                    else if (input.length() != 5) {
                        System.out.println("ERROR: The ID Number you have entered is too long or too short. Please " +
                                           "try again.");
                        invalid = true;
                    }
                    else if (quickIDSearch(Integer.parseInt(input))) {
                        System.out.println("ERROR: The ID Number already exists in the database! Please try again.");
                        invalid = true;
                    }
                    else {
                        invalid = false;
                    }
                } while (invalid);
                emp_temp_obj.setIDNO(Integer.parseInt(input));

                System.out.print("Enter the FIRST NAME of the employee: ");
                input = in.nextLine();
                emp_temp_obj.setFirstName(input);

                System.out.print("Enter the LAST NAME of the employee: ");
                input = in.nextLine();
                emp_temp_obj.setLastName(input);

                float salary;
                do {
                    System.out.print("Enter the MONTHLY SALARY of the employee (in USD): ");
                    salary = in.nextFloat();
                    if (salary < 0) {
                        System.out.print("ERROR: Monthly salary cannot be a negative value. Please try again");
                        invalid = true;
                    }
                    else {
                        invalid = false;
                    }
                } while (invalid);
                emp_temp_obj.setMonthlySalary(salary);

                do {
                    System.out.print("Please enter the employee's BANK ACCOUNT NUMBER: ");
                    query = in.nextInt();
                    if (query < 0) {
                        System.out.print("ERROR: Invalid bank account number");
                        invalid = true;
                    }
                    else {
                        invalid = false;
                    }
                } while (invalid);
                emp_temp_obj.setDirectDepNo(query);

                user_db.add(emp_temp_obj);
                System.out.println("SUCCESS: New employee has been added to the database!");
                break;
        }

    }

    /**
     * updateUser allows the user to either update an Customer or Employee object
     */
    void updateUser () {
        Scanner sc = new Scanner(System.in);
        String search;
        boolean leaveLoop;

        do {
            System.out.print("\nPlease enter the user's ID Number you wish to update: ");
            search = sc.nextLine();

            if (search.length() != 5) {
                System.out.print("ERROR: The ID Number is too short or too long.");
                leaveLoop = false;
            }
            else {
                leaveLoop = true;
            }
        } while (!leaveLoop);

        int record = returnObjID(Integer.parseInt(search));
        int choice;

        if (record != -1) {
            System.out.println("User record has been located. See below for his/her information.");
            printSeparatorLine();
            printHeaderLine();
            printSpecificUser(record);
            printSeparatorLine();
            System.out.println("Which field would you like to update?");
            System.out.println("1. FIRST NAME & LAST NAME");
            if (user_db.get(record) instanceof Employee) {
                System.out.println("2. MONTHLY SALARY\n" +
                                   "3. BANK ACCOUNT NUMBER");
            }
            else {
                System.out.println("2. PHONE NUMBER\n" +
                                   "3. DRIVER'S LICENSE INFO");
            }
            System.out.print("Please select a choice from above: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter the person's new FIRST NAME: ");
                    sc.nextLine();
                    user_db.get(record).setFirstName(sc.nextLine());
                    System.out.print("Enter the person's new LAST NAME: ");
                    user_db.get(record).setLastName(sc.nextLine());
                    break;
                case 2:
                    float change;
                    boolean jj;
                    String ph;
                    if (user_db.get(record) instanceof Employee) {
                        User myObj = user_db.get(record);
                        Employee mySub = (Employee) myObj;
                        System.out.print("Enter the employee's new MONTHLY SALARY: ");
                        do {
                            change = sc.nextFloat();
                            if (change < 0) {
                                System.out.print("Negative value detected. Please try again");
                                jj = true;
                            }
                            else {
                                jj = false;
                            }
                        } while (jj);
                        mySub.setMonthlySalary(change);
                    }
                    else {
                        User custObj = user_db.get(record);
                        Customer myCustObj = (Customer) custObj;
                        System.out.print("Enter the customer's new PHONE NUMBER: ");
                        sc.nextLine();
                        ph = sc.nextLine();
                        myCustObj.setPhoneNumber(ph);
                    }
                    break;
                case 3:
                    String abbr;
                    if (user_db.get(record) instanceof Employee) {
                        User myEmpObj = user_db.get(record);
                        Employee myEmpSub = (Employee) myEmpObj;
                        System.out.print("Enter the employee's new BANK ACCOUNT NUMBER: ");
                        myEmpSub.setDirectDepNo(sc.nextInt());
                    }
                    else {
                        System.out.println("Now updating customer's driver's license information: ");
                        User myCustObj = user_db.get(record);
                        Customer myCustSub = (Customer) myCustObj;
                        System.out.print("Enter the 2 character state abbreviation for DL: ");
                        sc.nextLine(); // consume newline char
                        abbr = sc.nextLine();
                        myCustSub.setStateAbbr(abbr);

                        System.out.print("Enter the DL number: ");
                        myCustSub.setDL(sc.nextInt());
                    }
                    break;
            }
            System.out.print("SUCCESS: User information has been updated!");
        }
        else {
            System.out.println("No records exist under the search term you specified.");
        }
    }

    /**
     * quickIDSearch is helper function that returns a boolean based upon SEARCH term
     * @param SEARCH a final int, which is the search term
     * @return a boolean, true == found, else false o/w
     */
    private boolean quickIDSearch (final int SEARCH) {
        for (User i : user_db) {
            if (i.getID_NO() == SEARCH) {
                return true;
            }
        }
        return false;
    }

    /**
     * returnInstanceTypeEmp returns a User object of Employee instance assuming the SEARCH matches with ID_NO
     * @param SEARCH, is a final int, which is the ID_NO
     * @return a Employee object, else null
     */
    User returnInstanceTypeEmp (final int SEARCH) {
        for (User i : user_db) {
            if (i.getID_NO() == SEARCH && i instanceof Employee) {
                return i;
            }
        }
        return null;
    }

    /**
     * returnInstanceTypeCust returns a Customer object assuming the SEARCH == getID_NO
     * @param SEARCH is a final int, representing the ID_NO
     * @return a Customer object, else null
     */
    User returnInstanceTypeCust (final int SEARCH) {
        for (User i : user_db) {
            if (i.getID_NO() == SEARCH && i instanceof Customer) {
                return i;
            }
        }
        return null;
    }

    /**
     * returnObjID is a helper function which returns the index at which the object is found
     * @param SEARCH represents the seach term
     * @return an int, representing the index at which object is found, else -1 is returned
     */
    private int returnObjID (final int SEARCH) {
        for (int i = 0; i < user_db.size(); ++i) {
            if (SEARCH == user_db.get(i).getID_NO()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * isEmpty determins whether or not the ArrayList is empty
     * @return
     */
    boolean isEmpty () {
        return user_db.isEmpty();
    }
}

/**
 * Employee is a subclass of User, includes monthly salary and direct deposit attributes
 */
class Employee extends User {
    private float monthly_salary;
    private int direct_dep_no;

    /**
     * setMonthlySalary is setter
     * @param monthly_salary, a float
     */
    void setMonthlySalary (float monthly_salary) {
        this.monthly_salary = monthly_salary;
    }

    /**
     * getMonthlySalary is a getter for monthly_salary
     * @return monthly_salary, a float
     */
    float getMonthlySalary () {
        return monthly_salary;
    }

    /**
     * setDirectDepNo is a setter
     * @param direct_dep_no, an int
     */
    void setDirectDepNo (int direct_dep_no) {
        this.direct_dep_no = direct_dep_no;
    }

    /**
     * getDirectDepNo is a getter
     * @return direct_dep_no, an int
     */
    int getDirectDepNo () {
        return direct_dep_no;
    }
}

/**
 * Customer extends User class with attributes phone number, state_abbrv, drivers_license_no
 */
class Customer extends User {
    private String phone_number;
    private String state_abbr;
    private int drivers_license_no;

    /**
     * setPhoneNumber is a setter
     * @param phone_number is a String
     */
    void setPhoneNumber (String phone_number) {
        this.phone_number = phone_number;
    }

    /**
     * getPhoneNumber is a getter
     * @return phone_number, a String
     */
    String getPhoneNumber () {
        return phone_number;
    }

    /**
     * setDL is a setter
     * @param drivers_license_no, an int
     */
    void setDL (int drivers_license_no) {
        this.drivers_license_no = drivers_license_no;
    }

    /**
     * getDL is a getter
     * @return drivers_license_no, an int
     */
    int getDL () {
        return drivers_license_no;
    }

    /**
     * setStateAbbr is a setter
     * @param state_abbr, is a String
     */
    void setStateAbbr (String state_abbr) {
        this.state_abbr = state_abbr;
    }

    /**
     * getStateAbbr is a getter
     * @return state_abbr, a String
     */
    String getStateAbbr () {
        return state_abbr;
    }
}