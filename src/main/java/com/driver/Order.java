package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id = id;
        String[] dt = deliveryTime.split(":");
        int delTime = Integer.parseInt(dt[0])*60 + Integer.parseInt(dt[1]) ;
        this.deliveryTime = delTime;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }


    public static int getDeliveryTimeAsInt(String timeString){
        return (Integer.parseInt(timeString.substring(0,2)) * 60 )+
                Integer.parseInt(timeString.substring(3) );
    }

    public static String getDeliveryTimeAsString(int time){
        int hrs = time/60;
        int mins = time % 60;
        String hrString = "";
        String minString = "";
        if(hrs < 10) hrString = "0" + hrs;
        else hrString = "" + hrs;

        if(mins < 10) minString = "0" + mins;
        else minString = "" + mins;

        return hrString + ":" + minString;
    }
}
