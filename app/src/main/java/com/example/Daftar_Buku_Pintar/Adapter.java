package com.example.Daftar_Buku_Pintar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.list_buku_android.R;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    List<Buku> buku, bukuFilter;
    private Context context;
    private RecyclerViewClickListener mListener;
    CustomFilter filter;

    public Adapter(List<Buku> bukuu, Context context, RecyclerViewClickListener listener) {
        this.buku = buku;
        this.bukuFilter = buku;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_buku, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.mJudulbuku.setText(buku.get(position).getJudulbuku());
        holder.mNamapengarang.setText(buku.get(position).getNamapengarang() + " / "
                + buku.get(position).getPenerbit());
        holder.mTahunterbit.setText(buku.get(position).getTahunterbit());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_baseline_book_24);
        requestOptions.error(R.drawable.ic_baseline_book_24);

        Glide.with(context)
                .load(buku.get(position).getPicture())
                .apply(requestOptions)
                .into(holder.mPicture);

        final Boolean love = buku.get(position).getThumb();

        if (love) {
            holder.mThumb.setImageResource(R.drawable.ic_baseline_thumb_ups_24);
        } else {
            holder.mThumb.setImageResource(R.drawable.ic_baseline_thumb_up_24);
        }

    }

    @Override
    public int getItemCount() {
        return buku.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter((ArrayList<Buku>) bukuFilter, this);

        }
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener mListener;
        private CircleImageView mPicture;
        private ImageView mThumb;
        private TextView mJudulbuku, mNamapengarang, mTahunterbit;
        private RelativeLayout mRowContainer;

        public MyViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mPicture = itemView.findViewById(R.id.picture);
            mJudulbuku = itemView.findViewById(R.id.judulbuku);
            mNamapengarang = itemView.findViewById(R.id.pilihtema);
            mThumb = itemView.findViewById(R.id.thumb);
            mTahunterbit = itemView.findViewById(R.id.tahunterbit);
            mRowContainer = itemView.findViewById(R.id.row_container);

            mListener = listener;
            mRowContainer.setOnClickListener(this);
            mThumb.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.row_container:
                    mListener.onRowClick(mRowContainer, getAdapterPosition());
                    break;
                case R.id.thumb:
                    mListener.onLoveClick(mThumb, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);

        void onLoveClick(View view, int position);

    }
}
