package com.acro.hackathon.trekking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * Created by ps11 on 01/04/17.
 */

public class CustomSlide extends SlideFragment {
   // private Spinner trekSelect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.trek_select_screen, container, false);
        //trekSelect = (Spinner) view.findViewById(trekSelect);
//        ArrayList<String> spinnerArray =  new ArrayList<String>();
//        spinnerArray.add("Select Trek Route");
//        spinnerArray.add("item2");
//        setDataOnSpinner(spinnerArray, trekSelect);
        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.colorPrimary;
    }

    @Override
    public int buttonsColor() {
        return R.color.colorAccent;
    }

    @Override
    public boolean canMoveFurther() {
        return false;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "This is the last slide.";
    }


//    public void setDataOnSpinner(ArrayList<String> data, Spinner s) {
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data) {
//            @Override
//            public boolean isEnabled(int position) {
//                return position != 0;
//            }
//
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if (position == 0) {
//                    tv.setTextColor(Color.GRAY);
//                } else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        s.setAdapter(dataAdapter);
//    }
}
