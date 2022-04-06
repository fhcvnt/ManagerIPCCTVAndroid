package sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.ResultSet;

import lacty.cctv.manageripcctv.ConnectSQL;
import lacty.cctv.manageripcctv.R;
import lacty.cctv.manageripcctv.databinding.ActivitySqliteReportBinding;

public class SQLiteReport extends AppCompatActivity {
    ActivitySqliteReportBinding binding;
    private int total = 0;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_report);

        binding = ActivitySqliteReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // tao rotate xoay
        Animation animationrotate = AnimationUtils.loadAnimation(this, R.anim.rotate_view_360);

        binding.imageViewSQLiteReportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationrotate);
                finish();
            }
        });

        // radio Location
        binding.radioButtonSQLiteReportLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioButtonSQLiteReportLocation.setChecked(true);
                binding.radioButtonSQLiteReportIP.setChecked(false);
                binding.radioButtonSQLiteReportMachinetype.setChecked(false);
                binding.textviewSQLiteReportCountname.setText("Location");

                String select = "SELECT Location,COUNT(*) AS Countnumber FROM DanhSachIP GROUP BY Location ORDER BY Countnumber";
                selectSQL(select);
            }
        });

        // radio IP
        binding.radioButtonSQLiteReportIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioButtonSQLiteReportLocation.setChecked(false);
                binding.radioButtonSQLiteReportIP.setChecked(true);
                binding.radioButtonSQLiteReportMachinetype.setChecked(false);
                binding.textviewSQLiteReportCountname.setText("IP");

                String select = "SELECT LopIP,COUNT(*) AS CountNumber FROM DanhSachIP GROUP BY LopIP ORDER BY CountNumber";
                selectSQL(select);
            }
        });

        // radio Machine Type
        binding.radioButtonSQLiteReportMachinetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioButtonSQLiteReportLocation.setChecked(false);
                binding.radioButtonSQLiteReportIP.setChecked(false);
                binding.radioButtonSQLiteReportMachinetype.setChecked(true);
                binding.textviewSQLiteReportCountname.setText("Machine Type");

                String select = "SELECT LoaiMay, COUNT(*) AS CountNumber FROM DanhSachIP GROUP BY LoaiMay ORDER BY CountNumber";
                selectSQL(select);
            }
        });
    }

    // ham xoa du lieu bang table - count -1 dong, khong xoa dong tieu de
    public void clearTable() {
        int countrow = binding.tableSQLiteReport.getChildCount();
        binding.tableSQLiteReport.removeViews(1, countrow - 1);
    }

    // seclect sql
    public void selectSQL(String select) {
        try {
            clearTable();
            total = 0;
            database = new Database(getApplicationContext(), "IPManagerCCTV", null, 3);
            Cursor result = database.GetData(select);

            while (result.moveToNext()) {
                TableRow tr = new TableRow(getApplicationContext());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                TextView textview = new TextView(getApplicationContext());
                textview.setText(result.getString(0));
                textview.setPadding(5, 0, 5, 0);
                textview.setHeight(80);
                textview.setBackground(getDrawable(R.drawable.cell_border));
                textview.setTextSize(20);
                textview.setGravity(Gravity.CENTER_VERTICAL);
                textview.setTypeface(null, Typeface.NORMAL);

                TextView textview2 = new TextView(getApplicationContext());
                textview2.setText(result.getString(1));
                textview2.setPadding(5, 0, 5, 0);
                textview2.setHeight(80);
                textview2.setBackground(getDrawable(R.drawable.cell_border));
                textview2.setTextSize(20);
                textview2.setGravity(Gravity.CENTER_VERTICAL);
                textview2.setTypeface(null, Typeface.NORMAL);

                // tinh tong = cong cac dong count lai
                try {
                    total = total + Integer.parseInt(result.getString(1));
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

                tr.addView(textview);
                tr.addView(textview2);

                binding.tableSQLiteReport.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
            result.close();
            database.close();
        } catch (Exception se) {
            se.printStackTrace();
        }

        // in dong TOTAL
        TableRow tr = new TableRow(getApplicationContext());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView textview = new TextView(getApplicationContext());
        textview.setText("TOTAL");
        textview.setPadding(5, 0, 5, 0);
        textview.setHeight(80);
        textview.setBackground(getDrawable(R.drawable.cell_border_hong));
        textview.setTextSize(20);
        textview.setGravity(Gravity.CENTER_VERTICAL);
        textview.setTypeface(null, Typeface.NORMAL);

        TextView textview2 = new TextView(getApplicationContext());
        textview2.setText(total + "");
        textview2.setPadding(5, 0, 5, 0);
        textview2.setHeight(80);
        textview2.setBackground(getDrawable(R.drawable.cell_border_hong));
        textview2.setTextSize(20);
        textview2.setGravity(Gravity.CENTER_VERTICAL);
        textview2.setTypeface(null, Typeface.NORMAL);

        tr.addView(textview);
        tr.addView(textview2);

        binding.tableSQLiteReport.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
    }
}