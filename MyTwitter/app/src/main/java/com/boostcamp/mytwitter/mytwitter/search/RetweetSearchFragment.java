package com.boostcamp.mytwitter.mytwitter.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.detail.DetailActivity;
import com.boostcamp.mytwitter.mytwitter.search.adapter.RetweetSearchAdapter;
import com.boostcamp.mytwitter.mytwitter.search.model.TwitterSearchDTO;
import com.boostcamp.mytwitter.mytwitter.search.presenter.RetweetSearchPresenter;
import com.boostcamp.mytwitter.mytwitter.search.presenter.SearchPresenter;
import com.boostcamp.mytwitter.mytwitter.search.presenter.RetweetSearchPresenterImpl;
import com.boostcamp.mytwitter.mytwitter.util.Define;

import java.util.List;

import butterknife.ButterKnife;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-17.
 */

public class RetweetSearchFragment extends Fragment implements RetweetSearchPresenter.View {

    private RecyclerView twitterSearchList;

    private RetweetSearchPresenterImpl presenter;
    private RetweetSearchAdapter adapter;
    public static final String DETAIL_STATUS_KEY = "detail_status_key";
    public static final String VIEWHOLDER_TYPE = "viewholder_type";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(R.layout.fragment_twittersearch, container, false);
        twitterSearchList = (RecyclerView) view.findViewById(R.id.twitter_search_word_list);

        ButterKnife.bind(this.getActivity());
        init();

        return view;
    }

    void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        adapter = new RetweetSearchAdapter(this.getActivity());
        presenter = new RetweetSearchPresenterImpl();

        // SearchAdapter 설정.
        twitterSearchList.setLayoutManager(layoutManager);
        twitterSearchList.setAdapter(adapter);

        presenter.setView(this);
        presenter.setSearchListAdapterView(adapter);
        presenter.setSearchListAdapterModel(adapter);
        presenter.loadSearchWordList(Define.TWITTER_SEARCH_FLAG);
    }

    @Override
    public void moveToDetail(Status status) {
        Intent intent = new Intent(this.getActivity(), DetailActivity.class);

        Bundle bundle = new Bundle();
        URLEntity[] urlResult = status.getURLEntities();
        Log.d("클릭", status.toString());

        int type = 0;
        // Tweet에 이미지가 있는 경우
        if (status.getMediaEntities().length != 0) {
            type = Define.TIMELINE_IMAGE_TYPE;

            // Tweet에 CardView가 있는 경우
        } else if (urlResult.length != 0 && urlResult[0].getExpandedURL() != null) {
            type = Define.TIMELINE_CARDVIEW_TYPE;

            // Tweet에 이미지와 CardView가 없는 경우
        } else {
            type = Define.TIMELINE_COMMON_TYPE;
        }

        bundle.putSerializable(DETAIL_STATUS_KEY, status);
        intent.putExtra(VIEWHOLDER_TYPE, type);
        intent.putExtra(DETAIL_STATUS_KEY, bundle);

        startActivity(intent);
    }
}
