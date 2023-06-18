package edu.sjsu.android.foodmenu;

import android.content.Intent ;
import android.net.Uri ;
import android.os.Bundle ;
import android.view.Menu ;
import android.view.MenuItem ;
import android.widget.ImageView ;
import android.widget.TextView ;
import androidx.appcompat.app.AppCompatActivity ;

public class DetailActivity extends AppCompatActivity
{
    private ImageView imageView ;
    private TextView nameView ;
    private TextView descriptionView ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details) ;

        Intent intent = getIntent() ;
        Bundle extras = intent.getExtras() ;
        int image = extras.getInt("image") ;
        int name = extras.getInt("name") ;
        int description = extras.getInt("description") ;

        imageView = findViewById(R.id.image);
        nameView = findViewById(R.id.name);
        descriptionView = findViewById(R.id.description);
        imageView.setImageResource(image) ;
        nameView.setText(name) ;
        descriptionView.setText(description) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu) ;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.info:
                Intent infoIntent = new Intent(this, InfoActivity.class) ;
                startActivity(infoIntent) ; //go to Information page
                return true;

            case R.id.uninstall:
                Uri link = Uri.parse("package:" + getApplicationContext().getPackageName()) ; //package URI
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, link) ;
                startActivity(uninstallIntent) ;    //uninstall the app from device
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

