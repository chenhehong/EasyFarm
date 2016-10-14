package com.scau.easyfarm.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.R;
import com.scau.easyfarm.base.ListBaseAdapter;
import com.scau.easyfarm.bean.News;
import com.scau.easyfarm.util.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsAdapter extends ListBaseAdapter<News> {

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_news, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        News news = mDatas.get(position);
        vh.title.setText(news.getTitle());

//        String description = news.getContent();
//        vh.description.setVisibility(View.GONE);
//        if (description != null && !StringUtils.isEmpty(description)) {
//            vh.description.setVisibility(View.VISIBLE);
//            vh.description.setText(description.trim());
//        }

        vh.source.setText(news.getAuthor());
        vh.time.setText(StringUtils.friendly_time(news.getPubDate()));
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_title)
        TextView title;
        @InjectView(R.id.tv_description)
        TextView description;
        @InjectView(R.id.tv_source)
        TextView source;
        @InjectView(R.id.tv_time)
        TextView time;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
