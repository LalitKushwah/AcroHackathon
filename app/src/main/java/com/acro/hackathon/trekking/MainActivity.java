package com.acro.hackathon.trekking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.acro.hackathon.LocationBaseActivity;
import com.acro.hackathon.LocationConfiguration;
import com.acro.hackathon.LocationManager;
import com.acro.hackathon.constants.FailType;
import com.acro.hackathon.constants.LogType;
import com.acro.hackathon.constants.ProviderType;
import com.acro.hackathon.trekking.POJO.weather.ResponseData;
import com.acro.hackathon.trekking.network.TrekkingRoutes;
import com.acro.hackathon.trekking.network.WeatherDataInterface;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.security.cert.CertificateException;
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

import static com.acro.hackathon.trekking.R.id.map;

public class MainActivity extends LocationBaseActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    public Double latitude=9.9312,longitude=76.2673;
    private ProgressDialog progressDialog;
    public TextView weatherText;
    private String drawerOptions[];
    JSONObject dataset;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherText=(TextView)findViewById(R.id.weatherText);
        LocationManager.setLogType(LogType.GENERAL);
        getTrekkingDatas();
        drawerOptions = getResources().getStringArray(R.array.drawer_options);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mDrawerList = (ListView)findViewById(R.id.navigation_list);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerOptions));

        getLocation();

//        try {
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng indore =new LatLng((latitude+0.05),(longitude+0.05));
        Marker laundary= mMap.addMarker(new MarkerOptions().position(indore).title("Your location"));
     //   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 11.0f));


            SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


   /*     Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(10000)
                .strokeColor(Color.GRAY)
                .fillColor(Color.WHITE)); //Inside color
*/


//            JSONArray routes = (JSONArray) dataset.get("routes");
//            JSONObject route = (JSONObject) routes.get(0);
//            JSONArray coordinates = (JSONArray)route.get("coordinates");
//
//            for(int i=0;i<coordinates.length()-1;i++){
//                String coord1  = (String) coordinates.get(i);
//                String coord2 = (String) coordinates.get(i+1);
//
//                String coord1Arr[] = coord1.split("/");
//                String coord2Arr[] = coord2.split("/");
//
//
//                Polyline line = mMap.addPolyline(new PolylineOptions()
//                .add(new LatLng(Long.parseLong(coord1Arr[1]), Long.parseLong(coord1Arr[0])), new LatLng(Long.parseLong(coord2Arr[1]), Long.parseLong(coord2Arr[0])))
//                .width(8)
//                .color(Color.RED));









        //TODO : Below is the code to draw line between two GPS coordinates on a map. Use this code to draw Trekking Routes on Map.
//        Polyline line = mMap.addPolyline(new PolylineOptions()
//                .add(new LatLng(10.0081428,76.3670165), new LatLng(10.009687, 76.364758))
//                .width(8)
//                .color(Color.RED));
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        Retrofit login = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherDataInterface services = login.create(WeatherDataInterface.class);
        final Call<ResponseData> weatherDataResponse = services.getWheatherReport(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),"e415c72a39337b04fc6749b09b2406f3","metric");
        weatherDataResponse.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
               weatherText.setText(response.body().getList().get(0).getMain().getTemp()+(char) 0x00B0 +"C/"+response.body().getList().get(0).getWeather().get(0).getMain());
                Log.d("weatherText",weatherText.getText().toString());
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("Failure",t.getLocalizedMessage());
            }
        });


    }

    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.bankBtn:
             //   Toast.makeText(this, "Bank button clicked", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(MainActivity.this,NearByPlacesActivity.class);
                i.putExtra("type","bank");
                i.putExtra("latitude",String.valueOf(latitude));
                i.putExtra("longitude",String.valueOf(longitude));
                startActivity(i);
            break;
            case R.id.hospitalBtn:
             //   Toast.makeText(this, "Hospital button clicked", Toast.LENGTH_SHORT).show();
                Intent i1=new Intent(MainActivity.this,NearByPlacesActivity.class);
                i1.putExtra("type","hospital");
                i1.putExtra("latitude",String.valueOf(latitude));
                i1.putExtra("longitude",String.valueOf(longitude));
                startActivity(i1);
            break;
            case R.id.eatries:
            //    Toast.makeText(this, "Eatries button clicked", Toast.LENGTH_SHORT).show();
                Intent i2=new Intent(MainActivity.this,NearByPlacesActivity.class);
                i2.putExtra("type","eatries");
                i2.putExtra("latitude",String.valueOf(latitude));
                i2.putExtra("longitude",String.valueOf(longitude));
                startActivity(i2);
                break;
            case R.id.accomodation:
             //   Toast.makeText(this, "Accomodation button clicked", Toast.LENGTH_SHORT).show();
                Intent i3=new Intent(MainActivity.this,NearByPlacesActivity.class);
                i3.putExtra("type","hotel");
                i3.putExtra("latitude",String.valueOf(latitude));
                i3.putExtra("longitude",String.valueOf(longitude));
                startActivity(i3);
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


    public void getTrekkingDatas(){
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("http://acrokids-ps11.rhcloud.com/")
                .client(MainActivity.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TrekkingRoutes service = adapter.create(TrekkingRoutes.class);

        Call<com.acro.hackathon.trekking.POJO.routes.TrekkingRoutes> response = service.getTrekkingRoutes();

        response.enqueue(new Callback<com.acro.hackathon.trekking.POJO.routes.TrekkingRoutes>() {
            @Override
            public void onResponse(Call<com.acro.hackathon.trekking.POJO.routes.TrekkingRoutes> call, Response<com.acro.hackathon.trekking.POJO.routes.TrekkingRoutes> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.body().getRoutes().get(0).getName(), Toast.LENGTH_SHORT).show();


//                    dataset = response.body()
//                    JSONArray routes = (JSONArray) dataset.get("routes");
//            JSONObject route = (JSONObject) routes.get(0);
//            JSONArray coordinates = (JSONArray)route.get("coordinates");

                    List<com.acro.hackathon.trekking.POJO.routes.Route> routes = response.body().getRoutes();

            for(int i=0;i<routes.size();i++){

            List<String> coordinates = routes.get(i).getCoordinates();

                //TODO : resolve NuberFormat exception


                for(int j=0;j<coordinates.size()-1;j++){
                    String coord1  =  coordinates.get(j);
                    String coord2 =  coordinates.get(j+1);
                    String coord1Arr[] = coord1.split("/");
                    String coord2Arr[] = coord2.split("/");
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(
                                    Double.parseDouble(coord1Arr[1].replaceAll("\"","")),
                                    Double.parseDouble(coord1Arr[0].replaceAll("\"",""))),
                                    new LatLng(Double.parseDouble(coord2Arr[1].replaceAll("\"","")),
                                            Double.parseDouble(coord2Arr[0].replaceAll("\"",""))))
                            .width(8)


                            .color(Color.RED));
                }
            }
                }
                else{
                    Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.acro.hackathon.trekking.POJO.routes.TrekkingRoutes> call, Throwable t) {
                Toast.makeText(MainActivity.this, "On Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}