package com.example.onlineelectronicsstore;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private List<Product> products;

    private Cart() {
        products = new ArrayList<>();
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addToCart(Product product) {
        products.add(product);
    }

    public void removeFromCart(Product product) {
        products.remove(product);
    }

    public List<Product> getCartItems() {
        return products;
    }

    public void clearCart() {
        products.clear();
    }


    public double getTotalPrice() {
        double totalPrice = 0;
        for (Product product : products) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    public double getTotalPrice(TextView priceTextView) {
        double totalPrice = 0;
        for (Product product : products) {
            totalPrice += product.getPrice();
        }
        priceTextView.setText(String.format("%.2f", totalPrice));
        return totalPrice;
    }
}

