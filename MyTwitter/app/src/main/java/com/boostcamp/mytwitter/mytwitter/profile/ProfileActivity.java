package com.boostcamp.mytwitter.mytwitter.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.detail.DetailActivity;
import com.boostcamp.mytwitter.mytwitter.profile.presenter.ProfilePresenter;
import com.boostcamp.mytwitter.mytwitter.profile.presenter.ProfilePresenterImpl;
import com.boostcamp.mytwitter.mytwitter.timeline.TimelineActivity;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.TimelineAdapter;
import com.boostcamp.mytwitter.mytwitter.util.Define;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import twitter4j.User;

public class ProfileActivity extends AppCompatActivity implements ProfilePresenter.View {

    @BindView(R.id.profile_background)
    ImageView profileBackground;
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.profile_id)
    TextView profileId;
    @BindView(R.id.btn_profile_setting)
    Button profileSetting;
    @BindView(R.id.following_count)
    TextView followingCount;
    @BindView(R.id.follower_count)
    TextView followerCount;
    @BindView(R.id.back_button)
    ImageButton backButton;
    @BindView(R.id.user_tweet_list)
    RecyclerView userTweetList;

    private ProfilePresenterImpl presenter;
    private TimelineAdapter adapter;
    private User user;
    private RequestManager mGlideRequestManager;
    private String profileImagePath;
    private String profileBackgroundPath;

    private static final String DEFAULT_PROFILE_URL = "https://abs.twimg.com/a/1487131246/img/t1/highline/empty_state/owner_empty_avatar.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        init();
    }

    void init() {
        mGlideRequestManager = Glide.with(this);
        presenter = new ProfilePresenterImpl();
        presenter.setView(this);

        int profileFlag = getIntent().getIntExtra("ProfileFlag", -1);

        switch(profileFlag) {
            case Define.MY_PROFILE :
                presenter.initMyProfile();
                presenter.loadUserTweetList(user.getId());
                profileSetting.setVisibility(View.VISIBLE);
                break;
            case Define.OTHER_PROFILE :
                Intent intent = getIntent();
                long id = intent.getLongExtra(Define.USER_ID_KEY, -1);
                if (id != -1) {
                    presenter.initOtherProfile(id);
                    presenter.loadUserTweetList(id);
                }
                profileSetting.setVisibility(View.GONE);
                break;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new TimelineAdapter(this);

        userTweetList.setLayoutManager(layoutManager);
        userTweetList.setAdapter(adapter);

        presenter.setTimelineListAdapterModel(adapter);
        presenter.setTimelineListAdapterView(adapter);
    }

    @Override
    public void setMyProfile(User user) {
        this.user = user;

        profileBackgroundPath = user.getProfileBackgroundImageURL();
        // Profile Background
        if (profileBackgroundPath != null) { // ProfileBackground 설정이 존재할 경우

            profileBackground.post(new Runnable() {
                @Override
                public void run() {
                    mGlideRequestManager
                            .load(profileBackgroundPath)
                            .centerCrop()
                            .into(profileBackground);
                }
            });
        } else { // ProfileBackground 설정이 존재하지 않을 경우
            profileBackground.setBackgroundColor(getResources().getColor(R.color.twitter_default_background_color));
        }


        // Profile Image
        if (!user.isDefaultProfileImage()) { // ProfileImage 설정이 존재할 경우
            profileImagePath = user.getOriginalProfileImageURL();

            profileImage.post(new Runnable() {
                @Override
                public void run() {
                    mGlideRequestManager
                            .load(profileImagePath)
                            .into(profileImage);
                }
            });

        } else { // ProfileImage 설정이 존재하지 않을 경우
            profileImage.setImageResource(R.drawable.default_profile);
        }

        profileName.setText(user.getName());
        profileId.setText("@" + user.getScreenName());
        followingCount.setText(user.getFriendsCount() + "");
        followerCount.setText(user.getFollowersCount() + "");
    }

    @Override
    public void moveToDetail(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TimelineActivity.DETAIL_STATUS_KEY, adapter.getListData(position));
        intent.putExtra(TimelineActivity.VIEWHOLDER_TYPE, adapter.getItemViewType(position));
        intent.putExtra(TimelineActivity.DETAIL_STATUS_KEY, bundle);
        startActivity(intent);
    }

    @OnClick(R.id.back_button)
    void backButtonPressed() {
        finish();
    }
}
