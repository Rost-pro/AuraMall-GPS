package ru.rostpro.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.rostpro.exceptions.MException;
import ru.rostpro.gpsnavigation.R;
import ru.rostpro.mapUtils.GPSItemizedOverlay;
import ru.rostpro.mapUtils.RoutePathOverlay;
import ru.rostpro.model.BusModel;
import ru.rostpro.model.BusModelCoollection;
import ru.rostpro.navigationUtils.LegacyLastLocationFinder;
import ru.rostpro.netUtils.HttpClientWrapper;
import ru.rostpro.netUtils.MUrlCreator;
import ru.rostpro.utils.MLogger;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GPSMapActivity extends MapActivity implements LocationListener{
	
	private LocationManager locationManager;
	private long minTimeForUpdates;
	private float minDistanceForUpdates;
	private Location location;
	private LegacyLastLocationFinder finder;
	private MUrlCreator creator,creator2;
	private String serverResponse;
	private Criteria crta;
	
	
	public static String VEHICLE_ID = "2"; 
	
	ArrayList<GeoPoint> stops,path;
	String[] pathPoints;
	
	MapView mapView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mapView = (MapView) findViewById(R.id.mapview1);
        mapView.setBuiltInZoomControls(true);
		
		
		path = new ArrayList<GeoPoint>();
		stops = new ArrayList<GeoPoint>();
       	stops.add(new GeoPoint(55028822,82938213));
       	stops.add(new GeoPoint(55057480,82939095));
       	stops.add(new GeoPoint(55064608,82963986));
       	stops.add(new GeoPoint(55056890,82977032));
       	stops.add(new GeoPoint(55051654,82965788));
       	stops.add(new GeoPoint(55041574,82961797));
       	stops.add(new GeoPoint(55043320,82953622));
       	stops.add(new GeoPoint(55038967,82946391));
       	stops.add(new GeoPoint(55038143,82936606));
		
       	pathPoints =getResources().getStringArray(R.array.path_points);
       	for(int i=0; i<pathPoints.length;i += 2){
       		path.add(new GeoPoint(Integer.parseInt(pathPoints[i]),Integer.parseInt(pathPoints[i+1])));
       	}
		
		//mapUpdater = new AsyncTaskUpdater();
		finder = new LegacyLastLocationFinder(this);
		creator = new MUrlCreator();
		creator2 = new MUrlCreator();
		locationManager =  (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		location = finder.getLastBestLocation(0, 0);
		onLocationChanged(location);
		
		
		
		
		crta = new Criteria(); 
		crta.setAccuracy(Criteria.ACCURACY_FINE); 
		crta.setAltitudeRequired(false); 
		crta.setBearingRequired(false); 
		crta.setCostAllowed(true); 
		crta.setPowerRequirement(Criteria.POWER_HIGH);
		requestLocationUpdates(1, 0, crta, GPSMapActivity.this);
		
		Timer myTimer = new Timer();
		final Handler uiHandler = new Handler();
		myTimer.schedule(new TimerTask() { 
			
		    @Override
		    public void run() {
			creator.clear();
			creator.setPrefix(new String[]{"pathaura"});
			creator.setOperation("getdata.php");
			try {
				MLogger.console(getClass(), creator.getUrl());
				serverResponse = HttpClientWrapper.executeGet(GPSMapActivity.this, creator.getUrl());	
				MLogger.console(getClass(), serverResponse);
			} catch (MException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        uiHandler.post(new Runnable(){
					public void run() {
						// TODO Auto-generated method stub
						try {
							updateMap(serverResponse);
							MLogger.console(getClass(), "map update");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		        });
		    }}, 0L, 60L * 1000); 
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_starter, menu);
        return true;
    }

	public void requestLocationUpdates(long minTime, long minDistance, Criteria criteria, LocationListener listener) {
		    String provider = locationManager.getBestProvider(criteria, true);
		    if (provider != null)
		      locationManager.requestLocationUpdates(provider, minTime, minDistance, listener);
		    MLogger.console(getClass(), provider);
		  }

	public void requestPassiveLocationUpdates(long minTime, long minDistance, PendingIntent pendingIntent) {
		    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, minTimeForUpdates, minDistanceForUpdates, pendingIntent);    
		  }
	  
	public class AsyncTaskUpdater extends AsyncTask<Location,Void,String>{

		@Override
		protected String doInBackground(Location... arg0) {
			// TODO Auto-generated method stub
			String response = null;
			creator2.clear();
			creator2.setPrefix(new String[]{"pathaura"});
			creator2.setOperation("setdata.php");
			creator2.addArgument("id", VEHICLE_ID);
			MLogger.console(getClass(),creator2.getUrl().toString());
			if(arg0 != null){
			
			creator2.addArgument("posx", String.valueOf((double)arg0[0].getLatitude()));
			creator2.addArgument("posy", String.valueOf((double)arg0[0].getLongitude()));
			HttpClientWrapper.SimplePost(creator2.getUrl());	
			MLogger.console(getClass(),creator2.getUrl().toString());
		}
			return response;
		}
	}
	
	private void updateMap(String jArray) throws JSONException{
		JSONArray jsonArray = new JSONArray(jArray);
		 BusModelCoollection busArray = new BusModelCoollection();
		 BusModelCoollection stopsArray = new BusModelCoollection();
		 
		for (int i = 0; i < jsonArray.length(); i++) {
		    JSONObject jItem = jsonArray.getJSONObject(i);
		    GeoPoint point = new GeoPoint((int)(jItem.getDouble("POS_X") * 1E6),
                    						(int)(jItem.getDouble("POS_Y") * 1E6));
		    busArray.add(new BusModel(point, "Номер автобуса: " + jItem.getInt("AVTO_NAME"),jItem.getInt("ID") ,
		    											"Маршрут: " + jItem.getString("AVTO_NAME")));
		}
		
		for(GeoPoint point : stops){
			stopsArray.add(new BusModel(point, "описание остановки ",0, "название остановки: "));
		}
		
		drawOverlays(mapView, busArray,stopsArray, path);
	}
	
	public void drawOverlays(MapView mapView, BusModelCoollection buses,BusModelCoollection path_stops, List<GeoPoint> pathPoints) {
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    mapOverlays.clear();
	    mapView.invalidate();
	    mapOverlays.add(new RoutePathOverlay(pathPoints));
	    Drawable drawable2 = this.getResources().getDrawable(R.drawable.busstop_simple);
	    Drawable drawable = this.getResources().getDrawable(R.drawable.ibus);
	    Drawable drawable1 = this.getResources().getDrawable(R.drawable.ibus_blue);
	    GPSItemizedOverlay busPath[] = new GPSItemizedOverlay[buses.size()];
	    for (int i = 0; i < buses.size(); i++) {
	    	busPath[i] = new GPSItemizedOverlay(drawable, this);
	    	if(buses.get(i).getId() == 2)
	    		busPath[i] = new GPSItemizedOverlay(drawable1, this);
	    	
	        OverlayItem overlayItem = new OverlayItem(buses.get(i).getPosition(),
	        										   buses.get(i).getDecription(), 
	        											buses.get(i).getRouteName());
	        busPath[i].addOverlay(overlayItem);
	        mapOverlays.add(busPath[i]);
	    }
	    
	    
	    GPSItemizedOverlay busStops[] = new GPSItemizedOverlay[path_stops.size()];
	    for (int i = 0; i < path_stops.size(); i++) {
	    	busStops[i] = new GPSItemizedOverlay(drawable2, this);
	    	
	        OverlayItem overlayItem = new OverlayItem(path_stops.get(i).getPosition(),
										        		path_stops.get(i).getDecription(), 
										        			path_stops.get(i).getRouteName());
	        busStops[i].addOverlay(overlayItem);
	        mapOverlays.add(busStops[i]);
	    }
	    
	     
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		MLogger.console(getClass(), "Location changed..?");
		if(location != null){
		AsyncTaskUpdater mapUpdater = new AsyncTaskUpdater();
		mapUpdater.execute(location);
		
		}
			else System.out.println("location changed! but its null((((((");
	} 
	
	@Override
	protected void onResume() {
	    super.onResume();
	    requestLocationUpdates(1, 0, crta, GPSMapActivity.this);
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(GPSMapActivity.this);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
