package com.example.signuplogin_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuplogin_app.Food;
import com.example.signuplogin_app.FoodCount;
import com.example.signuplogin_app.R;
import com.example.signuplogin_app.adapter.TopFoodAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticActivity extends AppCompatActivity {

    private TextView tvTotalOrders, tvTotalRevenue;
    private RecyclerView rvTopFoods;
    private Spinner spinnerMonth, spinnerYear;
    private Button btnFilter;
    private ImageView btn_back;

    private List<FoodCount> topFoods;
    private TopFoodAdapter topFoodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        // Ánh xạ view
        tvTotalOrders = findViewById(R.id.tv_total_orders);
        tvTotalRevenue = findViewById(R.id.tv_total_revenue);
        spinnerMonth = findViewById(R.id.spinner_month);
        spinnerYear = findViewById(R.id.spinner_year);
        btnFilter = findViewById(R.id.btn_filter);
        rvTopFoods = findViewById(R.id.rv_top_foods);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticActivity.this,MainActivity.class));
            }
        });
        // Cấu hình RecyclerView
        rvTopFoods.setLayoutManager(new LinearLayoutManager(this));
        topFoods = new ArrayList<>();
        topFoodAdapter = new TopFoodAdapter(topFoods);
        rvTopFoods.setAdapter(topFoodAdapter);

        // Lấy tháng năm hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        // Cài đặt spinner
        setupSpinners(currentMonth, currentYear);

        // Sự kiện nút lọc
        btnFilter.setOnClickListener(v -> {
            int month = Integer.parseInt(spinnerMonth.getSelectedItem().toString());
            int year = Integer.parseInt(spinnerYear.getSelectedItem().toString());
            filterOrders(month, year);
        });

        // Lọc mặc định
        filterOrders(currentMonth, currentYear);
    }

    private void setupSpinners(int currentMonth, int currentYear) {
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) months.add(String.valueOf(i));

        List<String> years = new ArrayList<>();
        for (int i = currentYear - 2; i <= currentYear + 1; i++) years.add(String.valueOf(i));

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);
        spinnerMonth.setSelection(currentMonth - 1);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
        spinnerYear.setSelection(years.indexOf(String.valueOf(currentYear)));
    }

    private void filterOrders(int month, int year) {
        FirebaseDatabase.getInstance().getReference("bills")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int totalOrders = 0;
                        long totalRevenue = 0;
                        Map<String, Integer> foodCountMap = new HashMap<>();

                        for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                            String timestampStr = billSnapshot.child("timestamp").getValue(String.class);
                            Long total = billSnapshot.child("total").getValue(Long.class);

                            if (timestampStr == null || total == null) continue;

                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date date = sdf.parse(timestampStr);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);

                                int billMonth = cal.get(Calendar.MONTH) + 1;
                                int billYear = cal.get(Calendar.YEAR);

                                if (billMonth == month && billYear == year) {
                                    totalOrders++;
                                    totalRevenue += total;

                                    for (DataSnapshot itemSnapshot : billSnapshot.child("items").getChildren()) {
                                        String name = itemSnapshot.child("name").getValue(String.class);
                                        Long quantity = itemSnapshot.child("quantity").getValue(Long.class);

                                        if (name != null && quantity != null) {
                                            name = name.trim();
                                            int count = foodCountMap.getOrDefault(name, 0);
                                            foodCountMap.put(name, count + quantity.intValue());
                                        }
                                    }
                                }

                            } catch (ParseException e) {
                                Toast.makeText(StatisticActivity.this,
                                        "Lỗi định dạng ngày giờ!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Cập nhật giao diện
                        tvTotalOrders.setText("Tổng đơn: " + totalOrders);

                        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                        tvTotalRevenue.setText("Doanh thu: " + formatter.format(totalRevenue) + "đ");

                        // Tính top 3 món ăn
                        List<FoodCount> foodCounts = new ArrayList<>();
                        for (Map.Entry<String, Integer> entry : foodCountMap.entrySet()) {
                            foodCounts.add(new FoodCount(entry.getKey(), entry.getValue()));
                        }

                        Collections.sort(foodCounts, (a, b) -> b.count - a.count);

                        topFoods.clear();
                        topFoods.addAll(foodCounts.subList(0, Math.min(3, foodCounts.size())));
                        topFoodAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StatisticActivity.this,
                                "Lỗi khi lấy dữ liệu từ Firebase!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
