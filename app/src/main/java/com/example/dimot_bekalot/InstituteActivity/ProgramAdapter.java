package com.example.dimot_bekalot.InstituteActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dimot_bekalot.R;

import java.util.Vector;

public class ProgramAdapter extends ArrayAdapter<String> {
    Context context;
    Vector<String> hourQueue;
    Vector<String> patientID;

    public ProgramAdapter(Context context, Vector<String> hourQueue, Vector<String> patientID) {
        super(context, R.id.hour_watch_queue, R.id.numberID_watch_queue);
        this.context = context;
        this.hourQueue = hourQueue;
        this.patientID = patientID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View singleItem = convertView;
        Progr
        return super.getView(position, convertView, parent);
    }
}
