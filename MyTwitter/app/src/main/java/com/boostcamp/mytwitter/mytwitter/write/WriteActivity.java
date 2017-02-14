package com.boostcamp.mytwitter.mytwitter.write;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.write.presenter.WritePresenter;
import com.boostcamp.mytwitter.mytwitter.write.presenter.WritePresenterImpl;
import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import twitter4j.StatusUpdate;
import twitter4j.User;

public class WriteActivity extends AppCompatActivity implements WritePresenter.View {

    @BindView(R.id.write_close)
    ImageButton closeButton;
    @BindView(R.id.user_profile_image)
    ImageView userProfileImage;
    @BindView(R.id.tweet_content)
    EditText tweetContent;

    @BindView(R.id.content_image)
    ImageView contentImage;
    @BindView(R.id.content_image_close)
    ImageButton closeImageButton;

    @BindView(R.id.btn_camera)
    ImageButton cameraButton;
    @BindView(R.id.btn_gallery)
    ImageButton galleryButton;

    private User user;
    private WritePresenterImpl presenter;
    private boolean imageFlag = false;

    private static final String EMPTY_TEXT = "내용을 입력해주세요.";
    private static final String WRITE_SUCCESS = "성공적으로 트윗되었습니다.";

    public static final int RESULT_SUCCESS_TWEET = 200;

    public static final int REQUEST_CAMERA = 300;
    public static final int REQUEST_PICK_FROM_ALBUM = 301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        ButterKnife.bind(this);
        init();
    }

    void init() {
        presenter = new WritePresenterImpl();
        presenter.setView(this);

        user = TwitterInfo.TwitUser;
        Glide.with(this)
             .load(user.getProfileImageURL())
             .into(userProfileImage);

        tweetContent.requestFocus();
        closeImageButton.setAlpha(0.7f);
        closeImage(); // 첨부 이미지 닫기(초기화면)

        //키보드 보이게 하는 부분
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @OnClick(R.id.write_close)
    void writeClose() {
        viewFinish();
    }

    @OnClick(R.id.send_tweet)
    void writeContent() {
        String text = tweetContent.getText().toString();
        Log.d("WriteActivity", text.length()+"");
        if(text.length() < 1){
            displayEmptyText();
            return;
        }

        StatusUpdate content = new StatusUpdate(text);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) contentImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();

        presenter.writeContent(content);
    }

    @OnClick(R.id.btn_camera)
    void getCameraContent() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    @OnClick(R.id.btn_gallery)
    void getGallryContent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_PICK_FROM_ALBUM);
    }

    @OnClick(R.id.content_image_close)
    void closeImage() {
        contentImage.setVisibility(View.GONE);
        closeImageButton.setVisibility(View.GONE);
        imageFlag = false;
    }

    void displayEmptyText() {
        Toast.makeText(this, EMPTY_TEXT, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void writeContentSuccess() {
        Toast.makeText(this, WRITE_SUCCESS, Toast.LENGTH_SHORT).show();
        setResult(RESULT_SUCCESS_TWEET);
    }

    @Override
    public void viewFinish() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap photo = null;
        if (data == null) {
           return;
        }

        switch (requestCode) {
            case REQUEST_CAMERA :

                photo = (Bitmap) data.getExtras().get("data");
                contentImage.setImageBitmap(photo);

            case REQUEST_PICK_FROM_ALBUM :
                contentImage.setImageURI(data.getData());
                break;
        }

        contentImage.setVisibility(View.VISIBLE);
        closeImageButton.setVisibility(View.VISIBLE);

        imageFlag = true;

    }

}
