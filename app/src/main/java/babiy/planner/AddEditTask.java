package babiy.planner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEditTask extends AppCompatActivity implements View.OnClickListener{

    EditText etDate;
    EditText etTime;
    EditText etTask;

    Button btnOk;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etDate = (EditText) findViewById(R.id.etDateEdit);
        etTime = (EditText) findViewById(R.id.etTimeEdit);
        etTask = (EditText) findViewById(R.id.etTaskEdit);

        btnOk = (Button) findViewById(R.id.btnOk);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                Intent intent = new Intent();
                intent.putExtra("task", etTask.getText().toString());
                intent.putExtra("time", etTime.getText().toString());
                intent.putExtra("date", etDate.getText().toString());
                setResult(RESULT_OK, intent);
                AddEditTask.this.finish();
                break;

            case R.id.btnCancel:
                AddEditTask.this.finish();
                break;
        }

    }
}
