package sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import lacty.cctv.manageripcctv.R;
import lacty.cctv.manageripcctv.databinding.ActivitySqliteIpSearchBinding;

public class SQLiteIPSearch extends AppCompatActivity {
    ActivitySqliteIpSearchBinding binding;
    private String textsearch_sql = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_ip_search);

        binding = ActivitySqliteIpSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

// tao rotate xoay
        Animation animationrotate = AnimationUtils.loadAnimation(this, R.anim.rotate_view_360);
        Animation animationtranslate = AnimationUtils.loadAnimation(this, R.anim.translate_back);

        binding.imageViewSQLiteIPSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationtranslate);
                finish();
            }
        });

        binding.buttonSQLiteIPSearchSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // text find
                String ten = binding.editTextSQLiteIPSearchName.getText().toString().isEmpty() ? ""
                        : " AND Ten LIKE '%" + binding.editTextSQLiteIPSearchName.getText().toString() + "%'";
                String khuvuc = binding.editTextSQLiteIPSearchLocation.getText().toString().isEmpty() ? ""
                        : " AND Location LIKE '%" + binding.editTextSQLiteIPSearchLocation.getText().toString() + "%'";
                String ip = binding.editTextSQLiteIPSearchIP.getText().toString().isEmpty() ? "" : " AND IP LIKE '%" + binding.editTextSQLiteIPSearchIP.getText().toString() + "'";
                String loaimay = binding.editTextSQLiteIPSearchMachinetype.getText().toString().isEmpty() ? ""
                        : " AND LoaiMay LIKE '%" + binding.editTextSQLiteIPSearchMachinetype.getText().toString() + "%'";

                // xử lý tìm theo lớp IP, nếu textIP nhận vào là lớp IP ví dụ: 30 thay vì nhận
                // được là 30.1 thì ta tìm theo lớp IP chứ không tìm theo IP
                int lopip = -1;
                try {
                    lopip = Integer.parseInt(binding.editTextSQLiteIPSearchIP.getText().toString());
                } catch (NumberFormatException ne) {

                }
                if (lopip >= 0) {
                    ip = binding.editTextSQLiteIPSearchIP.getText().toString().isEmpty() ? "" : " AND IP LIKE '%" + "%.%." + lopip + "." + "%'";
                }

                textsearch_sql = "SELECT Ten,Location,IP,LoaiMay,GhiChu,NgayCapNhat FROM DanhSachIP WHERE 1=1"
                        + ten + khuvuc + ip + loaimay + " ORDER BY Location ASC";

                v.startAnimation(animationrotate);

                Intent intentresult = new Intent();
                intentresult.putExtra("select", textsearch_sql);
                setResult(10, intentresult);
                finish();
            }
        });

        binding.buttonSQLiteIPSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationrotate);
                binding.editTextSQLiteIPSearchName.setText("");
                binding.editTextSQLiteIPSearchLocation.setText("");
                binding.editTextSQLiteIPSearchIP.setText("");
                binding.editTextSQLiteIPSearchMachinetype.setText("");
            }
        });
    }
}