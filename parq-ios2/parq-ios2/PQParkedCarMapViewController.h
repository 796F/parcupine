//
//  PQCarLocationMapViewController.h
//  Parq
//
//  Created by Mark Yen on 5/31/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>

@interface PQParkedCarMapViewController : UIViewController <MKMapViewDelegate>
@property (weak, nonatomic) IBOutlet MKMapView *map;
@property (nonatomic) CLLocationCoordinate2D parkedCarCoordinate;
@end
