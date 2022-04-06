package sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import lacty.cctv.manageripcctv.R;
import lacty.cctv.manageripcctv.databinding.ActivitySqliteMainBinding;

public class SQLiteMain extends AppCompatActivity {
    ActivitySqliteMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_main);

        binding = ActivitySqliteMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSQLiteManagerIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SQLiteIP.class);
                startActivity(intent);
            }
        });

        binding.buttonSQLiteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SQLiteLocation.class);
                startActivity(intent);
            }
        });

        binding.buttonSQLiteMachineType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SQLiteMachineType.class);
                startActivity(intent);
            }
        });

        binding.buttonSQLiteReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SQLiteReport.class);
                startActivity(intent);
            }
        });

        binding.buttonSQLiteGetIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SQLiteGetIP.class);
                startActivity(intent);
            }
        });

        binding.buttonSQLiteMainExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}