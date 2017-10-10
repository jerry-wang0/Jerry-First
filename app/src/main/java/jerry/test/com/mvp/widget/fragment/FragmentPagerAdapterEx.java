package jerry.test.com.mvp.widget.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Override support-v4 package version 22.1.1
 * @see FragmentPagerAdapter
 *
 * Created by Liqun Wan on 2015/7/29.
 */
public class FragmentPagerAdapterEx extends FragmentPagerAdapter {
	//
	private Fragment current;
	private FragmentManager manager;
	private List<Fragment> pages = new ArrayList<>();

	/**
	 *
	 */
	public FragmentPagerAdapterEx(FragmentManager manager) {
		super(manager); this.manager = manager;
	}

	/**
	 *
	 */
	@Override
	public int getCount() {
		return pages.size();
	}

	@Override
	public Fragment getItem(int position) {
		return this.pages.get(position);
	}

	public void addItem(ViewGroup vg, Fragment page, boolean create) {
		final String tag = page.getClass().getName(); this.pages.add(page);
		if(create) manager.beginTransaction().add(vg.getId(), page, tag).commit();
	}

	/**
	 *
	 */
	@Override
	public Object instantiateItem(ViewGroup root, int position) {
		Fragment f = getItem(position);
		String name = f.getClass().getName();
		Fragment fragment = this.manager.findFragmentByTag(name);
		if (fragment != null) {
			this.manager.beginTransaction().attach(fragment);
		} else {
			fragment = f;
			this.manager.beginTransaction().add(root.getId(), fragment, name);
		}
		if (fragment != current) {
			fragment.setMenuVisibility(false); fragment.setUserVisibleHint(false);
		}
		return fragment;
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment)object;
		if (fragment != current) {
			if (current != null) {
				current.setMenuVisibility(false); current.setUserVisibleHint(false);
			}
			if (fragment != null) {
				fragment.setMenuVisibility(true); fragment.setUserVisibleHint(true);
			}
			current = fragment;
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		this.manager.beginTransaction().detach((Fragment)object);
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		this.manager.beginTransaction().commitAllowingStateLoss();
		this.manager.executePendingTransactions();
	}
}
