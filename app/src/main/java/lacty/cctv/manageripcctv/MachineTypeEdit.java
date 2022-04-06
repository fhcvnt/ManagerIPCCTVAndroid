package lacty.cctv.manageripcctv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import lacty.cctv.manageripcctv.databinding.ActivityMachinetypeEditBinding;

public class MachineTypeEdit extends AppCompatActivity {
    ActivityMachinetypeEditBinding binding;
    String edit_id = "";
    String edit_location = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machinetype_edit);

        binding=ActivityMachinetypeEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        try {
            edit_id = intent.getStringExtra("id");
            edit_location = intent.getStringExtra("location");
            binding.editTextID.setText(edit_id);
            binding.editTextMachinetype.setText(edit_location);
        }catch (Exception e){
            e.printStackTrace();
        }

        // tao rotate xoay
        Animation animationrotate = AnimationUtils.loadAnimation(this, R.anim.rotate_view_360);
        Animation animationtranslate = AnimationUtils.loadAnimation(this, R.anim.translate_back);

        binding.imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectSQL connect = new ConnectSQL();
                connect.setConnection();

                String id = binding.editTextID.getText().toString();
                String machinetype = binding.editTextMachinetype.getText().toString();
                if (!id.trim().isEmpty()) {
                    if (!machinetype.trim().isEmpty()) {
                        String update = "UPDATE LoaiMay SET MaLoaiMay='" + id + "',LoaiMay=N'"+machinetype+"' WHERE MaLoaiMay='"+edit_id+"'";
                        int result = connect.execUpdateQuery(update);
                        if (result > 0) {
                            // xoay nut save
                            binding.imageButtonSave.startAnimation(animationrotate);
                            Toast.makeText(MachineTypeEdit.this, "Edit success!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MachineTypeEdit.this, "Location is empty!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MachineTypeEdit.this, "ID is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.imageButtonMachinetypeEditBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationtranslate);
                finish();
            }
        });

        binding.imageButtonMachinetypeEditClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageButtonMachinetypeEditClose.startAnimation(animationrotate);
                finish();
            }
        });
    }
}