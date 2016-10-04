package com.expanded_dealership;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *  Vehicle class of the expanded_dealership package represents a single object of a vehicle.
 *  @author Darren Rambaud (d_r273)
 *  @version 10/3/2016
 */
public class Vehicle implements Serializable {
    private static final int SIZE = 5,
                             CAR = 1,
                             TRUCK = 2,
                             MOTORCYCLE = 3,
                             MIN_YEAR = 1920,
                             MAX_YEAR = 2017,
                             FIELD_LEN = 20,
                             NUM_HEADER_CHARS = 145;

    private static final char HEADER_CHAR = '*';

    private static final String STR_VIN = "VIN",
                                STR_MAKE = "MAKE",
                                STR_MODEL = "MODEL",
                                STR_YEAR = "YEAR",
                                STR_MILEAGE = "MILEAGE",
                                STR_PRICE  = "PRICE (USD)";

    private String VIN,
                   make,
                   model;
    private int year,
                mileage;
    private float price;
    private ArrayList<Vehicle> vehicle_db = new ArrayList<>(SIZE);

    /**
     * Vehicle is the default constructor for this class. It has a blank body and only exists for the sole purpose
     * of the subclasses.
     */
    Vehicle () {
    }

    /**
     * printSeparatorLine prints to the screen via an iterating for loop over a desired NUM_HEADER_CHARS
     */
    void printSeparatorLine() {
        System.out.print("\n");
        for (int i = 0; i < NUM_HEADER_CHARS; ++i){
            System.out.print(HEADER_CHAR);
        }
        System.out.print("\n");
    }

    /**
     * printHeaderLine prints the header line to assist the user in determining the values
     */
    void printHeaderLine () {
        System.out.print(""+HEADER_CHAR+" ");
        System.out.printf("%-"+(FIELD_LEN - 13)+"s", STR_VIN);
        System.out.printf("%-"+(FIELD_LEN - 7)+"s", STR_MAKE);
        System.out.printf("%-"+(FIELD_LEN - 7)+"s", STR_MODEL);
        System.out.printf("%-"+(FIELD_LEN - 10)+"s", STR_YEAR);
        System.out.printf("%-"+(FIELD_LEN - 8)+"s", STR_MILEAGE);
        System.out.printf("%-"+(FIELD_LEN - 6)+"s", STR_PRICE);
        System.out.printf("%-"+(FIELD_LEN + 52)+"s "+HEADER_CHAR+"", " SPECIAL ATTRIBUTES");
    }

