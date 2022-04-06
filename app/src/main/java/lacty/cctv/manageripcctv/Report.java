package lacty.cctv.manageripcctv;

import androidx.appcompat.app.AppCompatActivity;

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
import java.sql.SQLException;
import java.util.ArrayList;

import lacty.cctv.manageripcctv.databinding.ActivityReportBinding;

public class Report extends AppCompatActivity {
    ActivityReportBinding binding;
    private int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tao rotate xoay
        Animation animationrotate = AnimationUtils.loadAnimation(this, R.anim.rotate_view_360);

        binding.imageViewReportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationrotate);
                finish();
            }
        });

        // radio Location
        binding.radioButtonReportLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioButtonReportLocation.setChecked(true);
                binding.radioButtonReportIP.setChecked(false);
                binding.radioButtonReportMachinetype.setChecked(false);
                binding.textviewReportCountname.setText("Location");

                String select = "SELECT DISTINCT Location.Location,(SELECT COUNT(ds.MaLocation) FROM DanhSachIP AS ds WHERE ds.MaLocation=DanhSachIP.MaLocation) FROM DanhSachIP,Location WHERE DanhSachIP.MaLocation=Location.MaLocation ORDER BY Location.Location ASC";
                selectSQL(select, "");
            }
        });

        // radio IP
        binding.radioButtonReportIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioButtonReportLocation.setChecked(false);
                binding.radioButtonReportIP.setChecked(true);
                binding.radioButtonReportMachinetype.setChecked(false);
                binding.textviewReportCountname.setText("IP");

                String selectclassip = "SELECT CAST(SUBSTRING(DanhSachIP.IP,9,(SELECT CHARINDEX('.',DanhSachIP.IP,9)-9)) AS INT) AS LopIP INTO ##LopIP FROM DanhSachIP";
                String select = "SELECT DISTINCT ##LopIP.LopIP,(SELECT COUNT(*) FROM ##LopIP AS LopIP2 WHERE LopIP2.LopIP=##LopIP.LopIP) FROM ##LopIP ORDER BY ##LopIP.LopIP DESC";
                selectSQL(select, selectclassip);
            }
        });

        // radio Machine Type
        binding.radioButtonReportMachinetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioButtonReportLocation.setChecked(false);
                binding.radioButtonReportIP.setChecked(false);
                binding.radioButtonReportMachinetype.setChecked(true);
                binding.textviewReportCountname.setText("Machine Type");

                String select = "SELECT DISTINCT LoaiMay.LoaiMay,(SELECT COUNT(*) FROM DanhSachIP AS DS WHERE DS.MaLoaiMay=DanhSachIP.MaLoaiMay) FROM DanhSachIP,LoaiMay WHERE DanhSachIP.MaLoaiMay=LoaiMay.MaLoaiMay";
                selectSQL(select, "");
            }
        });
    }

    // ham xoa du lieu bang table - count -1 dong, khong xoa dong tieu de
    public void clearTable() {
        int countrow = binding.tableReport.getChildCount();
        binding.tableReport.removeViews(1, countrow - 1);
    }

    // seclect sql
    public void selectSQL(String select, String select2) {
        try {
            clearTable();
            total = 0;
            ConnectSQL connect = new ConnectSQL();
            connect.setConnection();
            connect.setStatement();
            if (!select2.isEmpty()) {
                connect.getStatement().executeUpdate(select2);
                connect.setStatement();
            }
            ResultSet result = connect.getStatement().executeQuery(select);

            while (result.next()) {
                TableRow tr = new TableRow(getApplicationContext());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                TextView textview = new TextView(getApplicationContext());
                textview.setText(result.getString(1));
                textview.setPadding(5, 0, 5, 0);
                textview.setHeight(80);
                textview.setBackground(getDrawable(R.drawable.cell_border));
                textview.setTextSize(20);
                textview.setGravity(Gravity.CENTER_VERTICAL);
                textview.setTypeface(null, Typeface.NORMAL);

                TextView textview2 = new TextView(getApplicationContext());
                textview2.setText(result.getString(2));
                textview2.setPadding(5, 0, 5, 0);
                textview2.setHeight(80);
                textview2.setBackground(getDrawable(R.drawable.cell_border));
                textview2.setTextSize(20);
                textview2.setGravity(Gravity.CENTER_VERTICAL);
                textview2.setTypeface(null, Typeface.NORMAL);

                // tinh tong = cong cac dong count lai
                try {
                    total = total + Integer.parseInt(result.getString(2));
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

                tr.addView(textview);
                tr.addView(textview2);

                binding.tableReport.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
            result.close();
            connect.closeStatement();
            connect.closeConnection();
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

        binding.tableReport.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
    }
}