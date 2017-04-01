package com.acro.hackathon.trekking;

/**
 * Created by DELL on 4/1/2017.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acro.hackathon.trekking.POJO.Result;
import com.acro.hackathon.trekking.network.Registration;

import java.security.cert.CertificateException;

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

/**
 * Created by DELL on 3/28/2017.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name,mobile,gender,address;
    Button signup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        name = (EditText)findViewById(R.id.editTextname);
        mobile = (EditText)findViewById(R.id.editTextmobile);
        gender = (EditText)findViewById(R.id.editTextgender);
        address = (EditText)findViewById(R.id.editTextaddress);
        signup = (Button)findViewById(R.id.finalsignupbutton);
        signup.setOnClickListener(this);

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


    public void signup(){

        Retrofit registration=new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:80/")
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Registration service=registration.create(Registration.class);
        Call<Result> resultCall=service.updateUser(name.getText().toString(),mobile.getText().toString(),
                gender.getText().toString(),address.getText().toString());
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                int statuscode = response.code();
                String get = response.body().getResult();
                Toast.makeText(SignupActivity.this,get,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("failure",t.getMessage());
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v == signup){
            signup();
        }
    }

}
