package com.shammi.assignment2.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shammi.assignment2.R;
import com.shammi.assignment2.db.DBHelper;
import com.shammi.assignment2.model.CustomerModel;

public class TicketActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private Bundle extras;
    private Toolbar toolbar;
    private TextView tvRaffleTitle, tvTicketPrice, tvTicketNumber,tvCustomerID,tvCustomerName,tvCustomerContact,tvPurchased;

    private int ticketNumber,raffleID,customerID;
    private double ticketPrice;
    private String raffleTitle, customerName,customerContact,purchasedAt,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Ticket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(getApplicationContext());

        tvTicketNumber=findViewById(R.id.tvTicketNumber);
        tvRaffleTitle=findViewById(R.id.tvRaffleName);
        tvTicketPrice=findViewById(R.id.tvTicketPrice);
        tvCustomerID=findViewById(R.id.tvCustomerID);
        tvCustomerName=findViewById(R.id.tvCustomerName);
        tvCustomerContact=findViewById(R.id.tvCustomerContact);
        tvPurchased=findViewById(R.id.tvPurchasedAt);

        //get Intent Data
        extras = getIntent().getExtras();
        if (extras != null) {
            ticketNumber = extras.getInt("ticketNumber");
            raffleID = extras.getInt("raffleID");
            raffleTitle = extras.getString("raffleTitle");
            ticketPrice = extras.getInt("ticketPrice");
            customerID = extras.getInt("customerID");
            customerName = extras.getString("customerName");
            customerContact = extras.getString("customerContact");
            purchasedAt = extras.getString("purchasedAt");
            status = extras.getString("status");

            setData();
        }
    }

    private void setData() {
        String txtTicketPrice="$ "+ticketPrice;
        String txtPurchased="Purchased: "+purchasedAt;


        tvTicketNumber.setText(String.valueOf(ticketNumber));
        tvRaffleTitle.setText(raffleTitle);
        tvTicketPrice.setText(txtTicketPrice);
        tvCustomerID.setText(String.valueOf(customerID));
        tvCustomerName.setText(customerName);
        tvCustomerContact.setText(customerContact);
        tvPurchased.setText(txtPurchased);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, RaffleActivity.class);
        intent.putExtra("raffleID", raffleID);
        intent.putExtra("status", status);
        startActivity(intent);
        finishAffinity();
    }

    //edit ticket information
    public void editTicket(){

        //custom layout dialog for editing ticket
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.edit_ticket_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        TextView id;
        final EditText name,contact;

        id=view.findViewById(R.id.tvCustomerID);
        name=view.findViewById(R.id.etName);
        contact=view.findViewById(R.id.etContact);

        String txtID="Customer ID: "+customerID;
        id.setText(txtID);
        name.setText(customerName);
        contact.setText(customerContact);


        builder.setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button okButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String cName=name.getText().toString();
                        String cContact=contact.getText().toString();

                        if (cName.isEmpty()||cContact.isEmpty())
                            Toast.makeText(TicketActivity.this, "Required fields are missing!", Toast.LENGTH_SHORT).show();
                        else {
                            CustomerModel customerModel=new CustomerModel(customerID,cName,cContact);
                            dbHelper.updateCustomer(customerModel);
                            Toast.makeText(TicketActivity.this, "Ticket details updated!", Toast.LENGTH_SHORT).show();

                            String txtName="Name: "+cName,txtContact="Contact: "+cContact;
                            tvCustomerName.setText(cName);
                            tvCustomerContact.setText(cContact);

                            dialog.cancel();
                        }

                        }
                });

                };
        });

        alertDialog.show();
    }

    //For Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (status!=null&&status.equals("ended"))
            getMenuInflater().inflate(R.menu.menu_share, menu);
        else {
            getMenuInflater().inflate(R.menu.menu_ticket, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //for toolbar arrow
            case android.R.id.home:
                Intent intent0 = new Intent(this, RaffleActivity.class);
                intent0.putExtra("raffleID", raffleID);
                intent0.putExtra("status", status);
                startActivity(intent0);
                finish();
                break;

            case R.id.menuShare:
                //share ticket details

                String text=customerName+", \n"+
                        "Ticket "+ticketNumber+", \n"+
                        "Price: $"+ticketPrice+", \n"+
                        "Purchased: "+purchasedAt;
                Intent intent=new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT,raffleTitle);
                intent.putExtra(Intent.EXTRA_TITLE,raffleTitle);
                intent.putExtra(Intent.EXTRA_TEXT,text);
                startActivity(Intent.createChooser(intent,"Share Using"));

                break;

            case R.id.menuEdit:
                editTicket();
                break;

        }

        return true;
    }
}
