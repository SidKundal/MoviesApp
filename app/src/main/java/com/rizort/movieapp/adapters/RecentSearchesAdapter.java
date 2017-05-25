package com.rizort.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rizort.movieapp.R;
import com.rizort.movieapp.dtos.RecentSearchDTO;

import java.util.ArrayList;

public class RecentSearchesAdapter extends BaseAdapter{

    private ArrayList<RecentSearchDTO> mRecentSearches;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public RecentSearchesAdapter(Context context, ArrayList<RecentSearchDTO> recentSearches) {
        mContext = context;
        mRecentSearches = recentSearches;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewToReturn = mLayoutInflater.inflate(R.layout.item_search_recent, null);
        TextView searchItemTextView = (TextView) viewToReturn.findViewById(R.id.recentSearchTerm);
        searchItemTextView.setText(mRecentSearches.get(i).getSearchTerm());
        return viewToReturn;
    }

    @Override
    public int getCount() {
        return mRecentSearches.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }
}
