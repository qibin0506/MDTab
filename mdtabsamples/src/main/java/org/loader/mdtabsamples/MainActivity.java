package org.loader.mdtabsamples;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.loader.mdtab.MDTab;

public class MainActivity extends AppCompatActivity {
	
	private String[] mMenus = new String[]{"智能助理", "照片", "相册"};

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
