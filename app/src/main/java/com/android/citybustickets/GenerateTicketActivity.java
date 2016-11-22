package com.android.citybustickets;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import net.glxn.qrgen.android.QRCode;

public class GenerateTicketActivity extends AppCompatActivity {

    Button nfcBut, qrBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_ticket);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        qrBut = (Button) findViewById(R.id.qr_button);
        nfcBut = (Button) findViewById(R.id.nfc_button);
        nfcBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeTextPlainTag("CBTPAYMENT");
            }
        });
        qrBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap myBitmap = QRCode.from("CBTPAYMENT").bitmap();

                Dialog qrCode = new Dialog(GenerateTicketActivity.this);
                qrCode.setCanceledOnTouchOutside(false);
                qrCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
                qrCode.setContentView(R.layout.dialog_qr_code);
                ImageView myImage = (ImageView) qrCode.findViewById(R.id.rq_code);
                Button close = (Button) qrCode.findViewById(R.id.close_button);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callingHomeIntent = new Intent(GenerateTicketActivity.this, MainActivity.class);
                        callingHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callingHomeIntent);
                        finish();
                    }
                });
                myImage.setImageBitmap(myBitmap);
                qrCode.show();
            }
        });

    }

    private void writeTextPlainTag(String payment) {
        Intent intent = new Intent(GenerateTicketActivity.this, WriteTextPlainTagActivity.class);
        intent.putExtra("payment_id", payment);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
