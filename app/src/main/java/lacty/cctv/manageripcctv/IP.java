package lacty.cctv.manageripcctv;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lacty.cctv.manageripcctv.databinding.ActivityIpBinding;


public class IP extends AppCompatActivity {
    ActivityIpBinding binding;
    private String selectid = ""; // dung cho select dong
    private String selectlocation = "";
    private View select_view = null;
    private TextView select_view_name = null;
    private TextView select_view_location = null;
    private TextView select_view_ip = null;
    private TextView select_view_machinetype = null;
    private TextView select_view_note = null;
    private TableRow select_row; // vi tri dong chon trong bang
    private String text_select = "SELECT Ten,Location.Location,IP,LoaiMay.LoaiMay,GhiChu,NgayCapNhat FROM DanhSachIP,Location,LoaiMay WHERE DanhSachIP.MaLocation=Location.MaLocation AND DanhSachIP.MaLoaiMay=LoaiMay.MaLoaiMay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        binding = ActivityIpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tao nut quay lai tren thanh tieu de
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sqlSearch(text_select);
    }

    // tao menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ip, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuitem_ip_search) {
            // Search
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), IPSearch.class);
            startActivityForResult(intent, 10);

        } else if (item.getItemId() == R.id.menuitem_ip_add) {
            // Add
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), IPAdd.class);
            startActivityForResult(intent, 11);

        } else if (item.getItemId() == R.id.menuitem_ip_delete) {
            // Delete
            ConnectSQL connect = new ConnectSQL();
            connect.setConnection();
            if (!select_view_ip.getText().toString().trim().isEmpty()) {
                String delete = "DELETE DanhSachIP WHERE IP='" + select_view_ip.getText().toString() + "'";
                int result = connect.execUpdateQuery(delete);
                if (result > 0) {
                    try {
                        binding.tableIP.removeView(select_row);
                        select_row = null;
                        Toast.makeText(this, "Delele success!", Toast.LENGTH_SHORT).show();
                        select_view = null;
                    } catch (Exception ne) {
                        ne.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(this, "Please, seclect row!", Toast.LENGTH_SHORT).show();
            }

        } else if (item.getItemId() == R.id.menuitem_ip_edit) {
            // Edit
            if (select_view != null) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), IPEdit.class);
                intent.putExtra("editname", select_view_name.getText().toString());
                intent.putExtra("editlocation", select_view_location.getText().toString());
                intent.putExtra("editip", select_view_ip.getText().toString());
                intent.putExtra("editmachinetype", select_view_machinetype.getText().toString());
                intent.putExtra("editnote", select_view_note.getText().toString());
                startActivityForResult(intent, 12);
            }
        } else if (item.getItemId() == android.R.id.home) {
            // sự kiện nhấn nút quay lại trên thanh tiêu đề
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10: // nhan ve ket qua tim kiem la chuoi ket noi sql
                try {
                    text_select = data.getStringExtra("select");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                clearTable();
                sqlSearch(text_select);
                break;
            default:
                clearTable();
                sqlSearch(text_select);
                break;
        }
    }

    private View.OnClickListener onClickListenerData(View view, TextView view_name, TextView view_location, TextView view_ip, TextView view_machinetype, TextView view_note, TableRow row) {
        View.OnClickListener click = new View.OnClickListener() {
            public void onClick(View v) {
                if (select_view != null) {
                    select_view.setBackground(getDrawable(R.drawable.cell_border));
                }
                v.setBackground(getDrawable(R.drawable.select_row));
                select_view_name = view_name;
                select_view_location = view_location;
                select_view_ip = view_ip;
                select_view_machinetype = view_machinetype;
                select_view_note = view_note;
                select_view = v;
                select_row = row;
            }
        };
        return click;
    }

    // ham ket noi co so du lieu va lay du lieu cho table
    public void sqlSearch(String select) {
        try {
            int stt = 1;
            // doi dp to px
            int dp35 = (int) convertDpToPx(getApplicationContext(), 35);

            ConnectSQL connect = new ConnectSQL();
            connect.setConnection();
            connect.setStatement();

            //String select = "SELECT Ten,Location.Location,IP,LoaiMay.LoaiMay,GhiChu,NgayCapNhat FROM DanhSachIP,Location,LoaiMay WHERE DanhSachIP.MaLocation=Location.MaLocation AND DanhSachIP.MaLoaiMay=LoaiMay.MaLoaiMay";
            ResultSet result = connect.getStatement().executeQuery(select);
            while (result.next()) {
                // kiem tra so lan xuong dong cua ghi chu
                int countenter = 1;

                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tr.setBackground(getDrawable(R.drawable.cell_border));

                TextView textview = new TextView(this);
                textview.setText("" + stt);
                textview.setPadding(5, 0, 5, 0);
                textview.setHeight(dp35 * countenter);
                textview.setBackground(getDrawable(R.drawable.cell_border));
                textview.setTextSize(20);
                textview.setGravity(Gravity.CENTER_VERTICAL);
                textview.setTypeface(null, Typeface.NORMAL);

                TextView textview2 = new TextView(this);
                textview2.setText(result.getString(1));
                textview2.setPadding(5, 0, 5, 0);
                textview2.setHeight(dp35 * countenter);
                textview2.setBackground(getDrawable(R.drawable.cell_border));
                textview2.setTextSize(20);
                textview2.setGravity(Gravity.CENTER_VERTICAL);
                textview2.setTypeface(null, Typeface.NORMAL);

                TextView textview3 = new TextView(this);
                textview3.setText(result.getString(2));
                textview3.setPadding(5, 0, 5, 0);
                textview3.setHeight(dp35 * countenter);
                textview3.setBackground(getDrawable(R.drawable.cell_border));
                textview3.setTextSize(20);
                textview3.setGravity(Gravity.CENTER_VERTICAL);
                textview3.setTypeface(null, Typeface.NORMAL);

                TextView textview4 = new TextView(this);
                textview4.setText(result.getString(3));
                textview4.setPadding(5, 0, 5, 0);
                textview4.setHeight(dp35 * countenter);
                textview4.setBackground(getDrawable(R.drawable.cell_border));
                textview4.setTextSize(20);
                textview4.setGravity(Gravity.CENTER_VERTICAL);
                textview4.setTypeface(null, Typeface.NORMAL);

                TextView textview5 = new TextView(this);
                textview5.setText(result.getString(4));
                textview5.setPadding(5, 0, 5, 0);
                textview5.setHeight(dp35 * countenter);
                textview5.setBackground(getDrawable(R.drawable.cell_border));
                textview5.setTextSize(20);
                textview5.setGravity(Gravity.CENTER_VERTICAL);
                textview5.setTypeface(null, Typeface.NORMAL);

                TextView textview6 = new TextView(this);
                textview6.setText(result.getString(5));
                textview6.setPadding(5, 0, 5, 0);
                textview6.setHeight(dp35 * countenter);
                //textview6.setBackground(getDrawable(R.drawable.cell_border));
                textview6.setTextSize(20);
                textview6.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                textview6.setGravity(Gravity.START | Gravity.TOP);
                textview6.setTypeface(null, Typeface.NORMAL);

                TextView textview7 = new TextView(this);
                textview7.setText(result.getString(6));
                textview7.setPadding(5, 0, 5, 0);
                textview7.setHeight(dp35 * countenter);
                textview7.setBackground(getDrawable(R.drawable.cell_border));
                textview7.setTextSize(20);
                textview7.setGravity(Gravity.CENTER_VERTICAL);
                textview7.setTypeface(null, Typeface.NORMAL);

                tr.addView(textview);
                tr.addView(textview2);
                tr.addView(textview3);
                tr.addView(textview4);
                tr.addView(textview5);
                tr.addView(textview6);
                tr.addView(textview7);

                tr.setClickable(true);
                textview.setOnClickListener(onClickListenerData(textview, textview2, textview3, textview4, textview5, textview6, tr));
                textview2.setOnClickListener(onClickListenerData(textview2, textview2, textview3, textview4, textview5, textview6, tr));
                textview3.setOnClickListener(onClickListenerData(textview3, textview2, textview3, textview4, textview5, textview6, tr));
                textview4.setOnClickListener(onClickListenerData(textview4, textview2, textview3, textview4, textview5, textview6, tr));
                textview5.setOnClickListener(onClickListenerData(textview5, textview2, textview3, textview4, textview5, textview6, tr));
                textview6.setOnClickListener(onClickListenerData(textview6, textview2, textview3, textview4, textview5, textview6, tr));
                textview7.setOnClickListener(onClickListenerData(textview7, textview2, textview3, textview4, textview5, textview6, tr));

                binding.tableIP.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                stt++;
            }

            result.close();
            connect.closeStatement();
            connect.closeConnection();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    // ham xoa du lieu bang table - count -1 dong, khong xoa dong tieu de
    public void clearTable() {
        int countrow = binding.tableIP.getChildCount();
        binding.tableIP.removeViews(1, countrow - 1);
    }

    // dp to px
    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}