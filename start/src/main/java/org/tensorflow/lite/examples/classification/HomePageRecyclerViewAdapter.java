package org.tensorflow.lite.examples.classification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.classification.HomePageRecyclerViewInterface;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HomePageRecyclerViewAdapter extends RecyclerView.Adapter<HomePagePlantsViewHolder> {

    private final HomePageRecyclerViewInterface recyclerViewInterface;
    private Context context;

    private List<HomePagePlantModel> homePagePlantModel_List;

    public void setFilteredList(List<HomePagePlantModel> filteredList){
        this.homePagePlantModel_List = filteredList;
        notifyDataSetChanged();
    }

    public HomePageRecyclerViewAdapter(HomePageRecyclerViewInterface recyclerViewInterface, Context context,List<HomePagePlantModel> homePagePlantModel_List) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.homePagePlantModel_List = homePagePlantModel_List;
    }

    @NonNull
    @Override
    public HomePagePlantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomePagePlantsViewHolder(LayoutInflater.from(context).inflate(R.layout.flower_item_view,parent,false),recyclerViewInterface,homePagePlantModel_List);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePagePlantsViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.flower_name_tv.setText((homePagePlantModel_List.get(position).getPlant_name()));
        holder.flower_detail_tv.setText((homePagePlantModel_List.get(position).getSecondary_text()));
        holder.flower_supporting_text_tv.setText((homePagePlantModel_List.get(position).getSupporting_text()));

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            //ensure that the activity more well behaved with strictMode defined, avoid activity leakages
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //set image for the imageview
            holder.flower_iv.setImageBitmap(getBitmapFromURL(homePagePlantModel_List.get(position).getImg_url()));

        }

    }
    // get count of the recyclerview items
    @Override
    public int getItemCount() {
        return homePagePlantModel_List.size();
    }
    // function that used to convert image from URL to bitmap to be set on imageview
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
