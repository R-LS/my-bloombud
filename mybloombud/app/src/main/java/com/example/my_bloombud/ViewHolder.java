package com.example.my_bloombud;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class ViewHolder extends RecyclerView.ViewHolder {

    ImageView flower_iv;
    TextView flower_name_tv,flower_detail_tv,flower_supporting_text_tv;
    CardView flower_card;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        flower_iv = itemView.findViewById(R.id.flower_detail_iv);
        flower_name_tv = itemView.findViewById(R.id.flower_name_tv);
        flower_detail_tv = itemView.findViewById(R.id.flower_description);
        flower_supporting_text_tv = itemView.findViewById(R.id.flower_supporting_text);
        flower_card = itemView.findViewById(R.id.flower_card);
    }
}
