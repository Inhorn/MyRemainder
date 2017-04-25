package babiy.planner.fragment;

import babiy.planner.model.MessageEvent;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.getbase.floatingactionbutton.FloatingActionButton;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;

import babiy.planner.MyAdapter;
import babiy.planner.R;
import babiy.planner.Task;
import babiy.planner.TaskDataBase;

public class Current extends Fragment implements AdapterView.OnItemClickListener {

    ListView lvList;
    ArrayList<Task> tasks;
    MyAdapter adapter;
    ArrayList<Task> taskChecked;
    AlertDialog alertDialog;

    Button btnOk;
    Button btnCancel;
    TaskDataBase database;
    private FloatingActionButton fabAdd;

    private DatePickerDialog mDateDialog;
    private TimePickerDialog mTimeDialog;

    Calendar calendar;
    int myYear;
    int myMonth;
    int myDay;

    int myHour;
    int myMinute;

    EditText etDate;
    EditText etTime;
    EditText etTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.current, container, false);

        calendar = Calendar.getInstance();
        myYear = calendar.get(Calendar.YEAR);
        myMonth = calendar.get(Calendar.MONTH);
        myDay = calendar.get(Calendar.DAY_OF_MONTH);

        myHour = calendar.get(Calendar.HOUR_OF_DAY);
        myMinute = calendar.get(Calendar.MINUTE);

        fabAdd = (FloatingActionButton) v.findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View viewDialog = getActivity().getLayoutInflater().inflate(R.layout.activity_add_task, null, false);
                builder.setTitle("Write task").setView(viewDialog);
                alertDialog = builder.create();
                alertDialog.show();

                etDate = (EditText) viewDialog.findViewById(R.id.etDate);
                etTime = (EditText) viewDialog.findViewById(R.id.etTime);
                etTask = (EditText) viewDialog.findViewById(R.id.etTask);

                etDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDateDialog.show();
                    }
                });

                etTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTimeDialog.show();
                    }
                });

                btnOk = (Button) viewDialog.findViewById(R.id.btnOk);
                btnCancel = (Button) viewDialog.findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(etTask.getText().toString())) {
                            Task temp = new Task(etTask.getText().toString(), etDate.getText().toString(),
                                    etTime.getText().toString(), getString(R.string.current));
                            database.addTask(temp);
                            tasks.add(temp);
                        }

                        alertDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
                mDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etDate.setText(String.valueOf(year + "/" +getDD(month + 1) + "/" + getDD(dayOfMonth)));
                    }
                }, myYear, myMonth, myDay);

                mTimeDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etTime.setText(getDD(hourOfDay) + ":" + getDD(minute));
                    }
                }, myHour, myMinute, true);
            }
        });
        database = new TaskDataBase(getActivity());
        tasks = database.getAllTasks(getString(R.string.current));
        adapter = new MyAdapter(getActivity(), tasks);

        lvList = (ListView) v.findViewById(R.id.lvWithOutDate);
        lvList.setOnItemClickListener(this);
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
                            lt.setStatus(getString(R.string.completed));
                            database.editTask(lt);
                            tasks.remove(lt);
                        }
                        adapter.notifyDataSetChanged();
                        //EventBus.getDefault().post(new MessageEvent(true));
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
        return v;
    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
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
    }*/

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, final long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewDialog = getActivity().getLayoutInflater().inflate(R.layout.activity_add_task, null, false);
        final EditText etTaskEdit = (EditText) viewDialog.findViewById(R.id.etTask);
        final EditText etTimeEdit = (EditText) viewDialog.findViewById(R.id.etTime);
        final EditText etDateEdit = (EditText) viewDialog.findViewById(R.id.etDate);
        Button btnOk = (Button) viewDialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) viewDialog.findViewById(R.id.btnCancel);

        final DatePickerDialog myEditDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etDateEdit.setText(String.valueOf(year + "/" + getDD(month + 1) + "/" + dayOfMonth));
            }
        }, myYear, myMonth, myDay);

        final TimePickerDialog myEditTimeDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                etTimeEdit.setText(hourOfDay + ":" + minute);
            }
        }, myHour, myMinute, true);

        etDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEditDateDialog.show();
            }
        });

        etTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               myEditTimeDialog.show();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Task editTask = new Task(etTaskEdit.getText().toString(), etDateEdit.getText().toString(),
                        etTimeEdit.getText().toString(), getString(R.string.current));
                editTask.setId(tasks.get((int) id));
                database.editTask(editTask);
                tasks.remove(tasks.get((int) id));
                tasks.add(editTask);
                adapter.notifyDataSetChanged();
                alertDialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        builder.setTitle("Edit task").setView(viewDialog).setCancelable(true);
        Task temp = tasks.get(position);
        etDateEdit.setText(temp.getDate());
        etTimeEdit.setText(temp.getTime());
        etTaskEdit.setText(temp.getTask());
        alertDialog = builder.create();
        alertDialog.show();
    }

    private String getDD(int num) {
        return num > 9 ? "" + num : "0" + num;
    }
}
