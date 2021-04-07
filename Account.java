package com.company;

import java.util.Random;

public class Account {
    private final String accNo;
    private int pin;
    private final String[] cardNumber;

    public Account() {
        this.pin = setPin();
        this.accNo = setAccNo();
        this.cardNumber = setCardNumber();
    }

    public String getAccNo() {
        return accNo;
    }

    public String[] getCardNumber() {
        return cardNumber;
    }

    public String[] setCardNumber() {
        String nums = "1234567890";
        String card;
        char[] digits = new char[16];
        char[] arr = {'3','4','5'};
        int rnd = new Random().nextInt(arr.length);
        for (int i = 1; i <= 15; i++){
            int val = (int)(Math.random() * nums.length());
            digits[i] = nums.charAt(val);
        }
        digits[0] = arr[rnd];
        if(digits[0] == '3'){
            card =  "DINER'S CLUB";
        }else if(digits[0] == '4'){
            card =  "VISA";
        }else
            card = "MASTERCARD";

        String[] str = new String[2];
        str[0] = card;
        str[1] = new String(digits);
        return str;
    }

    public String setAccNo() {
        String nums = "1234567890";
        char[] digits = new char[11];
        for (int i = 0; i <= 10; i++){
            int val = (int)(Math.random() * nums.length());
            digits[i] = nums.charAt(val);
        }
        return new String(digits);
    }

    public int getPin() {
        return pin;
    }

    public int setPin() {
        Random rand = new Random();
        for (int i = 0; i <= 3; i++){
            pin = rand.nextInt(((9999 - 10) + 1) + 10);
        }
        return pin;
    }
}
