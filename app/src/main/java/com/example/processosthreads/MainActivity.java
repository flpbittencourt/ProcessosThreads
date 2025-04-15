package com.example.processosthreads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtImageLink;
    private Button btnDownload;
    private ProgressBar pgbLoading;
    private ImageView imgDownloaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtImageLink = findViewById(R.id.edtImageLink);
        pgbLoading = findViewById(R.id.pgbLoading);
        imgDownloaded = findViewById(R.id.imgDownloaded);
        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String imgUrl = edtImageLink.getText().toString();
        if(!imgUrl.isEmpty()){
            imgDownloaded.setVisibility(View.INVISIBLE);
            pgbLoading.setVisibility(View.VISIBLE);

            try {
                URL url = new URL(imgUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();
                imgDownloaded.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }else {
            imgDownloaded.setVisibility(View.VISIBLE);
            pgbLoading.setVisibility(View.INVISIBLE);
        }
        imgDownloaded.setVisibility(View.VISIBLE);
        pgbLoading.setVisibility(View.INVISIBLE);
    }
}