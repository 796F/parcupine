//
//  PQViewController.m
//  parq-ios2
//
//  Created by Mark Yen on 3/16/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQViewController.h"
#import "MKPolygon+Color.h"

@implementation PQViewController
@synthesize mapView;
@synthesize searchBar;
@synthesize IOSGeocoder;

#define METERS_PER_MILE 1609.344


-(void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    
    //remove all existing overlays and stuff,
    [mapView removeOverlays: mapView.overlays];
    NSString* addr = @"new york";
    //do forward geocode and zoom to result.  
    [IOSGeocoder geocodeAddressString:addr completionHandler:^(NSArray *placemarks, NSError *error) {
        //USE ONLY FIRST ONE.  
        CLPlacemark* place = [placemarks objectAtIndex:0];
        CLLocation* locationObject = [place location];    //we can use a location to drag out lat/lon
        
        /*  HERE TO SHOW STRUCTURE ONLY.  UNUSED
         CLLocationCoordinate2D zoomLocation;  //this object is essentially geopoint
         zoomLocation.latitude = 39.281516;  
         zoomLocation.longitude = -76.580806;
         */
        
        //set the zoom to fit 12 grids perfectly
        
        MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(locationObject.coordinate, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE);  //this is essentially zoom level in android
        
        MKCoordinateRegion adjustedRegion = [mapView regionThatFits:viewRegion]; //is this a check?               
        
        [mapView setRegion:adjustedRegion animated:YES];  //this is animateTo or w/e. 
        
    }];
    
    //draw the grids and set their color.  
    
    
}

-(void)searchBarBookmarkButtonClicked:(UISearchBar *)searchBar{
    //load recently parked spots.  
    
}


-(MKOverlayView *)mapView:(MKMapView *)mapView viewForOverlay:(id<MKOverlay>)overlay{
    if([overlay isKindOfClass:[MKPolygon class]]){
        MKPolygon* casted = (MKPolygon*) overlay;
        MKPolygonView *view = [[MKPolygonView alloc] initWithOverlay:overlay];
        view.lineWidth=1;
        NSLog(@"Color got as:%d", casted.color);
        //need subclass of MKOverlay, with additional int field to decide color.  
        view.strokeColor=[UIColor blueColor];
        view.fillColor=[[UIColor blueColor] colorWithAlphaComponent:0.2];
        return view;
    }
    return nil;    
}

- (IBAction)gridButtonPressed:(id)sender {
    UISearchBar* searchBar = self.searchDisplayController.searchBar;
    [mapView removeOverlays:mapView.overlays];
    NSString* addr = @"University of Maryland";
    //do forward geocode and zoom to result.  
    [IOSGeocoder geocodeAddressString:addr completionHandler:^(NSArray *placemarks, NSError *error) {
        //USE ONLY FIRST ONE.  
        CLPlacemark* place = [placemarks objectAtIndex:0];
        CLLocation* locationObject = [place location];    //we can use a location to drag out lat/lon
        
        /*  HERE TO SHOW STRUCTURE ONLY.  UNUSED
         CLLocationCoordinate2D zoomLocation;  //this object is essentially geopoint
         zoomLocation.latitude = 39.281516;  
         zoomLocation.longitude = -76.580806;
         */
        
        //set the zoom to fit 12 grids perfectly
        
        MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(locationObject.coordinate, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE);  //this is essentially zoom level in android
        
        MKCoordinateRegion adjustedRegion = [mapView regionThatFits:viewRegion]; //is this a check?               
        
        [mapView setRegion:adjustedRegion animated:YES];  //this is animateTo or w/e. 
        
        CLLocationCoordinate2D zoomLocation = locationObject.coordinate;
        double x = 0.001;
        CLLocationCoordinate2D topLeft = CLLocationCoordinate2DMake(zoomLocation.latitude+x ,zoomLocation.longitude-x);
        CLLocationCoordinate2D topRight = CLLocationCoordinate2DMake(zoomLocation.latitude+x,zoomLocation.longitude+x);
        CLLocationCoordinate2D botRight = CLLocationCoordinate2DMake(zoomLocation.latitude-x,zoomLocation.longitude+x);
        CLLocationCoordinate2D botLeft = CLLocationCoordinate2DMake(zoomLocation.latitude-x, zoomLocation.longitude-x);
        
        CLLocationCoordinate2D testLotCoords[5]={topLeft, topRight, botRight, botLeft, topLeft};
        
        MKPolygon *commuterPoly1 = [MKPolygon polygonWithCoordinates:testLotCoords count:5];
        [commuterPoly1 setColor:15];
        NSLog(@"color set to:%d", commuterPoly1.color);
        [self.mapView addOverlay:commuterPoly1];
    }];
    
}
- (IBAction)blockButtonPressed:(id)sender {
}
- (IBAction)spotButtonPressed:(id)sender {
}
- (IBAction)noneButtonPressed:(id)sender {
    [mapView removeOverlays:mapView.overlays];

    [locationManager startUpdatingLocation];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation{
    
    if (MAX(newLocation.horizontalAccuracy, newLocation.verticalAccuracy) < 100) {
        //One location is obtained.. just zoom to that location
        
        MKCoordinateRegion region;
        region.center=newLocation.coordinate;
        //Set Zoom level using Span
        MKCoordinateSpan span;
        span.latitudeDelta=.005;
        span.longitudeDelta=.005;
        region.span=span;
        
        [mapView setRegion:region animated:TRUE];
        
        [locationManager stopUpdatingLocation];
    }
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
    searchBar.delegate = self;
	
    // Do any additional setup after loading the view, typically from a nib.
    mapView.delegate=self;
    mapView.showsUserLocation = YES;
    
    locationManager=[[CLLocationManager alloc] init];
    locationManager.delegate=self;
    locationManager.desiredAccuracy=kCLLocationAccuracyNearestTenMeters;
    
    [locationManager startUpdatingLocation];
}

- (void)viewDidUnload
{
    [self setMapView:nil];
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
    //prepare geocoder upon view load.  
    if(IOSGeocoder ==nil){
        IOSGeocoder = [[CLGeocoder alloc] init];
    }
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];

    [locationManager stopUpdatingLocation];
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
