package com.example.TrafficJam;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 3.11.2013
 * Time: 20:39
 * To change this template use File | Settings | File Templates.
 */
public class PuzzleListAdapter extends ArrayAdapter<Puzzle> {

    private Context context;
    private Filter billsFilter;
    private List<Puzzle> puzzles;
    private boolean isFinished;
    private PuzzleAdapter mStudentsAdapter;


    public PuzzleListAdapter(List<Puzzle> puzzles, Context ctx){
        super(ctx,R.layout.custom_list_item);
        this.puzzles  = puzzles;
        this.context = ctx;
        isFinished = false;
        mStudentsAdapter =  new PuzzleAdapter( ctx );
    }
    public int getCount() {
        return puzzles.size();
    }

    public Puzzle getItem(int position) {
        return puzzles.get(position);
    }

    public long getItemId(int position) {
        return puzzles.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        BillsHolder holder = new BillsHolder();

        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_list_item, null);
            TextView tv = (TextView) v.findViewById(R.id.name);
            TextView infoView = (TextView) v.findViewById(R.id.date);
            TextView colorView = (TextView) v.findViewById(R.id.color);

            holder.billsNameView = tv;
            holder.infoView = infoView;
            holder.colorView = colorView;

            v.setTag(holder);
        }
        else{
            holder = (BillsHolder) v.getTag();
        }

        isFinished = checkIsFinished(getItem(position).number);

        holder.billsNameView.setText(getItem(position).description);

        if(isFinished){

            holder.colorView.setBackgroundColor(Color.GREEN);
            holder.infoView.setText("Completed");
        }
        else {
            if(getLatestPuzzle() == getItem(position).number){
                holder.colorView.setBackgroundColor(Color.YELLOW);
                holder.infoView.setText("Unresolved");
            }  else {
                holder.colorView.setBackgroundColor(Color.RED);
                holder.infoView.setText("Unresolved");
            }
        }

        return v;

    }

    public int getLatestPuzzle(){
        Cursor cursor = mStudentsAdapter.queryPuzzle();

        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                if(!(cursor.getInt(cursor.getColumnIndex("isFinished")) > 0)){
                    return cursor.getInt(cursor.getColumnIndex("puzzleNumber"));
                }
                cursor.moveToNext();
            }
        }
        return 39;
    }


    @Override
    public boolean isEnabled(int position) {
        if(getItem(position).number == getLatestPuzzle()){
            return true;
        }
        return checkIsFinished(getItem(position).number);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean checkIsFinished(int number){
        Cursor cursor = mStudentsAdapter.getPuzzleByPuzzleNumber(number);
        if(cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex("isFinished"))>0;
        }
        return true;
    }

    private static class BillsHolder{
        public TextView billsNameView;
        public TextView infoView;
        public TextView colorView;
    }


}
