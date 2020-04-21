package com.example.smarthomeiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    ToggleButton btn1;
    ToggleButton btn2;

    TextView jarak;
    TextView ldr;

    String valueJarak;
    String valueLDR;
    String valueLampu1;
    String valueLampu2;

    DatabaseReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (ToggleButton) findViewById(R.id.btn1);
        btn2 = (ToggleButton) findViewById(R.id.btn2);

        jarak = (TextView) findViewById(R.id.jarak);
        ldr = (TextView) findViewById((R.id.ldr));

        dref = FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                valueJarak = dataSnapshot.child("Node1/distance").getValue().toString();
                jarak.setText("Jarak : "+valueJarak);
                valueLDR = dataSnapshot.child("Node1/ldr").getValue().toString();
                ldr.setText("Nilai LDR : "+valueLDR);

                valueLampu1 = dataSnapshot.child("Node1/ldr").getValue().toString();
                if(Integer.parseInt(valueLampu1) <= 500)
                    btn1.setChecked(false);
                else
                    btn1.setChecked(true);

                valueLampu2 = dataSnapshot.child("Node1/distance").getValue().toString();
                if(Integer.parseInt(valueLampu2) > 10 || Integer.parseInt(valueLampu2) == -1)
                    btn2.setChecked(false);
                else
                    btn2.setChecked(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference lampu1Ref = FirebaseDatabase.getInstance().getReference("Node1/led1");
                    lampu1Ref.setValue(1);
                }
                else
                {
                    DatabaseReference lampu1Ref = FirebaseDatabase.getInstance().getReference("Node1/led1");
                    lampu1Ref.setValue(0);
                }
            }
        });

        btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference lampu2Ref = FirebaseDatabase.getInstance().getReference("Node1/led2");
                    lampu2Ref.keepSynced(true);
                    lampu2Ref.setValue(1);
                }
                else
                {
                    DatabaseReference lampu2Ref = FirebaseDatabase.getInstance().getReference("Node1/led2");
                    lampu2Ref.keepSynced(true);
                    lampu2Ref.setValue(0);
                }
            }
        });
    }
}
