package kivi.ugran.com.core;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 *  This class has a WebView + Progress Bar + AsyncTask to load the Url
 */
public class DialogWebView extends FrameLayout {

    private static final int LAYOUT = R.layout.webview_dialog;
    public static final String TAG = "TOS_TERMS";
    private WebView webView = null;
    private ProgressBar progressBar = null;
    WebViewClient webViewClient = null;
    public DialogWebView(Context context) {
        this(context, null, 0);
    }

    public DialogWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(21) public DialogWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    @SuppressLint("SetJavaScriptEnabled") private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(LAYOUT, this);

            webView = findViewById(R.id.webView);
            progressBar = findViewById(R.id.webProgressBar);

            //web view callbacks default behaviour. Any overriding behavior to handle the visibility logic
            webViewClient = new WebViewClient() {
                @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    setProgressVisibility(View.VISIBLE);
                }

                @Override public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    setProgressVisibility(View.GONE);
                    setWebViewVisibility(View.VISIBLE);
                }
            };
            setWebViewClient(webViewClient);

            //initial visibility set to Gone.
            webView.setVisibility(View.GONE);
        }
    }

    public WebSettings getSettings() {
        return webView.getSettings();
    }

    public void setProgressVisibility(int value) {
        progressBar.setVisibility(value);
    }

    public void setWebViewVisibility(int value) {
        webView.setVisibility(value);
    }

    public void setWebViewClient(@NonNull WebViewClient webViewClient) {
        webView.setWebViewClient(webViewClient);
    }

    public void setUpUrl(String tosUrl) {
        if (tosUrl != null) {
            webView.loadUrl(tosUrl);
        }
    }

    public void loadData(String content) {
        if (webView != null) {
            webView.loadData(content, "text/html; charset=UTF-8", null);
        }
    }
}

