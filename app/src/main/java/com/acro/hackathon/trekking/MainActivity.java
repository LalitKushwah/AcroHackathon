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
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.acro.hackathon.trekking.POJO.mapDirection.MapDirectionResponse;
import com.acro.hackathon.trekking.network.MapDirectionCalls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.acro.hackathon.trekking.POJO.weather.ResponseData;
import com.acro.hackathon.LocationBaseActivity;
import com.acro.hackathon.LocationConfiguration;
import com.acro.hackathon.LocationManager;
import com.acro.hackathon.constants.FailType;
import com.acro.hackathon.constants.LogType;
import com.acro.hackathon.constants.ProviderType;
import com.acro.hackathon.trekking.POJO.routes.Feature;
import com.acro.hackathon.trekking.POJO.routes.Treks;
import com.acro.hackathon.trekking.network.TrekkingRoutes;
import com.acro.hackathon.trekking.network.WeatherDataInterface;
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
    String treks[];

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

        /*
       final DangerMedicalCall service=retrofitObj.create(DangerMedicalCall.class);

        danger=(Button)findViewById(R.id.danger);
        medical=(Button)findViewById(R.id.medical);



        danger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.danger) {
                    Call<DangerMedicalResponse> responseCall = service.sendDangerMedical("6545665","y","n");
                    responseCall.enqueue(new Callback<DangerMedicalResponse>() {
                        @Override
                        public void onResponse(Call<DangerMedicalResponse> call, Response<DangerMedicalResponse> response) {

                        }

                        @Override
                        public void onFailure(Call<DangerMedicalResponse> call, Throwable t) {

                        }
                    });
                }
                else {
                    Call<DangerMedicalResponse> responseCall = service.sendDangerMedical("6545665","y","n");
                    responseCall.enqueue(new Callback<DangerMedicalResponse>() {
                        @Override
                        public void onResponse(Call<DangerMedicalResponse> call, Response<DangerMedicalResponse> response) {

                        }

                        @Override
                        public void onFailure(Call<DangerMedicalResponse> call, Throwable t) {

                        }
                    });
                }
            }



        });*/


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


        //TODO : Dialog






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

   /*     Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(10000)
                .strokeColor(Color.GRAY)
                .fillColor(Color.WHITE)); //Inside color
*/


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

             //   Toast.makeText(this, "Bank button clicked", Toast.LENGTH_SHORT).show();
                /*Intent i=new Intent(MainActivity.this,NearByPlacesActivity.class);
                i.putExtra("type","bank");
                i.putExtra("latitude",String.valueOf(latitude));
                i.putExtra("longitude",String.valueOf(longitude));
                startActivity(i);*/
            break;
            case R.id.hospitalBtn:
                Intent i1=new Intent(MainActivity.this,InstructionActivity.class);
                i1.putExtra("type","Kit");
                startActivity(i1);


             //   Toast.makeText(this, "Hospital button clicked", Toast.LENGTH_SHORT).show();
               /* Intent i1=new Intent(MainActivity.this,NearByPlacesActivity.class);
                i1.putExtra("type","hospital");
                i1.putExtra("latitude",String.valueOf(latitude));
                i1.putExtra("longitude",String.valueOf(longitude));
                startActivity(i1);*/
            break;
            case R.id.eatries:
                Intent i2=new Intent(MainActivity.this,InstructionActivity.class);
                i2.putExtra("type","Procedure");
                startActivity(i2);
            //    Toast.makeText(this, "Eatries button clicked", Toast.LENGTH_SHORT).show();
                /*Intent i2=new Intent(MainActivity.this,NearByPlacesActivity.class);
                i2.putExtra("type","eatries");
                i2.putExtra("latitude",String.valueOf(latitude));
                i2.putExtra("longitude",String.valueOf(longitude));
                startActivity(i2);*/
                break;
            case R.id.accomodation:
                Intent i3=new Intent(MainActivity.this,InstructionActivity.class);
                i3.putExtra("type","BaseCamp Information");
                startActivity(i3);

                //   Toast.makeText(this, "Accomodation button clicked", Toast.LENGTH_SHORT).show();
                /*Intent i3=new Intent(MainActivity.this,NearByPlacesActivity.class);
                i3.putExtra("type","hotel");
                i3.putExtra("latitude",String.valueOf(latitude));
                i3.putExtra("longitude",String.valueOf(longitude));
                startActivity(i3);
                break;*/


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



    /*public void callDangerMedical(View v) {
        String mobile,danger,medical;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:80/")
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create());

        switch (v.getId()) {
            case R.id.danger:
                mobile="74845454";
                danger="y";
                medical="n";
                DangerMedicalCall service=retrofit.create(DangerMedicalCall.class);
                Call<DangerMedicalResponse> responseCall=service.sendDangerMedical(new PostDataForMedicalAndDanger(mobile,danger,medical));
                responseCall.enqueue(new Callback<DangerMedicalResponse>() {
                    @Override
                    public void onResponse(Call<DangerMedicalResponse> call, Response<DangerMedicalResponse> response) {

                    }
                    @Override
                    public void onFailure(Call<DangerMedicalResponse> call, Throwable t) {

                    }
                });
              break;
            case R.id.medical:
                mobile="74845454";
                danger="n";
                medical="y";
                DangerMedicalCall service2=retrofit.create(DangerMedicalCall.class);
                Call<DangerMedicalResponse> responseCall2=service2.sendDangerMedical(new PostDataForMedicalAndDanger(mobile,danger,medical));
                responseCall2.enqueue(new Callback<DangerMedicalResponse>() {
                    @Override
                    public void onResponse(Call<DangerMedicalResponse> call, Response<DangerMedicalResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<DangerMedicalResponse> call, Throwable t) {

                    }
                });
                break;
        }

    }*/
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
//                    dataset = response.body()
//                    JSONArray routes = (JSONArray) dataset.get("routes");
//            JSONObject route = (JSONObject) routes.get(0);
//            JSONArray coordinates = (JSONArray)route.get("coordinates");

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

//            for(int i=0;i<routes.size();i++){
//
//            List<String> coordinates = routes.get(i).getCoordinates();
//
//
//
//
//                for(int j=0;j<coordinates.size()-1;j++){
//                    String coord1  =  coordinates.get(j);
//                    String coord2 =  coordinates.get(j+1);
//                    String coord1Arr[] = coord1.split("/");
//                    String coord2Arr[] = coord2.split("/");
//                    Polyline line = mMap.addPolyline(new PolylineOptions()
//                            .add(new LatLng(
//                                    Double.parseDouble(coord1Arr[1].replaceAll("\"","")),
//                                    Double.parseDouble(coord1Arr[0].replaceAll("\"",""))),
//                                    new LatLng(Double.parseDouble(coord2Arr[1].replaceAll("\"","")),
//                                            Double.parseDouble(coord2Arr[0].replaceAll("\"",""))))
//                            .width(8)
//
//
//                            .color(Color.RED));
//                }
//            }

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