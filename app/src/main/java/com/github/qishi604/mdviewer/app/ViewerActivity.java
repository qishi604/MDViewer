package com.github.qishi604.mdviewer.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.qishi604.mdviewer.R;
import com.github.qishi604.mdviewer.app.base.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author seven
 * @version V1.0
 * @since 2017/2/15
 */
public class ViewerActivity extends BaseActivity {

	private static final String URL = "file:///android_asset/md/html/md.html";

	private WebView mWebView;

	private String getDataString(String innerHtml) {
		return "<html lang=\"en\">\n" +
				"<head>\n" +
				"<meta charset=\"UTF-8\">\n" +
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
				"<title>Markdown</title>\n" +
				"<script src=\"js/marked.js\"></script>\n" +
				"<script src=\"js/index.js\"></script>\n" +
				"<link rel=\"stylesheet\" href=\"css/github2.css\">\n" +
				"</head>\n" +
				"<body>\n" + innerHtml +
				"</body>\n" +
				"</html>";
	}

	private String getIntentFileString() {
		Intent intent = getIntent();
		Uri uri = intent.getData();

		InputStream in = null;

		if (null != uri) {
			File file = new File(uri.getPath());
			if (file.exists()) {
				try {
					in = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				in = getAssets().open("md/html/about.md");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			InputStreamReader reader = new InputStreamReader(in);
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[1024];
			int n = 0;
			while (-1 != (n = reader.read(buf))) {
				sb.append(buf, 0, n);
			}

			String ss = sb.toString();
//			String des = ss.replaceAll("#", "%23").replaceAll("%", "%25").replaceAll("\\\\", "%27").replaceAll("\\?", "%3f");

			return ss;
		} catch (IOException e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	private void initView() {
		mWebView = new WebView(this);

		ViewGroup container = (ViewGroup) findViewById(R.id.lContainer);
		container.addView(mWebView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		initWebSetting(mWebView);

		mWebView.loadDataWithBaseURL("file:///android_asset/md/", getDataString(getIntentFileString()), "text/html", "utf-8", null);
	}

	private void initWebSetting(WebView webView) {
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setAllowFileAccess(true);
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		settings.setAppCacheEnabled(true);
		settings.setDatabaseEnabled(true);

		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewer);
		initView();
	}
}
