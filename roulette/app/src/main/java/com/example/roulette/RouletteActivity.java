package com.example.roulette;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roulette.R;

public class RouletteActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button rotate;

    private Bitmap mBitmap;
    private float angle = 0.0f; // 초기 각도
    private final int IMG_DP = 300; // 이미지 DP

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.wheel);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.roulette_menu);
        mImageView.setImageBitmap(onResizeImage(mBitmap));

        rotate = findViewById(R.id.rotate);
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWheelImage();
            }
        });

    }


    /**
     * from DP to Pixel
     *
     * @param dp
     * @param context
     * @return
     */
    private static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }


    /**
     * image resizing
     *
     * @param bitmap
     * @return
     */
    private Bitmap onResizeImage(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Float size = convertDpToPixel(IMG_DP, this);

        Bitmap resized = null;
        while (height > size.intValue()) {
            resized = Bitmap.createScaledBitmap(bitmap, (width * size.intValue()) / height, size.intValue(), true);
            height = resized.getHeight();
            width = resized.getWidth();
        }
        return resized;
    }

    private int getRandom(int max) {
        return (int) (Math.random() * max);
    }

    private void onWheelImage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int random = getRandom(360);
                float fromAngle = random + 720 + angle; // 회전수 제어(랜덤(0~360)+720도+회전각)
                // 로테이션 애니메이션 초기화
                // 시작각, 종료각, 자기 원을 그리며 회전 옵
                RotateAnimation animation = new RotateAnimation(angle, fromAngle
                        , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


                // 초기 시작각도 업데이트
                angle = fromAngle;
                animation.setDuration(2000); // 지속시간이 길수록 느려짐
                animation.setFillEnabled(true); // 애니메이션 종료된 후 상태 고정 옵션
                animation.setFillAfter(true);
                mImageView.startAnimation(animation);
            }
        });
    }
}