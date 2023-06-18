package edu.sjsu.android.androidmultithreading ;

import androidx.appcompat.app.AppCompatActivity ;

import android.app.ProgressDialog ;
import android.graphics.Bitmap ;
import android.graphics.BitmapFactory ;
import android.os.AsyncTask ;
import android.os.Bundle ;
import android.os.Handler;
import android.os.Message ;
import android.view.View ;
import android.widget.EditText ;
import android.widget.ImageView ;
import android.widget.Toast ;

import java.io.InputStream ;
import java.net.HttpURLConnection ;
import java.net.URL ;

public class MainActivity extends AppCompatActivity
{
    private ImageView imageView ;
    private ProgressDialog progressDialog ;
    private ThreadHandlerMainClass handler;
    private String errorMessage = "" ;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main) ;

        imageView = findViewById(R.id.image) ;
        progressDialog = new ProgressDialog(this) ;
        handler = new ThreadHandlerMainClass(imageView) ;
    }

    private Bitmap downloadBitmap(String string)
    {
        try
        {
            URL url = new URL(string) ;

            HttpURLConnection urlC = (HttpURLConnection) url.openConnection() ;
            urlC.setDoInput(true) ;
            urlC.connect() ;

            InputStream input = urlC.getInputStream() ;
            Bitmap myBitmap = BitmapFactory.decodeStream(input) ;
            urlC.disconnect() ;
            return myBitmap;
        }
        catch (Exception e)
        {
            if (progressDialog.isShowing()) progressDialog.dismiss() ;
            errorMessage = "" + e.getMessage() ;
            return null;
        }
    }

    public void runRunnable(View view)
    {
        try
        {
            EditText url = findViewById(R.id.url) ;
            if (url.getText().toString().length() == 0) // check if empty url
            {
                Toast.makeText(this, "Please enter image URL first", Toast.LENGTH_SHORT).show() ;
                return;
            }

            // set up progress dialog
            progressDialog.setCancelable(false) ;
            progressDialog.setCanceledOnTouchOutside(false) ;
            progressDialog.setTitle("Download") ;
            progressDialog.setMessage("Downloading via Runnable") ;
            progressDialog.show() ;

            new Thread(new Runnable()
            {
                public void run()
                {
                    Bitmap image = downloadBitmap(url.getText().toString()) ;
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            if (image != null) imageView.setImageBitmap(image) ;
                            if (progressDialog.isShowing()) progressDialog.dismiss() ;
                        }
                    });
                }
            }).start() ;
        }
        catch (Exception e)
        {
            if (progressDialog.isShowing()) progressDialog.dismiss() ;
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show() ;
        }
    }

    public void runMessage(View view)
    {
        try
        {
            EditText url = findViewById(R.id.url) ;
            if (url.getText().toString().length() == 0) // check if empty url
            {
                Toast.makeText(this, "Please enter image URL first", Toast.LENGTH_SHORT).show() ;
                return;
            }

            // set up progress dialog
            progressDialog.setCancelable(false) ;
            progressDialog.setCanceledOnTouchOutside(false) ;
            progressDialog.setTitle("Download") ;
            progressDialog.setMessage("Downloading via Message") ;
            progressDialog.show() ;

            new Thread(new Runnable()   // new thread
            {
                public void run()
                {
                    Bitmap image = downloadBitmap(url.getText().toString()) ;

                    Message msg = handler.obtainMessage(0, image) ;
                    handler.sendMessage(msg) ;
                    if (progressDialog.isShowing()) progressDialog.dismiss() ;
                }
            }).start() ;
        }
        catch (Exception e)
        {
            if (progressDialog.isShowing()) progressDialog.dismiss() ;
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show() ;
        }
    }

    public void runAsyncTask(View view)
    {
        EditText url = findViewById(R.id.url) ;
        if (url.getText().toString().length() == 0)
        {
            Toast.makeText(this, "Please enter image URL first", Toast.LENGTH_SHORT).show() ;
            return;
        }

        // set up progress dialog
        progressDialog.setCancelable(false) ;
        progressDialog.setCanceledOnTouchOutside(false) ;
        progressDialog.setTitle("Download") ;
        progressDialog.setMessage("Downloading via AsyncTask") ;
        progressDialog.show() ;

        AsyncTaskFromUrl asyncTaskFromUrl = new AsyncTaskFromUrl() ;
        asyncTaskFromUrl.execute("" + url.getText().toString()) ;
    }

    public void resetImage(View view)
    {
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.android)) ;
    }

    public class AsyncTaskFromUrl extends AsyncTask<String, Void, Bitmap>
    {
        @Override protected Bitmap doInBackground(String... params) {return downloadBitmap(params[0]);}

        @Override protected void onPostExecute(Bitmap bitmap)
        {
            if (bitmap == null)
            {
                Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show() ;
                return;
            }
            imageView.setImageBitmap(bitmap) ;
            if (progressDialog.isShowing()) progressDialog.dismiss() ;
        }
    }

    class ThreadHandlerMainClass extends Handler
    {
        private ImageView imageView ;

        public ThreadHandlerMainClass(ImageView view) {imageView = view ;}

        public void handleMessage(Message msg)
        {
            if(((Bitmap) msg.obj) != null) imageView.setImageBitmap((Bitmap)msg.obj) ;
            else Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show() ;
        }
    }
}
