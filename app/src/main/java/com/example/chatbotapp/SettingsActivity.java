package com.example.chatbotapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerTheme;
    private Spinner spinnerColorTheme;
    private Spinner spinnerLanguage;

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.applyLocale(this);
        ThemeHelper.applyThemeColor(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.settings));
        }

        prefManager = new PrefManager(this);

        spinnerTheme = findViewById(R.id.spinnerTheme);
        spinnerColorTheme = findViewById(R.id.spinnerColorTheme);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);

        // --- Theme Mode Spinner ---
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"System Default", "Light", "Dark"}
        );
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(themeAdapter);

        int currentMode = AppCompatDelegate.getDefaultNightMode();
        int themeIndex;
        switch (currentMode) {
            case AppCompatDelegate.MODE_NIGHT_YES:
                themeIndex = 2;
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                themeIndex = 1;
                break;
            default:
                themeIndex = 0;
        }
        spinnerTheme.setSelection(themeIndex);

        spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedMode;
                switch (position) {
                    case 1:
                        selectedMode = AppCompatDelegate.MODE_NIGHT_NO;
                        break;
                    case 2:
                        selectedMode = AppCompatDelegate.MODE_NIGHT_YES;
                        break;
                    default:
                        selectedMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                }

                if (selectedMode != AppCompatDelegate.getDefaultNightMode()) {
                    AppCompatDelegate.setDefaultNightMode(selectedMode);
                    recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // --- Language Spinner ---
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"English", "Türkçe"}
        );
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(languageAdapter);

        String currentLang = prefManager.getLanguage();
        spinnerLanguage.setSelection(currentLang.equals("tr") ? 1 : 0);

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = (position == 1) ? "tr" : "en";
                if (!selectedLang.equals(prefManager.getLanguage())) {
                    prefManager.setLanguage(selectedLang);
                    setLocale(selectedLang);
                    recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // --- Color Theme Spinner ---
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.color_theme_options,
                android.R.layout.simple_spinner_item
        );
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColorTheme.setAdapter(colorAdapter);

        int savedColorIndex = prefManager.getInt("theme_color_index", 0);
        spinnerColorTheme.setSelection(savedColorIndex);

        spinnerColorTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != savedColorIndex) {
                    prefManager.setInt("theme_color_index", position);

                    // Restart MainActivity to apply the color theme
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setLocale(String langCode) {
        java.util.Locale locale = new java.util.Locale(langCode);
        java.util.Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
