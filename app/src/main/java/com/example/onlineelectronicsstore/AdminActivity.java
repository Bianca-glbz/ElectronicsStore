package com.example.onlineelectronicsstore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private EditText nameEditText, manufacturerEditText, priceEditText, categoryEditText, imageEditText;
    private Button addProductButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameEditText = findViewById(R.id.nameEditText);
        manufacturerEditText = findViewById(R.id.manufacturerEditText);
        priceEditText = findViewById(R.id.priceEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        imageEditText = findViewById(R.id.imageUrlEditText);

        addProductButton = findViewById(R.id.addProductButton);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String manufacturer = manufacturerEditText.getText().toString();
                double price = Double.parseDouble(priceEditText.getText().toString());
                String category = categoryEditText.getText().toString();
                String image = imageEditText.getText().toString();

                addProductToFirestore(name, manufacturer, price, category, image);
            }
        });
    }

    private void addProductToFirestore(String name, String manufacturer, double price, String category, String image) {
        Map<String, Object> product = new HashMap<>();
        product.put("title", name);
        product.put("manufacturer", manufacturer);
        product.put("price", price);
        product.put("category", category);
        product.put("imageUrl", image);

        db.collection("productList")
                .document()
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error adding product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AdminActivity", "Error adding product", e);
                    }
                });
    }

    private void clearInputFields() {
        nameEditText.getText().clear();
        manufacturerEditText.getText().clear();
        priceEditText.getText().clear();
        categoryEditText.getText().clear();
        imageEditText.getText().clear();
    }
}
