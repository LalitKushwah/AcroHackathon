package com.acro.hackathon.trekking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        discription=(TextView)findViewById(R.id.description);
        imageView=(ImageView)findViewById(R.id.imageView);

        textView.setText(type);

        if(type.equals("Do's and Don'ts")) {

            discription.setText("                                             Do's \n\n\n 1) Firstly, although we have a weight limit of 9 kg for backpacks \n\n" +
                    "2) Every time you offload a backpack, we take in an additional porter/mule. With that comes the need for extra resources which results in excess waste " +
                    "generation.  Hence, I recommend to give it a serious thought before choosing to offload your backpack.\n\n " +
                    "3) Many trekkers find it inconvenient to wash their own utensils. Some bring disposable items for the trek.  Although we insist on reusable water " +
                    "bottles, use and throw bottles are frequently used by many,so kindly bring your own utensils.\n\n" +
                    "4) Finding places to properly dispose waste.\n\n" +
                    "5) Women trekkers must use sanitary napkins in zip locks and take them back to the cities." +
                    "\n\n\n                                             Dont's \n\n  You should not bring your food from,If necessary you may bring them in zip locks or" +
                    " reusable boxes or you can by from locals \n\n" +
                    "2) Trekkers are excited to find Maggi there. I won’t deny how comforting it is to have warm Maggi after a cold and hard climb. But " +
                    "it is precisely after such a strenuous day that you need to avoid Maggi because indigestion is a common ailment.\n\n" +
                    "3) The rivers and streams you cross are the source of drinking water for thousands. Those in the valley directly consume " +
                    "this water without filtering. So, peeing and washing in the stream is a big no-no \n\n" +
                    "4) Trekking is all enjoying nature and surviving with minimal requirements. So adopt that idea and imbibe it. Even " +
                    "recyclable and organic waste is waste. So let’s avoid wasting.\n\n" +
                    "5) We don’t belong in the mountains. We are visitors. Animals and birds often shun away from the noises and change their behavioral patterns.  So let’s make" +
                    " it a point to not disturb our hosts and enjoy them in their natural setting."
            );
            imageView.setImageResource(R.drawable.tr1);
        }
        if(type.equals("Kit")) {
            discription.setText("Being well equipped with essentials is mandatory for a pleasant and enthralling" +
                    " trekking tour. \n Here is the list of items you must bring along with you on your trekking tour:\n\n" +
                    "Waterproofed trekking boots\n\n" +
                    "Running shoes\n\n" +
                    "torch\n\n" +
                    "Down jacket or something like this that has a hood\n\n" +
                    "A sweater as the weather may get chilly\n\n" +
                    "Two cotton shirts\n\n" +
                    "A pair of woolen trousers\n\n" +
                    "Three pairs of underwear as you may wash them on the tour\n\n" +
                    "Wind-gear and rain-gear having a hood\n\n" +
                    "Sun hat\n\n" +
                    "Woolen hat\n\n" +
                    "Woolen gloves\n\n" +
                    "Cotton socks\n\n" +
                    "Woolen socks\n\n" +
                    "Personal first-aid kit including medication for common ailments such as" +
                    " headaches, dysentery etc., moleskin, antiseptic cream, surgical tape, " +
                    "band aids, sun cream and medicines\n\n" +
                    "Tablets to purify the water\n\n" +
                    "Toilet kit that includes extra towel, toilet paper and soap\n\n" +
                    "Torches with extra cells\n\n" +
                    "Sunglasses (It is wise to bring an extra pair)\n\n" +
                    "Water bottle that can hold sufficient water\n\n" +
                    "Pocketknife\n\n" +
                    "Swimsuit\n\n" +
                    "Plastic bags\n\n" +
                    "Weight daypack\n\n" +
                    "Waterproof Duffel bag\n\n" +
                    "Camera with many fully charged batteries\n\n" +
                    "Binoculars\n\n" +
                    "Umbrella\n\n" +
                    "Extra water bottle\n\n ");
            imageView.setImageResource(R.drawable.tr2);
        }
        if(type.equals("BaseCamp Information")) {
            discription.setText("BaseCamp is a place in which rescue team will be present for the safety purpose"+
                    "of the trekkers where, by client server thay will determine the current location of the trekkers");
            imageView.setImageResource(R.drawable.tr3);
        }



    }
}
