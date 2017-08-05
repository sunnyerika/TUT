package com.woodamax.stm32freshview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.woodamax.stm32freshview.R.drawable.view;

/**
 * Created by maxim on 30.04.2017.
 */

public class ReadersViewFragment extends Fragment {
    DatabaseHelper myDBH;

    ImageView tutImg;
    ImageView tutImgNA;
    //ImageButton tutAvailability;
    int articleID;
    String str_articleID;
    String str_title;

    HashMap<String, String> hashMapCheck = new HashMap<>();
    String parseResultCheck;
    String httpresponsecheck;
    HttpParse httpParse = new HttpParse();
    String HttpUrl = "http://10.0.2.2/TUTcheckConnTUTStable.php";
    TextView resultTV;
    ArrayList<String> resultArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.article_readers_view_fragment,container,false);
        buildReadersView(view);

        //Used to display the article button when the article is shown
        Button articleButton = (Button) getActivity().findViewById(R.id.my_reading_article_button);
        Button submitButton = (Button) getActivity().findViewById(R.id.my_reading_submit_button);
        if(MainActivity.fh.getCenter().equals("Article_reading_fragment")){
            articleButton.setVisibility(view.VISIBLE);
            if(MainActivity.fh.isAuthor()){
                submitButton.setVisibility(view.VISIBLE);
                submitButton.setTextColor(Color.GRAY);
                submitButton.setClickable(false);
            }
        }

        articleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fh.setCenter("Article_selection_Fragment");
                FragmentManager fm3 = getFragmentManager();
                FragmentTransaction ft3 = fm3.beginTransaction();
                ArticleSelectionFragment article = new ArticleSelectionFragment();
                ft3.replace(R.id.article_text_container, article, "Article_selection_Fragment");
                ft3.detach(fm3.findFragmentByTag("Article_reading_fragment"));
                ft3.commit();
            }
        });
        return view;
    }

    /**
     * The helper helps to find the selected article
     * The article object can be used to edit an article
     * @param view the view which has to be build
     */
    private void buildReadersView(View view) {
        myDBH = new DatabaseHelper(getActivity());
        Cursor res = myDBH.getArticleDescription();
        while (res.moveToNext()){
            if(ArticleScreen.helper.getId() == Integer.parseInt(res.getString(0))){
                articleID = ArticleScreen.helper.getId();
                str_articleID = Integer.toString(articleID);
                Article article = new Article(res.getString(1),res.getString(2),res.getString(3)," ");
                int controllerType = getControllerType(res.getString(1));
                MainActivity.fh.setControllerType(controllerType);
                TextView title = (TextView) view.findViewById(R.id.article_readers_view_title);
                TextView description = (TextView) view.findViewById(R.id.article_readers_view_description);
                TextView articletext = (TextView) view.findViewById(R.id.article_readers_view_text);
                tutImg = (ImageView) view.findViewById((R.id.tut_img_availability));
                               /*
                tutImg.setImageResource(R.drawable.study_available);
                tutImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getContext(), TUTconnFreshView.class);
                        //intent.putExtra("article_id", "16");
                        intent.putExtra("article_id", str_articleID);
                        startActivity(intent);
                    }
                });
                */

                checkTutAvailability(str_articleID, HttpUrl);

/*
                if (resultCheck.equals("y")) {
                    tutImg = (ImageView) view.findViewById((R.id.tut_img_availability));
                    tutImg.setImageResource(R.drawable.study_available);
                    tutImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(getContext(), TUTconnSingleActivity.class);
                            //intent.putExtra("article_id", "16");
                            intent.putExtra("article_id", str_articleID);
                            startActivity(intent);
                        }
                    });
                }
                else {
                    tutImgNA = (ImageView) view.findViewById((R.id.tut_img_NA));
                    tutImgNA.setImageResource(R.drawable.study_na);
                    tutImgNA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (MainActivity.fh.isAuthor()){
                                Intent intent = new Intent(getContext(), TUTconnSingleCreateActivity.class);
                                //intent.putExtra("article_id", "16");
                                intent.putExtra("article_id", str_articleID);
                                intent.putExtra("title", str_title);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getContext(),"Only Authors can create tutorials", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }*/
                title.setText(res.getString(1));
                str_title = res.getString(1);
                description.setText(res.getString(2));
                articletext.setText(res.getString(4));

            }

        }



/*
        tutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), TUTconnFreshView.class);
                //intent.putExtra("article_id", "16");
                intent.putExtra("article_id", str_articleID);
                startActivity(intent);
            }
        });

        tutImgNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.fh.isAuthor()){
                    Intent intent = new Intent(getContext(), TUTconnSingleCreateActivity.class);
                    //intent.putExtra("article_id", "16");
                    intent.putExtra("article_id", str_articleID);
                    intent.putExtra("title", str_title);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(),"Only Authors can create tutorials", Toast.LENGTH_LONG).show();
                }

            }
        });*/

    }

    public void checkTutAvailability(String tempItem, String url){//checks if article_id is found in TUTS
        class Check extends AsyncTask<String, Void, String> {
           @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressDialog2 = ProgressDialog.show(TUTconnSingleEditActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                //Toast.makeText(getContext(),httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                httpresponsecheck = httpResponseMsg;


               if (httpresponsecheck.contains("NORESULTSFOUND")) {
                    tutImg.setImageResource(R.drawable.study_na);
                    tutImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (MainActivity.fh.isAuthor()){
                                Intent intent = new Intent(getContext(), TUTconnFreshViewEdit.class);
                                intent.putExtra("article_id", str_articleID);
                                intent.putExtra("title", str_title);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getContext(),"Only Authors can create tutorials", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    tutImg.setImageResource(R.drawable.study_available);
                    tutImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), TUTconnFreshView.class);
                            intent.putExtra("article_id",str_articleID);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            protected String doInBackground(String... params) {
                // Sending id
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
        Check check = new Check();
        check.execute(tempItem, url);
    }

    /**
     * This method is used to determine the controller category
     * @param string passed from the Article object
     * @return is equal to the switch/case in ArticleSelectionFragment
     */
    private int getControllerType(String string) {
        if (string.contains("F1")){
            return 1;
        }
        if(string.contains("F3")){
            return 3;
        }else{
            return 4;
        }
    }
}
