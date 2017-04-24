package babiy.planner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskDataBase extends SQLiteOpenHelper implements MyDataBase {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myTasks";
    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_ID = "id";
    private static final String KEY_TASK = "task";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_STATUS = "status";

    public TaskDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TASK + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_TIME + " TEXT," + KEY_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_TASKS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        onCreate(db);
    }

    @Override
    public void addTask(Task task) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK, task.getTask());
        values.put(KEY_DATE, task.getDate());
        values.put(KEY_TIME, task.getTime());
        values.put(KEY_STATUS, task.getStatus());

        database.insert(TABLE_TASKS, null, values);
        database.close();
    }

    @Override
    public ArrayList<Task> getAllTasks(String status) {
        ArrayList<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + KEY_STATUS
                + " = " + " '" + status + "'";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setId(Integer.parseInt(cursor.getString(0)));
                    task.setTask(cursor.getString(1));
                    task.setDate(cursor.getString(2));
                    task.setTime(cursor.getString(3));
                    task.setStatus(cursor.getString(4));
                    taskList.add(task);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return taskList;
    }

    @Override
    public int editTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TASK, task.getTask());
        values.put(KEY_DATE, task.getDate());
        values.put(KEY_TIME, task.getTime());
        values.put(KEY_STATUS, task.getStatus());

        return database.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    @Override
    public void deleteTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[]{String.valueOf(task.getId())});

        database.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_TASKS, null, null);
        database.close();

    }

    @Override
    public void delAllInStatus(String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_TASKS, KEY_STATUS + " = ?", new String[]{status});
        database.close();
    }

}

