package lacty.cctv.manageripcctv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import lacty.cctv.manageripcctv.databinding.ActivityLocationEditBinding;

public class LocationEdit extends AppCompatActivity {
    ActivityLocationEditBinding binding;
    String edit_id = "";
    String edit_location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_edit);

        binding = ActivityLocationEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        try {
            edit_id = intent.getStringExtra("id");
            edit_location = intent.getStringExtra("location");
            binding.editTextID.setText(edit_id);
            binding.editTextLocation.setText(edit_location);
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
                String location = binding.editTextLocation.getText().toString();
                if (!id.trim().isEmpty()) {
                    if (!location.trim().isEmpty()) {
                        String update = "UPDATE Location SET MaLocation='" + id + "',Location=N'"+location+"' WHERE MaLocation='"+edit_id+"'";
                        int result = connect.execUpdateQuery(update);
                        if (result > 0) {
                            // xoay nut save
                            binding.imageButtonSave.startAnimation(animationrotate);
                            Toast.makeText(LocationEdit.this, "Edit success!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LocationEdit.this, "Location is empty!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LocationEdit.this, "ID is empty!", Toast.LENGTH_SHORT).show();
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