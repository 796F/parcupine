//
//  PQViewController.h
//  parq-ios2
//
//  Created by Mark Yen on 3/16/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>


#define METERS_PER_MILE 1609.344
#define ZOOM_GRID 0
#define ZOOM_BLOCK 1
#define ZOOM_SPOT 2

#define BLOCK_MAP_SPAN 0.011796
#define SPOT_MAP_SPAN 0.001474

#define GPS_LAUNCH_ALERT 10

@interface PQViewController : UIViewController <MKMapViewDelegate, CLLocationManagerDelegate,UISearchBarDelegate,UIGestureRecognizerDelegate> {
        
    CLLocationManager *locationManager;
}
@property (weak, nonatomic) IBOutlet MKMapView *mapView;
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;

@property int ZOOM_STATE; //state used to determine how we should handle the gestures.     
@property (nonatomic, retain) CLGeocoder* IOSGeocoder;

@property float destLat;
@property float destLon;

@end
