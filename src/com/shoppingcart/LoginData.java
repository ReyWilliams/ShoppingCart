package com.shoppingcart;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class that handles login data (user/pass)
 * @author Katie Dao
 */
public class LoginData {

    /**
     * Default constructor called when class is created
     */
    public LoginData(){

        //create login hashmap
        loginMap = new HashMap<>();

        //try opening file that will read login data
        try {
            //scanner "in" will open file loginData.txt
            in = new Scanner(new FileReader("src/com/shoppingcart/loginData.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (in.hasNextLine()){ //while there are more lines to read
            String[] values = in.nextLine().split("\\s+"); //make an array of strings split by whitespace

            //code to ignore the header
            if(values[0].equals("Username")) continue;

            //add the first string as key, the second as value
            HashMap<String, Boolean> temp = new HashMap<>();
            temp.put(values[1],Boolean.valueOf(values[2]));
            loginMap.put(values[0],temp);
        }

    }

    /**
     * function to return instance of the loginMap, does not clone
     * @return hashmap that has loginmap
     */
    HashMap<String, HashMap<String, Boolean>> getLoginMap(){
        return loginMap;
    }

    //local vairables
    HashMap<String, HashMap<String, Boolean>> loginMap;
    Scanner in;
}
