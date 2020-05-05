package com.shammi.assignment2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shammi.assignment2.R;
import com.shammi.assignment2.db.DBHelper;
import com.shammi.assignment2.model.CustomerModel;
import com.shammi.assignment2.model.TicketModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AddTicket extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView tvTicketNumber, tvTicketPrice, tvTotalPrice, tvQuantity;
    private EditText etCustomerName, etCustomerContact, etCustomerID;
    private ImageView ivPlus, ivMinus;

    private String raffleTitle, customerName, customerContact, purchasedAt, date, time, type;
    private int ticketNumber, tempNumber, raffleID, ticketSold=0, ticketLimit, quantity;
    private int customerID = 0;
    private double ticketPrice,totalPrice;

    private Bundle extras;
    private boolean isUpdate;
    private DBHelper dbHelper;
    private ProgressDialog dialog;
    private List<Integer> numberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sell Ticket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);
        numberList = new ArrayList<>();

        tvTicketNumber = findViewById(R.id.tvTicketNumber);
        tvTicketPrice = findViewById(R.id.tvTicketPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvQuantity = findViewById(R.id.tvQuantity);

        etCustomerID = findViewById(R.id.etCustomerID);
        etCustomerName = findViewById(R.id.etName);
        etCustomerContact = findViewById(R.id.etContact);

        ivMinus = findViewById(R.id.btnQtyMinus);
        ivPlus = findViewById(R.id.btnQtyPlus);

        dbHelper = new DBHelper(this);

        //get Intent Data
        extras = getIntent().getExtras();
        if (extras != null) {
            raffleID = extras.getInt("raffleID");
            raffleTitle = extras.getString("raffleTitle");
            ticketPrice = extras.getInt("ticketPrice");
            ticketLimit = extras.getInt("ticketLimit");
            ticketSold = extras.getInt("ticketSold");
            type = extras.getString("type");
            isUpdate = false;

            setData();
        }
    }

    public void setData() {

        if (type.equals("Normal")) {
            ticketNumber = dbHelper.getTicketSold(raffleID) + 1;
        } else {
            dialog.setMessage("Getting Ticket Number...");
            dialog.show();
            generateRandomTicketNumber();

        }
        dialog.cancel();

        numberList.add(ticketNumber);
        tvTicketNumber.setText(numberList.toString());
        tvTicketPrice.setText(String.valueOf(ticketPrice));
        tvTotalPrice.setText(String.valueOf(ticketPrice));
        totalPrice = ticketPrice;

        Log.d("extras add ticket", raffleID + " " + ticketNumber + " " + ticketPrice);
    }

    //search button click for repeating customer
    public void searchCustomer(View view) {
        String txtCustomerID = etCustomerID.getText().toString();


        if (txtCustomerID.isEmpty())
            Toast.makeText(this, "Please enter customer ID!", Toast.LENGTH_SHORT).show();
        else {
            customerID = Integer.parseInt(txtCustomerID);

            ArrayList<CustomerModel> customerDetails = new ArrayList<>();
            customerDetails = dbHelper.getCustomerDetails(customerID);
            if (customerDetails.size() > 0) {
                etCustomerName.setText(customerDetails.get(0).getName());
                etCustomerContact.setText(customerDetails.get(0).getContact());
            }
            else
                Toast.makeText(this, "No customer found!", Toast.LENGTH_SHORT).show();
        }
    }

    //quantity plus/minus operation
    public void quantityFn(View view) {

        quantity = Integer.parseInt(tvQuantity.getText().toString().trim());

        if (view.getId() == R.id.btnQtyMinus) {

            if (quantity > 1) {

                totalPrice = totalPrice - ticketPrice;

                quantity--;
                String qty = String.valueOf(quantity);
                String totals = String.valueOf(totalPrice);

                tvTotalPrice.setText(totals);
                tvQuantity.setText(qty);

                generateTicketNumber("minus");

                tvTicketNumber.setText(numberList.toString());
            }

        }
        if (view.getId() == R.id.btnQtyPlus) {

            if (quantity + ticketSold < ticketLimit) {

                totalPrice = totalPrice + ticketPrice;

                quantity++;
                String qty = String.valueOf(quantity);
                tvQuantity.setText(qty);
                String totals = String.valueOf(totalPrice);
                tvTotalPrice.setText(totals);

                generateTicketNumber("plus");

                tvTicketNumber.setText(numberList.toString());
            } else
                Toast.makeText(this, "No more ticket available!", Toast.LENGTH_SHORT).show();
        }
    }

    //generate ticket number for selected quantity
    public void generateTicketNumber(String op) {
        int newNumber = 0;

        //check if the raffle is normal
        if (type.equals("Normal")) {

            if (op.equals("plus")) {
                ticketNumber = ticketNumber + 1;

                //add ticket number in a list
                numberList.add(ticketNumber);
            } else {
                ticketNumber = ticketNumber - 1;

                //remove ticket number from the list
                numberList.remove(numberList.size() - 1);
            }

        }
        //if the raffle is marginal
        else {
            if (op.equals("plus")) {

                //generate a random ticket number for marginal raffle
                generateRandomTicketNumber();
                //add ticket number in a list
                numberList.add(ticketNumber);

            } else {
                //remove ticket number from the list
                numberList.remove(numberList.size() - 1);
            }
        }
        dialog.cancel();
    }

    //generate a random ticket number for marginal raffle
    public void generateRandomTicketNumber() {

        //generate a random number
        ticketNumber = new Random().nextInt(ticketLimit) + 1;

        //check if the random number is already sold or already selected, if true then generate a new number
        if (dbHelper.isSold(ticketNumber, raffleID) || numberList.contains(ticketNumber))
            generateRandomTicketNumber();
    }

    public void addTicket() {

        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        date = day + "/" + month + "/" + year;
        if (minute < 10) {
            time = formatTime(String.valueOf(hour) + ":" + "0" + String.valueOf(minute));

        } else {
            time = formatTime(String.valueOf(hour) + ":" + String.valueOf(minute));

        }

        //current time
        purchasedAt = date + " " + time;

        customerName = etCustomerName.getText().toString().trim();
        customerContact = etCustomerContact.getText().toString().trim();
        quantity = 1;

        if (customerName.isEmpty() || customerContact.isEmpty()) {
            Toast.makeText(AddTicket.this, "Required fields are missing!", Toast.LENGTH_SHORT).show();
        } else {
            CustomerModel customerModel = new CustomerModel(customerName, customerContact);

            //check if the customer is new
            if (customerID == 0)
                customerID = dbHelper.insertCustomer(customerModel);
            //if the customer is repeating
            else
            {
                CustomerModel customerUpdateModel = new CustomerModel(customerID,customerName, customerContact);
                dbHelper.updateCustomer(customerUpdateModel);
            }


                Log.d("add ticket CID", "addTicket: " + customerID);

            //save each ticket in database
            for (int i = 0; i < numberList.size(); i++) {
                TicketModel ticketModel = new TicketModel(numberList.get(i), raffleID, raffleTitle, ticketPrice, purchasedAt, customerID);
                int ticketID = dbHelper.insertTicket(ticketModel);
                Log.d("add ticket TID", "addTicket: " + ticketID);
            }

            Log.d("add ticket Sold", "addTicket: " + ticketSold);
            //if any ticket sold the raffle is started
            if (ticketSold == 0) {
                dbHelper.updateRaffleStart(raffleID, purchasedAt);
            }
            Toast.makeText(this, "Ticket Added!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RaffleActivity.class);
            intent.putExtra("raffleID", raffleID);
            startActivity(intent);
            finishAffinity();
        }
    }


    //formate time with AM,PM for button
    public String formatTime(String time) {
        String format, formattedTime, minutes;
        String[] dateParts = time.split(":");
        int hour = Integer.parseInt(dateParts[0]);
        int minute = Integer.parseInt(dateParts[1]);
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        if (minute < 10)
            minutes = "0" + minute;
        else
            minutes = String.valueOf(minute);
        formattedTime = hour + ":" + minutes + " " + format;

        return formattedTime;
    }


    //For Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_add, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //for toolbar arrow
            case android.R.id.home:
                finish();
                break;

            case R.id.menuSave:
                addTicket();
                break;

        }

        return true;
    }
}
