package com.objects;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapOverlays extends ItemizedOverlay{

	private ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
	Context mContext;
	
	public MapOverlays(Drawable marker){
		super(boundCenterBottom(marker));
	}
	
	public MapOverlays(Drawable marker, Context con){
		super(boundCenterBottom(marker));
		mContext = con;
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
	protected boolean onTap(int index){
		OverlayItem item = overlayItems.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
}
