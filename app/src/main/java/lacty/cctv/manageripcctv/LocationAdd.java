package lacty.cctv.manageripcctv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import lacty.cctv.manageripcctv.databinding.ActivityLocationAddBinding;

public class LocationAdd extends AppCompatActivity {
ActivityLocationAddBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_add);

        binding=ActivityLocationAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tao rotate xoay
        Animation animationrotate = AnimationUtils.loadAnimation(this, R.anim.rotate_view_360);
        Animation animationtranslate = AnimationUtils.loadAnimation(this, R.anim.translate_back);

        binding.imageButtonLocationAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectSQL connect = new ConnectSQL();
                connect.setConnection();

                String id = binding.editTextLocationAddID.getText().toString();
                String location = binding.editTextLocationAddLocation.getText().toString();
                if (!id.trim().isEmpty()) {
                    if (!location.trim().isEmpty()) {
                        String insert = "INSERT INTO Location ( MaLocation, Location ) VALUES  ( '" + id + "',N'" + location + "' )";
                        int result = connect.execUpdateQuery(insert);
                        if (result > 0) {
                            binding.editTextLocationAddID.setText("");
                            binding.editTextLocationAddLocation.setText("");
                            // xoay nut save
                            binding.imageButtonLocationAddSave.startAnimation(animationrotate);
                            Toast.makeText(LocationAdd.this, "Add success!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LocationAdd.this, "Location is empty!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LocationAdd.this, "ID is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.imageButtonLocationAddBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationtranslate);
                finish();
            }
        });

        binding.imageButtonLocationAddClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageButtonLocationAddClose.startAnimation(animationrotate);
                finish();
            }
        });
    }
}