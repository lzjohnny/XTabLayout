package com.bug95.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BlankFragment extends Fragment {

    private CharSequence title;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.title = args.getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_blank, null);
        TextView textView = (TextView) inflate.findViewById(R.id.fragment_text);
        textView.setText(title);
        return inflate;
    }
}
