package per.yrj.movietime.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import per.yrj.movietime.R;


public abstract class BaseFragment extends Fragment implements IBaseFragment{
//	static final String HEAD_URL = "http://api.douban.com/v2/movie/";
//	RequestQueue mRequestQueue;

	//请求数据的几种状态
	public static final int STATE_UNKNOWN = 0;
	public static final int STATE_ERR = 1;
	public static final int STATE_LOADING = 2;
	public static final int STATE_EMPTY = 3;
	public static final int STATE_SUCCESS = 4;
	protected static FragmentActivity mContext;

	private FrameLayout frameLayout;
	private View errPageView;
	private View loadingPageView;
	private View emptyPageView;

	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
        //初始化为未知状态
		showLoadingState(STATE_LOADING);
		return frameLayout;
	}

	private void init() {
		mContext = getActivity();
		frameLayout = new FrameLayout(getActivity());
		errPageView = View.inflate(getActivity(), R.layout.page_err_state, null);
		loadingPageView = View.inflate(getActivity(),
				R.layout.page_loading_state, null);
		emptyPageView = View.inflate(getActivity(), R.layout.page_empty_state,
				null);
		frameLayout.addView(errPageView);
		frameLayout.addView(loadingPageView);
		frameLayout.addView(emptyPageView);
		//初始化RequestQueue
//		mRequestQueue = Volley.newRequestQueue(mContext);
		requestData();

		Button bt = ((Button) errPageView.findViewById(R.id.bt_reload));
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// 重新加载
				showLoadingState(STATE_LOADING);
				requestData();
			}
		});
	}


	/**
	 * 显示请求数据的状态
	 */
	public void showLoadingState(int curState) {
		emptyPageView.setVisibility(curState == STATE_EMPTY ? View.VISIBLE
				: View.INVISIBLE);
		errPageView.setVisibility(curState == STATE_ERR ? View.VISIBLE
				: View.INVISIBLE);
		loadingPageView.setVisibility(curState == STATE_UNKNOWN
				|| curState == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
		if (curState == STATE_SUCCESS) {
			frameLayout.addView(getSucceedPageView());
		}
	}

	/**
	 * 如果要显示加载完成页面必须将mCurState设置为STATE_SUCCESS
	 */
	public abstract void requestData();

	/**
	 * 如果STATE_SUCCESS加载成功将会调用此方法用于显示加载成功后的页面
	 */
	public abstract View getSucceedPageView();
}
