package com.example.onlineelectronicsstore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartList;

    private TextView totalPriceTextView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartList = Cart.getInstance().getCartItems(); // get the list of products in the cart

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartAdapter = new CartAdapter(cartList, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        totalPriceTextView = findViewById(R.id.cart_total_price);
        Cart.getInstance().getTotalPrice(totalPriceTextView);
        // totalPriceTextView.setText(String.format("Total Price: €%.2f", totalPrice));

        checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this, "Checkout complete!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateCartTotal(){
        totalPriceTextView = findViewById(R.id.cart_total_price);

        Cart.getInstance().getTotalPrice(totalPriceTextView);
    }
}


