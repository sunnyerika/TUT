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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class TUTconnSingleEditActivity extends AppCompatActivity {

    String mode = "edit";
    String articleID;
    int articleID_int;

    HttpParse httpParse = new HttpParse();


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

    TextView textView_tempItem, textView_idi;
    EditText title, text, img_url, idi;//tut_id is used as foreign key to image DB- it's not auto incremented
    String tutTitleHolder, tutTextHolder, img_urlHolder, img_TxtHolder, idi_holder;
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
        setContentView(R.layout.activity_tutconn_single_edit);
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
                        //textFavorites.setVisibility(View.GONE);
                        //textSchedules.setVisibility(View.VISIBLE);
                        //textMusic.setVisibility(View.GONE);
                        Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.action_bottom_mail:
                        //Tut Title
                        //idi

                        break;
                }
                // return true if you want the item to be displayed as the selected item
                return false;
            }
        });

        articleID = getIntent().getStringExtra("article_id");
        articleID_int = Integer.parseInt(articleID);

        //String data = getIntent().getStringExtra("data");
        //Log.d("data:", data);

        title = (EditText) findViewById(R.id.editTitle);
        //tutTitleHolder = getIntent().getStringExtra("title");
        title.setText(tutTitleHolder);

        //TempItem = articleID;
        TempItem = "3";
        tutTitleHolder = "testTitle";

        //textView_tempItem= (TextView) findViewById(R.id.tempID_edit);
        //textView_tempItem.setText(TempItem);

        //getImg(TempItem);//fills idi array
        //tutSubmitUpdate() is called on menu item submit click
        //since idi array is already filled we can use the idis of the records to change the IMAGE table
        //tut_id is used to change the title in the TUTS table

        //Log.d("imgurl :",img_url.getText().toString());
        String bwResult = getIntent().getStringExtra("tutBWresult");
        debug = bwResult;

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_sa_edit, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_edit: //no edit in EditActivity
                Intent intent = new Intent(getApplicationContext(),UpdateActivity.class);
                startActivity(intent);
                return true;
*/
            case R.id.action_submit:
                getDataFromEditTextTitle();
                getDataFromEditTextImages();
                transferValues();
                //Log.d("idi:", idi_holder);
               /* if (transValues.get(1) != "" || transValues.get(2) !=""){
                    tutUpdateIMAGEStable(transValues.get(0), transValues.get(1), transValues.get(2));
                }*/
                tutUpdateIMAGEStable(transValues.get(0), transValues.get(1), transValues.get(2));
                tutUpdateTUTStable(TempItem, tutTitleHolder);
                //tutUpdateTUTStable2(TempItem, tutTitleHolder);
                //tutUpdateTUTStable2(TempItem);
                return true;


            case R.id.action_view://view button clicked
                Intent intent = new Intent(getApplicationContext(), TUTconnSingleActivity.class);
                intent.putExtra("article_id", TempItem);
                startActivity(intent);

            case R.id.action_debug://for debugging
                //getDataFromEditTextTitle();
               // tutUpdateTUTStable2(TempItem, tutTitleHolder);
                //checkTutAvailability(TempItem, "new");
                TextView cv = (TextView) findViewById(R.id.check);
                cv.setText(debug);

            default:
                // Invoke the superclass to handle it.
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

          /* */
        /*sharedPreference = new SharedPreference();
        idi_holder = sharedPreference.getValue(context);
        tutTextHolder = text.getText().toString();
        img_urlHolder = img_url.getText().toString();
        idi_holder = idi.getText().toString();*/

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
                progressDialog = ProgressDialog.show(TUTconnSingleEditActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(TUTconnSingleEditActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
                progressDialog1 = ProgressDialog.show(TUTconnSingleEditActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog1.dismiss();
                Toast.makeText(TUTconnSingleEditActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
                progressDialog3 = ProgressDialog.show(TUTconnSingleEditActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog3.dismiss();
                Toast.makeText(TUTconnSingleEditActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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

    public void getImg(final String tutID) {
        class TutGetImage extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog2 = ProgressDialog.show(TUTconnSingleEditActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog2.dismiss();
                finalJSONobjectImg = httpResponseMsg ;//httpResponseMsg is the return value of the doInBgrd - parseResultImg
                new TUTconnSingleEditActivity.GetHttpResponseImage(TUTconnSingleEditActivity.this).execute();
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
            addBottomDots(0);
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
        sliderPagerAdapter = new SliderPagerAdapter(TUTconnSingleEditActivity.this, imgUrlArray,imgTxtArray,idi_array, mode);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                addBottomDots(position);
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
