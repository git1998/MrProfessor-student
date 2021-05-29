package com.akshay.minglishmantra_beta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.akshay.minglishmantra_beta.Modal.StudyIntroScreenItem;

import com.akshay.minglishmantra_beta.R;

import java.util.List;


public class StudyIntroViewPagerAdapter extends PagerAdapter {

   Context mContext ;
   List<StudyIntroScreenItem> mListScreen;

    public StudyIntroViewPagerAdapter(Context mContext, List<StudyIntroScreenItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.single_study_viewpager_card,null);

        ImageView imgSlide = layoutScreen.findViewById(R.id.single_study_viewpager_card_image);
        imgSlide.setImageResource(mListScreen.get(position).getScreenImg());

        container.addView(layoutScreen);

        return layoutScreen;





    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);

    }
}
