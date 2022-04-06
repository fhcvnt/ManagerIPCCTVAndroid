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
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;

import lacty.cctv.manageripcctv.ConnectSQL;
import lacty.cctv.manageripcctv.GetIP;
import lacty.cctv.manageripcctv.R;
import lacty.cctv.manageripcctv.databinding.ActivitySqliteGetipBinding;

public class SQLiteGetIP extends AppCompatActivity {
    ActivitySqliteGetipBinding binding;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_getip);

        binding = ActivitySqliteGetipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tao rotate xoay
        Animation animationrotate = AnimationUtils.loadAnimation(this, R.anim.rotate_view_360);
        Animation animationtranslate = AnimationUtils.loadAnimation(this, R.anim.translate_back);

        binding.imageViewSQLiteGetIPBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationtranslate);
                finish();
            }
        });

        binding.buttonSQLiteGetIPGetIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextSQLiteGetIPClassIP.getText().toString().isEmpty()) {
                    Toast.makeText(SQLiteGetIP.this, "Class IP is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // tao database IPManagerCCTV
                        database = new Database(getApplicationContext(), "IPManagerCCTV", null, 3);

                        String select = "SELECT IP FROM DanhSachIP WHERE IP LIKE '%." + binding.editTextSQLiteGetIPClassIP.getText().toString() + ".%' ORDER BY IP ASC";
                        Cursor result = database.GetData(select);

                        if (Integer.parseInt(binding.editTextSQLiteGetIPClassIP.getText().toString()) >= 0
                                && Integer.parseInt(binding.editTextSQLiteGetIPClassIP.getText().toString()) <= 255) {
                            clearTable();
                            int stt = 0;
                            ArrayList<String> danhsachip = new ArrayList<String>();
                            String chuoiip = "192.168.";
                            String lopip = binding.editTextSQLiteGetIPClassIP.getText().toString();
                            for (int i = 1; i <= 254; i++) {
                                danhsachip.add(chuoiip + lopip + "." + i);
                            }

                            ArrayList<String> danhsachipcsdl = new ArrayList<String>();

                            while (result.moveToNext()) {
                                danhsachipcsdl.add(result.getString(0));
                            }

                            danhsachip.removeAll(danhsachipcsdl);
                            int heightrow = 80;
                            stt = 0;
                            for (String list : danhsachip) {
                                stt++;
                                TableRow tr = new TableRow(getApplicationContext());
                                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                TextView textview = new TextView(getApplicationContext());
                                textview.setText("" + stt);
                                textview.setPadding(10, 0, 10, 0);
                                textview.setHeight(heightrow);
                                textview.setBackground(getDrawable(R.drawable.cell_border));
                                textview.setTextSize(22);
                                textview.setGravity(Gravity.CENTER_VERTICAL);
                                textview.setTypeface(null, Typeface.NORMAL);

                                TextView textview2 = new TextView(getApplicationContext());
                                textview2.setText(list);
                                textview2.setPadding(10, 0, 10, 0);
                                textview2.setHeight(heightrow);
                                textview2.setBackground(getDrawable(R.drawable.cell_border));
                                textview2.setTextSize(22);
                                textview2.setGravity(Gravity.CENTER_VERTICAL);
                                textview2.setTypeface(null, Typeface.NORMAL);

                                tr.addView(textview);
                                tr.addView(textview2);

                                binding.tableSQLiteGetIP.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            }
                        }
                        result.close();
                        database.close();
                        v.startAnimation(animationrotate);
                    } catch (Exception se) {
                        se.printStackTrace();
                    }
                }
            }
        });

    }

    // ham xoa du lieu bang table - count -1 dong, khong xoa dong tieu de
    public void clearTable() {
        int countrow = binding.tableSQLiteGetIP.getChildCount();
        binding.tableSQLiteGetIP.removeViews(1, countrow - 1);
    }
}