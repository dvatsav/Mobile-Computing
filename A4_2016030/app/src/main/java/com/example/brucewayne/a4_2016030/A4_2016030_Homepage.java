package com.example.brucewayne.a4_2016030;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

public class A4_2016030_Homepage extends AppCompatActivity {

    private static final long MIN_DIST_GPS = 2;
    private static final long MIN_TIME_GPS = 1000*5;
    private static final int SHAKE_THRESHOLD = 70;

    private SensorManager mSensorManager;
    private LocationManager mLocationManager;
    private Sensor mAccelerometer = null;
    private Sensor mGyroscope = null;
    private Sensor mProximity = null;
    private TextView accelerometerValues, gyroscopeValues, orientationValues, gpsValues, proximityValues;
    private ToggleButton accelerometerToggle, gyroscopeToggle, orientationToggle, gpsToggle, proximityToggle;
    private boolean orientationActive = true, accelerometerActive = true, gyroscopeActive = true, proximityActive = true, gpsActive = true;
    private long lastAccel = 0, lastGyro = 0, lastProx = 0, lastOrient = 0;
    private float lastX = 0, lastY = 0, lastZ = 0;
    private double lastAzimuth = 0, lastRoll = 0, lastPitch = 0;

    private float[] gravity = new float[3];
    private float[] geomag = new float[3];
    private float[] orientationVals = new float[3];
    private float[] inR = new float[16];

