package com.example.Daftar_Buku_Pintar;
import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    Adapter adapter;
    ArrayList<Buku> filterList;

    public CustomFilter(ArrayList<Buku> filterList,Adapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        if(constraint != null && constraint.length() > 0)
        {

            constraint=constraint.toString().toUpperCase();

            ArrayList<Buku> filteredbukuu=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {

                if(filterList.get(i).getJudulbuku().toUpperCase().contains(constraint))
                {

                    filteredbukuu.add(filterList.get(i));
                }
            }

            results.count=filteredbukuu.size();
            results.values=filteredbukuu;

        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.buku= (ArrayList<Buku>) results.values;

        adapter.notifyDataSetChanged();

    }
}
