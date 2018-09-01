package com.example.deepak.a2_2016030;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment_A2_2016030 extends Fragment{
    public MusicListFragment_A2_2016030() {
        // Required empty public constructor
    }

    public static int rawSongCount = 0;

    public static ArrayList<String> songNames = new ArrayList<>();
    public static ArrayAdapter<String> listViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        final Field[] fields = R.raw.class.getFields();

        //String[] songNames = new String[fields.length];


        for (int i = rawSongCount ; i < fields.length ; ++i) {
            System.out.println("I got executed");
            songNames.add(fields[i].getName().replace("_", " "));
        }
        rawSongCount = fields.length;
        ListView listView = view.findViewById(R.id.musicList);

        listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                songNames
        );

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri songUri;
                if (position < rawSongCount) {
                    songUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + songNames.get(position).replace(" ", "_"));
                } else {
                    System.out.println("Music Activity " + getActivity().getApplicationContext().getFilesDir().getAbsolutePath());
                    songUri = Uri.parse("/data/data/com.example.deepak.a2_2016030/files" + "/" + songNames.get(position).replace(" ", "_") + ".mp3");

                }
                MainActivity_A2_2016030.musicService.playSong(songUri, position, fields.length);

            }
        });

        return view;
    }



}
