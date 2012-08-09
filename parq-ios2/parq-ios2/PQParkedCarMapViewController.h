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
#import "SpotInfo.h"
@class PQMapViewController;
@interface PQParkedCarMapViewController : UIViewController <MKMapViewDelegate>
@property (weak, nonatomic) IBOutlet MKMapView *map;
@property (nonatomic) CLLocationCoordinate2D parkedCarCoordinate;
@property (nonatomic, retain) SpotInfo* spotInfo;

@property (weak, nonatomic) PQMapViewController* parent;

@end