    private void initializeSensors() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else{
            Toast.makeText(this, "Accelerometer not Available", Toast.LENGTH_SHORT).show();
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
            mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        } else{
            Toast.makeText(this, "Gyroscope not Available", Toast.LENGTH_SHORT).show();
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        } else{
            Toast.makeText(this, "Magnetometer not Available", Toast.LENGTH_SHORT).show();
        }


        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
            mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        } else{
            Toast.makeText(this, "Proximity Sensor not Available", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page__a4_2016030);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        accelerometerValues = findViewById(R.id.id_values_accelerometer);
        gyroscopeValues = findViewById(R.id.id_values_gyroscope);
        orientationValues = findViewById(R.id.id_values_orientation);
        gpsValues = findViewById(R.id.id_values_gps);
        proximityValues = findViewById(R.id.id_values_proximity);
        accelerometerToggle = findViewById(R.id.id_accelerometer_toggle);
        gyroscopeToggle = findViewById(R.id.id_gyroscope_toggle);
        orientationToggle = findViewById(R.id.id_orientation_toggle);
        gpsToggle = findViewById(R.id.id_gps_toggle);
        proximityToggle = findViewById(R.id.id_proximity_toggle);

        initializeSensors();

        accelerometerToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    accelerometerActive = true;
                    //mSensorManager.registerListener(accelerometerSensor, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    accelerometerActive = false;
                    //mSensorManager.unregisterListener(accelerometerSensor);
                }
            }
        });

        gyroscopeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gyroscopeActive = true;
                    //mSensorManager.registerListener(gyrometerSensor, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    gyroscopeActive = false;
                    //mSensorManager.unregisterListener(gyrometerSensor);
                }
            }
        });

        orientationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    orientationActive = true;
                } else {
                    orientationActive = false;
                }
            }
        });

        gpsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (ActivityCompat.checkSelfPermission(A4_2016030_Homepage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(A4_2016030_Homepage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) A4_2016030_Homepage.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                    }
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_GPS, MIN_DIST_GPS, gpsSensor);
                } else {

                    mLocationManager.removeUpdates(gpsSensor);
                }
            }
        });

        proximityToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSensorManager.registerListener(proximitySensor, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    mSensorManager.unregisterListener(proximitySensor);
                }
            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(accelerometerSensor, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(gyrometerSensor, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(proximitySensor, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_GPS, MIN_DIST_GPS, gpsSensor);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(accelerometerSensor);
        mSensorManager.unregisterListener(gyrometerSensor);
        mSensorManager.unregisterListener(proximitySensor);
        mLocationManager.removeUpdates(gpsSensor);

    }

    SensorEventListener accelerometerSensor = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {


            A4_2016030_DbManagement dbHelper = new A4_2016030_DbManagement(A4_2016030_Homepage.this);



            long curTime = System.currentTimeMillis();
            long difference = curTime - lastAccel/1000000;

            float x = event.values[0], y = event.values[1], z = event.values[2];
            long timestamp = event.timestamp;

            float speed = Math.abs(x+y+z-lastX-lastY-lastZ) / difference;
            speed *= 10000000000000.0;
            System.out.println(speed);
            if (speed > SHAKE_THRESHOLD) {
                Toast.makeText(A4_2016030_Homepage.this, "Shake Detected", Toast.LENGTH_SHORT).show();
            }

            gravity = event.values.clone();

            SQLiteDatabase wdb = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            String display = "";
            if (accelerometerActive) {
                display = Float.toString(x) + "; " + Float.toString(y) + "; " + Float.toString(z);
                accelerometerValues.setText(display);


                values.put("X_ACCELERATION", Float.toString(x));
                values.put("Y_ACCELERATION", Float.toString(y));
                values.put("Z_ACCELERATION", Float.toString(z));
                values.put("TIMESTAMP", Long.toString(event.timestamp));

                wdb.insert(A4_2016030_DbManagement.TABLE_NAME_S1, null, values);
                values = new ContentValues();
            }


            if (gravity != null && geomag != null && orientationActive) {
                boolean success = SensorManager.getRotationMatrix(inR, null, gravity, geomag);
                if (success) {
                    SensorManager.getOrientation(inR, orientationVals);
                    display = Double.toString(Math.toDegrees(orientationVals[0])) + "; " + Double.toString(Math.toDegrees(orientationVals[1])) + "; " + Double.toString(Math.toDegrees(orientationVals[2]));
                    orientationValues.setText(display);
                    lastAzimuth = Math.toDegrees(orientationVals[0]);
                    lastRoll = Math.toDegrees(orientationVals[2]);
                    lastPitch = Math.toDegrees(orientationVals[1]);
                    values.put("AZIMUTH", Double.toString(Math.toDegrees(orientationVals[0])));
                    values.put("PITCH", Double.toString(Math.toDegrees(orientationVals[1])));
                    values.put("ROLL", Double.toString(Math.toDegrees(orientationVals[2])));
                    values.put("TIMESTAMP", event.timestamp);
                    wdb.insert(A4_2016030_DbManagement.TABLE_NAME_S3, null, values);

                }
            }

            lastX = x;
            lastY = y;
            lastZ = z;
            lastAccel = timestamp;
            lastOrient = timestamp;
            wdb.close();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    SensorEventListener gyrometerSensor = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {


            A4_2016030_DbManagement dbHelper = new A4_2016030_DbManagement(A4_2016030_Homepage.this);
            SQLiteDatabase wdb = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();


            String display = "";
            geomag = event.values.clone();
            if (gyroscopeActive) {
                display = Float.toString(event.values[0]) + "; " + Float.toString(event.values[1]) + "; " + Float.toString(event.values[2]);
                gyroscopeValues.setText(display);


                values.put("X_ACCELERATION", Float.toString(event.values[0]));
                values.put("Y_ACCELERATION", Float.toString(event.values[1]));
                values.put("Z_ACCELERATION", Float.toString(event.values[2]));
                values.put("TIMESTAMP", Long.toString(event.timestamp));

                wdb.insert(A4_2016030_DbManagement.TABLE_NAME_S2, null, values);
                values = new ContentValues();
            }


            if (gravity != null && geomag != null && orientationActive) {
                boolean success = SensorManager.getRotationMatrix(inR, null, gravity, geomag);
                if (success) {
                    SensorManager.getOrientation(inR, orientationVals);
                    display = Double.toString(Math.toDegrees(orientationVals[0])) + "; " + Double.toString(Math.toDegrees(orientationVals[1])) + "; " + Double.toString(Math.toDegrees(orientationVals[2]));
                    orientationValues.setText(display);
                    lastAzimuth = Math.toDegrees(orientationVals[0]);
                    lastRoll = Math.toDegrees(orientationVals[2]);
                    lastPitch = Math.toDegrees(orientationVals[1]);
                    values.put("AZIMUTH", Double.toString(Math.toDegrees(orientationVals[0])));
                    values.put("PITCH", Double.toString(Math.toDegrees(orientationVals[1])));
                    values.put("ROLL", Double.toString(Math.toDegrees(orientationVals[2])));
                    values.put("TIMESTAMP", event.timestamp);
                    wdb.insert(A4_2016030_DbManagement.TABLE_NAME_S3, null, values);

                }
            }
            wdb.close();

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    SensorEventListener proximitySensor = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {


            A4_2016030_DbManagement dbHelper = new A4_2016030_DbManagement(A4_2016030_Homepage.this);
            SQLiteDatabase wdb = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            float distance = event.values[0];
            String display = Float.toString(distance);
            proximityValues.setText(display);
            if (distance == 0) {
                if (lastPitch < -45 && lastPitch < 135) {
                    Toast.makeText(A4_2016030_Homepage.this, "Portrait mode", Toast.LENGTH_SHORT).show();
                } else if (lastPitch > 45 && lastPitch < 135) {
                    Toast.makeText(A4_2016030_Homepage.this, "Reverse Portrait mode", Toast.LENGTH_SHORT).show();
                } else if (lastRoll > 45) {
                    Toast.makeText(A4_2016030_Homepage.this, "Landscape mode", Toast.LENGTH_SHORT).show();
                } else if (lastRoll < -45) {
                    Toast.makeText(A4_2016030_Homepage.this, "Reverse Landscape mode", Toast.LENGTH_SHORT).show();
                }
                
            }
            values.put("DISTANCE_FROM_OBJECT", Float.toString(distance));
            values.put("TIMESTAMP", Long.toString(event.timestamp));
            wdb.insert(A4_2016030_DbManagement.TABLE_NAME_S5, null, values);
            wdb.close();

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    LocationListener gpsSensor = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            A4_2016030_DbManagement dbHelper = new A4_2016030_DbManagement(A4_2016030_Homepage.this);
            SQLiteDatabase wdb = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();



            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String display = Double.toString(latitude) + "; " + Double.toString(longitude);
            gpsValues.setText(display);

            values.put("LATITUDE", latitude);
            values.put("LONGITUDE", longitude);
            values.put("TIMESTAMP", System.currentTimeMillis());
            wdb.insert(A4_2016030_DbManagement.TABLE_NAME_S4, null, values);
            wdb.close();
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

}
