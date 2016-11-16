package com.android.citybustickets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PaymentContactActivity extends AppCompatActivity {

    private EditText mNameEditText, mPhoneNumberEditText, mAgeEditText, mEmailEditText;
    private Button mPayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_contact);
        initiateViews();
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
                if (mNameEditText.getText().toString() != null && mNameEditText.getText().toString().length() != 0 && mNameEditText.getText().toString().length() > 5) {
                    if (isValidEmail(mEmailEditText.getText())) {
                        if (mPhoneNumberEditText.getText().toString() != null && mPhoneNumberEditText.getText().toString().length() != 0 && mPhoneNumberEditText.getText().toString().length() == 10) {
                            if (mAgeEditText.getText().toString() != null && mAgeEditText.getText().toString().length() != 0) {
                                Toast.makeText(PaymentContactActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                                Intent callingHomeIntent = new Intent(PaymentContactActivity.this, MainActivity.class);
                                callingHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callingHomeIntent);
                                finish();
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
