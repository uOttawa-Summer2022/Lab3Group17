package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button addBtn, findBtn, deleteBtn;
    TextView productId, productIdValue, productName, productPrice;
    EditText productNameValue, productPriceValue;
    ListView list;
    ArrayAdapter adapter;
    ArrayList<String> productList;
    MyDBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addBtn = findViewById(R.id.addBtn);
        findBtn = findViewById(R.id.findBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        productId = findViewById(R.id.productId);
        productIdValue = findViewById(R.id.productIdValue);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productNameValue = findViewById(R.id.productNameValue);
        productPriceValue = findViewById(R.id.productPriceValue);
        list = findViewById(R.id.list);
        dbHandler = new MyDBHandler(this);
        productList  = new ArrayList<>();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productNameValue.getText().toString();
                String priceStr = (productPriceValue.getText().toString());
                if(name.length() >= 1 && priceStr.length() >= 1) {
                    double price = Double.parseDouble(priceStr);
                    Product product = new Product(name, price);
                    dbHandler.addProduct(product);
                    productNameValue.setText("");
                    productPriceValue.setText("");
                    viewProducts();
                }else{
                    Toast.makeText(MainActivity.this, "You need a name AND a price!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productNameValue.getText().toString();
                Product product = dbHandler.findProduct(name);
                if(product != null){
                    Toast.makeText(MainActivity.this, "Item found. Item price is: $"+product.getPrice(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "No record found.", Toast.LENGTH_SHORT).show();
                }
                productNameValue.setText("");
                productPriceValue.setText("");
                viewProducts();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = dbHandler.deleteProduct(productNameValue.getText().toString());
                if (result) {
                    Toast.makeText(MainActivity.this, "Record Deleted.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "No record found.", Toast.LENGTH_SHORT).show();
                }
                productNameValue.setText("");
                productPriceValue.setText("");
                viewProducts();
            }
        });

    }

    private void viewProducts() {
        productList.clear();
        Cursor cursor = dbHandler.getData();
        if(cursor.getCount() == 0)
            Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
        else{
            while(cursor.moveToNext()){
                productList.add(cursor.getString(1) + " ($" + cursor.getString(2) + ")");
            }
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productList);
        list.setAdapter(adapter);

    }
}