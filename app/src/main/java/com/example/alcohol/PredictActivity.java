package com.example.alcohol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PredictActivity extends AppCompatActivity {

    TextView result, confidence;
    ImageView imageView;
    Button picture;

    int imageSize = 224;

    private long doubleBackPressed;
    private static final int Interval = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {

                    Intent cameraIntent =
                            new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);

                } else {
                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }


    public void classifyImage(Bitmap image) {
        // Placeholder result
        result.setText("Normal");
        confidence.setText("Confidence: 100%");
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Bitmap image = (Bitmap) data.getExtras().get("data");

            int dimension =
                    Math.min(image.getWidth(), image.getHeight());
            image =
                    ThumbnailUtils.extractThumbnail(image, dimension, dimension);

            imageView.setImageBitmap(image);

            image =
                    Bitmap.createScaledBitmap(
                            image, imageSize, imageSize, false);

            classifyImage(image);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (doubleBackPressed + Interval > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(
                    this,
                    "Press Again To Exit",
                    Toast.LENGTH_SHORT
            ).show();
            doubleBackPressed = System.currentTimeMillis();
        }
    }
}
