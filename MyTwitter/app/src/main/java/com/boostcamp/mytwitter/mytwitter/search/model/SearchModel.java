package com.boostcamp.mytwitter.mytwitter.search.model;

import android.os.AsyncTask;
import android.util.Log;

import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.timeline.model.TimelineModel;
import com.boostcamp.mytwitter.mytwitter.util.Define;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.api.TrendsResources;

/**
 * Created by DaHoon on 2017-02-17.
 */

public class SearchModel {

    private TwitterSearchDTO twitterSearchList;
    private SearchModel.ModelDataChange mModelDataChange;
    private SearchModel.RetweetModelDataChange mRetweetModelDataChange;

    public interface RetweetModelDataChange {
        void update(TwitterSearchDTO list);
    }


    public interface ModelDataChange<T> {
        void update(List<T> list);
    }


    public interface TwitterService {
        @GET("rank/realtime.php")
        Call<TwitterSearchDTO> listTwitterWordSearch(@Query("from") int start, @Query("to") int end);
    }


    public void setOnChangeListener(ModelDataChange dataChange) {
        mModelDataChange = dataChange;
    }


    public void setRetweetOnChangeListener(RetweetModelDataChange dataChange) {
        mRetweetModelDataChange = dataChange;
    }


    public void callLoadSearchWordList(int type) {

        switch(type) {
            case Define.TWITTER_SEARCH_FLAG :
                new TwitterLoadSearchTask().execute(Define.TWITTER_URL);
                break;
            case Define.NAVER_SEARCH_FLAG :
                new NaverLoadSearchWordTask().execute(Define.NAVER_URL);
                break;
            case Define.DAUM_SEARCH_FLAG :
                new DaumLoadSearchWordTask().execute(Define.DAUM_URL);
                break;
        }

    }

    // RT 순위 내용 클릭 시 Tweet의 내용을 가져온다.
    public Status getStatusInfo(Long id) {
        Status result = null;

        try {
            result = new GetStatusInfoTask().execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // RT 순위 내용 클릭 시 내용을 가져오는 AsyncTask
    class GetStatusInfoTask extends AsyncTask<Long, Void, Status> {

        @Override
        protected twitter4j.Status doInBackground(Long... params) {
            long id = params[0];
            Twitter mTwit = TwitterInfo.TwitInstance;

            twitter4j.Status status = null;
            try {
                status = mTwit.showStatus(id);
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return status;
        }
    }


    class TwitterLoadSearchTask extends AsyncTask<String, Void, List<TwitterSearchDTO>> {

        @Override
        protected List<TwitterSearchDTO> doInBackground(String... params) {
            String baseUrl = params[0];
            Log.d("", baseUrl);

            Retrofit retrofit = new Retrofit.Builder()
                                   .baseUrl(baseUrl)
                                   .addConverterFactory(GsonConverterFactory.create())
                                   .build();

            TwitterService service = retrofit.create(TwitterService.class);
            Call<TwitterSearchDTO> call = service.listTwitterWordSearch(1, 10);
            Log.d("", call.request().toString());

            call.enqueue(new Callback<TwitterSearchDTO>() {

                @Override
                public void onResponse(Call<TwitterSearchDTO> call, Response<TwitterSearchDTO> response) {
                    if (response.isSuccessful()) {
                        twitterSearchList = response.body();

                        Log.d("", "load pre");
                        if (mRetweetModelDataChange != null) {
                            Log.d("", "load");
                            mRetweetModelDataChange.update(twitterSearchList);
                        }
                    }

                }

                @Override
                public void onFailure(Call<TwitterSearchDTO> call, Throwable t) {
                    Log.d("ERROR CODE", t.toString());
                    Log.d("Twitter Search", "ERROR");
                }
            });

            /*Twitter mTwit = TwitterInfo.TwitInstance;
            ResponseList<Location> locations = null;
            int idTrendLocation = 0;
            try {
                locations = mTwit.getAvailableTrends();
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            for (Location location : locations) {
                if (location.getName().toLowerCase().equals("korea".toLowerCase())) {
                    idTrendLocation = location.getWoeid();
                    break;
                }
            }

            if (idTrendLocation > 0) {
                try {
                    Trends data = mTwit.getPlaceTrends(idTrendLocation);
                    for (int i = 0; i < data.getTrends().length; i++) {
                        Log.d("Twitter Trend", data.getTrends()[i].getName());
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }*/

            return null;
        }

        @Override
        protected void onPostExecute(List<TwitterSearchDTO> twitterSearchDTOs) {
            super.onPostExecute(twitterSearchDTOs);
        }
    }

    class NaverLoadSearchWordTask extends AsyncTask<String, Void, List<SearchDTO>> {

        private String url = "";

        @Override
        protected List<SearchDTO> doInBackground(String... params) {

            url = params[0];

            Connection conn = Jsoup.connect(url);
            Document doc = null;
            try {
                doc = conn.get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<SearchDTO> result = new ArrayList<>();

            if (null != doc) {
                Elements elements = doc.select("ol#realrank > li:not(#lastrank) > a");

                for (int i = 0; i < elements.size(); i++) {
                    SearchDTO searchDTO = new SearchDTO().setRank((i+1))
                                                         .setSearchWord(elements.get(i).attr("title"));
                    result.add(searchDTO);
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<SearchDTO> searchDTOs) {
            super.onPostExecute(searchDTOs);
            mModelDataChange.update(searchDTOs);
        }
    }

    class DaumLoadSearchWordTask extends AsyncTask<String, Void, List<SearchDTO>> {

        private String url = "";

        @Override
        protected List<SearchDTO> doInBackground(String... params) {

            url = params[0];

            Connection conn = Jsoup.connect(url);
            Document doc = null;
            try {
                doc = conn.get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<SearchDTO> result = new ArrayList<>();

            if (null != doc) {
                Elements elements = doc.select("ol#realTimeSearchWord > li > div > div > span.txt_issue > a");

                for (int i = 0; i < elements.size(); i++) {
                    if(i % 2 != 1){
                        continue;
                    }

                    SearchDTO searchDTO = new SearchDTO().setRank((i+1)/2)
                            .setSearchWord(elements.get(i).text());
                    result.add(searchDTO);
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<SearchDTO> searchDTOs) {
            super.onPostExecute(searchDTOs);
            mModelDataChange.update(searchDTOs);
        }
    }
}
