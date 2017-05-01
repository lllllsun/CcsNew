package com.ccs.adapters;

import android.content.ContentProvider;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccs.model.ScuNoticesBean;
import com.ccs.news.R;

import java.util.List;

/**
 * Created by DangH on 2017/5/1.
 */

public class AdapterNotices extends BaseAdapter {
    List<ScuNoticesBean> listNotices;
    LayoutInflater layoutInflater;

    private DataWrapper dataWrapper;

    public AdapterNotices(Context context, List<ScuNoticesBean> listNews) {
        this.listNotices = listNews;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return listNotices.size();
    }

    public Object getItem(int positon) {
        return listNotices.get(positon);
    }

    public long getItemId(int position) {
        return position;
    }

    private final class DataWrapper {
        TextView noticeTime;
        TextView noticeTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ScuNoticesBean snb = listNotices.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_listview_notice, null);

            dataWrapper = new DataWrapper();

            dataWrapper.noticeTime = (TextView) convertView.findViewById(R.id.notice_time);
            dataWrapper.noticeTitle = (TextView) convertView.findViewById(R.id.notice_title);

            convertView.setTag(dataWrapper);
        } else {
            dataWrapper = (DataWrapper) convertView.getTag();
        }

        dataWrapper.noticeTime.setText("发布日期：" + snb.noticeTime);
        dataWrapper.noticeTitle.setText(snb.noticeTitle);

        return convertView;
    }
}
