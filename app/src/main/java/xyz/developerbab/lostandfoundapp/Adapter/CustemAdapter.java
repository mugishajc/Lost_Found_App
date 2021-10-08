package xyz.developerbab.lostandfoundapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.developerbab.lostandfoundapp.R;
import xyz.developerbab.lostandfoundapp.model.Category;

public class CustemAdapter extends BaseAdapter {
    Context c;
    ArrayList<Category> listdata;
    LayoutInflater inflater;

    public CustemAdapter(Context c, ArrayList<Category> listdata) {
        this.c = c;
        this.listdata = listdata;
        inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int i) {
        return listdata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.layout_categories,viewGroup,false);
        TextView tvcategory=(TextView)view.findViewById(R.id.tvcategory);

        tvcategory.setText(listdata.get(i).getCategoryname()+" - "+listdata.get(i).getXyz());

        return view;
    }
}