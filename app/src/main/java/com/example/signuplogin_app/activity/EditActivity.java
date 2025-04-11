package com.example.signuplogin_app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuplogin_app.Food;
import com.example.signuplogin_app.R;
import com.example.signuplogin_app.adapter.EditFoodAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private ImageView btn_back;
    private EditFoodAdapter editFoodAdapter;
    private List<Food> foodList;
    private EditText editText_search;
    private TextView btn_addnewfood;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);

        recyclerView=findViewById(R.id.recyclerView);
        btn_addnewfood=findViewById(R.id.btn_addnewfood);
        editText_search=findViewById(R.id.editText_search);

        btn_addnewfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditActivity.this, AddFoodActivity.class));
            }
        });
        foodList=new ArrayList<>();

        editFoodAdapter=new EditFoodAdapter(foodList, new EditFoodAdapter.OnFoodEditClickListener() {
            @Override
            public void onEditFoodClick(Food food) {
                Toast.makeText(EditActivity.this, "sua mon"+food.getNamefood(), Toast.LENGTH_SHORT).show();
                showFormEdit(food);
            }

            @Override
            public void onDeleteFoodClick(Food food) {
                showDeleteConfirmDialog(food);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(editFoodAdapter);

        loadFoodData();

        btn_back=findViewById(R.id.btn_back);
        editText_search=findViewById(R.id.editText_search);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditActivity.this, MainActivity.class));
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
    }

    private  void loadFoodData(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference menuRef=database.getReference("menu");

        menuRef.get().addOnSuccessListener(dataSnapshot -> {
            if(dataSnapshot.exists()){
                foodList.clear();
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()){
                    String id= foodSnapshot.getKey();
                    String name = foodSnapshot.child("name").getValue(String.class);
                    int price = foodSnapshot.child("price").getValue(Integer.class);
                    String img = foodSnapshot.child("url").getValue(String.class);
                    Food food = new Food(id,name, price, img);
                    foodList.add(food);
                }
                editFoodAdapter.notifyDataSetChanged();  // Cập nhật dữ liệu vào adapter
            }
        }).addOnFailureListener(e->{
            Toast.makeText(EditActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDeleteConfirmDialog(Food food){
        Dialog dialog= new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm_delete);
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
                DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("menu");
                menuRef.child(food.getId()).removeValue().addOnSuccessListener(aVoid -> {
                    foodList.remove(food);  // Xoá khỏi danh sách hiển thị
                    editFoodAdapter.notifyDataSetChanged();
                    Toast.makeText(EditActivity.this, "Đã xóa món " + food.getNamefood(), Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(EditActivity.this, "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showFormEdit(Food food){
        startActivity(new Intent(EditActivity.this, EditFoodActivity.class));
    }

    private void filterList(String Key){
        List<Food> filterList =new ArrayList<>();
        for(Food food : foodList){
            if(food.getNamefood().toLowerCase().contains(Key.toLowerCase())){
                filterList.add(food);
            }
        }

        editFoodAdapter.updateList(filterList);
    }

}