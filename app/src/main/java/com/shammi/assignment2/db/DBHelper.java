package com.shammi.assignment2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shammi.assignment2.model.CustomerModel;
import com.shammi.assignment2.model.RaffleModel;
import com.shammi.assignment2.model.TicketModel;
import com.shammi.assignment2.model.WinnerModel;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 6/5/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "appdb.db";
    private static String TABLE_RAFFLE = "raffles";
    private static String TABLE_TICKET = "tickets";
    private static String TABLE_CUSTOMER = "customers";
    private static String TABLE_WINNER = "winners";

    private static String ID = "id";
    private static String Image = "image";
    private static String RaffleTitle = "title";
    private static String Desc = "description";
    private static String Type = "type";
    private static String TicketPrice = "ticket_price";
    private static String TicketSold = "ticket_sold";
    private static String TicketLimit = "ticket_limit";
    private static String WinnerLimit = "winner_limit";
    private static String Start = "start";
    private static String Draw = "draw";


    private static String TicketNumber = "ticket_number";
    private static String PurchasedAt = "purchased_at";

    private static String CustomerID = "customer_id";
    private static String CustomerName = "customer_name";
    private static String CustomerContact = "customer_contact";

    private static String RaffleID = "raffle_id";
    private static String WinnerNumber = "winner_number";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE_RAFFLE + " ( " + ID + " integer primary key autoincrement, " + RaffleTitle + " text not null, " + Desc + " text not null, " + Image + " blob, " + Type + " text," + TicketPrice + " integer," + TicketLimit + " integer," + WinnerLimit + " integer," + Start + " datetime," + Draw + " datetime)";
        db.execSQL(query);
       String query2 = "create table " + TABLE_TICKET + " ( " + ID + " integer primary key autoincrement," + RaffleID + " integer not null," + RaffleTitle + " text not null, " + TicketNumber + " integer not null, " + TicketPrice + " double not null," + PurchasedAt + " datetime," + CustomerID + " integer not null)";
        db.execSQL(query2);
        String query3 = "create table " + TABLE_CUSTOMER + " ( " + ID + " integer primary key autoincrement, " + CustomerName + " text not null, " + CustomerContact + " text not null)";
        db.execSQL(query3);
        String query4 = "create table " + TABLE_WINNER + " ( " + ID + " integer primary key autoincrement, " + RaffleID + " integer not null," + TicketNumber + " integer not null)";
        db.execSQL(query4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = " Drop table if exists " + TABLE_RAFFLE;
        db.execSQL(query);
        String query2 = " Drop table if exists " + TABLE_TICKET;
        db.execSQL(query2);
        String query3 = " Drop table if exists " + TABLE_CUSTOMER;
        db.execSQL(query3);
        String query4 = " Drop table if exists " + TABLE_WINNER;
        db.execSQL(query4);
    }



    public int insertRaffle(RaffleModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RaffleTitle, data.getTitle());
        values.put(Desc, data.getDesc());
        values.put(Image, data.getImage());
        values.put(Type, data.getType());
        values.put(TicketPrice, data.getTicketPrice());
        values.put(TicketLimit, data.getTicketLimit());
        values.put(WinnerLimit, data.getWinnerLimit());
        values.put(Start, data.getStart());
        values.put(Draw, data.getDraw());

        long id = sd.insert(TABLE_RAFFLE, null, values);
        sd.close();
        Log.d(TAG, String.valueOf(id));
        return (int) id;
    }

    public int updateRaffle(RaffleModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        int id=data.getId();

        values.put(RaffleTitle, data.getTitle());
        values.put(Desc, data.getDesc());
        values.put(Image, data.getImage());
        values.put(Type, data.getType());
        values.put(TicketPrice, data.getTicketPrice());
        values.put(TicketLimit, data.getTicketLimit());
        values.put(WinnerLimit, data.getWinnerLimit());
        values.put(Start, data.getStart());
        values.put(Draw, data.getDraw());

        long code = sd.update(TABLE_RAFFLE, values,"id=" + id, null );
        sd.close();
        Log.d(TAG, String.valueOf(code));
        return (int) code;
    }

    public int updateRaffleDraw(int raffleId,String date) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Draw, date);

        long code = sd.update(TABLE_RAFFLE, values,"id=" + raffleId, null );
        sd.close();
        Log.d(TAG, String.valueOf(code));
        return (int) code;
    }

    public int updateRaffleStart(int raffleId,String date) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Start, date);

        long code = sd.update(TABLE_RAFFLE, values,"id=" + raffleId, null );
        sd.close();
        Log.d(TAG, String.valueOf(code));
        return (int) code;
    }

    public ArrayList<RaffleModel> getRaffleList() {
        SQLiteDatabase sd = getReadableDatabase();
//        String query = "Select "+TABLE_RAFFLE+".*,count("+TABLE_TICKET+".id) as ticket_sold from " + TABLE_RAFFLE + " left join "+TABLE_TICKET+" on "+TABLE_RAFFLE+".id="+TABLE_TICKET+"."+RaffleID+"  group by "+TABLE_RAFFLE+".id ";
        String query = "Select "+TABLE_RAFFLE+".*  from " + TABLE_RAFFLE + " left join "+TABLE_WINNER+" on "+TABLE_RAFFLE+".id="+TABLE_WINNER+"."+RaffleID+" where "+TABLE_WINNER+".id is null  group by "+TABLE_RAFFLE+".id order by id desc";
        Cursor cur = sd.rawQuery(query, null);
        ArrayList<RaffleModel> data = new ArrayList<>();

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                int id = cur.getInt(0);
                String title = cur.getString(1);
                String desc = cur.getString(2);
                byte[] image = cur.getBlob(3);
                String type = cur.getString(4);
                int ticketPrice = cur.getInt(5);
                int ticketLimit = cur.getInt(6);
                int winnerLimit = cur.getInt(7);
                String start = cur.getString(8);
                String draw = cur.getString(9);
//                int ticketSold = cur.getInt(10);
                data.add(new RaffleModel(id,title,desc,image,type,ticketPrice,0,ticketLimit,winnerLimit,start,draw));

            } while (cur.moveToNext());
        }
        cur.close();

        Log.d("raffle list in db", String.valueOf(data.size()));
        return data;
    }

    public ArrayList<RaffleModel> getEndedRaffleList() {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select "+TABLE_RAFFLE+".*  from " + TABLE_RAFFLE + " join "+TABLE_WINNER+" on "+TABLE_RAFFLE+".id="+TABLE_WINNER+"."+RaffleID+"  group by "+TABLE_RAFFLE+".id order by id desc";
        Cursor cur = sd.rawQuery(query, null);
        ArrayList<RaffleModel> data = new ArrayList<>();

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                int id = cur.getInt(0);
                String title = cur.getString(1);
                String desc = cur.getString(2);
                byte[] image = cur.getBlob(3);
                String type = cur.getString(4);
                int ticketPrice = cur.getInt(5);
                int ticketLimit = cur.getInt(6);
                int winnerLimit = cur.getInt(7);
                String start = cur.getString(8);
                String draw = cur.getString(9);
                data.add(new RaffleModel(id,title,desc,image,type,ticketPrice,0,ticketLimit,winnerLimit,start,draw));

            } while (cur.moveToNext());
        }
        cur.close();

        Log.d("raffle list in db", String.valueOf(data.size()));
        return data;
    }

    public ArrayList<RaffleModel> getRaffleDetails(int raffleId) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select * from " + TABLE_RAFFLE + "  where "+TABLE_RAFFLE+".id="+raffleId+"  ";
        Cursor cur = sd.rawQuery(query, null);
        ArrayList<RaffleModel> data = new ArrayList<>();

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                int id = cur.getInt(0);
                String title = cur.getString(1);
                String desc = cur.getString(2);
                byte[] image = cur.getBlob(3);
                String type = cur.getString(4);
                int ticketPrice = cur.getInt(5);
                int ticketLimit = cur.getInt(6);
                int winnerLimit = cur.getInt(7);
                String start = cur.getString(8);
                String draw = cur.getString(9);
                data.add(new RaffleModel(id,title,desc,image,type,ticketPrice,0,ticketLimit,winnerLimit,start,draw));

                Log.d("cursor raffle details", String.valueOf(ticketLimit));
            } while (cur.moveToNext());
        }
        cur.close();

        Log.d("raffle details in db", String.valueOf(data.size()));
        return data;
    }

    public ArrayList<Integer> getAllTicketNumber(int raffleId) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select ticket_number from " + TABLE_TICKET + "  where "+TABLE_TICKET+".raffle_id="+raffleId+" ";
        Cursor cur = sd.rawQuery(query, null);
        ArrayList<Integer> listTicketNumber = new ArrayList<>();

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                int ticketNumber = cur.getInt(0);
                listTicketNumber.add(ticketNumber);

                Log.d("cursor ticket number", String.valueOf(ticketNumber));
            } while (cur.moveToNext());
        }
        cur.close();

        Log.d("ticket number list db", String.valueOf(listTicketNumber.size()));
        return listTicketNumber;
    }

    public int insertTicket(TicketModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RaffleID, data.getRaffleID());
        values.put(RaffleTitle, data.getRaffleTitle());
        values.put(TicketNumber, data.getNumber());
        values.put(TicketPrice, data.getPrice());
        values.put(PurchasedAt, data.getPurchasedAt());
        values.put(CustomerID, data.getCustomerId());


        long id = sd.insert(TABLE_TICKET, null, values);
        sd.close();
        Log.d(TAG, String.valueOf(id));
        return (int) id;
    }

    public int updateTicket(TicketModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        int id=data.getId();

        values.put(TicketNumber, data.getNumber());
        values.put(RaffleID, data.getRaffleID());
        values.put(TicketPrice, data.getPrice());
        values.put(PurchasedAt, data.getPurchasedAt());
        values.put(CustomerID, data.getCustomerId());

        long code = sd.update(TABLE_TICKET, values,"id=" + id, null );
        sd.close();
        Log.d(TAG, String.valueOf(code));
        return (int) code;
    }

    public  boolean isSold(int id,int raffleID) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select ticket_number from " + TABLE_TICKET + " where  " + TicketNumber + " = '" + id + "' and " + RaffleID + " = '" + raffleID + "' " ;
        Cursor cur = sd.rawQuery(query, null);
        int ticketNumber=0;
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            do {
                ticketNumber = cur.getInt(0);

            } while (cur.moveToNext());
        }
        cur.close();
        Log.d("check TicketNumber", String.valueOf(ticketNumber));

        return ticketNumber > 0;
    }

    public  int getTicketSold(int id) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select count(ticket_number) as ticket_sold from " + TABLE_TICKET + " where  " + RaffleID + " = '" + id + "' " ;
        Cursor cur = sd.rawQuery(query, null);
        int ticketNumber=0;
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            do {
                ticketNumber = cur.getInt(0);

            } while (cur.moveToNext());
        }
        cur.close();
        Log.d("check lastTicketNumber", String.valueOf(ticketNumber));
        return ticketNumber;
    }

    public ArrayList<TicketModel> getTicketList(int raffleIdValue) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select "+TABLE_TICKET+".*,"+TABLE_CUSTOMER+".customer_name,"+TABLE_CUSTOMER+".customer_contact from " + TABLE_TICKET + " left join "+TABLE_CUSTOMER+" on "+TABLE_TICKET+".customer_id="+TABLE_CUSTOMER+".id where "+TABLE_TICKET+".raffle_id = '" + raffleIdValue + "' order by id desc ";
        Cursor cur = sd.rawQuery(query, null);
        ArrayList<TicketModel> data = new ArrayList<>();

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                int id = cur.getInt(0);
                int raffleID = cur.getInt(1);
                String raffleTitle = cur.getString(2);
                int ticketNumber = cur.getInt(3);
                double ticketPrice = cur.getDouble(4);
                String purchasedAt = cur.getString(5);
                int customerID = cur.getInt(6);
                String customerName = cur.getString(7);
                String customerContact = cur.getString(8);

                data.add(new TicketModel(id,ticketNumber,raffleID,raffleTitle,ticketPrice,purchasedAt,customerID,customerName,customerContact));

                Log.d("ticket cursor", customerID+" "+customerName+" "+customerContact+" "+ticketPrice);
            } while (cur.moveToNext());
        }
        cur.close();

        Log.d("ticket list in db", String.valueOf(data.size()));
        return data;
    }

    public int insertCustomer(CustomerModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CustomerName, data.getName());
        values.put(CustomerContact, data.getContact());

        long id = sd.insert(TABLE_CUSTOMER, null, values);
        sd.close();
        Log.d("CID in DB", String.valueOf(id));
        return (int) id;
    }

    public ArrayList<CustomerModel> getCustomerDetails(int customerID) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select * from " + TABLE_CUSTOMER + " where  " + ID + " = '" + customerID + "' " ;
        Cursor cur = sd.rawQuery(query, null);
        ArrayList<CustomerModel> customerList=new ArrayList<>();
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            do {
                int ID = cur.getInt(0);
                String Name = cur.getString(1);
                String Contact = cur.getString(2);

                customerList.add(new CustomerModel(ID,Name,Contact));

            } while (cur.moveToNext());
        }
        cur.close();
        Log.d("check winners", customerList.toString());

        return customerList;
    }

    public int updateCustomer(CustomerModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        int id=data.getId();

        values.put(CustomerName, data.getName());
        values.put(CustomerContact, data.getContact());

        long code = sd.update(TABLE_CUSTOMER, values,"id=" + id, null );
        sd.close();
        Log.d(TAG, String.valueOf(code));
        return (int) code;
    }

    public int insertWinner(WinnerModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RaffleID, data.getRaffleID());
        values.put(TicketNumber, data.getTicketNumber());

        long id = sd.insert(TABLE_WINNER, null, values);
        sd.close();
        Log.d("CID in DB", String.valueOf(id));
        return (int) id;
    }

    public ArrayList<Integer> getWinner(int raffleId) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select ticket_number from " + TABLE_WINNER + " where  " + RaffleID + " = '" + raffleId + "' " ;
        Cursor cur = sd.rawQuery(query, null);
        ArrayList<Integer> winnerList=new ArrayList<>();
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            do {
                winnerList.add(cur.getInt(0));

            } while (cur.moveToNext());
        }
        cur.close();
        Log.d("check winners", winnerList.toString());

        return winnerList;
    }

    public  byte[] getImageByte(int id) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select image from " + TABLE_RAFFLE + " where  " + ID + " = '" + id + "' " ;
        Cursor cur = sd.rawQuery(query, null);
        byte[] image = new byte[0];

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                image = cur.getBlob(0);

            } while (cur.moveToNext());
        }
        cur.close();

        Log.d("check", "getByte");

        return image;
    }

    public void deleteRaffle(int id) {
        SQLiteDatabase sd = getWritableDatabase();
        String query = " Delete from " + TABLE_RAFFLE + " where " + ID + " = '" + id + "'";
       sd.execSQL(query);
        sd.close();
    }




}
