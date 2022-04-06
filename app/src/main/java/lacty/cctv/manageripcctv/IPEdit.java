package lacty.cctv.manageripcctv;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import lacty.cctv.manageripcctv.databinding.ActivityIpEditBinding;

public class IPEdit extends AppCompatActivity {
    ActivityIpEditBinding binding;
    private ArrayList<String> arrayListLocation;
    private ArrayList<String> arrayListLoaimay;
    private String ip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_edit);

        binding = ActivityIpEditBinding.inflate(getLayoutInflater());
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
        binding.spinnerIPEditLocation.setAdapter(adapterlocation);

        ConnectSQL connectmachinetype = new ConnectSQL();
        connectmachinetype.setConnection();
        select = "SELECT LoaiMay FROM LoaiMay ORDER BY LoaiMay ASC";
        arrayListLoaimay = new ArrayList<>();
        arrayListLoaimay = connectmachinetype.getArraySeclect(select);
        ArrayAdapter<String> adaptermachinetype = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListLoaimay);
        adaptermachinetype.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        binding.spinnerIPEditMachinetype.setAdapter(adaptermachinetype);

        // gan gia tri edit
        Intent intentedit = getIntent();
        String name = intentedit.getStringExtra("editname");
        String location = intentedit.getStringExtra("editlocation");
        ip = intentedit.getStringExtra("editip");
        String machinetype = intentedit.getStringExtra("editmachinetype");
        String note = intentedit.getStringExtra("editnote");

        binding.editTextIPEditName.setText(name);
        binding.editTextIPEditIP.setText(ip);
        binding.editTextIPEditNote.setText(note);

        int vitrilocationedit = 0;
        for (int i = 0; i < arrayListLocation.size(); i++) {
            if (arrayListLocation.get(i).equalsIgnoreCase(location)) {
                vitrilocationedit = i;
                break;
            }
        }
        binding.spinnerIPEditLocation.setSelection(vitrilocationedit);

        int vitrimachinetypeedit = 0;
        for (int i = 0; i < arrayListLoaimay.size(); i++) {
            if (arrayListLoaimay.get(i).equalsIgnoreCase(machinetype)) {
                vitrimachinetypeedit = i;
                break;
            }
        }
        binding.spinnerIPEditMachinetype.setSelection(vitrimachinetypeedit);

        binding.imageViewIPEditBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationrotate);
                finish();
            }
        });

        binding.imageButtonIPEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateIP(binding.editTextIPEditIP.getText().toString())) {
                    String update = "UPDATE dbo.DanhSachIP SET Ten=N'" + binding.editTextIPEditName.getText().toString() + "',MaLocation=(SELECT TOP 1 MaLocation FROM Location WHERE Location=N'" + arrayListLocation.get(binding.spinnerIPEditLocation.getSelectedItemPosition()) + "'),IP='" + binding.editTextIPEditIP.getText().toString() + "',MaLoaiMay=(SELECT TOP 1 MaLoaiMay FROM LoaiMay WHERE LoaiMay=N'" + arrayListLoaimay.get(binding.spinnerIPEditMachinetype.getSelectedItemPosition()) + "'),GhiChu=N'" + binding.editTextIPEditNote.getText().toString() + "',NgayCapNhat=GETDATE() WHERE IP='" + ip + "'";
                    ConnectSQL connectinsert = new ConnectSQL();
                    connectinsert.setConnection();
                    int result = connectinsert.execUpdateQuery(update);
                    if (result > 0) {
                        Toast.makeText(IPEdit.this, "Edit success!", Toast.LENGTH_SHORT).show();
                        v.startAnimation(animationrotate);
                        ip = binding.editTextIPEditIP.getText().toString();
                    }
                } else {
                    Toast.makeText(IPEdit.this, "Wrong ip format!", Toast.LENGTH_SHORT).show();
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