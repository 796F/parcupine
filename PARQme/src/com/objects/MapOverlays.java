package com.objects;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class MapOverlays extends BalloonItemizedOverlay<OverlayItem>{

	private ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
	Context mContext;
	
	public MapOverlays(Drawable marker, MapView mapView){
		super(boundCenterBottom(marker), mapView);
		mContext = mapView.getContext();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlayItems.get(i);
	}
	@Override
	public int size() {
		return overlayItems.size();
	}
	
	public void addOverlay(OverlayItem ov){
		overlayItems.add(ov);
		populate();
	}
	@Override
	protected boolean onBalloonTap(int index, OverlayItem item){
		Toast.makeText(mContext, "YOU JUST TOUCHED ME!!!" +index, Toast.LENGTH_LONG).show();
		return true;
	}
}
