package com.android.citybustickets;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

public class WriteTextPlainTagActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = WriteTextPlainTagActivity.class.getSimpleName();

    private EditText mTextPlainEditText;

    private String inputTextPlain;

    private NfcAdapter mNfcAdapter;

    private PendingIntent mPendingIntent;

    private IntentFilter[] mFilters;

    private String[][] mTechLists;

    private Locale locale = Locale.getDefault();
    boolean writeMode;
    Tag mytag;
    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_text_plain_tag);
        prepareNfcAdapter();
        fillUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String temp = inputTextPlain;
        if (temp != null) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
            byte[] textBytes = inputTextPlain.getBytes(Charset.forName("US-ASCII"));
            byte[] payload = new byte[langBytes.length + textBytes.length + 1];
            payload[0] = (byte) langBytes.length;

            System.arraycopy(langBytes, 0, payload, 1, langBytes.length);
            System.arraycopy(textBytes, 0, payload, langBytes.length + 1, textBytes.length);

            NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
            NdefMessage newMessage = new NdefMessage(new NdefRecord[]{textRecord});

            writeNdefMessageToTag(newMessage, tag);
        }
//        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
//            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            Toast.makeText(this, "Ok Detection" + mytag.toString(), Toast.LENGTH_LONG ).show();
//        }
    }

    private void prepareNfcAdapter() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        mFilters = new IntentFilter[]{ndef};
        mTechLists = new String[][]{new String[]{Ndef.class.getName()},
                new String[]{NdefFormatable.class.getName()}};
    }

    private void fillUi() {
        mTextPlainEditText = (EditText) findViewById(R.id.et_text_plain);
        mTextPlainEditText.setText(""+getIntent().getExtras().getString("payment_id"));
        Button saveTagBtn = (Button) findViewById(R.id.btn_save_tag);
        Button mainMenuBtn = (Button) findViewById(R.id.btn_main_menu);
        saveTagBtn.setOnClickListener(this);
        mainMenuBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_tag:
                saveTag();
                break;
            case R.id.btn_main_menu:
                Intent callingHomeIntent = new Intent(WriteTextPlainTagActivity.this, MainActivity.class);
                callingHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callingHomeIntent);
                finish();
                break;
        }
    }

    private void saveTag() {
        inputTextPlain = mTextPlainEditText.getText().toString();
//        try {
//            if(mytag==null){
//                Toast.makeText(ctx, "Error Detected", Toast.LENGTH_LONG ).show();
//            }else{
//                write(inputTextPlain,mytag);
//                Toast.makeText(ctx, "Ok Writing", Toast.LENGTH_LONG ).show();
//            }
//        } catch (IOException e) {
//            Toast.makeText(ctx, "Error Writing", Toast.LENGTH_LONG ).show();
//            e.printStackTrace();
//        } catch (FormatException e) {
//            Toast.makeText(ctx, "Error Writing" , Toast.LENGTH_LONG ).show();
//            e.printStackTrace();
//        }
        if (inputTextPlain.isEmpty()) {
            Toast.makeText(this, "Please write something to register a tag", Toast.LENGTH_LONG).show();
        } else {
            TextView mMessageTextView = (TextView) findViewById(R.id.tv_message);
            mMessageTextView.setText("Touch NFC Tag to write the text : \n" + inputTextPlain);
        }
    }

    private void writeNdefMessageToTag(NdefMessage message, Tag detectedTag) {
        if (!writeNdef(message, detectedTag)) {
            formatNdef(message, detectedTag);
        }
    }

    private boolean writeNdef(NdefMessage message, Tag detectedTag ) {
        Ndef ndef = Ndef.get(detectedTag);
        if (ndef == null) {
            return false;
        }
        int size = message.toByteArray().length;
        try {
            ndef.connect();
            if (!ndef.isWritable()) {
                Toast.makeText(this, "Tag is read-only", Toast.LENGTH_SHORT).show();
            }
            if (ndef.getMaxSize() < size) {
                Toast.makeText(this, "The data cannot written to tag, Tag capacity is "
                                + ndef.getMaxSize() + " bytes, message is "
                                + size + "bytes",
                        Toast.LENGTH_SHORT).show();
            }
            ndef.writeNdefMessage(message);
            ndef.close();
            Toast.makeText(this, "Message is written tag.", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    private void formatNdef(NdefMessage message, Tag detectedTag) {
        NdefFormatable ndefFormat = NdefFormatable.get(detectedTag);
        if (ndefFormat != null) {
            try {
                ndefFormat.connect();
                ndefFormat.format(message);
                ndefFormat.close();
                Toast.makeText(this, "The data is written to the tag", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to format tag", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage(), e);
            }
        } else {
            Toast.makeText(this, "NDEF is not supported", Toast.LENGTH_SHORT).show();
        }
    }
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {

        //create the message in according with the standard
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;

        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
        return recordNFC;
    }

    private void write(String text, Tag tag) throws IOException, FormatException {

        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
    }
}

