package com.mazad.mazadangy.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mazad.mazadangy.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder>{

    public List<Uri> fileNameList;

    public UploadListAdapter(List<Uri> fileNameList){

        this.fileNameList = fileNameList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Uri fileName = fileNameList.get(position);
        Picasso.get().load(fileName).into(holder.upload_icon);
        holder.deleteImge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileNameList.remove(position);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public ImageView upload_icon,deleteImge;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


            upload_icon = (ImageView) mView.findViewById(R.id.upload_icon);
            deleteImge= (ImageView) mView.findViewById(R.id.deleteImge);

        }

    }

}
