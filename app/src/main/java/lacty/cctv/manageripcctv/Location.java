package lacty.cctv.manageripcctv;

import android.Manifest;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lacty.cctv.manageripcctv.databinding.ActivityLocationBinding;

public class Location extends AppCompatActivity {
    ActivityLocationBinding binding;
    TableLayout table;
    private static String ip = "192.168.30.7";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "IPManagerCCTV";
    private static String username = "camera";
    private static String password = "ManagerIPcctv!20201016";
    private static String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;
    private Connection connection = null;
    private String selectid = ""; // dung cho select dong
    private String selectlocation = "";
    private View select_view = null;
    private TextView select_view_id = null;
    private TextView select_view_location = null;
    private int select_position_row = -1; // vi tri dong chon tron bang
    private String textsearch = ""; // chuoi tim kiem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tao nut quay lai tren thanh tieu de
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        table = (TableLayout) findViewById(R.id.tableLocation);

        // xin quyen truy cap internet
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ket noi SQL
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "ClassNotFoundException: " + e, Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "SQLException: " + e, Toast.LENGTH_SHORT).show();
        }

        sqlSearch("");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        clearTable();
        sqlSearch(textsearch);
    }

    // tao menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater menuInflater = getMenuInflater();
        // menuInflater.inflate(R.menu.menu_location, menu);

        getMenuInflater().inflate(R.menu.menu_location, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuitem_search).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        // gettext searchview
        // MenuItem searchItem = menu.findItem(R.id.menuitem_search);
        //searchView = (SearchView) searchItem.getActionView();
        // bat su kien thay doi du lieu tim kiem
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // delete data old
                clearTable();
                // result search
                sqlSearch(query);
                textsearch = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty() || newText == null) {
                    // delete data old
                    clearTable();
                    // result search
                    sqlSearch("");
                    textsearch = "";
                } else {
                    // delete data old
                    clearTable();
                    // result search
                    sqlSearch(newText);
                    textsearch = newText;
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuitem_add) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), LocationAdd.class);
            startActivityForResult(intent, 1);
        } else if (item.getItemId() == R.id.menuitem_delete) {
            // event delete
            ConnectSQL connect = new ConnectSQL();
            connect.setConnection();
            if (!select_view_id.getText().toString().trim().isEmpty()) {
                String delete = "DELETE Location WHERE MaLocation='" + select_view_id.getText().toString() + "'";
                int result = connect.execUpdateQuery(delete);
                if (result > 0) {
                    try {
                        //select_view.setBackground(getDrawable(R.drawable.cell_border));
                        //table.removeViewAt(select_position_row);

                        // cap nhat lai bang du lieu sau khi xoa
                        clearTable();
                        sqlSearch(textsearch);
                        Toast.makeText(this, "Delele success!", Toast.LENGTH_SHORT).show();
                        select_view = null;
                        select_position_row = -1;
                    } catch (Exception ne) {
                        ne.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(this, "Please, seclect row!", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.menuitem_edit) {
            if (select_view != null) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LocationEdit.class);
                intent.putExtra("id", select_view_id.getText().toString());
                intent.putExtra("location", select_view_location.getText().toString());
                startActivityForResult(intent, 2);
            }
        } else if (item.getItemId() == android.R.id.home) {
            // sự kiện nhấn nút quay lại trên thanh tiêu đề
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // dp to px
    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    // px to dp
    public static float convertPxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    // ham ket noi co so du lieu va lay du lieu cho table
    public void sqlSearch(String chuoi) {
        if (connection != null) {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                // so thu tu
                int stt = 1;
                String select = "SELECT MaLocation,Location FROM Location WHERE Location LIKE N'%" + chuoi + "%' OR dbo.convertUnicodetoASCII(Location) LIKE N'%" + chuoi + "%' ORDER BY Location ASC";
                ResultSet resultSet = statement.executeQuery(select);

                // doi dp to px
                int dp35 = (int) convertDpToPx(getApplicationContext(), 35);
                while (resultSet.next()) {
                    TableRow tr = new TableRow(this);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                    TextView textview = new TextView(this);
                    textview.setText("" + stt);
                    textview.setPadding(5, 0, 0, 0);
                    textview.setHeight(dp35);
                    textview.setBackground(getDrawable(R.drawable.cell_border));
                    textview.setTextSize(20);
                    textview.setGravity(Gravity.CENTER_VERTICAL);
                    textview.setTypeface(null, Typeface.NORMAL);

                    TextView textview2 = new TextView(this);
                    textview2.setText(resultSet.getString(1));
                    textview2.setPadding(5, 0, 0, 0);
                    textview2.setHeight(dp35);
                    textview2.setBackground(getDrawable(R.drawable.cell_border));
                    textview2.setTextSize(20);
                    textview2.setGravity(Gravity.CENTER_VERTICAL);
                    textview2.setTypeface(null, Typeface.NORMAL);

                    TextView textview3 = new TextView(this);
                    textview3.setText(resultSet.getString(2));
                    textview3.setPadding(5, 0, 0, 0);
                    textview3.setHeight(dp35);
                    textview3.setBackground(getDrawable(R.drawable.cell_border));
                    textview3.setTextSize(20);
                    textview3.setGravity(Gravity.CENTER_VERTICAL);
                    textview3.setTypeface(null, Typeface.NORMAL);

                    tr.addView(textview);
                    tr.addView(textview2);
                    tr.addView(textview3);

                    tr.setClickable(true);
                    textview.setOnClickListener(onClickListenerData(textview, textview2, textview3, table.getChildCount()));
                    textview2.setOnClickListener(onClickListenerData(textview2, textview2, textview3, table.getChildCount()));
                    textview3.setOnClickListener(onClickListenerData(textview3, textview2, textview3, table.getChildCount()));

                    table.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    stt++;
                }
                resultSet.close();
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Connection is null", Toast.LENGTH_SHORT).show();
        }
    }

    // event click to row table
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.setBackground(getDrawable(R.drawable.select_row));
        }
    };

    private View.OnClickListener onClickListenerData(View view, TextView view_id, TextView view_location, int position_row) {
        View.OnClickListener click = new View.OnClickListener() {
            public void onClick(View v) {
                if (select_view != null) {
                    select_view.setBackground(getDrawable(R.drawable.cell_border));
                }
                v.setBackground(getDrawable(R.drawable.select_row));
                select_view_id = view_id;
                select_view_location = view_location;
                select_view = v;
                select_position_row = position_row;
            }
        };
        return click;
    }

    // ham xoa du lieu bang table - count -1 dong, khong xoa dong tieu de
    public void clearTable() {
        int countrow = table.getChildCount();
        table.removeViews(1, countrow - 1);
    }
}