package ru.rinastachel.emmenia.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.rinastachel.emmenia.R;

public class AddFragment extends Fragment{

    public static AddFragment getInstance() {
        return new AddFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        return view;
    }
}
