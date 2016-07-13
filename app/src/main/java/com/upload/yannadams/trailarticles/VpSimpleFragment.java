package com.upload.yannadams.trailarticles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class VpSimpleFragment extends Fragment {
	public static final String BUNDLE_TITLE = "title";
	private String mTitle = "DefaultValue";

	public Essay essay;

	protected String data = "";

	DisplayImageOptions options;        // DisplayImageOptions是用于设置图片显示的类

	ListView listView;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list, container, false);

		listView = (ListView) rootView.findViewById(R.id.list);

		//创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(getActivity());

		//Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);

		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub)          // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_empty)  // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error)       // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
				.build();                                   // 创建配置过得DisplayImageOption对象

		listView.setAdapter(new ItemAdapter());

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 点击列表项转入ViewPager显示界面
				Intent intent = new Intent(getActivity(),ArticleActivity.class);
				Bundle b = new Bundle();
				b.putString("data", data);
				//此处使用putExtras，接受方就响应的使用getExtra
				intent.putExtras(b);

				GlobalVariable.getInstance().setArticle_id(Integer.parseInt(essay.ID[position]));

				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
				startActivity(intent);
			}
		});

		return rootView;
	}

	/**
	 *
	 * 自定义列表项适配器
	 *
	 */
	class ItemAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text;
			public ImageView image;
		}

		@Override
		public int getCount() {
			try {
				if (essay.Count > 0)
					return essay.PicUrl.length;
				else
					return 0;
			}catch (Exception e)
			{
				System.gc();
				System.exit(0);
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.text);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);        // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}

			holder.text.setText(essay.Title[position]);  // TextView设置文本

			/**
			 * 显示图片
			 * 参数1：图片url
			 * 参数2：显示图片的控件
			 * 参数3：显示图片的设置
			 * 参数4：监听器
			 */
			imageLoader.displayImage(essay.PicUrl[position], holder.image, options, animateFirstListener);

			return view;
		}
	}
	/**
	 * 图片加载第一次显示监听器
	 * @author Administrator
	 *
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}