    /**
     * importInvData method imports a serialized ArrayList object from VehicleData.ser. If the file does not exist,
     * a print trace stack is outputted to the screen and the user is allowed to continue with a blank database.
     * @return a boolean, success, which indicates whether or not import of the serialized object was clean
     */
    boolean importInvData () {
        ArrayList<Vehicle> temp;
        boolean success = true;
        try {
            FileInputStream fis = new FileInputStream("VehicleData.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
             temp = (ArrayList<Vehicle>) ois.readObject() ;
            vehicle_db = temp;
        }
        catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    /**
     * exportInvData method will write the vehicle_db object to VehicleData.ser
     * @return a boolean. If false, writing to VehicleData.ser was unsuccessful
     */
    boolean exportInvData () {
        boolean success = true;
        try {
            FileOutputStream fos = new FileOutputStream("VehicleData.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(vehicle_db);
            fos.close();
        }
        catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    /**
     * showAllExistingRecords prints to the screen a list of current records in vehicle_db
     */
    void showAllExistingRecords () {
        printSeparatorLine();
        printHeaderLine();
        if (vehicle_db.isEmpty()) {

            System.out.print("\n"+HEADER_CHAR+"             ~ The vehicle inventory is empty ~                    "+HEADER_CHAR+"");

        }
        else {
            printLineOfVehicleInfo();
        }
        printSeparatorLine();
    }

    /**
     * addNewVehicle allows a user to add Vehicle of object. Each field is input-checked before adding to the ArrayList
     * @param selection an int which indicates which subclass of Vehicle the user would like to add (1= Passenger,
     *                  2 = Truck, 3 = Motorcycle)
     * @param limit is an int which indicates the number of characters allowed for when inputting the VIN
     */
    void addNewVehicle (int selection, int limit) {
        Scanner sc = new Scanner(System.in);
        String temp;
        int i_temp;
        boolean hasNonAlphaNumeric,
                valid = false;
        float f_temp;

        switch (selection) {
            case CAR:
                Passenger temp_obj = new Passenger();

                do {
                    System.out.print("Please enter the VIN of the passenger vehicle: ");
                    temp = sc.nextLine();

                    Pattern x = Pattern.compile("^[a-zA-Z0-9]+$");
                    hasNonAlphaNumeric = x.matcher(temp).find();
                    if (temp.length() != limit) {
                        System.out.println("ERROR: You have entered a VIN that is too long or too short. Please try again.");
                    }
                    else if (!hasNonAlphaNumeric) {
                        System.out.println("ERROR: You have entered a VIN containing non-alphanumeric characters. Please try again.");
                    }
                    else {
                        valid = true;
                    }

                    if (doesVehicleExist(temp)) {
                        System.out.println("ERROR: The VIN is already associated with another vehicle. Please try again.");
                        valid = false;
                    }

                } while (!valid);
                temp_obj.setVIN(temp.toUpperCase());

                System.out.print("Please enter the MAKE of the passenger vehicle: ");
                temp = sc.nextLine();
                temp_obj.setMake(temp);

                System.out.print("Please enter the MODEL of the passenger vehicle: ");
                temp = sc.nextLine();
                temp_obj.setModel(temp);

                do {
                    System.out.print("Please enter the YEAR of the passenger vehicle: ");
                    i_temp = sc.nextInt();

                    if (i_temp < MIN_YEAR || i_temp > MAX_YEAR) {
                        System.out.println("ERROR: You have entered a value outside of the current range ("+MIN_YEAR+"-" +
                                         ""+MAX_YEAR+"). Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                temp_obj.setYear(i_temp);

                do {
                    System.out.print("Please enter the MILEAGE of the passenger vehicle: ");
                    i_temp = sc.nextInt();

                    if (i_temp < 0) {
                        System.out.println("ERROR: You have entered a negative value. Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                temp_obj.setMileage(i_temp);

                do {
                    System.out.print("Please enter the passenger vehicles retail PRICE (in USD) value: ");
                    f_temp = sc.nextFloat();

                    if (f_temp < 0) {
                        System.out.println("ERROR: You have entered a negative value. Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }

                } while (!valid);
                temp_obj.setPrice(f_temp);

                System.out.print("Please enter the BODY STYLE of the passenger vehicle: ");
                sc.nextLine(); // consuming '\n' character
                temp = sc.nextLine();
                temp_obj.setBodyStyle(temp);

                vehicle_db.add(temp_obj);
                System.out.println("\nSUCCESS: Your vehicle has been added to the database!");
                break;
            case TRUCK:
                Trucks t_temp_obj = new Trucks();

                do {
                    System.out.print("Please enter the VIN of the truck: ");
                    temp = sc.nextLine();

                    Pattern x = Pattern.compile("^[a-zA-Z0-9]+$");
                    hasNonAlphaNumeric = x.matcher(temp).find();
                    if (temp.length() != limit) {
                        System.out.println("ERROR: You have entered a VIN that is too long or too short. Please try again.");
                    }
                    else if (!hasNonAlphaNumeric) {
                        System.out.println("ERROR: You have entered a VIN containing non-alphanumeric characters. Please try again.");
                    }
                    else {
                        valid = true;
                    }

                    if (doesVehicleExist(temp)) {
                        System.out.println("ERROR: The VIN is already associated with another vehicle. Please try again.");
                        valid = false;
                    }
                } while (!valid);
                t_temp_obj.setVIN(temp.toUpperCase());

                System.out.print("Please enter the MAKE of the truck: ");
                temp = sc.nextLine();
                t_temp_obj.setMake(temp);

                System.out.print("Please enter the MODEL of the truck: ");
                temp = sc.nextLine();
                t_temp_obj.setModel(temp);

                do {
                    System.out.print("Please enter the YEAR of the truck: ");
                    i_temp = sc.nextInt();

                    if (i_temp < MIN_YEAR || i_temp > MAX_YEAR) {
                        System.out.println("ERROR: You have entered a value outside of the current range ("+MIN_YEAR+"-" +
                                ""+MAX_YEAR+"). Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                t_temp_obj.setYear(i_temp);

                do {
                    System.out.print("Please enter the MILEAGE of the truck: ");
                    i_temp = sc.nextInt();

                    if (i_temp < 0) {
                        System.out.println("ERROR: You have entered a negative value. Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                t_temp_obj.setMileage(i_temp);

                do {
                    System.out.print("Please enter the trucks retail PRICE (in USD) value: ");
                    f_temp = sc.nextFloat();

                    if (f_temp < 0) {
                        System.out.println("ERROR: You have entered a negative value. Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                t_temp_obj.setPrice(f_temp);

                do {
                    System.out.print("Please enter the MAXIMUM LOAD WEIGHT (in pounds) of the truck: ");
                    sc.nextLine(); // consuming '\n' character
                    f_temp = sc.nextFloat();

                    if (f_temp < 0.0) {
                        System.out.println("ERROR: You have entered a negative value. Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                t_temp_obj.setMaximumLoadWeight(f_temp);

                do {
                    System.out.print("Please enter the LENGTH (in feet) of the truck: ");
                    sc.nextLine(); // consuming '\n' character
                    i_temp = sc.nextInt();

                    if (i_temp < 0.0) {
                        System.out.println("ERROR: You have entered a negative value. Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                t_temp_obj.setLength(i_temp);

                vehicle_db.add(t_temp_obj);
                System.out.println("\nSUCCESS: Your truck has been added to the database!");
                break;
            case MOTORCYCLE:
                Motorcycle m_temp_obj = new Motorcycle();

                do {
                    System.out.print("Please enter the VIN of the motorcycle: ");
                    temp = sc.nextLine();

                    Pattern x = Pattern.compile("^[a-zA-Z0-9]+$");
                    hasNonAlphaNumeric = x.matcher(temp).find();
                    if (temp.length() != limit) {
                        System.out.println("ERROR: You have entered a VIN that is too long or too short. Please try again.");
                    }
                    else if (!hasNonAlphaNumeric) {
                        System.out.println("ERROR: You have entered a VIN containing non-alphanumeric characters. Please try again.");
                    }
                    else {
                        valid = true;
                    }

                    if (doesVehicleExist(temp)) {
                        System.out.println("ERROR: The VIN is already associated with another vehicle. Please try again.");
                        valid = false;
                    }
                } while (!valid);
                m_temp_obj.setVIN(temp.toUpperCase());

                System.out.print("Please enter the MAKE of the motorcycle: ");
                temp = sc.nextLine();
                m_temp_obj.setMake(temp);

                System.out.print("Please enter the MODEL of the motorcycle: ");
                temp = sc.nextLine();
                m_temp_obj.setModel(temp);

                do {
                    System.out.print("Please enter the YEAR of the motorcycle: ");
                    i_temp = sc.nextInt();

                    if (i_temp < MIN_YEAR || i_temp > MAX_YEAR) {
                        System.out.println("ERROR: You have entered a value outside of the current range ("+MIN_YEAR+"-" +
                                ""+MAX_YEAR+"). Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                m_temp_obj.setYear(i_temp);

                do {
                    System.out.print("Please enter the MILEAGE of the motorcycle: ");
                    i_temp = sc.nextInt();

                    if (i_temp < 0) {
                        System.out.println("ERROR: You have entered a negative value. Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                m_temp_obj.setMileage(i_temp);

                do {
                    System.out.print("Please enter the motorcycles retail PRICE (in USD) value: ");
                    f_temp = sc.nextFloat();

                    if (f_temp < 0) {
                        System.out.println("ERROR: You have entered a negative value. Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                m_temp_obj.setPrice(f_temp);

                System.out.print("Please enter the TYPE of motorcycle (eg, Scooter, Touring, Street Bike, etc): ");
                sc.nextLine(); // consuming '\n' character
                temp = sc.nextLine();
                m_temp_obj.setType(temp);

                do {
                    System.out.print("Please enter the motorcycles ENGINE DISPLACEMENT (in CC): ");
                    i_temp = sc.nextInt();

                    if (i_temp < 0) {
                        System.out.println("ERROR: You have entered a negative value. Please try again.");
                        valid = false;
                    }
                    else {
                        valid = true;
                    }
                } while (!valid);
                m_temp_obj.setEngineDisplacement(i_temp);

                vehicle_db.add(m_temp_obj);
                System.out.println("\nSUCCESS: Your motorcycle has been added to the database!");
                break;
        }
    }

    /**
     * quickDelVIN is a method which removes a single object from the ArrayList based upon VIN
     * @precondition VIN.size() is guaranteed to be  == 5
     * @param VIN is a String, which is the search term
     */
    void quickDelVIN (String VIN) {
        for (Vehicle i : vehicle_db) {
            if (VIN.equalsIgnoreCase(i.getVIN())) {
                vehicle_db.remove(i);
                return;
            }
        }
    }

    /**
     * delVehiclebyVIN is a method which goes through a detailed process to remove a vehicle based on a unique VIN
     * string
     * @param LIMIT is a final int, passed to the method to indicate the limit of the VIN inputted
     * @return an int, if -1, then the ArrayList is empty, if -2 then unable to locate vehicle based upon inputted VIN,
     *          and returns 0 if delete was successful
     */
    int delVehiclebyVIN (final int LIMIT) {
        if (vehicle_db.isEmpty()) {
            return -1;
        }
        Scanner in = new Scanner(System.in);
        String query;
        boolean valid;

        do {
            System.out.print("\nList of vehicle(s) in the database: ");
            showAllExistingRecords();
            System.out.println("\nPlease enter the VIN of the vehicle you with to remove: ");
            query = in.nextLine();

            if (query.length() != LIMIT) {
                System.out.println("\nERROR: Invalid VIN entered, please try again ...");
                valid = false;
            }
            else {
                valid = true;
            }
        } while(!valid);

        for (int i = 0; i < vehicle_db.size(); ++i) {
            if (query.equalsIgnoreCase(vehicle_db.get(i).getVIN())) {
                vehicle_db.remove(i);
                return 0;
            }
        }
        return -2; // NOT FOUND
    }

    /**
     * searchViaVIN allows the user to search for a vehicle based upon a desired VIN
     * @param LIMITATION indicates the number of characters allowed for a VIN entry
     * @return an int, which is the index of which the object is found at. If -1, then the object was not located
     */
    int searchViaVIN (int LIMITATION) {
        Scanner in = new Scanner(System.in);
        String query;
        boolean isValidEnt;

        do {
            System.out.println("\nPlease enter the VIN of the vehicle you with to locate: ");
            query = in.nextLine();

            if (query.length() != LIMITATION) {
                System.out.println("\nERROR: Invalid VIN entered, please try again ...");
                isValidEnt = false;
            }
            else {
                isValidEnt = true;
            }
        } while(!isValidEnt);

        for (int i = 0; i < vehicle_db.size(); ++i) {
            if (query.equalsIgnoreCase(vehicle_db.get(i).getVIN())) {
                return i;
            }
        }
        return -1;
    }
    /**
     *  showVehiclesWithinPriceRange method allows the user, based on the arguments passed, to search for a vehicle
     *  based on the aforementioned criteria
     * @param LOWER_THRES is a final int which indicates the lower bound the user wants
     * @param HIGHER_THRES is a final int which indicates the higher bound the user wants
     * @param TYPE is the type of vehicle the user is searching for (1 = Motorcycle, 2 = Passenger, 3 = Truck)
     */
    void showVehiclesWithinPriceRange (final int LOWER_THRES, final int HIGHER_THRES, final int TYPE) {
        ArrayList<Integer> indices = new ArrayList<>();
        boolean withinRange = false;

        if (TYPE == 1) {
            for (int i = 0; i < vehicle_db.size(); ++i) {
                if (((vehicle_db.get(i).getPrice() <= HIGHER_THRES) && (vehicle_db.get(i).getPrice() >= LOWER_THRES))
                        && vehicle_db.get(i) instanceof Motorcycle) {
                    indices.add(i);
                    withinRange = true;
                }
            }
        }
        else if (TYPE == 2) {
            for (int i = 0; i < vehicle_db.size(); ++i) {
                if (((vehicle_db.get(i).getPrice() <= HIGHER_THRES) && (vehicle_db.get(i).getPrice() >= LOWER_THRES))
                        && vehicle_db.get(i) instanceof Passenger) {
                    indices.add(i);
                    withinRange = true;
                }
            }
        }
        else {
            for (int i = 0; i < vehicle_db.size(); ++i) {
                if (((vehicle_db.get(i).getPrice() <= HIGHER_THRES) && (vehicle_db.get(i).getPrice() >= LOWER_THRES))
                        && vehicle_db.get(i) instanceof Trucks) {
                    indices.add(i);
                    withinRange = true;
                }
            }
        }

        if (!withinRange) {
            System.out.println("\nSorry, there are no vehicle matches between price range: $" +LOWER_THRES+ " - " +
                               "$" +HIGHER_THRES+ " in our inventory\n");
        }
        else {
            System.out.println("\nThe following vehicles match your desired criteria ($"+LOWER_THRES+" - " +
                               "$"+HIGHER_THRES+")...");
            printSeparatorLine();
            printHeaderLine();
            for (Integer i : indices) {
                printSpecificVehicle(i);
            }
            printSeparatorLine();
        }
    }

    /**
     * getVIN is simple getter function for attribute String VIN
     * @return VIN, class attribute
     */
    String getVIN() {
        return VIN;
    }

    /**
     * getMake is a getter function for attribute String make
     * @return make, a String of the class attribute
     */
    String getMake() {
        return make;
    }

    /**
     * getModel is a simple getter function for String Model
     * @return model, a String
     */
    String getModel() {
        return model;
    }

    /**
     * getYear is a simple getter function for attribute year
     * @return year, an int
     */
    int getYear() {
        return year;
    }

    /**
     * getMileage is a getter function for attribute mileage
     * @return mileage, an int
     */
    int getMileage() {
        return mileage;
    }

    /**
     * getPrice is a getter function for attribute price
     * @return price, a float
     */
    float getPrice() {
        return price;
    }

    /**
     * setVIN is a mutator function for attribute VIN
     * @param VIN, a String
     */
    void setVIN (String VIN) {
        this.VIN = VIN;
    }

    /**
     * setMake a mutator function for attribute make
     * @param make, a String
     */
    void setMake (String make) {
        this.make = make;
    }

    /**
     * setModel is a mutator function for attribute model
     * @param model, a String
     */
    void setModel (String model) {
        this.model = model;
    }

    /**
     * setYear is a setter function for attribute year
     * @param year, an int
     */
    void setYear (int year) {
        this.year = year;
    }

    /**
     * setMileage is a setter function for attribute mileage
     * @param mileage, an int
     */
    void setMileage (int mileage) {
        this.mileage = mileage;
    }

    /**
     * setPrice is a mutator function for attribute price
     * @param price, a float
     */
    void setPrice (float price) {
            this.price = price;
        }

    /**
     * doesVehicleExist is a helper method that searches the ArrayList based upon its VIN
     * @precondition search.size() == 5
     * @param search, a String representing the VIN
     * @return true, if vehicle exists, else false
     */
    private boolean doesVehicleExist (String search) {
        boolean exist = false;
        for (Vehicle i : vehicle_db) {
            if (search.equalsIgnoreCase(i.getVIN())) {
                exist = true;
            }
        }
        return exist;
    }

    /**
     * isEmpty quickly returns whether or not the ArrayList is empty
     * @return a boolean, specifically vehicle_db.isEmpty() (NOT A RECURSIVE FUNCTION)
     */
    boolean isEmpty () {
        return vehicle_db.isEmpty();
    }

    /**
     * doesVehicleExistObj is a method that searches based upon a unique VIN
     * @param search, a String value
     * @return a Vehicle object if there is a match, otherwise null
     */
    Vehicle doesVehicleExistObj (String search) {
        for (Vehicle i : vehicle_db) {
            if (search.equalsIgnoreCase(i.getVIN())) {
                return i;
            }
        }
        return null;
    }

    /**
     * printSpecificVehicle prints one object to the screen based upon index
     * @param g is an int and represents the index
     */
    void printSpecificVehicle (int g) {
        System.out.print("\n* ");
        System.out.printf("%-"+(FIELD_LEN - 13)+"s", vehicle_db.get(g).getVIN());
        System.out.printf("%-"+(FIELD_LEN - 7)+"s", vehicle_db.get(g).getMake());
        System.out.printf("%-"+(FIELD_LEN - 7)+"s", vehicle_db.get(g).getModel());
        System.out.printf("%-"+(FIELD_LEN - 10)+"d", vehicle_db.get(g).getYear());
        System.out.printf("%-"+(FIELD_LEN - 8)+"d", vehicle_db.get(g).getMileage());
        System.out.printf("$%-"+(FIELD_LEN - 6)+".2f", vehicle_db.get(g).getPrice());
        if (vehicle_db.get(g) instanceof Passenger) {
            Vehicle myObj = vehicle_db.get(g);
            Passenger mySubPass = (Passenger) myObj;
            System.out.printf("%-"+(FIELD_LEN + 51)+"s "+HEADER_CHAR+"", "(PASSENGER) BODY STYLE: "+mySubPass.getBodyStyle()+"");
        }

        if (vehicle_db.get(g) instanceof Trucks) {
            Vehicle myObj = vehicle_db.get(g);
            Trucks mySubTruck = (Trucks) myObj;
            System.out.printf("%-"+(FIELD_LEN + 21)+"s", "(TRUCK) MAX LOAD WEIGHT (LB): "+mySubTruck.getMaximumLoadWeight()+"; ");
            System.out.printf("%-"+(FIELD_LEN + 10)+"s "+HEADER_CHAR+"", "LENGTH(LB): "+mySubTruck.getLength()+"");
        }

        if (vehicle_db.get(g) instanceof Motorcycle) {
            Vehicle myObj = vehicle_db.get(g);
            Motorcycle mySubMotor = (Motorcycle) myObj;
            System.out.printf("%-"+(FIELD_LEN + 21)+"s", "(MOTORCYCLE) TYPE: "+mySubMotor.getType()+"; ");
            System.out.printf("%-"+(FIELD_LEN + 10)+"s "+HEADER_CHAR+"","ENGINE DISPLACEMENT (CC): "+mySubMotor.getEngDisp()+"");
        }
    }

    /**
     * printLineOfVehicleInfo prints to the screen the entire ArrayList of Vehicle objects. Since each subclass has
     * unique attributes, those attributes are printed based upon their instance association
     */
    private void printLineOfVehicleInfo() {
        for (Vehicle i : vehicle_db) {
            System.out.print("\n* ");
            System.out.printf("%-" + (FIELD_LEN - 13) + "s", i.getVIN());
            System.out.printf("%-" + (FIELD_LEN - 7) + "s", i.getMake());
            System.out.printf("%-" + (FIELD_LEN - 7) + "s", i.getModel());
            System.out.printf("%-" + (FIELD_LEN - 10) + "d", i.getYear());
            System.out.printf("%-" + (FIELD_LEN - 8) + "d", i.getMileage());
            System.out.printf("$%-" + (FIELD_LEN - 6) + ".2f", i.getPrice());
            if (i instanceof Passenger) {
                Vehicle myObj = i;
                Passenger mySubPass = (Passenger) myObj;
                System.out.printf("%-"+(FIELD_LEN + 51)+"s "+HEADER_CHAR+"","(PASSENGER) BODY STYLE: "+mySubPass.getBodyStyle()+"");
            }

            if (i instanceof Trucks) {
                Vehicle myObj = i;
                Trucks mySubTruck = (Trucks) myObj;
                System.out.printf("%-"+(FIELD_LEN + 21)+"s", "(TRUCK) MAX LOAD WEIGHT (LB): "+mySubTruck.getMaximumLoadWeight()+"; ");
                System.out.printf("%-"+(FIELD_LEN + 10)+"s "+HEADER_CHAR+"", "LENGTH(LB): "+mySubTruck.getLength()+"");
            }

            if (i instanceof Motorcycle) {
                Vehicle myObj = i;
                Motorcycle mySubMotor = (Motorcycle) myObj;
                System.out.printf("%-"+(FIELD_LEN + 21)+"s","(MOTORCYCLE) TYPE: "+mySubMotor.getType()+"; ");
                System.out.printf("%-"+(FIELD_LEN + 10)+"s "+HEADER_CHAR+"","ENGINE DISPLACEMENT (CC): "+mySubMotor.getEngDisp()+"");
            }
        }
    }
}

/**
 * Class Passenger is an inheritance or subclass of Vehicle with the exception of having a unique body style
 */
class Passenger extends Vehicle {
    private String body_style;

    /**
     * setBodyStyle is a mutator for body_style
     * @param body_style, a String
     */
    void setBodyStyle (String body_style) {
        this.body_style = body_style;
    }

    /**
     * getBodyStyile is a simple getter for body_style
     * @return body_style, a String
     */
    String getBodyStyle () {
        return body_style;
    }
}

/**
 * Class Trucks is an inheritance or subclass of Vehicle with unique attributes Maximum Load Weight and Length
 */
class Trucks extends Vehicle {
    private float maximum_load_weight;
    private float length;

    /**
     * setMaximumLoadWeight is a mutator for maximum_load_weight
     * @param weight, is a float
     */
    void setMaximumLoadWeight (float weight) {
        maximum_load_weight = weight;
    }

    /**
     * setLength is a mutator function for length
     * @param length, is a float
     */
    void setLength (float length) {
        this.length = length;
    }

    /**
     * getMaximumLoadWeight is getter
     * @return maximum_load_weight, a float
     */
    float getMaximumLoadWeight () {
        return maximum_load_weight;
    }

    /**
     * getLength is a getter for length
     * @return length, a float
     */
    float getLength () {
        return length;
    }
}

/**
 * Motorcycle is an extension/inheritance of Vehicle with attributes of type and engine displacement
 */
class Motorcycle extends Vehicle {
    private String type;
    private int eng_displacement;

    /**
     * setType is a setter for type attribute
     * @param type, a String
     */
    void setType(String type){
        this.type = type;
    }

    /**
     * setEngineDisplacement is a setter
     * @param displacement, an int
     */
    void setEngineDisplacement(int displacement) {
        eng_displacement = displacement;
    }

    /**
     * getType is a getter for type
     * @return type, a String
     */
    String getType() {
        return type;
    }

    /**
     * getEngDisp is a getter for eng_displacement
     * @return eng_displacement, an int
     */
    int getEngDisp() {
        return eng_displacement;
    }
}