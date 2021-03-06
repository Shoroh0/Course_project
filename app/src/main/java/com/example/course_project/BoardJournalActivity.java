package com.example.course_project;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

public class BoardJournalActivity extends Activity implements OnClickListener {

    private ProgressBar progressBar1, progressBar2, progressBar3;
    private TextView brakePadChange, motorOilChange, brakeDiscChange;
    private Button btnBack;
    private WorkWithDB workWithDBBoardJur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_journal_activity);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);

        brakeDiscChange = (TextView) findViewById(R.id.textView3_2);
        motorOilChange = (TextView) findViewById(R.id.textView2_2);
        brakePadChange = (TextView) findViewById(R.id.textView1_2);

        btnBack = (Button) findViewById(R.id.button);
        btnBack.setOnClickListener(this);

        workWithDBBoardJur = new WorkWithDB(this);
        
        ViewMaintenance();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){//кнопка назад в профиль
            case R.id.button:
                Intent backIntent = new Intent(BoardJournalActivity.this, ProfileActivity.class);
                startActivity(backIntent);
        }
    }

    void ViewMaintenance() {     //отображжение активити(прогрессбар и значения)

        Cursor cur = workWithDBBoardJur.getAccessToDB().query("maintenance", null, null, null, null, null, null);//создаем таблицу в БД

        cur.moveToPosition(workWithDBBoardJur.idValueMaintenace);
        int valbrakePadChange, valbrakeDiscChange, valmotorOilChange;

        if (cur.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int autoColIndex = cur.getColumnIndex("auto");
            int modelColIndex = cur.getColumnIndex("model");
            int yearOfIssueColIndex = cur.getColumnIndex("yearOfIssue");
            int brakePadChangeColIndex = cur.getColumnIndex("brakePadChange");
            int brakeDiscChangeColIndex = cur.getColumnIndex("brakeDiscChange");
            int motorOilChangeColIndex = cur.getColumnIndex("motorOilChange");
            do {
                // получаем значения по номерам
                valbrakePadChange = cur.getInt(brakePadChangeColIndex);
                valbrakeDiscChange = cur.getInt(brakeDiscChangeColIndex);
                valmotorOilChange = cur.getInt(motorOilChangeColIndex);
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (cur.moveToNext());
            cur.close();

            /*Отображаем сколько осталось до работ с помощью прогрессбара*/
            progressBar1.setMax(valbrakePadChange);//макс значение прогрессбар
            progressBar1.setProgress(workWithDBBoardJur.odoValue % valbrakePadChange);//значение для отображения прогресс бар.
            brakePadChange.setText(String.valueOf(valbrakePadChange - progressBar1.getProgress()));
            
            progressBar2.setMax(valmotorOilChange);
            progressBar2.setProgress(workWithDBBoardJur.odoValue % valbrakeDiscChange);
            motorOilChange.setText(String.valueOf(valmotorOilChange - progressBar2.getProgress()));
            
            progressBar3.setMax(valbrakeDiscChange);
            progressBar3.setProgress(workWithDBBoardJur.odoValue % valbrakeDiscChange);
            brakeDiscChange.setText(String.valueOf(valbrakeDiscChange - progressBar3.getProgress()));
        }

    }
}
