//
//  PQMapViewController.h
//  parq-ios2
//
//  Created by Mark Yen on 3/16/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import "DataLayer.h"
#import "PQAppDelegate.h"

@interface PQMapViewController : UIViewController <MKMapViewDelegate, CLLocationManagerDelegate,UISearchBarDelegate,UIGestureRecognizerDelegate, UITableViewDelegate, UIAlertViewDelegate> {
    DataLayer * dataLayer;
    NSManagedObjectContext* managedObjectContext;
    CLLocationManager *locationManager;
}

@property (nonatomic, retain) NSManagedObjectContext *managedObjectContext;

@property (weak, nonatomic) IBOutlet MKMapView *map;
@property (weak, nonatomic) IBOutlet UISearchBar *topSearchBar;
@property (weak, nonatomic) IBOutlet UINavigationItem *navigationBar;
@property (weak, nonatomic) IBOutlet UISegmentedControl *bottomSpotSelectionBar;
@property (weak, nonatomic) IBOutlet UISegmentedControl *topSpotSelectionBar;
@property (weak, nonatomic) IBOutlet UIView *availabilitySelectionView;
@property (weak, nonatomic) IBOutlet UIView *bottomSpotSelectionView;
@property (weak, nonatomic) IBOutlet UIView *topSpotSelectionView;

@property (weak, nonatomic) MKCircle* gCircle;
@property (nonatomic, retain) NSMutableArray* callouts;
@property (nonatomic, retain) NSMutableArray* calloutLines;
@property (nonatomic, retain) NSMutableArray* grids;
@property (nonatomic, retain) NSMutableArray* streets;
@property (nonatomic, retain) NSMutableArray* spots;
@property (nonatomic, retain) CLGeocoder* geocoder;
@property (nonatomic) CLLocationCoordinate2D user_loc;
@property (nonatomic) bool user_loc_isGood;
@property (nonatomic, retain) MKCircle* desired_spot;

@end
