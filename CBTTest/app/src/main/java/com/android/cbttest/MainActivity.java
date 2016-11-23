package com.android.cbttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.android.cbttest.NFC1Activity;
import com.android.cbttest.R;

public class MainActivity extends AppCompatActivity {

    Button nfcBut,qrBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrBut=(Button)findViewById(R.id.qr_button);
        nfcBut=(Button)findViewById(R.id.nfc_button);
        nfcBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nfcintent=new Intent(MainActivity.this,NFC1Activity.class);
                startActivity(nfcintent);
            }
        });
        qrBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.addExtra("PROMPT_MESSAGE", "Position The QR Code Within The Box");
                integrator.initiateScan();
            }
        });

    }
    private String parseSaidFromQrText(String qrText) {
        // The format of the QR code is SAID=xxxxxxxxxx;MAC=yy:yy:yy:yy:yy:yy
        // We need just the SAID from this
        String[] fields = qrText.split("=|;");
        return fields.length > 1 ? fields[1] : "";
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            if (!TextUtils.isEmpty(scanContent)) {
                Toast.makeText(MainActivity.this,"QR Code: "+scanContent,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
