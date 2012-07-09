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

#import "MKShape+Color.h"
#import "UIColor+Parq.h"
#import "CalloutMapAnnotation.h"
#import "CalloutMapAnnotationView.h"

#import "Segment.h"
#import "Spot.h"
#import "Grid.h"

//user @class to avoid cycles, which breaks the compiler
@class NetworkLayer;
@interface PQMapViewController : UIViewController 
<MKMapViewDelegate, 
CLLocationManagerDelegate,
UISearchBarDelegate,
UIGestureRecognizerDelegate, 
UITableViewDelegate, 
UIActionSheetDelegate> {
    DataLayer * dataLayer;
    NetworkLayer* networkLayer;
    NSManagedObjectContext* managedObjectContext;
    CLLocationManager *locationManager;
}
//core data stuff
@property (nonatomic, retain) NSManagedObjectContext *managedObjectContext;

//map and some control stuff
@property (weak, nonatomic) IBOutlet MKMapView *map;
@property (weak, nonatomic) IBOutlet UISearchBar *topSearchBar;
@property (weak, nonatomic) IBOutlet UINavigationItem *navigationBar;

//segmented control stuff
@property (weak, nonatomic) IBOutlet UISegmentedControl *bottomSpotSelectionBar;
@property (weak, nonatomic) IBOutlet UISegmentedControl *topSpotSelectionBar;
@property (weak, nonatomic) IBOutlet UISegmentedControl *availabilitySelectionBar;
@property (weak, nonatomic) IBOutlet UIView *availabilitySelectionView;
@property (weak, nonatomic) IBOutlet UIView *bottomSpotSelectionView;
@property (weak, nonatomic) IBOutlet UIView *topSpotSelectionView;
@property (weak, nonatomic) IBOutlet UIImageView *gradientIcon;

//internal management stuff
@property (weak, nonatomic) MKCircle* gCircle;
@property (nonatomic, retain) NSMutableArray* callouts;
@property (nonatomic, retain) NSMutableArray* calloutLines;

//@property (nonatomic, retain) NSMutableArray* grids;
//@property (nonatomic, retain) NSMutableArray* streets;
//@property (nonatomic, retain) NSMutableArray* spots;

@property (nonatomic, retain) CLGeocoder* geocoder;
@property (nonatomic) CLLocationCoordinate2D user_loc;
@property (nonatomic) bool user_loc_isGood;

@property (nonatomic, retain) MKCircle* desired_spot;

@property (nonatomic) MKCoordinateRegion oldStreetLevelRegion;
@property (atomic) bool shouldNotClearOverlays;

//microblock organization
@property (strong, nonatomic) NSMutableArray* currentMicroBlockIds; //list of current blocks
 /*         _         
          /   GID - *      
    MID  ---- GID - * 
          \_  GID - * 
 
    MID ...
    MID ...
    
    Structure of the following 3 dictionaries.  
 */
@property (strong, nonatomic) NSMutableDictionary* gridMicroBlockMap;
@property (strong, nonatomic) NSMutableDictionary* streetMicroBlockMap;
@property (strong, nonatomic) NSMutableDictionary* spotMicroBlockMap;

//callback methods for updating map
-(void) addNewOverlays:(NSDictionary*) overlayMap;
-(void) updateOverlays:(NSDictionary*) updateMap;

-(CLLocationCoordinate2D)topRightOfMap;
-(CLLocationCoordinate2D)botLeftOfMap;
@end