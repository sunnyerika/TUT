package com.woodamax.stm32freshview;

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

/**
 * Created by maxim on 07.04.2017.
 * This class is used to handle the DB connection for Login and Register
 */

public class BackgroundWorker extends AsyncTask<String, Void, String>{
    Context context;
    Toast toast;
    BackgroundWorker(Context ctx){
        context = ctx;
    }
    DatabaseHelper myDBH;
    ProgressDialog progressDialog;

    /**
     * The background works uses 5 different php scripts:
     * login.php                -> Used by the LoginScreen.java Activity
     * register.php             -> Used by the RegisterScreen.java Activity followed by the SubmitClickListener.java
     * fetch_article.php        -> Used to get the Title and the Description of an article from the external DB
     * fetch_articletext.php    -> Used to get the Text for an article
     * fetch_new_article.php    -> Used to get the actual number of all articles and compare them to the local DB
     */
    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String login_url = "http://m4xwe11o.ddns.net/MAD-Test/login.php";
        String register_url = "http://m4xwe11o.ddns.net/MAD-Test/register.php";
        String fetch_article_url = "http://m4xwe11o.ddns.net/MAD-Test/fetch_article.php";
        String fetch_articletext_url = "http://m4xwe11o.ddns.net/MAD-Test/fetch_articletext.php";
        String fetch_new_artcie_url = "http://m4xwe11o.ddns.net/MAD-Test/fetch_new_article.php";

        if(type.equals("Login")){
            try{
                String username = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("Register")) {
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
        }else if(type.equals("FetchArticleDescription")){
            try {
                String article = params[1];
                URL url = new URL(fetch_article_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("article", "UTF-8") + "=" + URLEncoder.encode(article, "UTF-8");
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
        }else if(type.equals("FetchNewArticle")){
            try {
                String article = params[1];
                URL url = new URL(fetch_new_artcie_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("article", "UTF-8") + "=" + URLEncoder.encode(article, "UTF-8");
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
        }else if(type.equals("FetchArticleText")){
            try {
                String article = params[1];
                URL url = new URL(fetch_articletext_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("articletext", "UTF-8") + "=" + URLEncoder.encode(article, "UTF-8");
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
        }else if(type.equals("writeQuestionsInDb")) {
            myDBH = new DatabaseHelper(context);
            for(int i=1;i<=MainActivity.bwh.getCount();i++){
                String question = params[i];
                MainActivity.qh.questions.add(question);
                Log.d("BW",MainActivity.qh.questions.get(i-1));
                if(!(myDBH.insertQuestionData(question))){
                    Log.e("Backgroundworker","Could not insert Question");
                    MainActivity.bwh.setError(1);
                }
            }
        }else if(type.equals("writeAnswersInDb")) {
            myDBH = new DatabaseHelper(context);
            for(int i=1;i<=MainActivity.bwh.getCount();i++){
                String answer = params[i];
                MainActivity.qh.answers.add(answer);
                Log.d("BW",MainActivity.qh.answers.get(i-1));
                if(!(myDBH.insertAnswerData(answer))){
                    Log.e("Backgroundworker","Could not insert Answer");
                    MainActivity.bwh.setError(1);
                }
            }
        }else if(type.equals("writeQusteionAnswerInDb")) {
            myDBH = new DatabaseHelper(context);
            Cursor question = myDBH.getQuestions();

            int q=0;
            while (question.moveToNext()){
                Cursor answer = myDBH.getAnswers();
                int a= 0;
                while(answer.moveToNext()){
                    if(a==8 && q==0){
                        myDBH.insertCorrectAnswerData(question.getInt(0),answer.getInt(0));
                        Log.e("Question",question.getString(1));
                        Log.e("Answer",answer.getString(1));
                    }
                    if(a==13 && q==1){
                        myDBH.insertCorrectAnswerData(question.getInt(0),answer.getInt(0));
                        Log.e("Question",question.getString(1));
                        Log.e("Answer",answer.getString(1));
                    }
                    if(a==10 && q==2 ){
                        myDBH.insertCorrectAnswerData(question.getInt(0),answer.getInt(0));
                        Log.e("Question",question.getString(1));
                        Log.e("Answer",answer.getString(1));
                    }
                    if((a==11||a==15) && q==3){
                        myDBH.insertCorrectAnswerData(question.getInt(0),answer.getInt(0));
                        Log.e("Question",question.getString(1));
                        Log.e("Answer",answer.getString(1));
                    }
                    a++;
                }
                q++;
            }
        }
        return null;
    }
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
        //Capture app crash
        if (result == null){result = "None";}

        if(result.toString() == ""){
            Log.e("Backgroundworker PE","DB query not sucessfull");
        }
        if(result.contains("Query")){
            getNewArticlesDescription(result);
        }
        if(result.contains("Articletext")){
            getNewArticlesText(result);
        }
        if(result.contains("Numbers")){
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
        }
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
