package lacty.cctv.manageripcctv;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import lacty.cctv.manageripcctv.databinding.ActivityIpAddBinding;

public class IPAdd extends AppCompatActivity {
    ActivityIpAddBinding binding;
    private ArrayList<String> arrayListLocation;
    private ArrayList<String> arrayListLoaimay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_add);

        binding = ActivityIpAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tao rotate xoay
        Animation animationrotate = AnimationUtils.loadAnimation(this, R.anim.rotate_view_360);
        Animation animationtranslate = AnimationUtils.loadAnimation(this, R.anim.translate_back);

        ConnectSQL connect = new ConnectSQL();
        connect.setConnection();
        String select = "SELECT Location FROM Location ORDER BY Location ASC";
        arrayListLocation = new ArrayList<>();
        arrayListLocation = connect.getArraySeclect(select);

        ArrayAdapter<String> adapterlocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListLocation);
        adapterlocation.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerIPAdLocation.setAdapter(adapterlocation);

        ConnectSQL connectmachinetype = new ConnectSQL();
        connectmachinetype.setConnection();
        select = "SELECT LoaiMay FROM LoaiMay ORDER BY LoaiMay ASC";
        arrayListLoaimay = new ArrayList<>();
        arrayListLoaimay = connectmachinetype.getArraySeclect(select);
        ArrayAdapter<String> adaptermachinetype = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListLoaimay);
        adaptermachinetype.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        binding.spinnerIPAdMachinetype.setAdapter(adaptermachinetype);

        binding.imageViewIPAddBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationrotate);
                finish();
            }
        });

        binding.imageButtonIPAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateIP(binding.editTextIPAddIP.getText().toString())) {
                    String insert = "INSERT INTO DanhSachIP(Ten,MaLocation,IP,MaLoaiMay,GhiChu,NgayCapNhat) VALUES  ( N'" + binding.editTextIPAddName.getText().toString() + "' ,(SELECT TOP 1 MaLocation FROM Location WHERE Location=N'" + arrayListLocation.get(binding.spinnerIPAdLocation.getSelectedItemPosition()) + "'),'" + binding.editTextIPAddIP.getText().toString() + "' ,(SELECT TOP 1 MaLoaiMay FROM LoaiMay WHERE LoaiMay=N'" + arrayListLoaimay.get(binding.spinnerIPAdMachinetype.getSelectedItemPosition()) + "') ,N'" + binding.editTextIPAddNote.getText().toString() + "' ,GETDATE())";
                    ConnectSQL connectinsert = new ConnectSQL();
                    connectinsert.setConnection();
                    int result = connectinsert.execUpdateQuery(insert);
                    if (result > 0) {
                        Toast.makeText(IPAdd.this, "Add success!", Toast.LENGTH_SHORT).show();
                        binding.editTextIPAddName.setText("");
                        binding.editTextIPAddIP.setText("");
                        binding.editTextIPAddNote.setText("");
                        v.startAnimation(animationrotate);
                    }
                } else {
                    Toast.makeText(IPAdd.this, "Wrong ip format!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Kiểm tra định dạng IP
    public static boolean validateIP(final String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(PATTERN);
    }
}