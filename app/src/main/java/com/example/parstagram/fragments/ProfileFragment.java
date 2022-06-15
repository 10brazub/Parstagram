package com.example.parstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {


    @Override
    protected void queryPosts() {
        super.queryPosts();

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground((posts, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with posts", e);
                return;
            }
            for (Post post: posts) {
                Log.i(TAG, "Post: " + post.getKeyDescription() + ", user: " + post.getKeyUser().getUsername());
            }
//            adapter.notifyDataSetChanged();
        });
    }
}
