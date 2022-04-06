package lacty.cctv.manageripcctv;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import lacty.cctv.manageripcctv.databinding.ActivityMachinetypeAddBinding;

public class MachineTypeAdd extends AppCompatActivity {
    ActivityMachinetypeAddBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machinetype_add);

        binding=ActivityMachinetypeAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                        String insert = "INSERT INTO LoaiMay ( MaLoaiMay, LoaiMay ) VALUES  ( '" + id + "',N'" + machinetype + "' )";
                        int result = connect.execUpdateQuery(insert);
                        if (result > 0) {
                            binding.editTextID.setText("");
                            binding.editTextMachinetype.setText("");
                            // xoay nut save
                            binding.imageButtonSave.startAnimation(animationrotate);
                            Toast.makeText(MachineTypeAdd.this, "Add success!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MachineTypeAdd.this, "Location is empty!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MachineTypeAdd.this, "ID is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationtranslate);
                finish();
            }
        });

        binding.imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageButtonClose.startAnimation(animationrotate);
                finish();
            }
        });
    }
}