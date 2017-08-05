package com.woodamax.stm32freshview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;

public class TUTconnSingleEditFieldsOnlyActivity extends AppCompatActivity {

    String mode = "edit";
    String articleID;
    int articleID_int;

    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    String HttpURLupdateTUTStable = "http://10.0.2.2/TUTupdateConnTUTStable.php";
    String HttpURLupdateIMAGEStable = "http://10.0.2.2/TUTupdateConnIMAGEStable.php";
    String HttpUrlGetImage = "http://10.0.2.2/TUTgetImage.php";

    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();//for IMAGES table

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutconn_single_edit_fields_only);
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

                        break;
                }
                // return true if you want the item to be displayed as the selected item
                return false;
            }
        });

        articleID = getIntent().getStringExtra("article_id");
        articleID_int = Integer.parseInt(articleID);

        title = (EditText) findViewById(R.id.editTitle);
        tutTitleHolder = getIntent().getStringExtra("title");
        title.setText(tutTitleHolder);

        TempItem = articleID;

        //textView_tempItem= (TextView) findViewById(R.id.tempID_edit);
        //textView_tempItem.setText(TempItem);

        //getImg(TempItem);//fills idi array
        //tutSubmitUpdate() is called on menu item submit click
        //since idi array is already filled we can use the idis of the records to change the IMAGE table
        //tut_id is used to change the title in the TUTS table

        //Log.d("imgurl :",img_url.getText().toString());

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_sa_edit, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_edit:
                Intent intent = new Intent(getApplicationContext(),UpdateActivity.class);
                startActivity(intent);
                return true;
*/

            case R.id.action_submit:
                getDataFromEditText();
                Log.d("idi:", idi_holder);
                //tutUpdateIMAGEStable(idi_holder, tutTextHolder, img_urlHolder);
                //tutUpdateTUTStable(TempItem, tutTitleHolder);
                return true;

            case R.id.action_view://view button clicked
                Intent intent = new Intent(getApplicationContext(), TUTconnSingleActivity.class);
                intent.putExtra("article_id", TempItem);
                startActivity(intent);

            default:
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void getDataFromEditText(){
        tutTitleHolder = title.getText().toString();
        //sharedPreference = new SharedPreference();
        //idi_holder = sharedPreference.getValue(context);
        //tutTextHolder = text.getText().toString();
        //img_urlHolder = img_url.getText().toString();
        //idi_holder = idi.getText().toString();
    }

    public void tutUpdateIMAGEStable(final String idi, final String text, final String url){
        class TutUpdateClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(TUTconnSingleEditFieldsOnlyActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(TUTconnSingleEditFieldsOnlyActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
                progressDialog = ProgressDialog.show(TUTconnSingleEditFieldsOnlyActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(TUTconnSingleEditFieldsOnlyActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
    private void addBottomDots(int currentPage) {//gets added on page selected function
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
