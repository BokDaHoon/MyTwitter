package com.boostcamp.mytwitter.mytwitter.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;

public class LoginAuthorityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_authority);

        WebView webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url != null && url.equals("http://mobile.twitter.com/")) {
                    finish();
                } else if (url != null && url.startsWith(TwitterInfo.TWIT_CALLBACK_URL)) {
                    String[] params = url.split("\\?")[1].split("&");
                    String oauthToken = "";
                    String oauthVerifier = "";

                    try {
                        if (params[0].startsWith("oauth_token")) {
                            oauthToken = params[0].split("=")[1];
                        } else if (params[1].startsWith("oauth_token")) {
                            oauthToken = params[1].split("=")[1];
                        }

                        if (params[0].startsWith("oauth_verifier")) {
                            oauthVerifier = params[0].split("=")[1];
                        } else if (params[1].startsWith("oauth_verifier")) {
                            oauthVerifier = params[1].split("=")[1];
                        }

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("oauthToken", oauthToken);
                        resultIntent.putExtra("oauthVerifier", oauthVerifier);

                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });


        Intent passedIntent = getIntent();
        String authUrl = passedIntent.getStringExtra("authUrl");
        webview.loadUrl(authUrl);
    }
}
