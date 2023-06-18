package edu.sjsu.android.foodmenu;
import android.content.Intent ;
import android.net.Uri ;
import android.os.Bundle ;
import android.view.Menu ;
import android.view.MenuItem ;
import android.view.View;
import android.widget.Button ;
import androidx.appcompat.app.AppCompatActivity ;

public class InfoActivity extends AppCompatActivity
{
    private Button phoneButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.info) ;

        phoneButton = (Button) findViewById(R.id.phone) ;
        phoneButton.setOnClickListener( new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:8888888")) ;
                startActivity(callIntent) ;
            }
        }) ;
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
            case R.id.info: //click Information
                Intent infoIntent = new Intent(this, InfoActivity.class) ;
                startActivity(infoIntent) ; //go to Information page
                return true;

            case R.id.uninstall: //click Uninstall
                Uri link = Uri.parse("package:" + getApplicationContext().getPackageName()) ;
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, link) ;
                startActivity(uninstallIntent) ;
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
