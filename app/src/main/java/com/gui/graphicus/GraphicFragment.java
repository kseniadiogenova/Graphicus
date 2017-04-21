package com.gui.graphicus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class GraphicFragment extends Fragment {
    private GraphView grView;
    private LineGraphSeries<DataPoint> series;
    @Override
    @Nullable
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.graphics, container, false);
        grView = (GraphView) v.findViewById(R.id.graph);
        grView.getViewport().setMinX(-10.0);
        grView.getViewport().setMaxX(10.0);
        grView.getViewport().setMinY(0.0);
        grView.getViewport().setXAxisBoundsManual(true);
        return v;

    }

    @Override
    public void onResume(){

        super.onResume();
        //grView.setTitle("resumed");
        if(series != null) grView.addSeries(series);
        grView.refreshDrawableState();
    }

    public void setSeries(LineGraphSeries<DataPoint> data){
        series = data;
    }
}
