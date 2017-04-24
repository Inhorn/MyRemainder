package babiy.planner;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Task> {

    ArrayList<Task> taskList;
    Context context;
    private LayoutInflater myInflater;

    public MyAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
        this.context = context;
        this.myInflater =(LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        taskList = tasks;
    }

    @Override
    public Task getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = myInflater.inflate(R.layout.item, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Task item = getItem(position);

        vh.textViewTask.setText(item.getTask());
        vh.textViewDate.setText(item.getDate()+ "\n" + item.getTime());
        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView textViewDate;
        public final TextView textViewTask;

        private ViewHolder(LinearLayout rootView, TextView textViewDate, TextView textViewTask) {
            this.rootView = rootView;
            this.textViewDate = textViewDate;
            this.textViewTask = textViewTask;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView tvDate = (TextView) rootView.findViewById(R.id.tvDate);
            TextView tvTask = (TextView) rootView.findViewById(R.id.tvTask);

            return new ViewHolder(rootView, tvDate, tvTask);
        }


    }

}



