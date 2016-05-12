package org.loader.mdtabsamples;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.loader.mdtab.MDTab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private ViewPager mViewPager;
	
	private String[] mMenus = new String[]{"智能助理", "照片", "相册"};
	private List<ContentFragment> mContentFragment = new ArrayList<ContentFragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MDTab tab = (MDTab) findViewById(R.id.tab);
		tab.setAdapter(new Adapter());
		tab.itemChecked(0);
		tab.setOnItemCheckedListener(new MDTab.OnItemCheckedListener() {
			@Override
			public void onItemChecked(int position, View view) {
				Toast.makeText(MainActivity.this, mMenus[position], Toast.LENGTH_SHORT).show();
			}
		});

		initFragments();
		initViewPager();

		tab.setupWithViewPager(mViewPager);
	}

	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return mContentFragment.get(position);
			}

			@Override
			public int getCount() {
				return mContentFragment.size();
			}
		});


	}

	private void initFragments() {
		ContentFragment fragment;
		Bundle bundle;
		for(int i=0;i<mMenus.length;i++) {
			fragment = new ContentFragment();
			bundle = new Bundle();
			bundle.putString("content", mMenus[i]);
			fragment.setArguments(bundle);
			mContentFragment.add(fragment);
		}
	}
	
	class Adapter extends MDTab.TabAdapter {
		
		@Override
		public int getItemCount() {
			return mMenus.length;
		}

		@Override
		public Drawable getDrawable(int position) {
			int res = getResources().getIdentifier("icon_" + position, "drawable", getPackageName());
			return getResources().getDrawable(res);
		}

		@Override
		public CharSequence getText(int position) {
			return mMenus[position];
		}
	}
}
