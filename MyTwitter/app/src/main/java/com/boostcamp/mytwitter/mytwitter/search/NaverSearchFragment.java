package com.boostcamp.mytwitter.mytwitter.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.search.adapter.SearchAdapter;
import com.boostcamp.mytwitter.mytwitter.search.presenter.SearchPresenter;
import com.boostcamp.mytwitter.mytwitter.search.presenter.SearchPresenterImpl;
import com.boostcamp.mytwitter.mytwitter.searchresult.SearchResultActivity;
import com.boostcamp.mytwitter.mytwitter.util.Define;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DaHoon on 2017-02-17.
 */

public class NaverSearchFragment extends Fragment implements SearchPresenter.View {

    private RecyclerView naverSearchWordList;

    private SearchPresenterImpl presenter;
    private SearchAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(R.layout.fragment_naversearch, container, false);
        naverSearchWordList = (RecyclerView) view.findViewById(R.id.naver_search_word_list);

        ButterKnife.bind(this.getActivity());
        init();

        return view;
    }

    void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        adapter = new SearchAdapter(this.getActivity());
        presenter = new SearchPresenterImpl();

        // SearchAdapter 설정.
        naverSearchWordList.setLayoutManager(layoutManager);
        naverSearchWordList.setAdapter(adapter);

        presenter.setView(this);
        presenter.setSearchListAdapterView(adapter);
        presenter.setSearchListAdapterModel(adapter);
        presenter.loadSearchWordList(Define.NAVER_SEARCH_FLAG);
    }

    @Override
    public void moveToSearchResultByWord(int position) {
        Intent intent = new Intent(this.getActivity(), SearchResultActivity.class);
        String word = adapter.getListData(position).getSearchWord();
        intent.putExtra(Define.SEARCH_WORD_KEY, word);
        startActivity(intent);
    }
}
