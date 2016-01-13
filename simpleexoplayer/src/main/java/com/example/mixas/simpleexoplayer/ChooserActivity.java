package com.example.mixas.simpleexoplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ChooserActivity extends AppCompatActivity {

    ListView lView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        lView = (ListView) findViewById(R.id.chooser_list);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item,R.id.video_path, Samples.exmaples);
        lView.setAdapter(adapter);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  videoUri = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class).putExtra(Intent.EXTRA_TEXT, videoUri);
                startActivity(intent);
            }
        });
    }
}
