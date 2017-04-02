package com.acro.hackathon.trekking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.acro.hackathon.LocationBaseActivity;
import com.acro.hackathon.LocationConfiguration;
import com.acro.hackathon.LocationManager;
import com.acro.hackathon.constants.FailType;
import com.acro.hackathon.constants.LogType;
import com.acro.hackathon.constants.ProviderType;
import com.acro.hackathon.trekking.POJO.mapDirection.MapDirectionResponse;
import com.acro.hackathon.trekking.POJO.routes.Feature;
import com.acro.hackathon.trekking.POJO.routes.Treks;
import com.acro.hackathon.trekking.POJO.weather.ResponseData;
import com.acro.hackathon.trekking.network.MapDirectionCalls;
import com.acro.hackathon.trekking.network.TrekkingRoutes;
import com.acro.hackathon.trekking.network.WeatherDataInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends LocationBaseActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    public Retrofit retrofit;
    public static Double latitude=10.057847,longitude=76.680466;
    private ProgressDialog progressDialog;
    public TextView weatherText;
    public static ArrayList<com.acro.hackathon.trekking.POJO.weather.List> weatherList=new ArrayList<>();
    public static ArrayList<String> details=new ArrayList<>();
    public Button danger,medical;
    private String drawerOptions[];
    JSONObject dataset;
    Dialog dialog;
    ArrayList<String> names;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    int pos;
    String treks[]= new String[14];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherText=(TextView)findViewById(R.id.weatherText);
        LocationManager.setLogType(LogType.GENERAL);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:80")
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherData();
        drawerOptions = getResources().getStringArray(R.array.drawer_options);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mDrawerList = (ListView)findViewById(R.id.navigation_list);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerOptions));
        getLocation();
        names = new ArrayList<>();
        names = getIntent().getStringArrayListExtra("names");
        treks = getIntent().getStringArrayExtra("treks");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Trek");
        builder.setItems(treks, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                getTrekkingData(item);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //Map related stuff
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng indore =new LatLng((latitude),(longitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 11.0f));
        Marker laundary= mMap.addMarker(new MarkerOptions().position(indore).title("Your location"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("San Francisco").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return new LocationConfiguration()
                .keepTracking(true)
                .askForGooglePlayServices(true)
                .setMinAccuracy(200.0f)
                .setWaitPeriod(ProviderType.GOOGLE_PLAY_SERVICES, 5 * 1000)
                .setWaitPeriod(ProviderType.GPS, 10 * 1000)
                .setWaitPeriod(ProviderType.NETWORK, 5 * 1000)
                .setGPSMessage("Please enable GPS")
                .setRationalMessage("Allow the app to use GPS");
    }


    //Should be used for the server calls stuff like (weather,location,server php hit
    @Override
    public void onLocationChanged(Location location) {
        dismissProgress();
        setText(location);
    }

    @Override
    public void onLocationFailed(int failType) {
        dismissProgress();

        switch (failType) {
            case FailType.PERMISSION_DENIED: {
               //locationText.setText("Couldn't get location, because user didn't give permission!");
                break;
            }
            case FailType.GP_SERVICES_NOT_AVAILABLE:
            case FailType.GP_SERVICES_CONNECTION_FAIL: {
                //locationText.setText("Couldn't get location, because Google Play Services not available!");
                break;
            }
            case FailType.NETWORK_NOT_AVAILABLE: {
                //locationText.setText("Couldn't get location, because network is not accessible!");
                break;
            }
            case FailType.TIMEOUT: {
                //locationText.setText("Couldn't get location, and timeout!");
                break;
            }
            case FailType.GP_SERVICES_SETTINGS_DENIED: {
                //locationText.setText("Couldn't get location, because user didn't activate providers via settingsApi!");
                break;
            }
            case FailType.GP_SERVICES_SETTINGS_DIALOG: {
                //locationText.setText("Couldn't display settingsApi dialog!");
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            displayProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        dismissProgress();
    }

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Loading...");
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void setText(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.bankBtn:
                Intent i=new Intent(MainActivity.this,InstructionActivity.class);
                i.putExtra("type","Do's and Don'ts");
                startActivity(i);
            break;
            case R.id.hospitalBtn:
                Intent i1=new Intent(MainActivity.this,InstructionActivity.class);
                i1.putExtra("type","Kit");
                startActivity(i1);
            break;
            case R.id.eatries:
                Intent i2=new Intent(MainActivity.this,InstructionActivity.class);
                i2.putExtra("type","Procedure");
                startActivity(i2);
                break;
        }

    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.addInterceptor(logging);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //For gettting weather data (temp,wind,humid etc)
    public void weatherData() {
            weatherList.clear();
        Retrofit login = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherDataInterface services = login.create(WeatherDataInterface.class);
        final Call<ResponseData> weatherDataResponse = services.getWheatherReport(String.valueOf(latitude),String.valueOf(longitude),"e415c72a39337b04fc6749b09b2406f3","metric");
        weatherDataResponse.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                details.clear();
                weatherText.setText("weather-"+response.body().getList().get(0).getMain().getTemp()+(char) 0x00B0 +"C/"+response.body().getList().get(0).getWeather().get(0).getMain());
                weatherList.addAll(response.body().getList());

                details.add(response.body().getList().get(0).getMain().getTempMax());
                details.add(response.body().getList().get(0).getMain().getTempMin());
                details.add(response.body().getList().get(0).getWeather().get(0).getDescription());
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("Failure",t.getMessage().toString());
            }
        });


    }


    //For drawing path between two lat,long
   public void mapDirectionData() {
       Retrofit retrofit = new Retrofit.Builder()
               .baseUrl("https://maps.googleapis.com/")
               .client(getUnsafeOkHttpClient())
               .addConverterFactory(GsonConverterFactory.create())
               .build();
       MapDirectionCalls calls = retrofit.create(MapDirectionCalls.class);
       Call<MapDirectionResponse> responseCall=calls.mapDirectionPath("Toronto","Montreal","bicycling","AIzaSyAvfrpCpuJb3lyDr0xXdSdWyesBcs5vago");
       responseCall.enqueue(new Callback<MapDirectionResponse>() {
           @Override
           public void onResponse(Call<MapDirectionResponse> call, Response<MapDirectionResponse> response) {
           }

           @Override
           public void onFailure(Call<MapDirectionResponse> call, Throwable t) {
           }
       });
   }

    public void callWeather(View v) {
        Intent i=new Intent(MainActivity.this,WeatherDetailsActivity.class);
        startActivity(i);
    }



    public void callDangerMedical(View v) {
        String mobile,danger,medical;
        switch (v.getId())
        {
            case R.id.danger:
                Intent i =new Intent(MainActivity.this,TrekkingActivity.class);
                startActivity(i);
              break;
        }

    }
    public static class PostDataForMedicalAndDanger {
        String mobile,danger,medical;
        public PostDataForMedicalAndDanger(String mobile,String danger,String medical) {
            this.mobile=mobile;
            this.danger=danger;
            this.medical=medical;
        }


    }
    public void getTrekkingData(int position){
            pos = position;
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("http://acrokids-ps11.rhcloud.com/")
                .client(MainActivity.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TrekkingRoutes service = adapter.create(TrekkingRoutes.class);
        Call<Treks> response = service.getTrekkingRoutes();
        response.enqueue(new Callback<Treks>() {
            @Override
            public void onResponse(Call<Treks> call, Response<Treks> response) {
                if (response.isSuccessful()) {
                    Treks routes = response.body();
                    List<Feature> features = routes.getFeatures();
                    List<List<Double>> coordinates = features.get(pos).getGeometry().getCoordinates();
                        int i=0;
                    do{
                        List<Double> data = coordinates.get(i);
                        Double long1 = data.get(0);
                        Double lat1 = data.get(1);
                        i++;
                        data = coordinates.get(i);
                        Double long2 = data.get(0);
                        Double lat2 = data.get(1);
                        Polyline line = mMap.addPolyline(
                                new PolylineOptions()
                            .add(new LatLng(
                                    lat1,
                                    long1),
                                    new LatLng(lat2,
                                            long2))
                            .width(8)
                            .color(Color.RED));
                    }while(i<coordinates.size()-1);


                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordinates.get(0).get(1), coordinates.get(0).get(0)), 9.0f));
                }
                else{
                    Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Treks> call, Throwable t) {
                Log.e("Response ERROR**", "onFailure: "+ t.getLocalizedMessage());
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}