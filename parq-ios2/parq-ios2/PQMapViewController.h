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
#import "SpotInfo.h"
#import "MKShape+Color.h"
#import "UIColor+Parq.h"
#import "CalloutMapAnnotation.h"
#import "CalloutMapAnnotationView.h"
#import "PQPinAnnotation.h"
#import "PQBookmarksViewController.h"
#import "LoginNavigationController.h"
#import "PQParkedCarAnnotation.h"
#import "Segment.h"
#import "Spot.h"
#import "Grid.h"
#import "PopOutView.h"

//user @class to avoid cycles, which breaks the compiler
@class NetworkLayer;
@interface PQMapViewController : UIViewController 
<MKMapViewDelegate, 
CLLocationManagerDelegate,
UISearchBarDelegate,
UIGestureRecognizerDelegate, 
UITableViewDelegate, 
UIActionSheetDelegate,
UIAlertViewDelegate> {
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

@property (weak, nonatomic) IBOutlet UIView *dropPinSelectionView;
@property (weak, nonatomic) IBOutlet UIView *availabilitySelectionView;
@property (weak, nonatomic) IBOutlet UIView *justParkSelectionView;
@property (weak, nonatomic) IBOutlet UIView *bottomSpotSelectionView;
@property (weak, nonatomic) IBOutlet UIView *topSpotSelectionView;

@property (weak, nonatomic) IBOutlet UIImageView *gradientIcon;
@property (weak, nonatomic) IBOutlet UIButton *findMeButton;
@property (weak, nonatomic) IBOutlet UIButton *parkMeButton;
@property (weak, nonatomic) IBOutlet UIButton *dropPinButton;
@property (weak, nonatomic) IBOutlet UIButton *cancelDropPinButton;
//internal management stuff
@property (weak, nonatomic) MKCircle* gCircle;
@property (nonatomic, retain) NSMutableArray* callouts;
@property (nonatomic, retain) NSMutableArray* calloutLines;

@property (nonatomic, retain) CLGeocoder* geocoder;
@property (nonatomic) CLLocationCoordinate2D user_loc;
@property (nonatomic) bool user_loc_isGood;
@property (atomic) bool isDroppingPin;

@property (nonatomic, retain) PQSpotAnnotation* desired_spot;
@property (nonatomic, retain) SpotInfo* spotInfo;
@property (atomic, retain) PQParkedCarAnnotation* bookmarkPin;

@property (nonatomic) MKCoordinateRegion oldStreetLevelRegion;
@property (atomic) bool shouldNotClearOverlays;
@property (atomic) bool doubleTapAlreadyCalled;

//list of current blocks
@property (strong, nonatomic) NSMutableArray* currentMicroBlockIds;
//@property (strong, nonatomic) NSMutableArray* currentGridMBIDs;
//@property (strong, nonatomic) NSMutableArray* currentSpotMBIDs;
@property (strong, nonatomic) NSMutableArray* allInsideCircle;

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

@property (weak, nonatomic) IBOutlet UIView* popOutSpotNumberView;
@property (weak, nonatomic) IBOutlet    UITextField* popOutSpotNumberField;

//callback methods for updating map
-(void) addNewOverlays:(NSDictionary*) overlayMap OfType:(EntityType) entityType;
-(void) updateOverlays:(NSDictionary*) updateMap OfType:(EntityType) entityType;

-(CLLocationCoordinate2D)topRightOfMap;
-(CLLocationCoordinate2D)botLeftOfMap;

-(void) showBookmarkWithLocation:(CLLocationCoordinate2D*) coord AndAnnotation:(id <MKAnnotation>)annotation;


@end