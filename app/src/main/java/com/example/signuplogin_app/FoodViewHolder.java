package com.example.signuplogin_app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class FoodViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgFood;
    public TextView txtName, txtPrice;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        imgFood = itemView.findViewById(R.id.item_icon);
        txtName = itemView.findViewById(R.id.txt_namefood);
        txtPrice = itemView.findViewById(R.id.txt_pricefood);
    }

    public void bind(Food food) {
        // Sử dụng thư viện Glide để tải và hiển thị hình ảnh từ URL
        Glide.with(itemView.getContext())
                .load(food.getImageUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(imgFood);

        txtName.setText(food.getNamefood());
        txtPrice.setText(String.valueOf(food.getPricefood()));

    }
}
