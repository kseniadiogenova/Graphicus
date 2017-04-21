package com.gui.graphicus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;


public class EquationsFragment extends Fragment {
    private LinearLayout eqList;
    private View view;
    private Function function;
    private EditText editText;
    private GraphicFragment grFrag;
    private  boolean exc;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        function = new Function();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.equations, container, false);
        eqList = (LinearLayout) view.findViewById(R.id.list);

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.equ_layout , null);
        eqList.addView(layout);
        editText = (EditText) layout.getChildAt(1);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setBackgroundResource(R.color.background);
                exc = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;

    }

    public void setGraphicFragment(GraphicFragment gf){
        grFrag = gf;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(editText != null) if(exc)editText.setBackgroundResource(R.color.warning);

    }

    @Override
    public void onPause(){
        super.onPause();
        String expr = editText.getText().toString();
        if(expr != null && !expr.equals(""))
        try {
            java.util.Map<String, Double> map = new TreeMap();
            map.put("x", 1.0);
            function.setVars(map);
            function.parce(expr);
            //теперь есть выражение, надо посчитать точки
            map.clear();
            Map.Entry<String, Double> entry;
            LineGraphSeries<DataPoint> data= new LineGraphSeries<>();
            for(double x = -10.0; x<10.0; x+=0.1){
                map.put("x", x);
                Double y = function.getFunctionValue();
                DataPoint dp = new DataPoint(x,y);
                data.appendData(dp,true,2000);

            }
            if(data != null) grFrag.setSeries(data);

        } catch (Exception e) {
            editText.setBackgroundResource(R.color.warning);
            editText.clearFocus();
            exc = true;
        }
    }
}
