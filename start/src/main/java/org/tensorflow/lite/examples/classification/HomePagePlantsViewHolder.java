package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.classification.HomePageRecyclerViewInterface;

import java.util.List;

public class HomePagePlantsViewHolder extends RecyclerView.ViewHolder{

    public ImageView flower_iv;
    public TextView flower_name_tv,flower_detail_tv,flower_supporting_text_tv;
    public CardView flower_card;

    public HomePagePlantsViewHolder(@NonNull View itemView, HomePageRecyclerViewInterface recyclerViewInterface, List<HomePagePlantModel> homePagePlantModel_List) {
        super(itemView);
        flower_iv = itemView.findViewById(R.id.flower_detail_iv);
        flower_name_tv = itemView.findViewById(R.id.flower_name_tv);
        flower_detail_tv = itemView.findViewById(R.id.flower_description);
        flower_supporting_text_tv = itemView.findViewById(R.id.flower_supporting_text);
        flower_card = itemView.findViewById(R.id.flower_card);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewInterface != null){
                    int pos = getBindingAdapterPosition();

                    String selectedPlant = homePagePlantModel_List.get(pos).getPlant_name();

                    if(pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos,selectedPlant);
                    }
                }
            }
        });

    }

}
