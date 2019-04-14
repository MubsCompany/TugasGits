package com.example.newsapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newsapi.api.ArticlesItem;
import com.squareup.picasso.Picasso;

import java.util.List;

class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    Context context;
    List<ArticlesItem> data;
    boolean isDark;
    MainActivity mainActivity;

    public NewsAdapter(boolean isDark) {
        this.isDark = isDark;
    }

    public NewsAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public NewsAdapter(Context context, List<ArticlesItem> data) {
        this.context = context;
        this.data = data;
    }


    public NewsAdapter(Context context, List<ArticlesItem> data, boolean isDark) {
        this.context = context;
        this.data = data;
        this.isDark = isDark;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NewsViewHolder( LayoutInflater.from( context ).inflate( R.layout.tampilan_list, viewGroup, false ) );
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder newsViewHolder, final int i) {

        newsViewHolder.itemView.startAnimation( AnimationUtils.loadAnimation( context,R.anim.scale ) );

        newsViewHolder.tvDesc.setText( data.get( i ).getDescription() );
        newsViewHolder.tvAuthor.setText( data.get( i ).getAuthor() );
        newsViewHolder.tvTitle.setText( data.get( i ).getTitle() );


        Picasso.get().load( data.get( i ).getUrlToImage() ).into( newsViewHolder.imgNews );

        newsViewHolder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, Detail.class ).putExtra( "url", data.get( i ).getUrl() );
                intent.setFlags(Intent .FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra( "desc",data.get( i ).getDescription() );
                intent.putExtra( "title",data.get( i ).getTitle() );
                intent.putExtra( "position", i );
                context.startActivity( intent );
            }
        } );

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor, tvDesc, tvTitle;
        ImageView imgNews, imgDetail;
        RelativeLayout relativeLayout;

        public NewsViewHolder(@NonNull View itemView) {
            super( itemView );

            tvTitle = itemView.findViewById( R.id.tv_title );
            tvAuthor = itemView.findViewById( R.id.tv_author );
            tvDesc = itemView.findViewById( R.id.tv_desc );
            imgNews = itemView.findViewById( R.id.img_news );
            relativeLayout = itemView.findViewById( R.id.relative_layout );

            if (isDark) {
                setDarkTheme();
            }
        }

       @SuppressLint("ResourceAsColor")
       private void setDarkTheme(){
            tvDesc.setTextColor(Color.parseColor("#ffffff"));
            tvAuthor.setTextColor(Color.parseColor("#ffffff"));
            relativeLayout.setBackgroundResource( R.drawable.dark_round );
       }
    }

}
