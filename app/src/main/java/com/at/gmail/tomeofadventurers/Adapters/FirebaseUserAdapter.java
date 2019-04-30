package com.at.gmail.tomeofadventurers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Classes.FirebaseUser;
import com.at.gmail.tomeofadventurers.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class FirebaseUserAdapter extends RecyclerView.Adapter<FirebaseUserAdapter.ViewHolder> {


    private Context mContext;
    private ArrayList<FirebaseUser> mFireUsers= new ArrayList<>();
    //private ArrayList<String> mImages = new ArrayList<>();
    //private ArrayList<String> mEmails = new ArrayList<>();
    //private ArrayList<String> mUserNames = new ArrayList<>();

    public FirebaseUserAdapter(Context context, ArrayList<FirebaseUser> FireUsers){//ArrayList<String> images, ArrayList<String> emails, ArrayList<String> userNames){

        mContext = context;
        mFireUsers = FireUsers;
        //mImages = images;
        //mEmails = emails;
        //mUserNames = userNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Glide.with(mContext)
                .asBitmap()
                .load( mFireUsers.get(i).GetProfilePic())//mImages.get(i))
                .into(viewHolder.image);

        viewHolder.email.setText( mFireUsers.get(i).GetEmail());//mEmails.get(i));
        viewHolder.userName.setText(mFireUsers.get(i).GetUserName());//mUserNames.get(i));

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mFireUsers.get(i).GetEmail(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFireUsers.size();//mEmails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView email;
        TextView userName;
        CardView parentLayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView_profile_picture);
                email = itemView.findViewById(R.id.textView_email);
                userName = itemView.findViewById(R.id.textView_name);
                parentLayout = itemView.findViewById(R.id.cardView_item_user);
            }
        }
}
