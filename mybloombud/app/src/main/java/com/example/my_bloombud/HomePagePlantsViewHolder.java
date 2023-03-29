package com.example.my_bloombud;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HomePagePlantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView flower_iv;
    public TextView flower_name_tv,flower_detail_tv,flower_supporting_text_tv;
    public CardView flower_card;

    public Context context;

    public HomePagePlantsViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context = context;
        flower_iv = itemView.findViewById(R.id.flower_detail_iv);
        flower_name_tv = itemView.findViewById(R.id.flower_name_tv);
        flower_detail_tv = itemView.findViewById(R.id.flower_description);
        flower_supporting_text_tv = itemView.findViewById(R.id.flower_supporting_text);
        flower_card = itemView.findViewById(R.id.flower_card);
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int position = getBindingAdapterPosition();
        TextView textView = v.findViewById(R.id.flower_name_tv);
        String plantName = textView.getText().toString();

        //Intent intent = new Intent(context,TestPage.class);

        //intent.putExtra("plantName", plantName);

        //context.startActivity(intent);

    }
}
