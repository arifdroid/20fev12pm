package com.example.dell.a19febnoon;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.InsideCustomHolder> {

    ArrayList<ContactObject> userList;

    public CustomRecyclerAdapter(ArrayList<ContactObject> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public InsideCustomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //return null;

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_layout,null,false);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        view.setLayoutParams(lp);

        return new InsideCustomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InsideCustomHolder insideCustomHolder, int i) {

        insideCustomHolder.textViewPhonee.setText(userList.get(i).getPhoneNumber());
        insideCustomHolder.textViewNamee.setText(userList.get(i).getNamee());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class InsideCustomHolder extends RecyclerView.ViewHolder {
        //here from our custom layout

        public TextView textViewNamee,textViewPhonee;

        public InsideCustomHolder(@NonNull View itemView) {
            super(itemView);

            textViewNamee = itemView.findViewById(R.id.textViewName);
            textViewPhonee = itemView.findViewById(R.id.textViewPhone);


        }
    }
}
