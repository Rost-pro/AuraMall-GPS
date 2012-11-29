package ru.rostpro.activities;

import ru.rostpro.gpsnavigation.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class StarterActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        Intent mapIntent = new Intent(StarterActivity.this, GPSMapActivity.class);
        startActivity(mapIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_starter, menu);
        return true;
    }
    
    public class AsyncTaskUpdater extends AsyncTask<String,Void,String>{

	@Override
	protected String doInBackground(String... url) {
	// TODO Auto-generated method stub
		return null;
		}
		
	    @Override
	    protected void onProgressUpdate(Void... values) {
	    super.onProgressUpdate(values);
	    }
    	
		@Override
		protected void onPostExecute(String result){
		super.onPostExecute(result);
		//return result;
		}
    }
}
