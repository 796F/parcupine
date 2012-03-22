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

/***
 * Preset UIColors 
 * black, darkGray, lightGray, white, gray, red, green, 
 * blue,cyan,yellow,magenta,orange,purple,brown
 ***/

-(MKOverlayView *)mapView:(MKMapView *)mapView viewForOverlay:(id<MKOverlay>)overlay{
    if([overlay isKindOfClass:[MKPolygon class]]){
        MKPolygon* casted = (MKPolygon*) overlay;
        MKPolygonView *view = [[MKPolygonView alloc] initWithOverlay:overlay];
        view.lineWidth=1;
        //grab the color from the subclass
        int color = casted.color;
        if(color==0){
            //no spots
            view.strokeColor=[UIColor redColor];
            view.fillColor=[[UIColor redColor] colorWithAlphaComponent:0.5];
            
        }else if(color==1){
            //minimal spots
            view.strokeColor=[UIColor redColor];
            view.fillColor=[[UIColor redColor] colorWithAlphaComponent:0.2];
            
        }else if(color==2){
            //some spots
            view.strokeColor=[UIColor yellowColor];
            view.fillColor=[[UIColor yellowColor] colorWithAlphaComponent:0.3];
            
        }else if(color==3){
            //healthy spots
            view.strokeColor=[UIColor greenColor];
            view.fillColor=[[UIColor greenColor] colorWithAlphaComponent:0.2];
            
        }else if(color==4){
            //lots of spots
            view.strokeColor=[UIColor greenColor];
            view.fillColor=[[UIColor greenColor] colorWithAlphaComponent:0.5];
            
        }else{
            //invalid color, don't do anything.  
            return nil;
        }
        return view;
    }
    return nil;    
}

- (NSArray*)loadGridData {
    
    return [NSArray arrayWithObjects:
                 [[NSDictionary alloc] initWithObjectsAndKeys:@"42.350393,-71.104159", @"nw_corner", @"42.360393,-71.114159", @"se_corner", [NSNumber numberWithInt:0], @"color", nil],
                 [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360393,-71.104159", @"nw_corner", @"42.370393,-71.114159", @"se_corner", [NSNumber numberWithInt:1], @"color",nil],
                 [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370393,-71.104159", @"nw_corner", @"42.380393,-71.114159", @"se_corner", [NSNumber numberWithInt:2], @"color",nil],
                 [[NSDictionary alloc] initWithObjectsAndKeys:@"42.350393,-71.114159", @"nw_corner", @"42.360393,-71.124159", @"se_corner", [NSNumber numberWithInt:3], @"color",nil],
                 [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360393,-71.114159", @"nw_corner", @"42.370393,-71.124159", @"se_corner", [NSNumber numberWithInt:4], @"color",nil],
                 [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370393,-71.114159", @"nw_corner", @"42.380393,-71.124159", @"se_corner", [NSNumber numberWithInt:0], @"color",nil], 
                 [[NSDictionary alloc] initWithObjectsAndKeys:@"42.350393,-71.124159", @"nw_corner",  @"42.360393,-71.134159", @"se_corner", [NSNumber numberWithInt:1], @"color",nil],
                 [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360393,-71.124159", @"nw_corner",         @"42.370393,-71.134159", @"se_corner", [NSNumber numberWithInt:2], @"color",nil],
                 [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370393,-71.124159", @"nw_corner", @"42.380393,-71.134159", @"se_corner", [NSNumber numberWithInt:3], @"color",nil], nil];

}

-(CLLocationCoordinate2D*) parseString:(NSString*)string {
    CLLocationCoordinate2D* point;
    NSArray *array = [string componentsSeparatedByString:@","];
    *point = CLLocationCoordinate2DMake([[array objectAtIndex:0] floatValue], [[array objectAtIndex:1] floatValue]);    
    return point;
}



-(void) drawSquareWithNW:(CLLocationCoordinate2D*)nw_loc 
                    SE:(CLLocationCoordinate2D*)se_loc 
                 Color:(int)color{
        
    CLLocationCoordinate2D ne_loc = CLLocationCoordinate2DMake(nw_loc->latitude ,se_loc->longitude);
    CLLocationCoordinate2D sw_loc = CLLocationCoordinate2DMake(se_loc->latitude ,nw_loc->longitude);
    
    CLLocationCoordinate2D testLotCoords[5]={*nw_loc, ne_loc, *se_loc, sw_loc, *nw_loc};
    
    MKPolygon *commuterPoly1 = [MKPolygon polygonWithCoordinates:testLotCoords count:5];
    [commuterPoly1 setColor:color];
    [self.mapView addOverlay:commuterPoly1];
}

-(void) drawGridWithData:(NSArray*)data{
    
    
    
}

- (IBAction)gridButtonPressed:(id)sender {
    UISearchBar* searchBar = self.searchDisplayController.searchBar;
    [mapView removeOverlays:mapView.overlays];
    NSString* addr = @"Cambridge, MA";
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
        CLLocationCoordinate2D point = CLLocationCoordinate2DMake(42.365045,-71.118965);
        
        MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, METERS_PER_MILE, METERS_PER_MILE);  //this is essentially zoom level in android
        
        MKCoordinateRegion adjustedRegion = [mapView regionThatFits:viewRegion]; //is this a check?               
        
        [mapView setRegion:adjustedRegion animated:YES];  //this is animateTo or w/e. 
        NSArray* data = [self loadGridData];
        
        for(id element in data){
            
            NSArray *array = [[element objectForKey:@"se_corner"] componentsSeparatedByString:@","];
            
            CLLocationCoordinate2D nw_point = CLLocationCoordinate2DMake([[array objectAtIndex:0] floatValue], [[array objectAtIndex:1] floatValue]);    
            array = [[element objectForKey:@"nw_corner"]componentsSeparatedByString:@","];
            CLLocationCoordinate2D se_point = CLLocationCoordinate2DMake([[array objectAtIndex:0] floatValue], [[array objectAtIndex:1] floatValue]);
            
            int color = [[element objectForKey:@"color"] intValue];
            
            CLLocationCoordinate2D ne_point = CLLocationCoordinate2DMake(nw_point.latitude ,se_point.longitude);
            CLLocationCoordinate2D sw_point = CLLocationCoordinate2DMake(se_point.latitude ,nw_point.longitude);
            
            CLLocationCoordinate2D testLotCoords[5]={nw_point, ne_point, se_point, sw_point, nw_point};
            
            MKPolygon *commuterPoly1 = [MKPolygon polygonWithCoordinates:testLotCoords count:5];
            [commuterPoly1 setColor:color];
            [self.mapView addOverlay:commuterPoly1];
            
        }
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
