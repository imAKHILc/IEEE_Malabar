package com.ieeemalabar.viewholder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ieeemalabar.R;
import com.ieeemalabar.fragment.PostListFragment;
import com.ieeemalabar.models.Post;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public TextView dateView;
    public ImageButton starView;
    public TextView numStarsView;
    public TextView bodyView;
    public ImageView feature;
    public RelativeLayout post_edit;

    public StorageReference imageRef;
    public FirebaseStorage mStorage;
    public StorageReference mStorageRef;
    public Bitmap bmp;

    public PostViewHolder(View itemView) {
        super(itemView);

        post_edit = (RelativeLayout) itemView.findViewById(R.id.post_edit);
        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        dateView = (TextView) itemView.findViewById(R.id.post_date);
        feature = (ImageView) itemView.findViewById(R.id.feature);
        starView = (ImageButton) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        dateView.setText(post.date);
        numStarsView.setText(String.valueOf(post.starCount));
        String s = post.body.substring(0, Math.min(post.body.length(), 100));
        s = s.replaceAll("[\\t\\n\\r]", " ");
        bodyView.setText(s + "...");
        feature.setVisibility(View.GONE);

        /*mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReferenceFromUrl("gs://project-3576505284407387518.appspot.com/");

        imageRef = mStorageRef.child("featured/" + PostListFragment.subPostKey + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //Toast.makeText(PostDetailActivity.this, "Invalid Name", Toast.LENGTH_SHORT).show()
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                feature.setImageBitmap(bmp);
                feature.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });*/

        starView.setOnClickListener(starClickListener);
    }
}
