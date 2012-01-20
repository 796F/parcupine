//
//  ParqMapViewController.m
//  Parq
//
//  Created by Mark Yen on 1/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqMapViewController.h"
#define METERS_PER_MILE 1609.344
#import <CoreLocation/CoreLocation.h>

@implementation ParqMapViewController

@synthesize mapView;
@synthesize locationManager;
@synthesize userLat;
@synthesize userLon;

- (IBAction)goAddress{
        //when the addr button is pressed, search and zoom to the address given.  
    CLLocationCoordinate2D zoomLocation;  //this object is essentially geopoint
    zoomLocation.latitude = 39.281516;
    zoomLocation.longitude = -76.580806;
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE);  //this is essentially zoom level in android
    
    MKCoordinateRegion adjustedRegion = [mapView regionThatFits:viewRegion]; //is this a check?               
    
    [mapView setRegion:adjustedRegion animated:YES];  //this is animateTo or w/e. 
    
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

- (void)startSignificantChangeUpdates
{
    // Create the location manager if this object does not
    // already have one.
    if (nil == locationManager)
        locationManager = [[CLLocationManager alloc] init];
    
    locationManager.delegate = self;
    [locationManager startMonitoringSignificantLocationChanges];
}

- (void)locationManager:(CLLocationManager *)manager
    didUpdateToLocation:(CLLocation *)newLocation
           fromLocation:(CLLocation *)oldLocation
{
    // If it's a relatively recent event, turn off updates to save power
    NSDate* eventDate = newLocation.timestamp;
    NSTimeInterval howRecent = [eventDate timeIntervalSinceNow];
    if (abs(howRecent) < 15.0)
    {
        NSLog(@"latitude %+.6f, longitude %+.6f\n",
              newLocation.coordinate.latitude,
              newLocation.coordinate.longitude);
        userLat = newLocation.coordinate.latitude;
        userLon = newLocation.coordinate.longitude;
        
    }
    // else skip the event and process the next one.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [self startSignificantChangeUpdates];
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
