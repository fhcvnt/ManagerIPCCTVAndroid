package lacty.cctv.manageripcctv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import lacty.cctv.manageripcctv.databinding.ActivityIpSearchBinding;

public class IPSearch extends AppCompatActivity {
    ActivityIpSearchBinding binding;
    private String textsearch_sql = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_search);

        binding = ActivityIpSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tao rotate xoay
        Animation animationrotate = AnimationUtils.loadAnimation(this, R.anim.rotate_view_360);
        Animation animationtranslate = AnimationUtils.loadAnimation(this, R.anim.translate_back);

        binding.imageViewIPSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationtranslate);
                finish();
            }
        });

        binding.buttonIPSearchSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // text find
                String ten = binding.editTextIPSearchName.getText().toString().isEmpty() ? ""
                        : " AND (Ten LIKE N'%" +  binding.editTextIPSearchName.getText().toString()
                        + "%' OR [dbo].[convertUnicodetoASCII](Ten) LIKE '%" +  binding.editTextIPSearchName.getText().toString() + "%')";
                String khuvuc = binding.editTextIPSearchLocation.getText().toString().isEmpty() ? ""
                        : " AND (Location LIKE N'%" + binding.editTextIPSearchLocation.getText().toString()
                        + "%' OR [dbo].[convertUnicodetoASCII](Location) LIKE '%" + binding.editTextIPSearchLocation.getText().toString()
                        + "%')";
                String ip = binding.editTextIPSearchIP.getText().toString().isEmpty() ? "" : " AND IP LIKE '%" + binding.editTextIPSearchIP.getText().toString() + "'";
                String loaimay = binding.editTextIPSearchMachinetype.getText().toString().isEmpty() ? ""
                        : " AND (LoaiMay.LoaiMay LIKE N'%" + binding.editTextIPSearchMachinetype.getText().toString()
                        + "%' OR [dbo].[convertUnicodetoASCII](LoaiMay.LoaiMay) LIKE '%"
                        + binding.editTextIPSearchMachinetype.getText().toString() + "%')";

                // xử lý tìm theo lớp IP, nếu textIP nhận vào là lớp IP ví dụ: 30 thay vì nhận
                // được là 30.1 thì ta tìm theo lớp IP chứ không tìm theo IP
                int lopip = -1;
                try {
                    lopip = Integer.parseInt(binding.editTextIPSearchIP.getText().toString());
                } catch (NumberFormatException ne) {

                }
                if (lopip >= 0) {
                    ip = binding.editTextIPSearchIP.getText().toString().isEmpty() ? "" : " AND IP LIKE '%" + "%.%." + lopip + "." + "%'";
                }

                textsearch_sql = "SELECT Ten,Location.Location,IP,LoaiMay.LoaiMay,GhiChu,NgayCapNhat FROM DanhSachIP,Location,LoaiMay WHERE DanhSachIP.MaLocation=Location.MaLocation AND DanhSachIP.MaLoaiMay=LoaiMay.MaLoaiMay"
                        + ten + khuvuc + ip + loaimay + " ORDER BY NgayCapNhat DESC";

                v.startAnimation(animationrotate);

                Intent intentresult=new Intent();
                intentresult.putExtra("select",textsearch_sql);
                setResult(10,intentresult);
                finish();
            }
        });

        binding.buttonIPSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationrotate);
                binding.editTextIPSearchName.setText("");
                binding.editTextIPSearchLocation.setText("");
                binding.editTextIPSearchIP.setText("");
                binding.editTextIPSearchMachinetype.setText("");
            }
        });
    }
}