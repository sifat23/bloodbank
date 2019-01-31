package com.example.namikaze.blooddonate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count-1 : count;
    }
}
