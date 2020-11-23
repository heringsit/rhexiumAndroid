package co.kr.inclass.herings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    private Context context;
    private WebView wv_content;
    private MainActivity mMainActivity;

    public MyWebViewClient(Context context, WebView wv_content) {

        this.context = context;
        this.wv_content = wv_content;
        this.wv_content.setBackgroundColor(Color.TRANSPARENT);
        mMainActivity = (MainActivity)context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }
}