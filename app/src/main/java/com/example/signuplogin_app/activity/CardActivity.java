package com.example.signuplogin_app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuplogin_app.CartItem;
import com.example.signuplogin_app.Food;
import com.example.signuplogin_app.R;
import com.example.signuplogin_app.adapter.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardActivity extends AppCompatActivity implements CartAdapter.CartUpdateListener {
    private ImageView btn_back;
    private RecyclerView recyclerView;
    private TextView txt_totalPrice,btn_payConfirm;
    private List<CartItem> cartItemList;
    private CartAdapter cartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card);

        btn_back=findViewById(R.id.btn_back);
        recyclerView=findViewById(R.id.recyclerView);
        txt_totalPrice=findViewById(R.id.txt_totalPrice);
        btn_payConfirm=findViewById(R.id.btn_payConfirm);


        btn_payConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItemList == null || cartItemList.isEmpty()) {
                    Toast.makeText(CardActivity.this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
                } else {
                    showConfirmDialog(new Food());  // Chỉ hiển thị nếu có món
                }
            }
        });

        Intent intent=getIntent();
        cartItemList=(List<CartItem>) intent.getSerializableExtra("cartList");

        cartAdapter=new CartAdapter(cartItemList,this);
        recyclerView.setAdapter(cartAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateTotalPrice();


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CardActivity.this,MainActivity.class));
            }
        });
    }
    private void updateTotalPrice() {
        int total = 0;
        for (CartItem item : cartItemList) {
            total += item.getQuantity() * item.getFood().getPricefood();
        }
        txt_totalPrice.setText(total + "đ");
    }


    @Override
    public void onCartUpdate() {
        updateTotalPrice();
    }

    private void showConfirmDialog(Food food){
        Dialog dialog= new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm_pay);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView btnCancel=dialog.findViewById(R.id.btn_cancel);
        ImageView btnConfirm=dialog.findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrderToFirebase();  // lưu hóa đơn
                cartItemList.clear();
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
                dialog.dismiss();
                Toast.makeText(CardActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CardActivity.this, MainActivity.class));
            }
        });
        dialog.show();
    }
    private void saveOrderToFirebase() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("bills");

        String orderId = ordersRef.push().getKey();  // Tạo ID tự động
        Map<String, Object> orderData = new HashMap<>();
        List<Map<String, Object>> items = new ArrayList<>();
        int total = 0;

        for (CartItem item : cartItemList) {
            Map<String, Object> foodMap = new HashMap<>();
            foodMap.put("name", item.getFood().getNamefood());
            foodMap.put("price", item.getFood().getPricefood());
            foodMap.put("quantity", item.getQuantity());
            items.add(foodMap);
            total += item.getFood().getPricefood() * item.getQuantity();
        }

        orderData.put("items", items);
        orderData.put("total", total);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentTime = sdf.format(new Date());
        orderData.put("timestamp", currentTime);


        ordersRef.child(orderId).setValue(orderData);
    }

}