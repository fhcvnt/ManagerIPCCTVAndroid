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
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;

import lacty.cctv.manageripcctv.databinding.ActivityGetIpBinding;

public class GetIP extends AppCompatActivity {
ActivityGetIpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ip);

        binding = ActivityGetIpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tao rotate xoay
        Animation animationrotate = AnimationUtils.loadAnimation(this, R.anim.rotate_view_360);
        Animation animationtranslate = AnimationUtils.loadAnimation(this, R.anim.translate_back);

        binding.imageViewGetIPBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationtranslate);
                finish();
            }
        });

        binding.buttonGetIPGetIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextGetIPClassIP.getText().toString().isEmpty()) {
                    Toast.makeText(GetIP.this, "Class IP is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        ConnectSQL connect = new ConnectSQL();
                        connect.setConnection();
                        connect.setStatement();

                        String select = "SELECT IP FROM DanhSachIP WHERE IP LIKE '%." + binding.editTextGetIPClassIP.getText().toString() + ".%' ORDER BY IP ASC";
                        ResultSet result = connect.getStatement().executeQuery(select);

                        if (Integer.parseInt(binding.editTextGetIPClassIP.getText().toString()) >= 0
                                && Integer.parseInt(binding.editTextGetIPClassIP.getText().toString()) <= 255) {
                            clearTable();
                            int stt = 0;
                            ArrayList<String> danhsachip = new ArrayList<String>();
                            String chuoiip = "192.168.";
                            String lopip = binding.editTextGetIPClassIP.getText().toString();
                            for (int i = 1; i <= 254; i++) {
                                danhsachip.add(chuoiip + lopip + "." + i);
                            }

                            ArrayList<String> danhsachipcsdl = new ArrayList<String>();

                            while (result.next()) {
                                danhsachipcsdl.add(result.getString(1));
                            }

                            danhsachip.removeAll(danhsachipcsdl);
                            int heightrow = 80;
                            stt=0;
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

                                binding.tableGetIP.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            }
                        }
                        result.close();
                        connect.closeStatement();
                        connect.closeConnection();
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
        int countrow = binding.tableGetIP.getChildCount();
        binding.tableGetIP.removeViews(1, countrow - 1);
    }
}