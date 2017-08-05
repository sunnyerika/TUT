package com.woodamax.stm32freshview;

/**
 * Created by school_erika on 8/4/2017.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class BackgroundWorkerTUT extends AsyncTask<String, Void, String>{
    Context context;
    Toast toast;
    BackgroundWorkerTUT(Context ctx){
        context = ctx;
    }
    DatabaseHelper myDBH;
    ProgressDialog progressDialog;
    String phpResult;

    @Override
    protected void onPreExecute() {
        if(MainActivity.bwh.getCode() == 1 || MainActivity.bwh.getCode() == 2){
            MainActivity.bwh.setCode(0);
            return;
        }
        /**
         * This block is used to display status information while all articles are loaded
         */
        Log.d("Backgroundtask","Preexecute Dialog");
        progressDialog = new ProgressDialog(context);

        progressDialog.setTitle("Updating database");
        progressDialog.setMessage("Please wait... it's loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(10);
        progressDialog.show();
        new Thread(new Runnable() {
            Handler handle = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    progressDialog.incrementProgressBy(15);
                }
            };
            @Override
            public void run() {
                try {
                    while (progressDialog.getProgress() <= progressDialog
                            .getMax()) {
                        Thread.sleep(150);
                        handle.sendMessage(handle.obtainMessage());
                        if (progressDialog.getProgress() == progressDialog
                                .getMax()) {
                            progressDialog.dismiss();
                        }
                    }

                    /**Better deleting the Database at the beginning... Than working on one of the creepy methods....
                     * Than the articles are fetched, and the tables inside the database are filled
                     */

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected String doInBackground(String... params) {

        String type = params[0];// type will be passed as the first parameter
        String login_url = "http://m4xwe11o.ddns.net/MAD-Test/login.php";
        String register_url = "http://m4xwe11o.ddns.net/MAD-Test/register.php";
        String fetch_article_url = "http://m4xwe11o.ddns.net/MAD-Test/fetch_article.php";
        String fetch_articletext_url = "http://m4xwe11o.ddns.net/MAD-Test/fetch_articletext.php";
        String fetch_new_artcie_url = "http://m4xwe11o.ddns.net/MAD-Test/fetch_new_article.php";

        String HttpUrlRead = "http://10.0.2.2/TUTreadConn.php";
        String HttpUrlDeleteRecord = "http://10.0.2.2/TUTdeleteConn.php";
        String HttpUrlGetImage = "http://10.0.2.2/TUTgetImage.php";

/*
//this equals our usual function call ie. getImage(TempItem) where we pass TempItem to the excute
//but instead implementing the asyncTask class inside the function the implementation of is in a separate java class file
//called BackgroundWorkerTUT
//the type is used to specify the various ifs in BW class, so that we can use one central BW for all cases
/rather than having to implement one in eacht function
//but we make it more generic by passing the required parameters instead of declaring them within the function like the one below
//implemented in MainActivity and called in MainActivity onOptionsSelected(MenuItem)
        private void fetchArticlesDescription() {
            String type = "FetchArticleDescription";
            String article = "article";
            BackgroundWorker backgroundworker = new BackgroundWorker(this);
            backgroundworker.execute(type,article);
        }
*/
        if(type.equals("readTut")){
            try{
                String tempItem = params[1];
                String UrlRead = params[2];
                URL url = new URL(HttpUrlRead);//http://10.0.2.2/TUTreadConn.php
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
        }else if(type.equals("editTut")) {
            try {
                String surname = params[1];
                String firstname = params[2];
                String address = params[3];
                String email = params[4];
                String password = params[5];
                String confpassword = params[6];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("surname", "UTF-8") + "=" + URLEncoder.encode(surname, "UTF-8") + "&"
                        + URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(firstname, "UTF-8") + "&"
                        + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("confpassword", "UTF-8") + "=" + URLEncoder.encode(confpassword, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("testReadTut")){
            try {
                String id = params[1];
                URL url = new URL(HttpUrlRead);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * The if's captures the message from the Server:
     * Query        Returns the following values from the DB sorted by Title ASC:
     *              ID
     *              Title   -> inside {}
     *              Description ->  inside ()
     *
     * Articletext  Returns the following values from the DB sorted by Title ASC:
     *              ID  ->  inside {}
     *              Articletext ->  inside ()
     * Numbers      Returns the number of articles in the external DB
     * OK           Returned when the username and password is correct
     * YES          Returned with OK when the user is registered as author
     * INSERT       Returned when the user wants to register
     * @param result the server message
     */
    @Override
    protected void onPostExecute(String result) {
        this.phpResult = "hello";
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        toast.makeText(context.getApplicationContext(),"Tut available",Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent (context.getApplicationContext(), ArticleScreen.class);
        //intent.putExtra("tutBWresult",result);
        //context.startActivity(intent);
        //Capture app crash
        if (result == null){result = "None";}

        if(result.toString() == ""){
            Log.e("Backgroundworker PE","DB query not sucessfull");

        }
        if(result.contains("id")){
            Log.d("Backgroundworker PE","DB query  sucessfull");

        }
        if(result.contains("no")){
            Log.e("Backgroundworker PE","DB query not sucessfull");
            //getNewArticlesText(result);
        }
        /*if(result.contains("Numbers")){
            if (checkForNewArticlclesInDb(result)){
                toast.makeText(context.getApplicationContext(),"New articles available",Toast.LENGTH_SHORT).show();
            }
        }
        if(result.contains("OK")){
            if(result.contains("Yes")){
                Intent intent = new Intent (context.getApplicationContext(), ArticleScreen.class);
                intent.putExtra(ArticleScreen.EXTRA_MESSAGE,result.toString());
                context.startActivity(intent);
            }else{
                toast.makeText(context.getApplicationContext(),"Not allowed to login",Toast.LENGTH_SHORT).show();
            }
        }else if (result.contains("Insert")){
        }*/
    }

    /**
     * This function checks if new articles are available by counting the numbers of article in the external DB
     * Then it is compared with the article count of the internal DB
     * @param result the server message
     * @return
     */
    private boolean checkForNewArticlclesInDb(String result) {
        myDBH = new DatabaseHelper(context);
        Cursor res = myDBH.getArticleDescription();
        result=result.replaceAll("Numbers: ", "");
        while (res.moveToNext()) {
            if(result.matches(res.getString(0))){
                return false;
            }
        }
        return true;
    }

    /**
     * Creepy little function - it makes all the magic needed to parse the values correct
     * Woolmice said DON'T touch it !
     * @param query the server message
     */
    private void getNewArticlesDescription(String query) {
        query=query.replaceAll("]", "X\n");
        //toast.makeText(context.getApplicationContext(),query,Toast.LENGTH_SHORT).show();
        Scanner scanner = new Scanner(query);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String title = line.substring(line.indexOf("{")+1,line.indexOf("}"));
            String desc = line.substring(line.indexOf("(")+1,line.indexOf(")"));
            insertIntoDatabase(title,desc);
        }
    }

    /**
     * Creepy little function - it makes all the magic needed to parse the values correct
     * Woolmice said DON'T touch it !
     * @param query the server message
     */
    private void getNewArticlesText(String query) {
        query=query.replaceAll("]", "X\n");
        Scanner scanner = new Scanner(query);
        String id = "1";
        int id2;
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String text = line.substring(line.indexOf("(")+1,line.indexOf(")"));
            updateDatabase(id,text);
            id2 = Integer.parseInt(id);
            id2++;
            id = Integer.toString(id2);
        }
    }

    /**
     * Used to update the existing articles with their text
     * @param id used to identify the article in the local DB
     * @param text the server message
     */
    private void updateDatabase(String id,String text) {
        myDBH = new DatabaseHelper(context);
        myDBH.updateArticleDataText(id,text);
    }

    /**
     * Creepy little function too - it makes all the checks needed to insert data only if its not there
     * @param title the server message
     * @param desc the server message
     */
    private void insertIntoDatabase(String title, String desc) {
        myDBH = new DatabaseHelper(context);
        Cursor res = myDBH.getArticleDescription();
        if(res.getCount() == 0) {
            myDBH.insertArticleData(title,desc," "," ");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            if(title.equals(res.getString(1)) || desc.equals(res.getString(2))){
                return;
            }
        }
        myDBH.insertArticleData(title,desc," "," ");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
