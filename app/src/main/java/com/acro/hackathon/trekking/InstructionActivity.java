package com.acro.hackathon.trekking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class InstructionActivity extends AppCompatActivity {
    String type;
    TextView textView,discription;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        Intent i=getIntent();

        type=i.getStringExtra("type");

        textView=(TextView)findViewById(R.id.title);
        discription=(TextView)findViewById(R.id.discription);
        imageView=(ImageView)findViewById(R.id.imageView);

        textView.setText(type);

        if(type.equals("Do's and Don'ts")) {

            discription.setText("");
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
        if(type.equals("Kit")) {
            discription.setText(" ");
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
        if(type.equals("Procedure")) {
            discription.setText(" ");
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
        if(type.equals("BaseCamp Information")) {
            discription.setText(" ");
            imageView.setImageResource(R.mipmap.ic_launcher);
        }



    }
}
