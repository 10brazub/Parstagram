package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import java.util.Date;

public class postDetails extends AppCompatActivity {

    public TextView username;
    public TextView timestamp;
    public ImageView picture;
    public TextView caption;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        username = findViewById(R.id.username);
        timestamp = findViewById(R.id.timestamp);
        picture = findViewById(R.id.picture);
        caption = findViewById(R.id.caption);

        post =  getIntent().getParcelableExtra("PARSE_OBJECT_EXTRA");
        username.setText(post.getKeyUser().getUsername());
        caption.setText(post.getKeyDescription());
        ParseFile picture1 = post.getKeyImage();
        if (picture1 != null) {
            Glide.with(getApplicationContext()).load(post.getKeyImage().getUrl()).into(picture);
        }
        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        timestamp.setText("• " + timeAgo);
    }
}