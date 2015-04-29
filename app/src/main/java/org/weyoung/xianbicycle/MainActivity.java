package org.weyoung.xianbicycle;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.weyoung.xianbicycle.data.BicycleData;
import org.weyoung.xianbicycle.net.Fetcher;
import org.weyoung.xianbicycle.net.Search;
import org.weyoung.xianbicycle.ui.DataAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;


public class MainActivity extends ActionBarActivity {
    @InjectView(R.id.query)
    EditText query;
    @InjectView(R.id.result)
    ListView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    query();
                }
                return false;
            }
        });

    }

    private void query() {
        Fetcher fetcher = new Fetcher();
        Search search = new Search(query.getText().toString());
        fetcher.getData(search).subscribe(new Action1<List<BicycleData>>() {
            @Override
            public void call(List<BicycleData> data) {
                if (data.size() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.no_result, Toast.LENGTH_SHORT).show();
                } else {
                    result.setAdapter(new DataAdapter(MainActivity.this, data));
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
