package com.aitc.mapapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private static final int PREFS_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeUI();
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
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, PrefsActivity.class);
                startActivityForResult(intent, PREFS_RESULT);
                return true;

            case R.id.more_data:
                showDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PREFS_RESULT:
                changeUI();
                break;
        }
    }

    void showDialog() {
        ADialogFragment newFragment = ADialogFragment.newInstance(R.string.hello_world);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void changeUI() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        MainActivityFragment frag = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        frag.setBackgroundColor(Color.parseColor(sharedPrefs.getString("color", "#FFFFFF")));

        setTitle(sharedPrefs.getString("username", getString(R.string.app_name)));
    }
}
