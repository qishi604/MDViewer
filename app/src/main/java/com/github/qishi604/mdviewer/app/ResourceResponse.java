package com.github.qishi604.mdviewer.app;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.github.qishi604.mdviewer.util.MimeUtils;

import java.io.InputStream;

/**
 * @author mchwind
 * @version V5.8
 * @since 16/12/8
 */
public class ResourceResponse {

	public static final boolean DEBUG = true;
	private static final String TAG = "ResourceResponse";
	private static final String ASSETS = "app";
	private static final String ENCODING = "utf-8";


	public static WebResourceResponse shouldInterceptRequest(WebView view, String requestUrl) {
		if (DEBUG) {
			Log.i(TAG, "shouldInterceptRequest: url:" + requestUrl);
		}

		String fileExtension = MimeTypeMap.getFileExtensionFromUrl(requestUrl);

		if (!shouldIntercept(requestUrl)) {
			return null;
		}

		InputStream data;
		try {
			Uri uri = Uri.parse(requestUrl);
			String path = uri.getPath();
			String mimeType = MimeUtils.guessMimeTypeFromExtension(fileExtension);

			data = view.getContext().getAssets().open(ASSETS + path);
			return new WebResourceResponse(mimeType, ENCODING, data);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param fileExtension
	 * @return
	 */
	private static boolean shouldIntercept(String fileExtension) {
		if ("js".equals(fileExtension) || "css".equals(fileExtension)) {
			return true;
		}
		return false;
	}
}
