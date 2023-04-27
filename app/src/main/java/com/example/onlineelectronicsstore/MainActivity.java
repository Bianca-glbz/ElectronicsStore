package com.example.onlineelectronicsstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private ProductListAdapter productListAdapter;
    private List<Product> productList;
    private Button adminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getProductList();

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productListAdapter = new ProductListAdapter(productList);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productListAdapter);

        Button cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


        checkAdmin();


        adminButton = findViewById(R.id.adminButton);
        adminButton.setVisibility(View.GONE);

    }

//    private List<Product> getProductList() {
//        // replace this with your own method to get the list of products
//        List<Product> productList = new ArrayList<>();
//        // Add some dummy products
//        productList.add(new Product("Product 1", "Manufacturer 1", 10.0, "Category 1", "https://cdn.shopclues.com/images/thumbnails/42622/320/320/96839480mg1376manhattanred14434339101470828231.jpg"));
//        productList.add(new Product("Product 2", "Manufacturer 2", 20.0, "Category 1", "https://cdn.shopclues.com/images/thumbnails/42622/320/320/96839480mg1376manhattanred14434339101470828231.jpg"));
//        productList.add(new Product("Product 3", "Manufacturer 3", 30.0, "Category 2", "https://cdn.shopclues.com/images/thumbnails/42622/320/320/96839480mg1376manhattanred14434339101470828231.jpg"));
//        productList.add(new Product("Product 4", "Manufacturer 4", 40.0, "Category 2", "https://cdn.shopclues.com/images/thumbnails/42622/320/320/96839480mg1376manhattanred14434339101470828231.jpg"));
//        productList.add(new Product("Product 5", "Manufacturer 5", 50.0, "Category 3", "https://cdn.shopclues.com/images/thumbnails/42622/320/320/96839480mg1376manhattanred14434339101470828231.jpg"));
//
//        return productList;
//    }

    private void getProductList() {
        productList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Task<QuerySnapshot> productList1 = db.collection("productList")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                    } else {
                        // If there was an error, just return an empty list
                        productList = new ArrayList<>();
                    }

                    // Update the adapter with the new list of products
                    productListAdapter.updateProductList(productList);
                });
    }


    private void checkAdmin() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists() && document.getBoolean("isAdmin")) {

                            adminButton = findViewById(R.id.adminButton);
                            adminButton.setVisibility(View.VISIBLE);

                            adminButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                    startActivityForResult(intent, 1); // 1 is the request code
                                }
                            });

                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Update the product list
            getProductList();
        }
    }
}
