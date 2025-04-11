package com.example.signuplogin_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuplogin_app.CartItem;
import com.example.signuplogin_app.Food;
import com.example.signuplogin_app.R;
import com.example.signuplogin_app.adapter.FoodAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView btn_nav;
    private EditText editText_search;
    private TextView number_order;
    private ImageView cardShop;
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private List<CartItem> cartList=new ArrayList<>();
    private int orderCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        drawerLayout =findViewById(R.id.drawerLayout);
        btn_nav=findViewById(R.id.btn_nav);
        navigationView =findViewById(R.id.nav_view);
        number_order= findViewById(R.id.number_order);
        cardShop =findViewById(R.id.cardShop);
        editText_search=findViewById(R.id.editText_search);

        cardShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CardActivity.class);
                intent.putExtra("cartList",(Serializable) cartList);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(foodList, new FoodAdapter.OnAddFoodClickListener() {
            @Override
            public void onAddFoodClick(Food food) {
                orderCount++;
                number_order.setText(String.valueOf(orderCount));

                boolean found= false;

                for(CartItem item :cartList){
                    if(item.getFood().getId().equals(food.getId())){
                        item.setQuantity((item.getQuantity())+1);
                        found=true;
                        break;
                    }
                }
                if(!found){
                    cartList.add(new CartItem(food,1));
                }
            }
        });
        recyclerView.setAdapter(foodAdapter);

        btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_edit) {
                    startActivity(new Intent(MainActivity.this, EditActivity.class));
                } else if (id == R.id.nav_thongke) {
                    startActivity(new Intent(MainActivity.this, StatisticActivity.class));
                } else if (id == R.id.nav_logout) {
                    Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish(); // để thoát hẳn MainActivity
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        loadFoodData();
    }
    private void loadFoodData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("menu");

        menuRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                foodList.clear();  // Xóa danh sách cũ trước khi thêm mới
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                    String id=foodSnapshot.getKey();
                    String name = foodSnapshot.child("name").getValue(String.class);
                    int price = foodSnapshot.child("price").getValue(Integer.class);
                    String img = foodSnapshot.child("url").getValue(String.class);
                    Food food = new Food(id,name, price, img);
                    foodList.add(food);
                }
                foodAdapter.notifyDataSetChanged();  // Cập nhật dữ liệu vào adapter
            }
        }).addOnFailureListener(e -> {
            // Xử lý khi có lỗi
            Toast.makeText(MainActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
        });
    }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void filterList(String Key){
        List<Food> filterList =new ArrayList<>();
        for(Food food : foodList){
            if(food.getNamefood().toLowerCase().contains(Key.toLowerCase())){
                filterList.add(food);
            }
        }

        foodAdapter.updateList(filterList);
    }
}