package com.oklab.githubjourney.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.oklab.githubjourney.githubjourney.R;

import java.util.Calendar;

import static com.oklab.githubjourney.githubjourney.R.id.container;
import static com.oklab.githubjourney.githubjourney.R.id.swipe_refresh_layout;

/**
 * Created by olgakuklina on 2017-01-24.
 */

public class ContributionsListAdapter extends BaseAdapter {
    private static final String TAG = ContributionsListAdapter.class.getSimpleName();
    private Calendar calendar = (Calendar) Calendar.getInstance().clone();
    private final Context context;
    private final int numberOfDays;
    private final int numberOfEmptyDaysInMonth;

    public ContributionsListAdapter(Context context, int offset) {
        this.context = context;
        calendar.add(Calendar.MONTH, -offset);
        numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        numberOfEmptyDaysInMonth = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
    }

    @Override
    public int getCount() {
        return numberOfDays + numberOfEmptyDaysInMonth;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;
        if(i >= numberOfEmptyDaysInMonth ) {
            v = inflater.inflate(R.layout.grid_list_item, viewGroup, false);
            ImageButton button = (ImageButton) v.findViewById(R.id.contribution_button);
            switch(i) {
                case 4:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_2));
                    break;
                case 10:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_3));
                    break;
                case 7:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_4));
                    break;
                case 15:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_5));
                    break;
                case 12:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_6));
                    break;
                case 20:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_7));
                    break;
                case 25:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_8));
                    break;
                case 30:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_9));
                    break;
                case 18:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_10));
                    break;
                case 13:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_1));
                    break;
                case 1:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_1));
                    break;
                case 22:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_1));
                    break;
                case 19:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_1));
                    break;
                case 29:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_1));
                    break;
                case 11:
                    button.setBackground(context.getResources().getDrawable(R.drawable.contributions_grid_color_1));
                    break;
                default:
                    button.setBackgroundColor(context.getResources().getColor(R.color.empty));
                    break;
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                }
            });
            Log.v(TAG, "i " + i);

        }
        else {
            v = inflater.inflate(R.layout.empty_grid_list_item, viewGroup, false);
        }

        return v;
    }
}
