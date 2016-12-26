package com.goodbaby.smartmanufacture.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by goodbaby on 16/12/6.
 */

public abstract class LazyFragment extends Fragment {

    protected boolean isVisible;//是否可见
    /**
     * 实现Fragment数据的懒加载(此方法在onCreateView()方法之前调用)
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {

            isVisible = true;

            onVisible();

        } else {

            isVisible = false;

            onInvisible();
        }
    }

    /**
     * 可见时调用lazyLoad()
     */
    protected void onVisible() {

        lazyLoad();

    }

    /**
     * lazyLoad具体实现,继承该类的子Fragment去实现
     */
    protected abstract void lazyLoad();

    /**
     * 不可见时,空实现
     */
    protected void onInvisible(){}
}
