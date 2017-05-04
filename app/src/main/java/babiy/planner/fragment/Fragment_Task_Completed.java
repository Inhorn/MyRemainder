package babiy.planner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import babiy.planner.MyAdapter;
import babiy.planner.R;
import babiy.planner.Task;
import babiy.planner.TaskDataBase;
import babiy.planner.model.MessageEvent;


public class Fragment_Task_Completed extends Fragment {

    ListView lvList;
    ArrayList<Task> tasks;
    MyAdapter adapter;
    ArrayList<Task> taskChecked;
    TaskDataBase database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_completed, container, false);

        database = new TaskDataBase(getActivity());

        tasks = database.getAllTasks(getString(R.string.completed));
        adapter = new MyAdapter(getActivity(), tasks);
        lvList = (ListView) v.findViewById(R.id.lvCompleted);

        lvList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    taskChecked.add(tasks.get(position));
                } else {
                    taskChecked.remove(tasks.get(position));
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
                taskChecked = new ArrayList<>();

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuDelete:
                        for (Task lt : taskChecked) {
                            database.deleteTask(lt);
                            tasks.remove(lt);
                        }
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.menuMove:
                        for (Task lt : taskChecked) {
                            lt.setStatus(getString(R.string.current));
                            database.editTask(lt);
                        }
                        adapter.notifyDataSetChanged();
                        EventBus.getDefault().post(new MessageEvent(true));
                }

                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                taskChecked.clear();
            }
        });
        lvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return v;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(event.isUpdate()) {
            tasks = database.getAllTasks(getString(R.string.completed));
            adapter = new MyAdapter(getActivity(), tasks);
            lvList.setAdapter(adapter);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}