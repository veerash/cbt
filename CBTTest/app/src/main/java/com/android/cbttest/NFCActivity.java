package com.android.cbttest;



import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.cbttest.R;


public class NFCActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    private NfcAdapter mNfcAdapter;

    private PendingIntent mPendingIntent;

    private IntentFilter[] mFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        prepareNfcAdapter();
        fillUi();
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
    protected void onResume() {
        super.onResume();
        if(mNfcAdapter!=null) {
            checkIfNfcEnabled();
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, null);
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!=null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "New intent");
        getTag(intent);
    }

    private void prepareNfcAdapter() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            tagIntentFilter.addDataType("text/plain");
            mFilters = new IntentFilter[]{tagIntentFilter};
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void fillUi() {
        Button writeTextPlainTagBtn = (Button) findViewById(R.id.btn_write_text_plain_tag);
        Button quitBtn = (Button) findViewById(R.id.btn_quit);
        writeTextPlainTagBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_write_text_plain_tag:
//                writeTextPlainTag();
                break;
            case R.id.btn_quit:
                finish();
                break;
        }
    }

    private void checkIfNfcEnabled() {
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "Enable NFC before using the app", Toast.LENGTH_LONG).show();
        }
    }

    private void getTag(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Log.d(TAG, "Action NDEF Found");
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag == null) {
                return;
            }
            Ndef ndef = Ndef.get(tag);
            try {
                ndef.connect();
                NdefMessage ndefMessage = ndef.getNdefMessage();
                NdefRecord[] ndefRecords = ndefMessage.getRecords();
                for (NdefRecord ndefRecord : ndefRecords) {
                    byte[] payload = ndefRecord.getPayload();
                    String message = new String(payload, "UTF-8").substring(3);
                    Toast.makeText(this, "The message \n\""
                            + message
                            + "\"\n is now written on the tag", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

//    private void writeTextPlainTag() {
//        Intent intent = new Intent(MainActivity.this, WriteTextPlainTagActivity.class);
//        startActivity(intent);
//    }
}
