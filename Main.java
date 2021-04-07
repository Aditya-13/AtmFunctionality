package com.company;

import java.sql.*;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final String connString = "oracle.jdbc.driver.OracleDriver";
    private static final String driverUrl = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String user = "system";
    private static final String password = "*********";
    private static double balance;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args){
        try{
            int ch;
            boolean flag  = true;
            System.out.println("*********************** WELCOME TO NC BANK ***********************");
            while(flag){
                System.out.println("******************************************************************");
                System.out.println("THESE ARE THE AVAILABLE OPTIONS:");
                System.out.println("1.REGISTER TO OUR BANK \t2.GET YOUR BALANCE \t3.WITHDRAW MONEY \t4.DEPOSIT MONEY \t5.UPDATE PIN \t6.CHANGE CARD \t7.EXIT");
                System.out.println("ENTER YOUR CHOICE: ");

                ch = scanner.nextInt();
                switch (ch){
                    case 1: register();
                        break;
                    case 2: balanceEnquiry();
                         break;
                    case 3: withdraw();
                        break;
                    case 4: deposit();
                        break;
                    case 5: updatePin();
                        break;
                    case 6: updateCard();
                        break;
                    case 7: flag = false;
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void register(){
        try{
            String name;
            Class.forName(connString);
            Connection con = DriverManager.getConnection(driverUrl,user,password);
            System.out.println("ENTER YOUR AGE: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            System.out.println("ENTER YOUR NAME: ");
            name = scanner.nextLine();
            scanner.nextLine();
            System.out.println("ENTER YOUR EMAIL:");
            String email = scanner.nextLine();
            Account account = new Account();

            if(age > 18) {
                System.out.println("YOUR NAME IS: " + name);
                System.out.println("YOUR AGE IS: " + age);
                System.out.println("YOUR EMAIL ID IS: " + email);
                System.out.println("YOUR ACCOUNT NUMBER IS: " + account.getAccNo());
                System.out.println("YOUR PIN FOR THIS CARD IS: " + account.getPin());
                System.out.println("YOUR CARD NUMBER FOR THIS CARD IS: " + account.getCardNumber()[1]);
                System.out.println("YOUR CARD WILL BE A " + account.getCardNumber()[0] + " CARD, WILL BE DELIVERED IN A COUPLE OF DAYS");
                System.out.println("ENTER INITIAL AMOUNT TO DEPOSIT: ");
                balance = scanner.nextDouble();
                System.out.print("\nYOUR CURRENT BALANCE IS ₹" + balance + "\n");
            }
            else{
                System.out.println("ACCOUNT WILL BE CREATED FOR PEOPLE OF AGE ABOVE 18");
            }
            String col1 = name;
            String col2 = account.getCardNumber()[1];
            String col3 = account.getAccNo();
            double col4 = balance;
            int col5 = account.getPin();
            String col6 = account.getCardNumber()[0];
            String col8 = email;

            String insertQuery = "insert into atm values(?,?,?,?,?,?,?,?)";
            PreparedStatement psmt = con.prepareStatement(insertQuery);
            psmt.setString(1,col1);
            psmt.setString(2,col2);
            psmt.setString(3,col3);
            psmt.setDouble(4,col4);
            psmt.setInt(5,col5);
            psmt.setString(6,col6);
            psmt.setInt(7, age);
            psmt.setString(8,email);
            psmt.execute();

            psmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void balanceEnquiry(){
        try{
            Class.forName(connString);
            Connection con = DriverManager.getConnection(driverUrl,user,password);

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select COUNT(*) from atm");
            int count = 0;
            while(rs.next()){
                count = rs.getInt(1);
            }
            System.out.println("ENTER THE PIN: ");
            int pin = scanner.nextInt();

            Statement stmt1 = con.createStatement();
            float dbBalance = 0;
            ResultSet rs1 = stmt1.executeQuery("select ACCBALANCE from ATM where PIN = " + pin);
            while(rs1.next()){
                dbBalance = rs.getFloat("ACCBALANCE");
            }
            System.out.println("YOUR CURRENT BALANCE IS: " + dbBalance);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void withdraw(){
        try{
            Class.forName(connString);
            Connection con = DriverManager.getConnection(driverUrl,user,password);
            System.out.println("ENTER YOUR PIN: ");
            int pin = scanner.nextInt();
            Statement stmt = con.createStatement();

            ResultSet rs2 = stmt.executeQuery("select COUNT(*) from atm");
            int count = 0;
            while(rs2.next()){
                count = rs2.getInt(1);
            }
            pin = (pin * 10) / count;
            float dbBalance = 0;

            ResultSet rs = stmt.executeQuery("select ACCBALANCE from ATM where PIN = " + pin);
            while(rs.next()){
                dbBalance = rs.getFloat("ACCBALANCE");
            }
            System.out.println("YOUR CURRENT BALANCE IS: " + dbBalance);
            System.out.println("ENTER AMOUNT TO WITHDRAW: ");
            float withdrawAmount = scanner.nextFloat();

            if(dbBalance > withdrawAmount){
                dbBalance -= withdrawAmount;
                System.out.println("AMOUNT WITHDRAWN SUCCESSFULLY, ACCOUNT BALANCE AFTER WITHDRAWAL OF ₹" + withdrawAmount + " IS: ₹" + dbBalance);
            }else{
                System.out.println("INSUFFICIENT BALANCE TO WITHDRAW ₹" + withdrawAmount + "FROM YOUR ACCOUNT");
            }

            String updateBalance = "update atm set accbalance = ? where pin = " + pin;
            PreparedStatement psmt = con.prepareStatement(updateBalance);

            float col1 = dbBalance;
            psmt.setFloat(1,col1);
            psmt.execute();
            String emailMessage = "ACCOUNT BALANCE AFTER WITHDRAWAL OF ₹" + withdrawAmount + " IS: ₹" + dbBalance;

            Statement stmt1 = con.createStatement();
            String email = "";
            ResultSet rs1 = stmt1.executeQuery("select EMAIL from ATM where PIN = " + pin);
            while (rs1.next()){
                email = rs.getString("EMAIL");
            }

            SendEmail sendEmail = new SendEmail(email,emailMessage);

            psmt.close();
            stmt.close();
            con.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deposit(){
        try{
            Class.forName(connString);
            Connection con = DriverManager.getConnection(driverUrl,user,password);

            Statement stmt = con.createStatement();
            System.out.println("ENTER THE PIN: ");
            int pin = scanner.nextInt();

            ResultSet rs2 = stmt.executeQuery("select COUNT(*) from atm");
            int count = 0;
            while(rs2.next()){
                count = rs2.getInt(1);
            }

            pin = (pin * 10) / count;
            float dbBalance = 0;

            ResultSet rs = stmt.executeQuery("select ACCBALANCE from ATM where PIN = " + pin);
            while(rs.next()){
                dbBalance = rs.getFloat("ACCBALANCE");
            }
            System.out.println("ENTER THE AMOUNT TO DEPOSIT: ");
            float depositAmount = scanner.nextFloat();
            dbBalance += depositAmount;

            String updateBalance = "update atm set accbalance = ? where pin = " + pin;
            PreparedStatement psmt = con.prepareStatement(updateBalance);
            float col1 = dbBalance;
            psmt.setFloat(1,col1);
            psmt.execute();

            System.out.println("ACCOUNT BALANCE AFTER DEPOSIT OF ₹" + depositAmount + " IS: ₹" + dbBalance);

            Statement stmt1 = con.createStatement();
            String email = "";
            ResultSet rs1 = stmt1.executeQuery("select EMAIL from ATM where PIN = " + pin);
            while (rs1.next()){
                email = rs.getString("EMAIL");
            }

            SendEmail email1 = new SendEmail(email, "ACCOUNT BALANCE AFTER DEPOSIT OF ₹" + depositAmount + " IS: ₹" + dbBalance);

            psmt.close();
            stmt.close();
            con.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updatePin(){
        try{
            Class.forName(connString);
            Connection con = DriverManager.getConnection(driverUrl,user,password);
            scanner.nextLine();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select COUNT(*) from atm");
            int count = 0;
            while(rs.next()){
                count = rs.getInt(1);
            }

            System.out.println("ENTER THE CURRENT PIN: ");
            int currPin = scanner.nextInt();
            currPin = (currPin * count) / 10;

            String email = "";
            ResultSet rs1 = stmt.executeQuery("select email from atm where pin = " + currPin);
            while(rs1.next()){
                email = rs1.getString("EMAIL");
                System.out.println(rs1.getString("EMAIL"));
            }

            Account account = new Account();
            int pin = account.setPin();
            System.out.println("YOUR NEW PIN IS: " + pin);
            SendEmail messageEmail = new SendEmail(email, "THANKS FOR USING OUR SERVICES \nYOUR NEW PIN IS: \n" + pin);
            pin = (pin * 10) / count;

            String updateYourPin = "update atm set pin = ? where pin = ?";
            PreparedStatement psmt = con.prepareStatement(updateYourPin);
            psmt.setInt(1, pin);
            psmt.setInt(2, currPin);
            psmt.execute();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void updateCard(){
        try{
            Class.forName(connString);
            Connection con = DriverManager.getConnection(driverUrl,user,password);
            scanner.nextLine();
            System.out.println("ENTER THE NAME TO UPDATE CARD: ");
            String name = scanner.nextLine();
            Account account = new Account();

            String col1 = account.setCardNumber()[1];
            String col2 = account.getCardNumber()[0];
            String col3 = name.toUpperCase(Locale.ROOT);

            String updateCard = "update atm set cardnumber = ?, cardtype = ? where name = ?";
            PreparedStatement psmt = con.prepareStatement(updateCard);
            psmt.setString(1,col1);
            psmt.setString(2,col2);
            psmt.setString(3,col3);
            psmt.execute();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}