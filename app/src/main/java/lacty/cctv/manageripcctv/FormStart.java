package lacty.cctv.manageripcctv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.sql.ResultSet;

import lacty.cctv.manageripcctv.databinding.ActivityFormStartBinding;
import sqlite.Database;
import sqlite.SQLiteMain;

import static lacty.cctv.manageripcctv.R.*;

public class FormStart extends AppCompatActivity {
    ActivityFormStartBinding binding;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_form_start);

        binding = ActivityFormStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // animation alpha
        Animation animationalpha = AnimationUtils.loadAnimation(this, R.anim.translate_back);
        Animation animationalphasyn = AnimationUtils.loadAnimation(this, anim.translate_back);

        // button Synchronized
        binding.buttonFormstartSynchronized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // an di cac button
                v.startAnimation(animationalphasyn);

                // tao database IPManagerCCTV
                database = new Database(getApplicationContext(), "IPManagerCCTV", null, 3);

                // tao bang LoaiMay
                String sql = "CREATE TABLE IF NOT EXISTS LoaiMay(MaLoaiMay VARCHAR(15) PRIMARY KEY NOT NULL, LoaiMay NVARCHAR(30) UNIQUE NOT NULL)";
                database.QueryData(sql);
                database.QueryData("DELETE FROM LoaiMay");

                // tao bang Location
                sql = "CREATE TABLE IF NOT EXISTS Location(MaLocation VARCHAR(10) PRIMARY KEY NOT NULL,Location NVARCHAR(50) UNIQUE NOT NULL)";
                database.QueryData(sql);
                database.QueryData("DELETE FROM Location");

                // tao bang DanhSachIP
                sql = "CREATE TABLE IF NOT EXISTS DanhSachIP(Ten NVARCHAR(40) NOT NULL,Location VARCHAR(50) NOT NULL,IP VARCHAR(15) UNIQUE NOT NULL,LoaiMay VARCHAR(30) NOT NULL,GhiChu NVARCHAR(255),NgayCapNhat DATE,LopIP VARCHAR(3))";
                database.QueryData(sql);
                database.QueryData("DELETE FROM DanhSachIP");

                // insert data
                ConnectSQL connect = new ConnectSQL();
                connect.setConnection();
                try {
                    if (connect.getConnection() != null) {
                        connect.setStatement();
                        // insert LoaiMay
                        String select = "SELECT MaLoaiMay,LoaiMay FROM LoaiMay";
                        ResultSet result = connect.getStatement().executeQuery(select);
                        while (result.next()) {
                            try {
                                database.QueryData("INSERT INTO LoaiMay VALUES('" + result.getString(1) + "','" + result.getString(2) + "')");
                            } catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        }
                        result.close();
                        connect.closeStatement();
                        connect.setStatement();

                        // insert Location
                        select = "SELECT MaLocation,Location FROM Location";
                        result = connect.getStatement().executeQuery(select);
                        while (result.next()) {
                            try {
                                database.QueryData("INSERT INTO Location VALUES('" + result.getString(1) + "','" + result.getString(2) + "')");
                            } catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        }
                        result.close();
                        connect.closeStatement();
                        connect.setStatement();

                        // insert DanhSachIP
                        select = " SELECT Ten,Location.Location,IP,LoaiMay.LoaiMay,GhiChu,NgayCapNhat FROM DanhSachIP,LoaiMay,Location WHERE DanhSachIP.MaLoaiMay=LoaiMay.MaLoaiMay AND DanhSachIP.MaLocation=Location.MaLocation";
                        result = connect.getStatement().executeQuery(select);
                        while (result.next()) {
                            try {
                                String lopip=result.getString(3);
                                lopip=lopip.substring(8,lopip.lastIndexOf("."));
                                database.QueryData("INSERT INTO DanhSachIP VALUES('" + result.getString(1) + "','" + result.getString(2) + "','" + result.getString(3) + "','" + result.getString(4) + "','" + result.getString(5) + "','" + result.getString(6) + "','"+lopip+"')");
                            } catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        }
                        result.close();
                        connect.closeStatement();
                        connect.setStatement();
                        connect.closeConnection();
                        Toast.makeText(getApplicationContext(), "Synchronized success!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // button Online
        binding.buttonFormstartOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationalpha);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // button Offline
        binding.buttonFormstartOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationalpha);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SQLiteMain.class);
                startActivity(intent);
                finish();
            }
        });

        // Exit
        binding.buttonFormstartExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationalpha);
                finish();
            }
        });
    }
}