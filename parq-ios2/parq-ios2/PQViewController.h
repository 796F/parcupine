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

@interface PQViewController : UIViewController <MKMapViewDelegate, CLLocationManagerDelegate> {
    CLLocationManager *locationManager;
}
@property (weak, nonatomic) IBOutlet MKMapView *mapView;

@end
