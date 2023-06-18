package edu.sjsu.android.foodmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager ;
import androidx.recyclerview.widget.RecyclerView ;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu ;
import android.view.MenuItem ;
import java.util.ArrayList ;
import java.util.List ;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView recyclerView ;
    private RecyclerView.Adapter myAdapter ;
    private RecyclerView.LayoutManager layoutManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main) ;

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true) ;

        layoutManager = new LinearLayoutManager(this) ;
        recyclerView.setLayoutManager(layoutManager) ;

        myAdapter = new MyAdapter(mkdir(), this) ;
        recyclerView.setAdapter(myAdapter) ;
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
                startActivity(infoIntent) ;
                return true;

            case R.id.uninstall:
                Uri link = Uri.parse("package:" + getApplicationContext().getPackageName()) ;
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, link) ;
                startActivity(uninstallIntent) ;
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private List<int[]> mkdir()
    {
        List<int[]> d = new ArrayList<>() ;

        int[] pizza = {R.drawable.pizza, R.string.pizza, R.string.pizzadesc} ;
        int[] burger = {R.drawable.burger, R.string.burger, R.string.burgerdesc} ;
        int[] fries = {R.drawable.fries, R.string.fries, R.string.friesdesc} ;
        int[] nuggets = {R.drawable.nuggets, R.string.nuggets, R.string.nuggetsdesc} ;
        int[] tacos = {R.drawable.tacos, R.string.tacos, R.string.tacosdesc} ;

        d.add(pizza) ;
        d.add(burger) ;
        d.add(fries) ;
        d.add(nuggets) ;
        d.add(tacos) ;

        return d;
    }
}
