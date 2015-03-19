package ru.rinastachel.emmenia;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import ru.rinastachel.emmenia.fragments.PrefFragment;

public class PrefActivity extends FragmentActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar bar = getActionBar();

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
