//
//  ParqMapViewController.m
//  Parq
//
//  Created by Mark Yen on 1/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqMapViewController.h"
#define METERS_PER_MILE 1609.344
//#define FIND_SPOT_ACCURACY 30.0  //varying accuracy no needed.  
#define MAP_LOC_ACCURACY 30.0
#import <CoreLocation/CoreLocation.h>

@implementation ParqMapViewController

@synthesize mapView;
@synthesize locationManager;
@synthesize userLat;
@synthesize userLon;
@synthesize IOSGeocoder;
@synthesize BSGeocoder;

- (IBAction)goAddress{
    if(true){  //IF IOS.5 OR LATER
        if(IOSGeocoder ==nil){
            IOSGeocoder = [[CLGeocoder alloc] init];
        }
        
        [IOSGeocoder geocodeAddressString:addressField.text completionHandler:^(NSArray *placemarks, NSError *error) {
            //USE ONLY FIRST ONE.  
            CLPlacemark* place = [placemarks objectAtIndex:0];
            CLLocation* locationObject = [place location];    //we can use a location to drag out lat/lon
            
            /*  HERE TO SHOW STRUCTURE ONLY.  UNUSED
             CLLocationCoordinate2D zoomLocation;  //this object is essentially geopoint
             zoomLocation.latitude = 39.281516;  
             zoomLocation.longitude = -76.580806;
             */
            MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(locationObject.coordinate, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE);  //this is essentially zoom level in android
            
            MKCoordinateRegion adjustedRegion = [mapView regionThatFits:viewRegion]; //is this a check?               
            
            [mapView setRegion:adjustedRegion animated:YES];  //this is animateTo or w/e. 
            
        }];
        //when the addr button is pressed, search and zoom to the address given.  
    }else{
        //EARLIER THAN IOS.5, must use third party geocoding lib.  
        if(BSGeocoder==nil)
            BSGeocoder = [[BSForwardGeocoder alloc] initWithDelegate:self];
        //REGION BIASING should be used, would make search faster/accurate.  
        [BSGeocoder forwardGeocodeWithQuery:addressField.text regionBiasing:nil success:^(NSArray *results) {
            BSKmlResult* place = [results objectAtIndex:0];
            //zoom to the coordinates of the most reliable place.  
            MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(place.coordinate, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE);  //this is essentially zoom level in android
            
            MKCoordinateRegion adjustedRegion = [mapView regionThatFits:viewRegion]; //is this a check?               
            
            [mapView setRegion:adjustedRegion animated:YES];  //this is animateTo or w/e. 
            
            
        } failure:^(int status, NSString *errorMessage) {
            //goodbye
        }];
        
    }
}
-(IBAction)goUser{
    //if the user is parked
    CLLocationCoordinate2D zoomLocation;  //this object is essentially geopoint
    zoomLocation.latitude = userLat;
    zoomLocation.longitude = userLon;
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE);  //this is essentially zoom level in android
    
    MKCoordinateRegion adjustedRegion = [mapView regionThatFits:viewRegion]; //is this a check?               
    
    [mapView setRegion:adjustedRegion animated:YES];  //this is animateTo or w/e.  
    
}

- (void)locationManager:(CLLocationManager *)manager
    didUpdateToLocation:(CLLocation *)newLocation
           fromLocation:(CLLocation *)oldLocation
{
    //if accuracy isn't close enough, don't allow park, keep getting location.  display dialog. 
    //these numbers represent radius, so higher = less accurate.  
    
    //this is horiz+vert, consider using pythagoreans for better estimate??
    double newAccuracy = (newLocation.verticalAccuracy)+(newLocation.horizontalAccuracy);
    //these numbers are in meters.  
    if (newAccuracy < MAP_LOC_ACCURACY){
        //if accuracy is acceptable, turn off gps to save power.  
        [locationManager stopUpdatingLocation];
    }
    userLat = newLocation.coordinate.latitude;
    userLon = newLocation.coordinate.longitude;
    
}

-(void)startGettingLocation{
    if (nil == locationManager)
        locationManager = [[CLLocationManager alloc] init];  //if doesn't exist make new.  
    locationManager.delegate = self;
    //setting accuracy to be 10 meters.  more powerful but uses battery more.  
    locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
    [locationManager startUpdatingLocation];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}
-(void)findParking{
    //CODE TO EXECUTE WHEN USER WANTS TO FIND PARKING NEAR THEM.
    
}


#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [self startGettingLocation];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

@end
