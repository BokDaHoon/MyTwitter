package com.boostcamp.mytwitter.mytwitter.write;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.AtomicFile;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.boostcamp.mytwitter.mytwitter.BuildConfig;
import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.listener.OnSearchClickListener;
import com.boostcamp.mytwitter.mytwitter.scrap.ScrapActivity;
import com.boostcamp.mytwitter.mytwitter.scrap.dialog.CustomDialog;
import com.boostcamp.mytwitter.mytwitter.scrap.model.SearchDTO;
import com.boostcamp.mytwitter.mytwitter.util.Define;
import com.boostcamp.mytwitter.mytwitter.write.dialog.TweetSelectDialog;
import com.boostcamp.mytwitter.mytwitter.write.presenter.WritePresenter;
import com.boostcamp.mytwitter.mytwitter.write.presenter.WritePresenterImpl;
import com.bumptech.glide.Glide;
import com.rengwuxian.materialedittext.MaterialEditText;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    MaterialEditText tweetContent;

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

    private Uri mImageCaptureUri;
    private boolean imageFlag = false;

    private static final String EMPTY_TEXT = "내용을 입력해주세요.";
    private static final String WRITE_SUCCESS = "성공적으로 트윗되었습니다.";

    // Permission REQUEST
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int PERMISSION_REQUEST_ALBUM = 101;

    public static final int RESULT_SUCCESS_TWEET = 200;

    public static final int REQUEST_CAMERA = 300;
    public static final int REQUEST_PICK_FROM_ALBUM = 301;
    private String mCurrentPhotoPath;
    private TweetSelectDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 화면을 벗어날 때 키보드를 내린다.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tweetContent.getWindowToken(), 0);
    }

    void init() {
        presenter = new WritePresenterImpl();
        presenter.setView(this);

        user = TwitterInfo.TwitUser;
        Glide.with(this)
             .load(user.getProfileImageURL())
             .into(userProfileImage);

        tweetContent.requestFocus();
        tweetContent.setHideUnderline(true);
        closeImageButton.setAlpha(0.7f);
        closeImage(); // 첨부 이미지 닫기(초기화면)

        // 키보드 보이게 하는 부분
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    @OnClick(R.id.write_close)
    void writeClose() {
        viewFinish();
    }

    @OnClick(R.id.send_tweet)
    void writeContent() {
        final CharSequence[] items = {"즉시 트윗", "예약 트윗"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // BackKey눌렀을 때 꺼지도록 셋팅.
        alertDialogBuilder
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog,
                                         int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            finish();
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

        // 제목셋팅
        alertDialogBuilder.setTitle("트윗 방식을 선택하십시오.");
        alertDialogBuilder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                switch(id) {
                    case Define.TWEET_DIRECT :
                        dialog.dismiss();
                        directTweet();
                        break;
                    case Define.TWEET_SCHEDULE :
                        dialog.dismiss();
                        scheduleSetting();
                        break;
                    default :
                        dialog.dismiss();
                        break;
                }

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create(); // 다이얼로그 생성
        alertDialog.show(); // 다이얼로그 보여주기

    }

    // 트윗 즉시 전송
    void directTweet() {
        String text = tweetContent.getText().toString();
        if (text.length() < 1) {
            displayEmptyText();
            return;
        }

        StatusUpdate content = new StatusUpdate(text);

        if (imageFlag) {
            File file = new File(mCurrentPhotoPath);

            content.setMedia(file);
        }

        Intent intent = getIntent();
        // 댓글인 경우
        if (intent.hasExtra("ReplyFlag") && intent.getBooleanExtra("ReplyFlag", false)) {
            content.setInReplyToStatusId(intent.getLongExtra(Define.TWEET_ID_KEY, -1));
        }

        presenter.writeContent(content);
    }

    // 트윗 예약 전송
    void scheduleSetting() {
        mDialog = new TweetSelectDialog(this, "[다이얼로그 제목]", listener);
        mDialog.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tweetContent.getWindowToken(), 0);
    }

    private OnSearchClickListener listener = new OnSearchClickListener() {
        @Override
        public void onItemClick(SearchDTO searchDTO) {
            // 검색이 성공했을 시에
            mDialog.dismiss();
        }

    };

    @OnClick(R.id.btn_camera)
    void getCameraContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkCameraPermission();
        } else {
            useCameraPerform();
        }

    }

    void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CAMERA);
        } else {
            useCameraPerform();
        }
    }

    void useCameraPerform() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mImageCaptureUri = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);
                } else {
                    mImageCaptureUri = Uri.fromFile(photoFile);
                }

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        }
    }

    @OnClick(R.id.btn_gallery)
    void getGallryContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAlbumPermission();
        } else {
            useAlbumPerform();
        }

    }

    void checkAlbumPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_ALBUM);
        } else {
            useAlbumPerform();
        }
    }

    void useAlbumPerform() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_PICK_FROM_ALBUM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA :
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    useCameraPerform();
                }

                break;

            case PERMISSION_REQUEST_ALBUM :
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    useAlbumPerform();
                }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File path = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                path      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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

        switch (requestCode) {
            case REQUEST_CAMERA :
                /* empty */
                break;
            case REQUEST_PICK_FROM_ALBUM :
                mImageCaptureUri = data.getData();
                mCurrentPhotoPath = getRealPathFromURI(mImageCaptureUri);
                break;
        }

        File file = new File(mCurrentPhotoPath);

        if (file.length() > 3000000) { // File이 3메가를 초과하면 에러

            FileOutputStream fos = null;
            AtomicFile atomicFile =  new AtomicFile(file);
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                fos = atomicFile.startWrite();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);

                fos.flush();
                fos.close();
                atomicFile.finishWrite(fos);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                atomicFile.failWrite(fos);
                e.printStackTrace();
            }
        }

        if (file.length() > 3000000) { // File 크기를 다시 확인해서 압축 후에도 3메가를 초과했다면 에러
            Toast.makeText(this, "파일 용량이 너무 큽니다.", Toast.LENGTH_SHORT).show();
            Log.d("File Size", file.length() + "");
            return;
        }

        /*File file = new File(mCurrentPhotoPath); // 파일 삭제
        if (!file.delete()) {
            throw new IllegalArgumentException("File delete failed");

        } else { // 파일 테이블 삭제
            MediaScannerConnection.scanFile(this, new String[]{mCurrentPhotoPath}, null, null);
        }*/

        contentImage.setImageURI(mImageCaptureUri);
        contentImage.setVisibility(View.VISIBLE);
        closeImageButton.setVisibility(View.VISIBLE);

        imageFlag = true;

    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}
