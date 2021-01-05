package ca.cmpt276.as2.dofcalculator.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ca.cmpt276.as2.dofcalculator.R;
import ca.cmpt276.as2.dofcalculator.model.Lens;
import ca.cmpt276.as2.dofcalculator.model.LensManager;

/**
 * Display list of lenses
 */
public class MainActivity extends AppCompatActivity {
    // Arbitrary numbers for startActivityForResult:
    private static final int ACTIVITY_RESULT_ADD = 101;
    private static final int ACTIVITY_RESULT_EDIT = 102;
    private static final int ACTIVITY_RESULT_CALCULATE = 103;

    private LensManager lenses;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lenses = LensManager.getInstance();

        // Populate the List with some test lenses
        lenses.add(new Lens("Canon", 1.8, 50));
        lenses.add(new Lens("Tamron", 2.8, 90));
        lenses.add(new Lens("Sigma", 2.8, 200));
        lenses.add(new Lens("Nikon", 4, 200));


        setupFloatingActionButton();
        setupLensView();
    }

    private void setupLensView() {
        // SOURCE: https://developer.android.com/guide/topics/ui/layout/recyclerview
        ListView rv = findViewById(R.id.lensListView);

        // Could also use an ArrayAdapter (as in tutorial video)
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return lenses.getNumLenses();
            }

            @Override
            public Lens getItem(int position) {
                return lenses.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;   // unused
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.lens_view_for_list, parent, false);
                }

                String description = getItem(position).getDescription();
                ((TextView) convertView)
                        .setText(description);
                return convertView;
            }
        };
        rv.setAdapter(adapter);

        rv.setOnItemClickListener(
            (parent, view, position, id) -> {
                Intent intent = CalculateActivity.makeCalculateIntent(MainActivity.this, position);
                startActivityForResult(intent, ACTIVITY_RESULT_CALCULATE);
            }
        );

    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = LensDetailsActivity.makeIntentForAdd(MainActivity.this);
            startActivityForResult(i, ACTIVITY_RESULT_ADD);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_RESULT_ADD:
            case ACTIVITY_RESULT_EDIT:
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
