package com.kelompok_a.tubes_sewa_kos;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kelompok_a.tubes_sewa_kos.API.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.PUT;

public class EditProfileService {
    private Context context;
    private ProgressDialog progressDialog;

    public void editUser(final EditProfileView view, final SharedPref sharedPref, final String strNama, final String strNoHp, final EditProfileCallback callback, Context context){
        this.context = context;
        progressDialog= new ProgressDialog(context);
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);
        showProgress("Mengedit user");
        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(PUT, UserAPI.URL_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    if(obj.getString("status").equals("Success")) {
                        callback.onSuccess(true);
//                        MainActivity.changeMenu(MainActivity.binding.bottomNavigation);
//                        loadFragment(new ProfileFragment());
                    }
                    else{
                        view.showErrorResponse(obj.getString("message"));
                        callback.onError();
                    }

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonMessage = new JSONObject(responseBody);
                    String message = jsonMessage.getString("message");
                    view.showErrorResponse(message);
                    callback.onError();
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama", strNama);
                params.put("noHp", strNoHp);
//                params.put("email", email);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + sharedPref.getToken());
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
    public void showProgress(String title) {
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle(title);
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
    public Boolean getValid(final EditProfileView view, String nama, String noHp) {
        final Boolean[] bool = new Boolean[1];
        editUser(view, new SharedPref(context), nama, noHp, new EditProfileCallback() {
            @Override
            public void onSuccess(boolean value) {
                bool[0] = true;
            }

            @Override
            public void onError() {
                bool[0] = false;
            }
        }, context);
        return bool[0];
    }
}
