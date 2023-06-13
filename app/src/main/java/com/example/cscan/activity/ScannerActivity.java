package com.example.cscan.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cscan.R;
import com.otaliastudios.cameraview.CameraView;

public class ScannerActivity extends AppCompatActivity {
    public CameraView cameraView;
    private static final String FRAGMENT_DIALOG = "dialog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_scanner);
        cameraView = (CameraView) findViewById(R.id.cameraView);
        cameraView.setLifecycleOwner(this);

    }
    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED) {
            cameraView.open();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.CAMERA")) {
            ConfirmationDialogFragment.newInstance(R.string.camera_permission_confirmation, new String[]{"android.permission.CAMERA"}, 1, R.string.camera_permission_not_granted).show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 1);
        }


    }
    public static class ConfirmationDialogFragment extends DialogFragment {
        private static final String ARG_MESSAGE = "message";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";

        public static ConfirmationDialogFragment newInstance(int i, String[] strArr, int i2, int i3) {
            ConfirmationDialogFragment confirmationDialogFragment = new ConfirmationDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_MESSAGE, i);
            bundle.putStringArray(ARG_PERMISSIONS, strArr);
            bundle.putInt(ARG_REQUEST_CODE, i2);
            bundle.putInt(ARG_NOT_GRANTED_MESSAGE, i3);
            confirmationDialogFragment.setArguments(bundle);
            return confirmationDialogFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle bundle) {
            final Bundle arguments = getArguments();
            return new AlertDialog.Builder(getActivity()).setMessage(arguments.getInt(ARG_MESSAGE)).setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String[] stringArray = arguments.getStringArray(ConfirmationDialogFragment.ARG_PERMISSIONS);
                    if (stringArray != null) {
                        ActivityCompat.requestPermissions(ConfirmationDialogFragment.this.getActivity(), stringArray, arguments.getInt(ConfirmationDialogFragment.ARG_REQUEST_CODE));
                        return;
                    }
                    throw new IllegalArgumentException();
                }
            }).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(ConfirmationDialogFragment.this.getActivity(), arguments.getInt(ConfirmationDialogFragment.ARG_NOT_GRANTED_MESSAGE), Toast.LENGTH_SHORT).show();
                }
            }).create();
        }
    }
}