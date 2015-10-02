package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by bora on 02.10.2015.
 */
public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    public static final String TAG = ScannerActivity.class.getSimpleName();
    public static final String BARCODE_TEXT = "barcodeText";
    public static final String BARCODE_TYPE = "barcodeType";


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        //OK
        Intent returnIntent = new Intent();
        returnIntent.putExtra(BARCODE_TEXT,rawResult.getText());
        returnIntent.putExtra(BARCODE_TYPE,rawResult.getBarcodeFormat().toString());
        setResult(RESULT_OK, returnIntent);
        finish();

        //CANCEL
        /*Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();*/

    }


}