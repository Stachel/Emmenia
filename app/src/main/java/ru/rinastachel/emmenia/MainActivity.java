package ru.rinastachel.emmenia;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import ru.rinastachel.emmenia.fragments.MainFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.container, MainFragment.getInstance(), "MainFragment");
            transaction.commit();
        }
    }
}
