package com.levimake.dicewarelite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ViewPassword extends AppCompatActivity {

    String password = "";
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            password = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        editText = (EditText) findViewById(R.id.password_display_text_view);
        editText.setText(password);

        if (String.valueOf(sharedPrefs.getBoolean("prefEditable",true)).equals("false")) {
            editText.setFocusable(false);
            editText.setClickable(false);
        }

    }

    public void copyButtonClick(View view) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", editText.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Password has been copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_pass_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.action_help:
                startActivity(new Intent(this, HelpActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (String.valueOf(sharedPrefs.getBoolean("prefEditable",true)).equals("true")) {
            editText.setFocusable(true);
            editText.setClickable(true);
        }
    }


}
