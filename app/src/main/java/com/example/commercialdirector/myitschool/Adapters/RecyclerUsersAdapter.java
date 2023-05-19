package com.example.commercialdirector.myitschool.Adapters;


import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commercialdirector.myitschool.Activity.UserActivity;
import com.example.commercialdirector.myitschool.R;
import com.example.commercialdirector.myitschool.models.User;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class RecyclerUsersAdapter extends RecyclerView.Adapter<RecyclerUsersAdapter.PersonViewHolder> {

    private final ArrayList<User> users;
    private final Context context;
    public static final String USER_ID = "id_user";
    public static final String USER_NAME = "id_name";

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_found, parent, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.person_name.setText(users.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public RecyclerUsersAdapter(ArrayList<User> users, Context cont) {
        this.users = users;
        this.context = cont;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MaterialCardView cv;
        MaterialTextView person_name;
        ImageView person_photo;

        public PersonViewHolder(View v) {
            super(v);
            cv = (MaterialCardView) v.findViewById(R.id.cv);
            person_name = (MaterialTextView) v.findViewById(R.id.person_name);
            person_photo = (ImageView) v.findViewById(R.id.person_photo);

            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.cv) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra(USER_ID, users.get(getPosition()).getId());
                intent.putExtra(USER_NAME, users.get(getPosition()).getName());
                person_name.setText(intent.getStringExtra(USER_NAME));
                context.startActivity(intent);
                ;
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
