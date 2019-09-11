package com.example.vijay.musicplayer;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class allMusic extends Fragment {

    ListView musicList;
    ArrayAdapter<String> musicArrayAdapter;
    String songs[];
    ArrayList<File> musics;
    boolean flag_loading=false;

    public allMusic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_music, container, false);

        musicList = view.findViewById(R.id.musicList);

        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                musics = findMusicFiles(Environment.getExternalStorageDirectory());
                songs = new String[musics.size()];

                for(int i=0;i<musics.size();i++){
                    songs[i] = musics.get(i).getName();

                }

                musicArrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,songs);
                musicList.setAdapter(musicArrayAdapter);

                musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(),player.class);
                        intent.putExtra("songFileList",musics);
                        intent.putExtra("position",position);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

        return view;
    }

    private ArrayList<File> findMusicFiles(File file) {
        ArrayList<File> allMusicFilesObject = new ArrayList<>();
        File[] files = file.listFiles();

        for (File currentFile : files) {
            if (currentFile.isDirectory() && !currentFile.isHidden()) {
                allMusicFilesObject.addAll(findMusicFiles(currentFile));
            } else {
                if(currentFile.getName().endsWith(".mp3") || currentFile.getName().endsWith(".mp4a") || currentFile.getName().endsWith(".wav"))
                allMusicFilesObject.add(currentFile);
            }

        }

        return allMusicFilesObject;
    }

}

