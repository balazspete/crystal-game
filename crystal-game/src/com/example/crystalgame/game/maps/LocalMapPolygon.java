package com.example.crystalgame.game.maps;

import java.util.ArrayList;

import android.graphics.Color;

import com.example.crystalgame.library.data.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class LocalMapPolygon {

	double posX1 =53.3436688;
	double posY1 = -6.245169;
	
	double posX2 =53.341424;
	double posY2 = -6.211535;

	public LocalMapPolygon() {
		// TODO Auto-generated constructor stub
		 
	}
	public GoogleMap createPolygon(GoogleMap googleMap)
	{
		
		Polygon polygon = googleMap.addPolygon(new PolygonOptions().add(new LatLng(53.3436688,-6.247169), new LatLng(53.3456688, -6.247169), new LatLng(53.3456688, -6.245169),new LatLng(53.3436688, -6.245169)).strokeColor(Color.BLACK));
		return googleMap;
	}
	public boolean playerPositionLocalMap(double latitude, double longitude)
	{
		/*if((latitude>= posX1 && latitude<=posX2) && (Double.compare(posY2,longitude)>=1   && (Double.compare(longitude, posY1)<1)) )
		{
			return true;
		}*/
		return true;
		//
	}
	public LatLng zoomCenterPoint(ArrayList<Location> gameBoundaryPoints)
	{
		double posLat = 0.0;
		double posLong = 0.0;
		for(Location location: gameBoundaryPoints)
		{
			posLat = posLat + location.getLatitude();
			posLong = posLong +location.getLongitude();
					
		}
		LatLng latLng = new LatLng((posLat)/gameBoundaryPoints.size(), (posLong)/gameBoundaryPoints.size());
		return latLng;
	}

}
