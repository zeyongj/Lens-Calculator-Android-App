package ca.cmpt276.as2.dofcalculator.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import ca.cmpt276.as2.dofcalculator.R;
import ca.cmpt276.as2.dofcalculator.model.DoFCalculator;
import ca.cmpt276.as2.dofcalculator.model.Lens;
import ca.cmpt276.as2.dofcalculator.model.LensManager;

/**
 * Enter photo details and calculate/display depth of field
 */
public class CalculateActivity extends AppCompatActivity {

    private static final String EXTRA_LENS_INDEX = "Extra - lens index";
    private Lens lens = null;

    // Reference UI elements
    private EditText etCircleOfConfusion;
    private EditText etDistanceToSubject;
    private EditText etSelectedAperture;

    private TextView tvLensInfo;
    private TextView tvNearFocalDistance;
    private TextView tvFarFocalDistance;
    private TextView tvHyperFocalDistance;
    private TextView tvDepthOfField;


    public static Intent makeCalculateIntent(Context c, int lensIdx) {
        Intent intent = new Intent(c, CalculateActivity.class);
        intent.putExtra(EXTRA_LENS_INDEX, lensIdx);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storeUiReferences();
        registerForDataEntry();
        extractExtras(this.getIntent());
        repopulateUi();
    }

    private void registerForDataEntry() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                repopulateUi();
            }
        };
        etCircleOfConfusion.addTextChangedListener(watcher);
        etDistanceToSubject.addTextChangedListener(watcher);
        etSelectedAperture.addTextChangedListener(watcher);
    }

    private void storeUiReferences() {
        etCircleOfConfusion = findViewById(R.id.etCircleOfConfusion);
        etDistanceToSubject = findViewById(R.id.etDistanceToSubjectM);
        etSelectedAperture = findViewById(R.id.etSelectedAperture);

        tvLensInfo = findViewById(R.id.tvSelectedLens);
        tvNearFocalDistance = findViewById(R.id.tvNearFocalDistanceM);
        tvFarFocalDistance = findViewById(R.id.tvFarFocalDistanceM);
        tvHyperFocalDistance = findViewById(R.id.tvHyperFocalDistanceM);;
        tvDepthOfField = findViewById(R.id.tvDepthOfFieldM);
    }


    private void extractExtras(Intent intent) {
        int lensIdx = intent.getIntExtra(EXTRA_LENS_INDEX, -1);
        LensManager lensMan = LensManager.getInstance();
        lens = lensMan.get(lensIdx);
    }

    private void repopulateUi() {
        // Lens
        tvLensInfo.setText(lens.getDescription());

        // Extract values (if able)
        String cocStr = etCircleOfConfusion.getText().toString();
        String distStr = etDistanceToSubject.getText().toString();
        String aperturStr = etSelectedAperture.getText().toString();

        // Calculate (when possible)
        String errorMessage = null;
        try {
            double coc = Double.parseDouble(cocStr);
            double distanceM = Double.parseDouble(distStr);
            double aperture = Double.parseDouble(aperturStr);

            // Error check:
            if (aperture < lens.getMaxAperture()) {
                throw new IllegalArgumentException("Invalid aperture");
            }

            DoFCalculator calc = new DoFCalculator(coc, lens, aperture, distanceM);

            tvNearFocalDistance.setText(formatM(calc.getNearFocalPointInM()));
            tvFarFocalDistance.setText(formatM(calc.getFarFocalPointInM()));
            tvHyperFocalDistance.setText(formatM(calc.getHpyerfocalDistanceInM()));
            tvDepthOfField.setText(formatM(calc.getDepthOfFieldInM()));

        } catch (NumberFormatException e) {
            errorMessage = "Enter all values";
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
        if (errorMessage != null) {
            tvNearFocalDistance.setText(errorMessage);
            tvFarFocalDistance.setText(errorMessage);
            tvHyperFocalDistance.setText(errorMessage);
            tvDepthOfField.setText(errorMessage);
        }
    }

    private String formatM(double distanceInM) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(distanceInM) + "m";
    }
}
