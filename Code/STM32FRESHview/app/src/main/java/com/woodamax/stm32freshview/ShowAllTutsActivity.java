package com.woodamax.stm32freshview;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowAllTutsActivity extends AppCompatActivity {

    ListView tutListView;
    ProgressBar progressBar;

    String HttpUrl = "http://10.0.2.2/TUTreadALL.php";
    List<String> IdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_tuts);
        tutListView = (ListView)findViewById(R.id.listview1);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        new GetHttpResponse(ShowAllTutsActivity.this).execute();
        //Adding ListView Item click Listener.
        tutListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowAllTutsActivity.this,ShowSingleRecordActivity.class);
                intent.putExtra("ListViewValue", IdList.get(position).toString());
                startActivity(intent);
                finish();
            }
        });
    }

    // JSON parse class started from here.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        String JSonResult;
        List<Tutorial> tutList;
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
            // Passing HTTP URL to HttpServicesClass Class.
            HttpServicesClass httpServicesClass = new HttpServicesClass(HttpUrl);
            try
            {
                httpServicesClass.ExecutePostRequest();
                if(httpServicesClass.getResponseCode() == 200)
                {
                    JSonResult = httpServicesClass.getResponse();
                    if(JSonResult != null)
                    {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(JSonResult);
                            JSONObject jsonObject;
                            Tutorial tutorial;// tutTitle is the only var
                            tutList = new ArrayList<Tutorial>();
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                tutorial = new Tutorial();
                                jsonObject = jsonArray.getJSONObject(i);

                                IdList.add(jsonObject.getString("id").toString());

                                tutorial.tutTitle = jsonObject.getString("title").toString();
                                tutList.add(tutorial);
                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpServicesClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
            progressBar.setVisibility(View.GONE);
            tutListView.setVisibility(View.VISIBLE);
            ListAdapterClass adapter = new ListAdapterClass(tutList, context);
            tutListView.setAdapter(adapter);
        }
    }
}

