package com.monim.onlineattendance.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.monim.onlineattendance.R;
import com.monim.onlineattendance.databinding.ActivityAttendanceBinding;
import com.monim.onlineattendance.models.PostAttendance;
import com.monim.onlineattendance.models.PostAttendanceResponse;
import com.monim.onlineattendance.utils.AppUtils;
import com.monim.onlineattendance.view_models.AttendanceViewModel;

public class AttendanceActivity extends AppCompatActivity {

    private AttendanceViewModel viewModel;

    private LocationManager locationManager;
    private final int LOCATION_REFRESH_TIME = 5000;
    private final int LOCATION_REFRESH_DISTANCE = 10;

    private final int LOCATION_PERMISSION = 1;
    private final int LOCATION_PERMISSION_SETTINGS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestLocationPermission();

        viewModel = ViewModelProviders.of(this).get(AttendanceViewModel.class);
        ActivityAttendanceBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_attendance);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        setTitle("Attendance");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setAttendanceObserver();
    }

    private void setAttendanceObserver() {
        viewModel.getAttendanceData().observe(this, new Observer<PostAttendance>() {
            @Override
            public void onChanged(PostAttendance postAttendance) {
                hideKeyboard();
                if (!AppUtils.isInternetEnabled(AttendanceActivity.this)) {
                    AppUtils.showToast(AttendanceActivity.this, getString(R.string.no_internet_toast), false);
                    return;
                }

                if (!AppUtils.isGpdEnabled(AttendanceActivity.this)) {
                    showAlert(getString(R.string.enable_gps), false);
                    return;
                }

                if (ActivityCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestLocationPermission();
                    return;
                }

                if (postAttendance.isUserNameEmpty()) {
                    AppUtils.showToast(AttendanceActivity.this, getString(R.string.no_user_name), false);
                    return;
                }

                if (postAttendance.isUserIdEmpty()) {
                    AppUtils.showToast(AttendanceActivity.this, getString(R.string.no_user_id), false);
                    return;
                }

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastKnownLocation == null) {
                    AppUtils.showToast(AttendanceActivity.this, getString(R.string.no_location_found), true);
                    return;
                }

                postAttendance.setLatitude(String.valueOf(lastKnownLocation.getLatitude()));
                postAttendance.setLongitude(String.valueOf(lastKnownLocation.getLongitude()));
                postAttendanceData(postAttendance);
            }
        });
    }

    private void postAttendanceData(PostAttendance postAttendance) {
        viewModel.getResponseData(postAttendance, this).observe(this, new Observer<PostAttendanceResponse>() {
            @Override
            public void onChanged(PostAttendanceResponse postAttendanceResponse) {
                if (postAttendanceResponse != null) {
                    AppUtils.showToast(AttendanceActivity.this, postAttendanceResponse.getUserMessage(), true);
                    finish();
                }
            }
        });
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                requestToEnableGPS();

            } else {
                showAlert(getString(R.string.accept_permission), true);
            }
        }
    }

    private void requestToEnableGPS() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    initLocationManager();

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(
                                        AttendanceActivity.this,
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
    }

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LocationRequest.PRIORITY_HIGH_ACCURACY) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("onActivityResult", "onActivityResult: GPS Enabled by user");

                initLocationManager();

            } else {
                showAlert(getString(R.string.enable_gps), false);
            }

        } else if (requestCode == LOCATION_PERMISSION_SETTINGS) {
            requestLocationPermission();
        }
    }

    private void initLocationManager() {
        if (ActivityCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            AppUtils.showToast(this, "Null", true);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE,
                listener);
    }

    private void showAlert(String message, boolean isPermission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton(isPermission ? "Settings" : "Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (isPermission)
                    openPermissionPage();
                else
                    requestToEnableGPS();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openPermissionPage() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, LOCATION_PERMISSION_SETTINGS);
    }

    private void hideKeyboard() {
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (methodManager != null) {
            View view = getCurrentFocus();
            if (view != null)
                methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}