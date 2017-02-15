package com.github.qishi604.mdviewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * markdown list activity
 * <p>
 * todo browse all the directory
 */
public class MainActivity extends Activity {

	private static final Map<String, String> EXTENSION_MAP = new HashMap();

	static {
		EXTENSION_MAP.put("md", "1");
		EXTENSION_MAP.put("markdown", "1");
	}

	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListView = new ListView(this);
		mListView.setDivider(null);

		setContentView(mListView);
		mListView.setFitsSystemWindows(true);
		listMdFiles();
	}

	private void viewMd(File file) {
		if (null == file) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "text/plain");
		startActivity(intent);
	}

	private boolean isMDOrTextFile(File file) {
		String name = file.getName();
		int dotIndex = name.lastIndexOf(".");
		if (dotIndex <= 1) {
			return false;
		}
		String fileExtension = name.substring(dotIndex + 1);

		return null != fileExtension && EXTENSION_MAP.get(fileExtension) != null;
	}

	private ArrayList<File> mdfiles = new ArrayList<>();

	private void listFile(File dir) {
		if (null != dir && dir.exists()) {
			File[] files = dir.listFiles();
			if (null != files && files.length > 0) {
				for (File file : files) {
					if (file.isDirectory()) {
						listFile(file);
					} else if (isMDOrTextFile(file)) {
						mdfiles.add(file);
					}
				}
			}
		}
	}

	private void listMdFiles() {
		File root = Environment.getExternalStorageDirectory();

		listFile(root);

		if (null != mdfiles && mdfiles.size() > 0) {

			BaseAdapter adapter = new BaseAdapter() {
				@Override
				public int getCount() {
					return mdfiles.size();
				}

				@Override
				public File getItem(int position) {
					return mdfiles.get(position);
				}

				@Override
				public long getItemId(int position) {
					return position;
				}

				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					ViewHolder holder = null;
					if (null == convertView) {
						convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_md, null);
						holder = new ViewHolder();
						holder.mTvNum = (TextView) convertView.findViewById(R.id.tvNum);
						holder.mTvFile = (TextView) convertView.findViewById(R.id.tvFile);
						convertView.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								viewMd((File) v.getTag(R.id.data));
							}
						});

						convertView.setTag(holder);
					} else {
						holder = (ViewHolder) convertView.getTag();
					}
					File file = getItem(position);
					holder.mTvNum.setText(position + ".");
					holder.mTvFile.setText(file.getName());
					convertView.setTag(R.id.data, file);

					return convertView;
				}
			};

			mListView.setAdapter(adapter);
		}
	}

	static class ViewHolder {
		TextView mTvNum;
		TextView mTvFile;
	}
}
