package com.example.sqlitedbapp_;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText etStdntID;
    private EditText etStdntName;
    private EditText etStdntProg;

    private Button btnAdd;
    private Button btnDelete;
    private Button btnSearch;
    private Button btnView;
    private SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("StudentDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(stdnt_id VARCHAR, stdnt_name VARCHAR, stdnt_prog VARCHAR);");

        etStdntID = findViewById(R.id.etStdntID);
        etStdntName = findViewById(R.id.etStdntName);
        etStdntProg = findViewById(R.id.etStdntProg);

        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnSearch = findViewById(R.id.btnSearch);
        btnView = findViewById(R.id.btnView);

        btnAdd.setOnClickListener(view -> clearText());
        btnSearch.setOnClickListener(view -> clearText());
        btnDelete.setOnClickListener(view -> clearText());
        btnView.setOnClickListener(view -> clearText());

        btnSearch.setOnClickListener(view -> showMessage("Title","Message"));

        btnAdd.setOnClickListener(this::onClick);
        btnSearch.setOnClickListener(this::onClick);
        btnDelete.setOnClickListener(this::onClick);
        btnView.setOnClickListener(this::onClick);

    }

    private void onClick(View view){
        if(view == btnAdd){
            db.execSQL("INSERT INTO student VALUES('" + etStdntID.getText() + "', '" + etStdntName.getText() + "', '"+ etStdntProg.getText() + "');");
            showMessage("Success", "Record Added!");
            clearText();
        }

        else if(view == btnDelete){
            Cursor c=db.rawQuery("SELECT * FROM student WHERE stdnt_id='" + etStdntID.getText() + "'", null);
            if(c.moveToFirst())
            {
                db.execSQL("DELETE FROM student WHERE stdnt_id'" + etStdntID.getText() + "'");
                showMessage("Success", "Record Deleted.");
            }
            clearText();
        }

        else if(view == btnSearch){
            Cursor c=db.rawQuery("SELECT * FROM student WHERE stdnt_id'" + etStdntID.getText() + "'", null);
            StringBuffer buffer = new StringBuffer();
            if(c.moveToFirst()){
                buffer.append("Name: " + c.getString(1) + "\n");
                buffer.append("Program: " + c.getString(2) + "\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }

        else if(view ==  btnView){
            Cursor c=db.rawQuery("SELECT * FROM student", null);
            if(c.getCount()== 0){
                showMessage("Error", "No records found.");
                return;
            }

            StringBuffer buffer = new StringBuffer();
            while(c.moveToNext()){
                buffer.append("ID:" + c.getString(0)+ "\n");
                buffer.append("Name: " + c.getString(1) + "\n");
                buffer.append("Program: " + c.getString(2) + "\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }
    }

    private void clearText() {
        btnAdd.setText("");
        btnView.setText("");
        btnSearch.setText("");
        btnDelete.setText("");
        btnAdd.requestFocus();
    }


    private void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}