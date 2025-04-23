package com.example.processosthreads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

        //Atribuindo os ids dos componentes visuais às respectivas variáveis
        edtImageLink = findViewById(R.id.edtImageLink);
        pgbLoading = findViewById(R.id.pgbLoading);
        imgDownloaded = findViewById(R.id.imgDownloaded);
        Button btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String imgUrl = edtImageLink.getText().toString();
        if(!imgUrl.isEmpty()){
            imgDownloaded.setVisibility(View.INVISIBLE);
            pgbLoading.setVisibility(View.VISIBLE);
            executarDownload(imgUrl);
        }else {
            Toast.makeText(MainActivity.this, R.string.no_url, Toast.LENGTH_SHORT).show();
        }
    }

    //Function para gerenciar o carregamento da imagem via link, bem como ativação da progress bar enquanto a imagem é carregada
    private void executarDownload(String imgUrl) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    //Realiza o download da imagem ná varrável bitmap
                    InputStream in = new URL(imgUrl).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();

                    //Atualiza a UI para que imgDownloaded receba o bitmap da URL e se torne visível
                    //Atualiza o progressLoadring para que não seja mais carregado
                    final Bitmap finalBitmap = bitmap;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imgDownloaded.setImageBitmap(finalBitmap);
                            imgDownloaded.setVisibility(View.VISIBLE);
                            pgbLoading.setVisibility(View.GONE);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    //Tratador da exceção
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pgbLoading.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }).start();

        //Código imcompleto - Teste função de carregamento do bitmap [NOK]
        /*new Thread(new Runnable() {
            @Override
            public void run() {
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
                    throw new RuntimeException(e);
                }
            }
        }).start();*/
    }
}