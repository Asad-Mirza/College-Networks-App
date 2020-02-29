package com.example.admin.mysvvvappbeta.J;

/**
 * Created by Asad Mirza on 13-03-2018.
 */

public class cd {
    public static String getResource(String p ){
        String temp="";
        p = p.substring(1,p.length()-1);
        temp = forLess(p);

        return temp;
    }


    public static String forLess(String e){
        String temp = "";
        for (int i=0 ; i <e.length();i++){

            if (i %2 == 0){
                char o;
                o = (char)((int)e.charAt(i)-5);
                temp  = temp+o;
            }

        }
        return temp;
    }
}
