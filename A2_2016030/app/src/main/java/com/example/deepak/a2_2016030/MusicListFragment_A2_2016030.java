package com.example.deepak.a2_2016030;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment_A2_2016030 extends Fragment{
    public MusicListFragment_A2_2016030() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        final Field[] fields = R.raw.class.getFields();
        String[] songNames = new String[fields.length];

        for (int i = 0 ; i < fields.length ; ++i) {
            songNames[i] = fields[i].getName().replace("_", " ");
        }

        ListView listView = view.findViewById(R.id.musicList);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                songNames
        );

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity_A2_2016030.musicService.playSong(fields[position], position, fields.length);
            }
        });

        return view;
    }

}
