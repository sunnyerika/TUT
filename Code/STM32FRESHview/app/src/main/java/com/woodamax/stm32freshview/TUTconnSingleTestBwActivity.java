package com.woodamax.stm32freshview;

import android.provider.MediaStore;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class TUTconnSingleTestBwActivity extends AppCompatActivity {

    String mode = "read";
    String articleID;
    int articleID_int;

    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    String HttpURL = "http://10.0.2.2/TUTreadConn.php";
    String HttpUrlDeleteRecord = "http://10.0.2.2/TUTdeleteConn.php";
    String HttpUrlGetImage = "http://10.0.2.2/TUTgetImage.php";

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();

    String ParseResult ;
    HashMap<String,String> ResultHash = new HashMap<>();

    String FinalJSonObject;

    String parseResultImg ;//in getImages
    HashMap<String,String> hashMapImg = new HashMap<>();//to pass id
    String finalJSONobjectImg;//getImages
    ArrayList<String> imgUrlArray = new ArrayList<>();
    ArrayList<String> imgTxtArray = new ArrayList<>();
    ArrayList<String> idi_array = new ArrayList<>();
    String idi_holder;

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;

    String [] slider_text_array;
    private TextView[] dots;
    int page_position = 0;

    TextView title, text, tut_id;//tut_id is used as foreign key to image DB- it's not auto incremented
    ImageView imgView;
    String titleHolder, textHolder, tut_idHolder, img_urlHolder, img_TxtHolder;
    //tut_idHolder contains the result of JSON


    Button UpdateButton, DeleteButton, ActivityButton;

    String TempItem;//holds ID(auto incremented id)
    String debug;

    ProgressDialog progressDialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutconn_single_test_bw);
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
                // handle desired action here
                // One possibility of action is to replace the contents above the nav bar
                switch (item.getItemId()) {
                    case R.id.action_bottom_articles:
                        //textFavorites.setVisibility(View.VISIBLE);
                        //textSchedules.setVisibility(View.GONE);
                        //textMusic.setVisibility(View.GONE);
                        //Toast.makeText(TUTconnSingleActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
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
        //TextView articleIDview = (TextView) findViewById(R.id.tutconn_txt_getID);
        //articleIDview.setText(articleID);
        //debug = getIntent().getStringExtra("test");
        //debug = "4";

        title = (TextView)findViewById(R.id.textTitle);
        //text = (TextView)findViewById(R.id.textText);
        //tut_id = (TextView)findViewById(R.id.textTut_id);

        // UpdateButton = (Button)findViewById(R.id.buttonUpdate);
        //DeleteButton = (Button)findViewById(R.id.buttonDelete);

        //Receiving the selected ListViewItem sent by previous activity
        //TempItem = getIntent().getStringExtra("id");//stores ID
        //TempItem = "15";
        //TempItem = getIntent().getStringExtra("ListViewValue");
        //TempItem = articleID;
        TempItem = "3";
        //BackgroundWorkerTUT backgroundWorkerTUT = new BackgroundWorkerTUT(this);
        //backgroundWorkerTUT.execute("editTut",TempItem);
        //backgroundWorkerTUT.execute("testReadTut","3");
        //debug = backgroundWorkerTUT.phpResult;//did not work
       /* String bwResult = getIntent().getStringExtra("tutBWresult");
        debug = bwResult;*/
        /*
        public void testDBread() {
            BackgroundWorker backgroundworker = new BackgroundWorker(this);
            backgroundworker.execute(type,article);
        }*/

        //HttpWebCall(TempItem);
        HttpWebCallConnection(TempItem, HttpURL);//WORKS
/*
        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TUTconnSingleActivity.this,UpdateActivity.class);

                // Sending values to next UpdateActivity.
                intent.putExtra("id", TempItem);
                intent.putExtra("title", titleHolder);
                intent.putExtra("text", textHolder);
                intent.putExtra("tut_id", tut_idHolder);

                startActivity(intent);
                // Finishing current activity after opening next activity.
                finish();
            }
        });
        */
/*
        // Add Click listener on Delete button.
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TutDelete(TempItem);
            }
        });

*/

//getImg(TempItem);
        getImgHttpUrlConn(TempItem, HttpUrlGetImage);
        debug = img_urlHolder;

    }


    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_sa, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                //Intent intent = new Intent(getApplicationContext(),TUTconnSingleEditFieldsOnlyActivity.class);
                Intent intent = new Intent(getApplicationContext(),TUTconnSingleEditActivity.class);
                intent.putExtra("article_id", TempItem);
                intent.putExtra("title", titleHolder);
                //intent.putExtra("article_id", "3");
                //intent.putExtra("title", "new");
                //intent.putExtra("imgJSON", finalJSONobjectImg);
                startActivity(intent);
                return true;

            case R.id.action_debug://for debugging
                //getDataFromEditTextTitle();
                // tutUpdateTUTStable2(TempItem, tutTitleHolder);
                //checkTutAvailability(TempItem, "new");
                /*TextView cv = (TextView) findViewById(R.id.test);
                cv.setText(debug);
                cv.setText(imgUrlArray.get(0));*/

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void HttpWebCallConnection(final String PreviousListViewClickedItem, String url){
        //passing TempItem received from intent by previous activity(selected ListView Item)
        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = ProgressDialog.show(TUTconnSingleTestBwActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                pDialog.dismiss();
                FinalJSonObject = httpResponseMsg ;
                new TUTconnSingleTestBwActivity.GetHttpResponse(TUTconnSingleTestBwActivity.this).execute();
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
                progressDialog2 = ProgressDialog.show(TUTconnSingleTestBwActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog2.dismiss();
                Toast.makeText(TUTconnSingleTestBwActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                finalJSONobjectImg = httpResponseMsg ;//httpResponseMsg is the return value of the doInBgrd - parseResultImg
                //Gson gson = new GsonBuilder().create();
                //ImagesTable igmTable = gson.fromJson(finalJSONobjectImg, ImagesTable.class);
                new TUTconnSingleTestBwActivity.GetHttpResponseImage(TUTconnSingleTestBwActivity.this).execute();
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

    public void getImg(final String tutID) {
        class TutGetImage extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog2 = ProgressDialog.show(TUTconnSingleTestBwActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog2.dismiss();
                finalJSONobjectImg = httpResponseMsg ;//httpResponseMsg is the return value of the doInBgrd - parseResultImg
                new TUTconnSingleTestBwActivity.GetHttpResponseImage(TUTconnSingleTestBwActivity.this).execute();
            }

            @Override
            protected String doInBackground(String... params) {
                // Sending id
                hashMapImg.put("id", params[0]);
                parseResultImg = httpParse.postRequest(hashMapImg, HttpUrlGetImage);
                return parseResultImg;
            }
        }

        TutGetImage tutGetImage = new TutGetImage();
        tutGetImage.execute(tutID);
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
            Toast.makeText(TUTconnSingleTestBwActivity.this, "gethttpresponse done", Toast.LENGTH_LONG).show();
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
        //ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        //slider_image_list_int = new int[] { R.drawable.wall, R.drawable.nemo,
        //      R.drawable.up, R.drawable.toystory};
        //sliderPagerAdapter = new SliderPagerAdapter(MainActivity.this, slider_image_list_int);
        //sliderPagerAdapter = new SliderPagerAdapter(TUTconnSingleTestBwActivity.this, imgUrlArray, imgTxtArray, mode);
        ArrayList<String> testArrayimgUrl = new ArrayList<>();
        testArrayimgUrl.add("http://10.0.2.2/imageFolder/i1.jpg");
        ArrayList<String> testArrayimgTxt = new ArrayList<>();
        testArrayimgUrl.add("i1.jpg text");

        //sliderPagerAdapter = new SliderPagerAdapter(TUTconnSingleTestBwActivity.this, testArrayimgUrl, testArrayimgTxt, "read");

        //SliderPagerAdapterFresh works without indicator
        //SliderPagerAdapterFresh sliderPagerAdapter = new SliderPagerAdapterFresh(TUTconnSingleTestBwActivity.this, testArrayimgUrl, testArrayimgTxt);
        SliderPagerAdapterFresh sliderPagerAdapter = new SliderPagerAdapterFresh(TUTconnSingleTestBwActivity.this, imgUrlArray, imgTxtArray);
        vp_slider.setAdapter(sliderPagerAdapter);


        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //addBottomDots(position);
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

}
