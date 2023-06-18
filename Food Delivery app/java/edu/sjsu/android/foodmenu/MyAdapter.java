package edu.sjsu.android.foodmenu;


import android.app.AlertDialog ;
import android.content.Context ;
import android.content.DialogInterface ;
import android.content.Intent ;
import android.view.LayoutInflater ;
import android.view.View ;
import android.view.ViewGroup ;
import android.widget.ImageView ;
import android.widget.TextView ;
import androidx.recyclerview.widget.RecyclerView ;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private List<int[]> value ;
    private Context cont ;
    public MyAdapter(List<int[]> myDataset, Context context)
    {
        value = myDataset ;
        this.cont = context ;
    }
    @Override public int getItemCount() {return value.size() ; }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView icon ;
        public TextView title ;
        public View layout ;

        public ViewHolder(View v)
        {
            super(v) ;
            layout = v ;
            icon = (ImageView) v.findViewById(R.id.icon) ;
            title = (TextView) v.findViewById(R.id.title) ;
        }
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()) ;
        View v = inflater.inflate(R.layout.rowlayout, parent, false) ;

        MyAdapter.ViewHolder view = new MyAdapter.ViewHolder(v) ;
        return view;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int pos)
    {
        final int[] data = value.get(pos) ;
        holder.icon.setImageResource(data[0]) ;
        holder.title.setText(data[1]) ;

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class) ;

            if (pos != getItemCount() - 1)
            {
                proceed(intent, data, v) ;
            }
            else
            {
                makeDialog(intent, data, v) ;
            }
        }) ;
    }
    private void makeDialog(Intent intent, int[] data, View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont) ;
        builder.setMessage(R.string.warning)
                .setPositiveButton(R.string.yes, (dialog, id) -> proceed(intent, data, v))
                .setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel()) ;

        AlertDialog alert = builder.create() ;
        alert.show() ;
    }

    private void proceed(Intent intent, int[] data, View v)
    {
        intent.putExtra("image", data[0]) ;
        intent.putExtra("name", data[1]) ;
        intent.putExtra("description", data[2]) ;
        v.getContext().startActivity(intent) ;
    }
}
