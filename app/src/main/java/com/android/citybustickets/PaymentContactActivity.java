package com.android.citybustickets;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.citybustickets.rqcode.QRCode;

public class PaymentContactActivity extends AppCompatActivity {

    private EditText mNameEditText, mPhoneNumberEditText, mAgeEditText, mEmailEditText;
    private Button mPayButton;
//    private final static String TAG = MainActivity.class.getSimpleName();
//
//    private NfcAdapter mNfcAdapter;
//
//    private PendingIntent mPendingIntent;
//
//    private IntentFilter[] mFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initiateViews();
//        prepareNfcAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkIfNfcEnabled();
//        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
//        Log.d(TAG, "New intent");
//        getTag(intent);
    }

//    private void prepareNfcAdapter() {
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        mPendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        try {
//            tagIntentFilter.addDataType("text/plain");
//            mFilters = new IntentFilter[]{tagIntentFilter};
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//
//
//    private void checkIfNfcEnabled() {
//        if (!mNfcAdapter.isEnabled()) {
//            Toast.makeText(this, "Enable NFC before using the app", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private void getTag(Intent intent) {
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//            Log.d(TAG, "Action NDEF Found");
//            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            if (tag == null) {
//                return;
//            }
//            Ndef ndef = Ndef.get(tag);
//            try {
//                ndef.connect();
//                NdefMessage ndefMessage = ndef.getNdefMessage();
//                NdefRecord[] ndefRecords = ndefMessage.getRecords();
//                for (NdefRecord ndefRecord : ndefRecords) {
//                    byte[] payload = ndefRecord.getPayload();
//                    String message = new String(payload, "UTF-8").substring(3);
//                    Toast.makeText(this, "The message \n\""
//                            + message
//                            + "\"\n is now written on the tag", Toast.LENGTH_LONG).show();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage(), e);
//            }
//        }
//    }

    private void writeTextPlainTag() {
        Intent intent = new Intent(PaymentContactActivity.this, WriteTextPlainTagActivity.class);
        startActivity(intent);
    }

    public void initiateViews() {
        mNameEditText = (EditText) findViewById(R.id.contact_details_name);
        mPhoneNumberEditText = (EditText) findViewById(R.id.contact_details_phone);
        mAgeEditText = (EditText) findViewById(R.id.contact_details_age);
        mEmailEditText = (EditText) findViewById(R.id.contact_details_email);
        mPayButton = (Button) findViewById(R.id.pay_button);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNameEditText.getText().toString() != null && mNameEditText.getText().toString().length() != 0 && mNameEditText.getText().toString().length() > 3) {
                    if (isValidEmail(mEmailEditText.getText())) {
                        if (mPhoneNumberEditText.getText().toString() != null && mPhoneNumberEditText.getText().toString().length() != 0 && mPhoneNumberEditText.getText().toString().length() == 10) {
                            if (mAgeEditText.getText().toString() != null && mAgeEditText.getText().toString().length() != 0) {
                                Toast.makeText(PaymentContactActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                                Bitmap myBitmap = QRCode.from("CBTPAYMENT").bitmap();
                                Dialog qrCode = new Dialog(PaymentContactActivity.this);
                                qrCode.setCanceledOnTouchOutside(false);
                                qrCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                qrCode.setContentView(R.layout.dialog_qr_code);
                                ImageView myImage = (ImageView) qrCode.findViewById(R.id.imageView);
                                Button close = (Button) qrCode.findViewById(R.id.close_button);
                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent callingHomeIntent = new Intent(PaymentContactActivity.this, MainActivity.class);
                                        callingHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(callingHomeIntent);
                                        finish();
                                    }
                                });
                                myImage.setImageBitmap(myBitmap);
                                qrCode.show();

//
//                                writeTextPlainTag();
                            } else {
                                Toast.makeText(PaymentContactActivity.this, "Please Enter A Valid Age", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PaymentContactActivity.this, "Please Enter A Valid Mobile Number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PaymentContactActivity.this, "Please Enter A Valid Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PaymentContactActivity.this, "Please Enter A Valid Name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
