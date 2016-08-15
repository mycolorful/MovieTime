package per.yrj.movietime.view.activities.news;

import android.os.Bundle;
import android.webkit.WebView;

import per.yrj.movietime.R;
import per.yrj.movietime.view.activities.BaseActivity;

/**
 * Created by YiRenjie on 2016/6/22.
 */
public class NewsActivity extends BaseActivity {
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        this.webview = (WebView) findViewById(R.id.web_view);
        webview.loadUrl(getIntent().getStringExtra("url"));
    }
}
