package ca.cmpt276.as2.dofcalculator.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.EditText;

import ca.cmpt276.as2.dofcalculator.R;
import ca.cmpt276.as2.dofcalculator.model.Lens;
import ca.cmpt276.as2.dofcalculator.model.LensManager;

/**
 * Edit a lens
 */
public class LensDetailsActivity extends AppCompatActivity {
    private EditText etMake;
    private EditText etAperture;
    private EditText etFocalLength;

    public static Intent makeIntentForAdd(Context context) {
        return new Intent(context, LensDetailsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lens_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etMake = findViewById(R.id.etMake);
        etAperture = findViewById(R.id.etAperture);
        etFocalLength = findViewById(R.id.etFocalLength);

        setupButtonCancel();
        setupButtonOk();
    }


    private void setupButtonCancel() {
        Button btn = findViewById(R.id.btnCancel);
        btn.setOnClickListener(
                (view) -> finish()
        );
    }
    private void setupButtonOk() {
        Button btn = findViewById(R.id.btnSave);
        btn.setOnClickListener(
                (view) -> {
                    // Extract data from screen
                    String make = etMake.getText().toString();
                    String apertureStr = etAperture.getText().toString();
                    String focLenStr = etFocalLength.getText().toString();
                    double aperture = Double.parseDouble(apertureStr);
                    int focLen = Integer.parseInt(focLenStr);

                    // Create new data object
                    Lens lens = new Lens(make, aperture, focLen);
                    LensManager lenses = LensManager.getInstance();
                    lenses.add(lens);
                    finish();
                }
        );
    }

}
