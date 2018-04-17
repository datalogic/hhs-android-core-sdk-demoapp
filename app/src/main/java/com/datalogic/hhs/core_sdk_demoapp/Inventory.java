package com.datalogic.hhs.core_sdk_demoapp;



public  class  Inventory {


    public static String[][] store = {
        { "40111445", "mms", "MMS Chocolate Candies", "4", "3,85"},
        { "072220805758", "spider", "Spider-Man Vinyl Bobble-Head", "5", "8,85" },
        { "9650777772092", "bart", "Bart Simpson", "66", "9,58" },
        { "8008843010936", "melatonin_pura", "Melatonin pura drops", "75", "11,00" },
        { "0DG48S", "enterogermina", "Enterogermina - 20 flacons", "12", "13,90" },
        { "VVJ90N", "erpe_sun_defend", "Herpe SUN Defend dietary supplement", "136", "8,90"},
        { "0RD4MR", "dicloreum", "Dicloreum Anti-inflammatory", "22", "3,50"},
        { "925596898", "apropos", "Apropos propolis", "58", "16,00"},
        { "FANCVF03239355", "shampoo", "Garnier Ultra dolce children", "45", "2,25" },
        { "8005200010448", "vera_water", "Vera Water 50cl bottle", "12", "0,50" },
        { "1234567890", "garnier", "Garnier Fructis curly hair", "12", "0,50" }
    };


    public static int getQuantity(String br) {
        int qt = 0;
        for (int i = 0; i < store.length; i++) {
            if (store[i][0].equals(br)) {
                qt = Integer.parseInt(store[i][3]);
                break;
            }
        }
        return qt;
    }


    public static String getImageName(String br) {
        String name = "";
        for (int i = 0; i < store.length; i++) {
            if (store[i][0].equals(br)) {
                name = store[i][1];
                break;
            }
        }
        return name;
    }

    public static void setQuantity(String br, int qt) {
        String newqt = new Integer(qt).toString();
        for (int i = 0; i < store.length; i++) {
            if (store[i][0].equals(br)) {
                store[i][3] = newqt;
                break;
            }
        }
    }


    public static String getDescription(String br) {
        String des = "" ;
        for (int i = 0; i < store.length; i++) {
            if (store[i][0].equals(br)) {
                des = store[i][2];
                break;
            }
        }
        return des;
    }


    public static String getPrice(String br) {
        String des = "" ;
        for (int i = 0; i < store.length; i++) {
            if (store[i][0].equals(br)) {
                des = store[i][4];
                break;
            }
        }
        return des;
    }
}

