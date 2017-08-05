package com.woodamax.stm32freshview;


import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.woodamax.stm32freshview.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class TUTconnFreshViewEdit extends AppCompatActivity {


    String mode = "edit";
    String articleID;
    int articleID_int;

    HttpParse httpParse = new HttpParse();

    String HttpURL = "http://10.0.2.2/TUTreadConn.php";
    String HttpURLupdateTUTStable = "http://10.0.2.2/TUTupdateConnTUTStable.php";
    String HttpURLupdateTUTStable2 = "http://10.0.2.2/TUTupdateConnTUTStable2.php";
    String HttpURLupdateIMAGEStable = "http://10.0.2.2/TUTupdateConnIMAGEStable.php";
    String HttpUrlGetImage = "http://10.0.2.2/TUTgetImage.php";
    String HttpUrl = "http://10.0.2.2/TUTcheckConnTUTStable.php";
    String HTTPUrlAuthor = "http://m4xwe11o.ddns.net/MAD-Test/login.php";

    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();//for IMAGES table

    String debug;
    HashMap<String, String> hashMapDebug = new HashMap<>();

    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();

    String FinalJSonObject;

    String parseResultImg;//in getImages
    HashMap<String, String> hashMapImg = new HashMap<>();//to pass id
    String finalJSONobjectImg;//getImages
    ArrayList<String> imgUrlArray = new ArrayList<>();
    ArrayList<String> imgTxtArray = new ArrayList<>();
    ArrayList<String> idi_array = new ArrayList<>();
    //needs be extracted from images JSON
    //holds auto-incremented id of IMAGES table records needed for changes to IMAGES table
    //has to be an array since one tutorial can have several images associated with it

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;

    private TextView[] dots;

    TextView textView_tempItem, textView_idi, title;
    EditText title_edit, text, img_url, idi;//tut_id is used as foreign key to image DB- it's not auto incremented
    String tutTitleHolder, tutTextHolder, img_urlHolder, img_TxtHolder, idi_holder, titleHolder, textHolder, tut_idHolder;
    //tut_idHolder contains the result of JSON
    private SharedPreference sharedPreference;
    Activity context = this;

    String TempItem;
    //holds article id which is used as foreign key called tut_id for TUTS and IMAGES table
    //works since it is also unique - no need to use the TUTS auto incremented id for changes

    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;
    ProgressDialog progressDialog2;
    ProgressDialog progressDialog3;
    ProgressDialog pDialog;

    //vars for getdata function contains edited data sent via slide adapter onChangedText
    String editedUrl;
    String editedImgText;
    String idiStrg;

    //vars for showValues function to show whats currently stored in activityViews
    String idiStrgVALUES;
    String editedUrlStrgVALUES;
    String editedImgTextStrgVALUES;
    TextView showValueIdi;
    TextView showValueUrl;
    TextView showValueImgText;


    String editedUrlStrgTransfer;
    String editedImgTextStrgTransfer;
    String idiStrgTransfer;

    ArrayList<String>transValues = new ArrayList<String>();

    String checkAv;
    String parseResultCheck;//in getImages
    HashMap<String, String> hashMapCheck = new HashMap<>();//to pass id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutconn_fresh_view_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tut_connSAtoolbar);
        toolbar.setTitle("Tutorial");
        setSupportActionBar(toolbar);

        //up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_bottom_articles:
                        Intent intent = new Intent(getApplicationContext(),ArticleScreen.class);
                        startActivity(intent);
                        break;
                    case R.id.action_bottom_home:
                        Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.action_bottom_mail:

                        break;
                }
                // return true if you want the item to be displayed as the selected item
                return false;
            }
        });

        articleID = getIntent().getStringExtra("article_id");
        articleID_int = Integer.parseInt(articleID);

        tutTitleHolder = getIntent().getStringExtra("title");
       /*title = (TextView) findViewById(R.id.textTitle);
        title.setText(tutTitleHolder);
        */
        title_edit = (EditText) findViewById(R.id.editTitle);
        title_edit.setText(tutTitleHolder);

        //TempItem = articleID;
        TempItem = "3";

        //Log.d("imgurl :",img_url.getText().toString());
        String bwResult = getIntent().getStringExtra("tutBWresult");
        debug = bwResult;

        //getTitleHttpUrlConn(TempItem,HttpURL);
        getImgHttpUrlConn(TempItem, HttpUrlGetImage);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_sa_edit, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_submit:
                getDataFromEditTextTitle();
                getDataFromEditTextImages();
                transferValues();

                tutUpdateIMAGEStable(transValues.get(0), transValues.get(1), transValues.get(2));
                tutUpdateTUTStable(TempItem, tutTitleHolder);
                return true;


            case R.id.action_view://view button clicked
                Intent intent = new Intent(getApplicationContext(), TUTconnSingleActivity.class);
                intent.putExtra("article_id", TempItem);
                startActivity(intent);

            case R.id.action_debug://for debugging
                TextView cv = (TextView) findViewById(R.id.check);
                cv.setText(debug);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getDataFromEditTextTitle(){
        tutTitleHolder = title.getText().toString();
    }

    public void getDataFromEditTextImages(){//activityTextViews get filled by sliderAdapter on input change
        //the VIEWVALUES get transmitted to PHP
        editedUrl = ((TextView) findViewById(R.id.activityTextViewUrl)).getText().toString();
        editedImgText = ((TextView) findViewById(R.id.activityTextViewImgText)).getText().toString();
        idiStrg = ((TextView) findViewById(R.id.activityTextViewIdi)).getText().toString();
        idiStrgTransfer = idiStrg;
        editedImgTextStrgTransfer = editedImgText;
        editedUrlStrgTransfer = editedUrl;

    }

    public void showValuesOfActivityView(){//for debugging
        idiStrgVALUES = idiStrg;
        editedUrlStrgVALUES = editedUrl;
        editedImgTextStrgVALUES = editedImgText;
    }

    public void transferValues(){
        transValues.clear();
        transValues.add(idiStrgTransfer);
        transValues.add(editedImgTextStrgTransfer);
        transValues.add(editedUrlStrgTransfer);
    }


    public void tutUpdateIMAGEStable(final String idi, final String text, final String url){
        class TutUpdateClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(TUTconnFreshViewEdit.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(TUTconnFreshViewEdit.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("idi",params[0]);
                hashMap.put("imgText",params[1]);
                hashMap.put("imgUrl",params[2]);
                finalResult = httpParse.postRequest(hashMap, HttpURLupdateIMAGEStable);
                return finalResult;
            }
        }
        TutUpdateClass tutUpdateClass = new TutUpdateClass();
        tutUpdateClass.execute(idi, text, url);
    }

    public void tutUpdateTUTStable(String TempItem, String title){
        class TutUpdateClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog1 = ProgressDialog.show(TUTconnFreshViewEdit.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog1.dismiss();
                Toast.makeText(TUTconnFreshViewEdit.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("id",params[0]);
                hashMap.put("title",params[1]);
                finalResult = httpParse.postRequest(hashMap, HttpURLupdateTUTStable);
                return finalResult;
            }
        }
        TutUpdateClass tutUpdateClass = new TutUpdateClass();
        tutUpdateClass.execute(TempItem, title);
    }

    //public void tutUpdateTUTStable2(String TempItem, String title){
    public void tutUpdateTUTStable2(String TempItem){
        class TutUpdateClass2 extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog3 = ProgressDialog.show(TUTconnFreshViewEdit.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog3.dismiss();
                Toast.makeText(TUTconnFreshViewEdit.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                hashMapDebug.put("id",params[0]);
                //hashMapDebug.put("title",params[1]);
                debug = httpParse.postRequest(hashMapDebug, HttpURLupdateTUTStable2);
                return debug;
            }
        }
        TutUpdateClass2 tutUpdateClass = new TutUpdateClass2();
        //tutUpdateClass.execute(TempItem, title);
        tutUpdateClass.execute(TempItem);
    }


    public void getTitleHttpUrlConn(final String PreviousListViewClickedItem, String url){
        //passing TempItem received from intent by previous activity(selected ListView Item)
        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = ProgressDialog.show(TUTconnFreshViewEdit.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                pDialog.dismiss();
                FinalJSonObject = httpResponseMsg ;
                new TUTconnFreshViewEdit.GetHttpResponse(TUTconnFreshViewEdit.this).execute();
            }

            @Override
            protected String doInBackground(String... params) {//what we pass is the tut_id
               /*
                ResultHash.put("id",params[0]);//key - value pair
                ParseResult = httpParse.postRequest(ResultHash, HttpURL);//TUTRead.php
                return ParseResult;*/
                try{
                    String tempItem = params[0];
                    String urlPara = params[1];
                    URL url = new URL(urlPara);//http://10.0.2.2/TUTreadConn.php
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    //post_data includes the key_value pairs that are passed to the php file
                    //EQUI to what we put in the hashmap array like hashMapImg.put("id", params[0]); but here the id gets stored in params[1]
                    //since we first pass the type
                    String post_data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(tempItem,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while((line = bufferedReader.readLine())!= null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    //result stores the echo of our php file
                    return result;
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();
        httpWebCallFunction.execute(PreviousListViewClickedItem, url);
    }

    // Parsing Complete JSON Object.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                if(FinalJSonObject != null)
                //httpResponsMsgreceived by postexecute
                {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(FinalJSonObject);
                        JSONObject jsonObject;
                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            titleHolder = jsonObject.getString("title").toString() ;
                            textHolder = jsonObject.getString("text").toString() ;//text of tuts table not needed anymore
                            tut_idHolder = jsonObject.getString("tut_id").toString() ;
                        }
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            title.setText(titleHolder);
            //text.setText(textHolder);
            //tut_id.setText(tut_idHolder);
            //title.setText("testTitle");
            //text.setText("testText");
        }
    }


    public void getImgHttpUrlConn(final String tutID, String url) {
        class TutGetImage extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog2 = ProgressDialog.show(TUTconnFreshViewEdit.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog2.dismiss();
                Toast.makeText(TUTconnFreshViewEdit.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                finalJSONobjectImg = httpResponseMsg ;//httpResponseMsg is the return value of the doInBgrd - parseResultImg
                //Gson gson = new GsonBuilder().create();
                //ImagesTable igmTable = gson.fromJson(finalJSONobjectImg, ImagesTable.class);
                new TUTconnFreshViewEdit.GetHttpResponseImage(TUTconnFreshViewEdit.this).execute();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String tempItem = params[0];
                    String urlPara = params[1];
                    URL url = new URL(urlPara);//http://10.0.2.2/TUTgetImage.php
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    //post_data includes the key_value pairs that are passed to the php file
                    //EQUI to what we put in the hashmap array like hashMapImg.put("id", params[0]); but here the id gets stored in params[1]
                    //since we first pass the type
                    String post_data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(tempItem,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while((line = bufferedReader.readLine())!= null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    //result stores the echo of our php file
                    return result;
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        TutGetImage tutGetImage = new TutGetImage();
        tutGetImage.execute(tutID, url);
    }

    private class GetHttpResponseImage extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        public GetHttpResponseImage(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                if(finalJSONobjectImg != null)
                {
                    JSONArray jsonArray = null;
                    imgUrlArray.clear();
                    img_urlHolder = "";
                    imgTxtArray.clear();
                    img_TxtHolder = "";
                    idi_array.clear();


                    try {
                        jsonArray = new JSONArray(finalJSONobjectImg);
                        JSONObject jsonObject;
                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            img_urlHolder = jsonObject.getString("imageurl").toString();//column name
                            imgUrlArray.add(img_urlHolder);
                            img_TxtHolder = jsonObject.getString("imageText").toString();
                            imgTxtArray.add(img_TxtHolder);
                            idi_holder = jsonObject.getString("idi").toString();
                            idi_array.add(idi_holder);

                        }
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            // method for initialisation
            init();
            // method for adding indicators
            //addBottomDots(0);
        }
    }

    private void init() {

        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //getSupportActionBar().hide();
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        //slider_image_list_int = new int[] { R.drawable.wall, R.drawable.nemo,
        //      R.drawable.up, R.drawable.toystory};
        //sliderPagerAdapter = new SliderPagerAdapter(MainActivity.this, slider_image_list_int);
        //sliderPagerAdapter = new SliderPagerAdapter(TUTconnFreshViewEdit.this, imgUrlArray,imgTxtArray,idi_array, mode);
        SliderPagerAdapterFresh sliderPagerAdapter = new SliderPagerAdapterFresh(TUTconnFreshViewEdit.this, imgUrlArray, imgTxtArray);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

               // addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        //dots = new TextView[slider_image_list.size()];
        dots = new TextView[imgUrlArray.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#FFFFFF"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#000000"));
    }

    public void checkTutAvailability(String tempItem, String title){//checks if article_id is found in TUTS
        class CheckA extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressDialog2 = ProgressDialog.show(TUTconnSingleEditActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                //Toast.makeText(getContext(),httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                //checkAv = httpResponseMsg;
                //resultArray.add(resultCheck);
            }

            @Override
            protected String doInBackground(String... params) {
                // Sending id
                hashMapCheck.put("id", params[0]);
                hashMapCheck.put("title", params[1]);
                parseResultCheck = httpParse.postRequest(hashMapCheck, HttpUrl);
                return parseResultCheck;
            }
        }

        CheckA checkA = new CheckA();
        checkA.execute(tempItem, title);


    }

}
