package com.android.citybustickets;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NFCActivity extends AppCompatActivity implements
        NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;

    TextView tvNFCContent;
    EditText message;
    Button btnWrite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_text_plain_tag);
        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tvNFCContent = (TextView) findViewById(R.id.tv_message);
        message = (EditText) findViewById(R.id.et_text_plain);
        btnWrite = (Button) findViewById(R.id.btn_main_menu);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callingHomeIntent = new Intent(NFCActivity.this, MainActivity.class);
                callingHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callingHomeIntent);
                finish();
            }
        });
        message.setText("" + getIntent().getExtras().getString("payment_id"));
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(NFCActivity.this,
                    "nfcAdapter==null, no NFC adapter exists",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(NFCActivity.this,
                    "Set Callback(s)",
                    Toast.LENGTH_LONG).show();
            nfcAdapter.setNdefPushMessageCallback(this, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
//        btnWrite.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if(myTag ==null) {
//                        Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
//                    } else {
//                        write(message.getText().toString(), myTag);
//                        Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG ).show();
//                    }
//                } catch (IOException e) {
//                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
//                    e.printStackTrace();
//                } catch (FormatException e) {
//                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (nfcAdapter == null) {
//            // Stop here, we definitely need NFC
//            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
//            finish();
//        }
//        readFromIntent(getIntent());
//
//        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
//        writeTagFilters = new IntentFilter[] { tagDetected };
    }


    //    /******************************************************************************
//     **********************************Read From NFC Tag***************************
//     ******************************************************************************/
//    private void readFromIntent(Intent intent) {
//        String action = intent.getAction();
//        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
//                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
//                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
//            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//            NdefMessage[] msgs = null;
//            if (rawMsgs != null) {
//                msgs = new NdefMessage[rawMsgs.length];
//                for (int i = 0; i < rawMsgs.length; i++) {
//                    msgs[i] = (NdefMessage) rawMsgs[i];
//                }
//            }
//            buildTagViews(msgs);
//        }
//    }
//    private void buildTagViews(NdefMessage[] msgs) {
//        if (msgs == null || msgs.length == 0) return;
//
//        String text = "";
////        String tagId = new String(msgs[0].getRecords()[0].getType());
//        byte[] payload = msgs[0].getRecords()[0].getPayload();
//        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
//        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
//        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
//
//        try {
//            // Get the Text
//            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
//        } catch (UnsupportedEncodingException e) {
//            Log.e("UnsupportedEncoding", e.toString());
//        }
//
//        tvNFCContent.setText("NFC Content: " + text);
//    }
//
//
//    /******************************************************************************
//     **********************************Write to NFC Tag****************************
//     ******************************************************************************/
//    private void write(String text, Tag tag) throws IOException, FormatException {
//        NdefRecord[] records = { createRecord(text) };
//        NdefMessage message = new NdefMessage(records);
//        // Get an instance of Ndef for the tag.
//        Ndef ndef = Ndef.get(tag);
//        // Enable I/O
//        ndef.connect();
//        // Write the message
//        ndef.writeNdefMessage(message);
//        // Close the connection
//        ndef.close();
//    }
//    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
//        String lang       = "en";
//        byte[] textBytes  = text.getBytes();
//        byte[] langBytes  = lang.getBytes("US-ASCII");
//        int    langLength = langBytes.length;
//        int    textLength = textBytes.length;
//        byte[] payload    = new byte[1 + langLength + textLength];
//
//        // set status byte (see NDEF spec for actual bits)
//        payload[0] = (byte) langLength;
//
//        // copy langbytes and textbytes into payload
//        System.arraycopy(langBytes, 0, payload, 1,              langLength);
//        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);
//
//        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);
//
//        return recordNFC;
//    }
//
//
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//        readFromIntent(intent);
//        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
//            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        }
//    }
//
//    @Override
//    public void onPause(){
//        super.onPause();
//        WriteModeOff();
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        WriteModeOn();
//    }
//
//
//
//    /******************************************************************************
//     **********************************Enable Write********************************
//     ******************************************************************************/
//    private void WriteModeOn(){
//        writeMode = true;
//        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
//    }
//    /******************************************************************************
//     **********************************Disable Write*******************************
//     ******************************************************************************/
//    private void WriteModeOff(){
//        writeMode = false;
//        nfcAdapter.disableForegroundDispatch(this);
//    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null && action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] parcelables =
                    intent.getParcelableArrayExtra(
                            NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord NdefRecord_0 = inNdefRecords[0];
            String inMsg = new String(NdefRecord_0.getPayload());
            tvNFCContent.setText(inMsg);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent callingHomeIntent = new Intent(NFCActivity.this, MainActivity.class);
                callingHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callingHomeIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {

        final String eventString = "onNdefPushComplete\n" + event.toString();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),
                        eventString,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        String stringOut = message.getText().toString();
        byte[] bytesOut = stringOut.getBytes();

        NdefRecord ndefRecordOut = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA,
                "text/plain".getBytes(),
                new byte[]{},
                bytesOut);

        NdefMessage ndefMessageout = new NdefMessage(ndefRecordOut);
        return ndefMessageout;
    }
}
