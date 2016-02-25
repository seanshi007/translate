package com.youdao.sean.translate;

import android.app.ActivityGroup;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActivityGroup implements android.view.View.OnClickListener {

    private ViewPager mViewPager;// 用来放置界面切换
    private PagerAdapter mPagerAdapter;// 初始化View适配器
    private List<View> mViews = new ArrayList<View>();// 用来存放Tab

    private Button mTabText;
    private Button mTabVoice;

    private boolean change = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initViewPage();
        initEvent();

    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpage);
        // 初始化四个LinearLayout
        mTabText = (Button) findViewById(R.id.id_tab_text);
        mTabVoice = (Button) findViewById(R.id.id_tab_voice);
    }

    private void initViewPage() {

        // 初妈化四个布局
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        //View tab01 = mLayoutInflater.inflate(R.layout.activity_text, null);
        //View tab02 = mLayoutInflater.inflate(R.layout.activity_voice, null);

        View tab01 = getLocalActivityManager().startActivity("First1", new Intent(this, TextActivity.class)).getDecorView();
        View tab02 = getLocalActivityManager().startActivity("Second2", new Intent(this, VoiceActivity.class)).getDecorView();

        mViews.add(tab01);
        mViews.add(tab02);

        // 适配器初始化并设置
        mPagerAdapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViews.get(position));

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);

                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return mViews.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initEvent() {

        mTabText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("1");
                //getLocalActivityManager().startActivity("First1", new Intent(getCurrentActivity(), TextActivity.class));
                //startActivity(new Intent(getCurrentActivity(), TextActivity.class));
                mViewPager.setCurrentItem(0);
            }
        });
        mTabVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("2");
                //getLocalActivityManager().startActivity("Second2", new Intent(getCurrentActivity(), VoiceActivity.class));
                //startActivity(new Intent(getCurrentActivity(), VoiceActivity.class));
                mViewPager.setCurrentItem(1);
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             *ViewPage左右滑动时
             */
            @Override
            public void onPageSelected(int arg0) {
                int currentItem = mViewPager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        if (change) {

                        }
                        break;
                    case 1:

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                change = true;
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

}
