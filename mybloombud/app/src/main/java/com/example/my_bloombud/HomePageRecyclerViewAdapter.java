package com.example.my_bloombud;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HomePageRecyclerViewAdapter extends RecyclerView.Adapter<HomePagePlantsViewHolder> {

    private Context context;
    private List<String> imgList;
    private List<String> nameList;

    public HomePageRecyclerViewAdapter(Context context, List<String> imgList, List<String> nameList) {
        this.context = context;
        this.imgList = imgList;
        this.nameList = nameList;

    }

    @NonNull
    @Override
    public HomePagePlantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("A","BYE");
        return new HomePagePlantsViewHolder(LayoutInflater.from(context).inflate(R.layout.flower_item_view,parent,false),context);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePagePlantsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d("A",nameList.get(position));
        holder.flower_name_tv.setText((nameList.get(position)));

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            holder.flower_iv.setImageBitmap(getBitmapFromURL(imgList.get(position)));

        }

    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

}
