package com.goodbaby.smartmanufacture.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goodbaby.smartmanufacture.R;;

public class MainFragment extends LazyFragment {

    private boolean isPrepared;//是否初始化完成
    NewsFragment newsFragment;
    BarChartFragment chartFragment;
    WarningCountFragment alarmTimesFragment;
    SiteLayoutFragment siteLayoutFragment;
    ModelFragment modelFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_main, null, false);

        newsFragment = new NewsFragment();//最新资讯
        chartFragment = new BarChartFragment();//产量统计
        alarmTimesFragment = new WarningCountFragment();//警报次数
        siteLayoutFragment = new SiteLayoutFragment();//产线站点
        modelFragment = new ModelFragment();//注释模块

        isPrepared = true;
        lazyLoad();

        return v;
    }

    @Override
    protected void lazyLoad() {

        if (!isPrepared || !isVisible)
            return;

        getChildFragmentManager().beginTransaction().replace(R.id.frame_news,newsFragment).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.frame_chart, chartFragment).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.frame_alarm_times, alarmTimesFragment).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.frame_site_layout, siteLayoutFragment).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.frame_notes, modelFragment).commit();

    }


}