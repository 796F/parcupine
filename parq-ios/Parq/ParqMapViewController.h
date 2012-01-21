//
//  ParqMapViewController.h
//  Parq
//
//  Created by Mark Yen on 1/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "ParqSpotViewController.h"

#import "BSGoogleV3KmlParser.h"
#import "BSForwardGeocoder.h"
#import "BSAddressComponent.h"

@interface ParqMapViewController : UIViewController <MKMapViewDelegate,CLLocationManagerDelegate>{
    IBOutlet UITextField *addressField;
    IBOutlet MKMapView *mapView;
    
}
@property double userLat;
@property double userLon;
@property (nonatomic, retain) CLLocationManager* locationManager;
@property (nonatomic, retain) MKMapView *mapView;
@property (nonatomic, retain) CLGeocoder* IOSGeocoder;
@property (nonatomic, retain) BSForwardGeocoder* BSGeocoder;
-(IBAction)goAddress;
-(IBAction)goUser;

@end
