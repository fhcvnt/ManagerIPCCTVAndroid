package sqlite;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;

import lacty.cctv.manageripcctv.R;
import lacty.cctv.manageripcctv.databinding.ActivityLocationAddBinding;
import lacty.cctv.manageripcctv.databinding.ActivitySqliteMachineTypeBinding;

public class SQLiteMachineType extends AppCompatActivity {
    ActivitySqliteMachineTypeBinding binding;
    Database database;
    private View select_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_machine_type);

        binding = ActivitySqliteMachineTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tao nut quay lai tren thanh tieu de
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // tao menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_sqlite, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuitem_search_sqlite).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        // bat su kien thay doi du lieu tim kiem
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // delete data old
                clearTable();
                // result search
                sqlSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty() || newText == null) {
                    // delete data old
                    clearTable();
                    // result search
                    sqlSearch("");
                } else {
                    // delete data old
                    clearTable();
                    // result search
                    sqlSearch(newText);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // su kien nhan vao nut quay lai tren thanh tieu de
            finish();
        } else if (item.getItemId() == R.id.menuitem_search_sqlite) {
            // delete data old
            clearTable();
            // result search
            sqlSearch("");
        }

        return super.onOptionsItemSelected(item);
    }

    // dp to px
    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    // ham ket noi co so du lieu va lay du lieu cho table
    public void sqlSearch(String chuoi) {
        database = new Database(getApplicationContext(), "IPManagerCCTV", null, 3);
        try {
            // so thu tu
            int stt = 1;
            String select = "SELECT MaLoaiMay,LoaiMay FROM LoaiMay WHERE LoaiMay LIKE '%" + chuoi + "%' ORDER BY LoaiMay ASC";
            Cursor result = database.GetData(select);

            // doi dp to px
            int dp35 = (int) convertDpToPx(getApplicationContext(), 35);
            while (result.moveToNext()) {
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
                textview2.setText(result.getString(0));
                textview2.setPadding(5, 0, 0, 0);
                textview2.setHeight(dp35);
                textview2.setBackground(getDrawable(R.drawable.cell_border));
                textview2.setTextSize(20);
                textview2.setGravity(Gravity.CENTER_VERTICAL);
                textview2.setTypeface(null, Typeface.NORMAL);

                TextView textview3 = new TextView(this);
                textview3.setText(result.getString(1));
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
                textview.setOnClickListener(onClickListenerData(textview));
                textview2.setOnClickListener(onClickListenerData(textview2));
                textview3.setOnClickListener(onClickListenerData(textview3));

                binding.tableSQLiteMachinetype.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                stt++;
            }
            result.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // event click to row table
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.setBackground(getDrawable(R.drawable.select_row));
        }
    };

    private View.OnClickListener onClickListenerData(View view) {
        View.OnClickListener click = new View.OnClickListener() {
            public void onClick(View v) {
                if (select_view != null) {
                    select_view.setBackground(getDrawable(R.drawable.cell_border));
                }
                v.setBackground(getDrawable(R.drawable.select_row));
                select_view = v;
            }
        };
        return click;
    }

    // ham xoa du lieu bang table - count -1 dong, khong xoa dong tieu de
    public void clearTable() {
        int countrow = binding.tableSQLiteMachinetype.getChildCount();
        binding.tableSQLiteMachinetype.removeViews(1, countrow - 1);
    }
}