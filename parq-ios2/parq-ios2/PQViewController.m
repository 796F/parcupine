//
//  PQViewController.m
//  parq-ios2
//
//  Created by Mark Yen on 3/16/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQViewController.h"
#import "MKPolygon+Color.h"
#import "MKPolyline+Color.h"
#import "MKCircle+Color.h"
#import "UIColor+Parq.h"

@interface PQViewController ()
@property (strong, nonatomic) UIView *disableViewOverlay;
@property (strong, nonatomic) UIBarButtonItem *leftBarButton;
@end

@implementation PQViewController
@synthesize mapView;
@synthesize topSearchBar;
@synthesize navigationBar;
@synthesize geocoder;
@synthesize zoomState;
@synthesize destLat;
@synthesize destLon;
@synthesize disableViewOverlay;
@synthesize leftBarButton;

-(void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSLog(@"hello\n");
}

- (NSArray*)loadGridData {
    return [NSArray arrayWithObjects:
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.350393,-71.104159", @"nw_corner", @"42.360393,-71.114159", @"se_corner", [NSNumber numberWithInt:0], @"color", nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360393,-71.104159", @"nw_corner", @"42.370393,-71.114159", @"se_corner", [NSNumber numberWithInt:4], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370393,-71.104159", @"nw_corner", @"42.380393,-71.114159", @"se_corner", [NSNumber numberWithInt:2], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.350393,-71.114159", @"nw_corner", @"42.360393,-71.124159", @"se_corner", [NSNumber numberWithInt:3], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360393,-71.114159", @"nw_corner", @"42.370393,-71.124159", @"se_corner", [NSNumber numberWithInt:3], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370393,-71.114159", @"nw_corner", @"42.380393,-71.124159", @"se_corner", [NSNumber numberWithInt:0], @"color",nil], 
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.350393,-71.124159", @"nw_corner",  @"42.360393,-71.134159", @"se_corner", [NSNumber numberWithInt:1], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360393,-71.124159", @"nw_corner",         @"42.370393,-71.134159", @"se_corner", [NSNumber numberWithInt:2], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370393,-71.124159", @"nw_corner", @"42.380393,-71.134159", @"se_corner", [NSNumber numberWithInt:3], @"color",nil], nil];
    
}

- (NSArray*)loadBlockData {
    return [NSArray arrayWithObjects:
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36441,-71.113901;42.365203,-71.104846", @"line", [NSNumber numberWithInt:0], @"color", nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.367074,-71.111155;42.363269,-71.110554", @"line", [NSNumber numberWithInt:4], @"color", nil], nil];
}

-(NSArray*) loadSpotData {
    return [NSArray arrayWithObjects:
            @"42.365354, -71.110843, 1",
            @"42.365292, -71.110835, 1",
            @"42.365239, -71.110825, 1",
            @"42.365187, -71.110811, 0",
            @"42.365140, -71.110806, 1",
            @"42.365092, -71.110798, 0",
            @"42.365045, -71.110790, 1",
            @"42.364995, -71.110782, 1",
            @"42.364947, -71.110768, 0",
            @"42.364896, -71.110766, 0",
            @"42.364846, -71.110752, 0",
            @"42.364797, -71.110739, 0",
            
            @"42.365348, -71.110924, 1",
            @"42.365300, -71.110916, 0",
            @"42.365251, -71.110905, 0",
            @"42.365203, -71.110900, 0",
            @"42.365154, -71.110892, 1",
            @"42.365104, -71.110876, 0",
            @"42.365049, -71.110868, 1",
            @"42.364993, -71.110860, 1",
            @"42.364943, -71.110849, 1",
            @"42.364894, -71.110846, 1",
            @"42.364846, -71.110835, 0",
            @"42.364799, -71.110830, 1",
            nil];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(alertView.tag==GPS_LAUNCH_ALERT && alertView.firstOtherButtonIndex==buttonIndex){
        //this URL kinda works.  http://maps.google.com/maps?daddr=Spot+1412@42,-73&saddr=Current+Location@42,-72
        
        //if yes, store the destination's lat/lon for return launch and start gps app.  
        NSString* destination =[NSString stringWithFormat:@"http://maps.google.com/maps?daddr=Spot+%d@%1.2f,%1.2f&saddr=Current+Location@%1.2f,%1.2f", 1412, 41.343, -74.115, 43.124, -72.31552];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:destination]];
    }
}

-(void) showBlockLevelWithCoordinates:(CLLocationCoordinate2D*)coords{
    
    //remove all overlays, store them somewhere for when the user zooms out?
    [mapView removeOverlays:mapView.overlays];
    //load the spot information from the asynchronous server call/cache to draw blocks.  
    NSArray* data = [self loadBlockData];
    
    for (id line in data) {
        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[raw_waypoints.count];
        int i=0;
        for (id raw_waypoint in raw_waypoints) {
            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
            CLLocationCoordinate2D coord = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
            waypoints[i++] = coord;
        }
        
        MKPolyline *routeLine = [MKPolyline polylineWithCoordinates:waypoints count:raw_waypoints.count];
        
        int color = [[line objectForKey:@"color"] intValue];
        [routeLine setColor:color];
        
        [self.mapView addOverlay:routeLine];
    }

}


-(void) showSpotLevelWithCoordinates:(CLLocationCoordinate2D*)coords{
    //remove old overlay items
    [mapView removeOverlays:mapView.overlays];
    //load data from server or machine cache
    NSArray* data = [self loadSpotData];
    //draw them.
    for(id spot in data){
        NSArray* point = [spot componentsSeparatedByString:@","];
        CLLocationCoordinate2D coord = CLLocationCoordinate2DMake([[point objectAtIndex:0] floatValue], [[point objectAtIndex:1] floatValue]);
        int color = [[point objectAtIndex:2] intValue];
        MKCircle* circle = [MKCircle circleWithCenterCoordinate:coord radius:2];
        [circle setColor:color];
        [self.mapView addOverlay:circle];
    }
    
}

-(void) zoomFromGridToBLockWithRegion:(MKCoordinateRegion*)reg{
    //zoom to the region provided
    [mapView setRegion:[mapView regionThatFits:(*reg)] animated:YES];
    
    //set zoom state to block level
    zoomState = ZOOM_BLOCK;
    
    //display overlays that are on the block level.  
    [self showBlockLevelWithCoordinates:&((*reg).center)];
}

//using the region to encompass the entire block selected.  
-(void) zoomToSpotLevelWithCoordinates:(CLLocationCoordinate2D*)coords{
    //use coordinates provided o make region
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(*coords, METERS_PER_MILE/32, METERS_PER_MILE/32);
    //zoom to the region
    [mapView setRegion:[mapView regionThatFits:viewRegion] animated:YES];
    //set zoom state to spot level
    zoomState =ZOOM_SPOT;
    //show the spots in the area.  
    [self showSpotLevelWithCoordinates:coords] ;
}

//called when zoom-out level event is triggered by user's pinching.  
-(void) enterGridLevel{
    zoomState=ZOOM_GRID;
    //remove currently shown spots/blocks
    //[mapView removeOverlays:mapView.overlays];
    //reload grids that belong in the curent view.  
    
}

-(void) enterBlockLevel{
    zoomState = ZOOM_BLOCK;
    //remove spot level info
    //[mapView removeOverlays:mapView.overlays];
    //reload blocks.  
    
}
-(void) enterSpotLevel{
    zoomState = ZOOM_SPOT;
    //[mapView removeOverlays:mapView.overlays];

}

-(void) mapView:(MKMapView *)myMapView regionDidChangeAnimated:(BOOL)animated{
    if(myMapView.region.span.latitudeDelta>=BLOCK_MAP_SPAN){
        NSLog(@"GRID:currSpan: %f\n", myMapView.region.span.latitudeDelta);
        //above block, in grid level.
        [self enterGridLevel];
    }else if (myMapView.region.span.latitudeDelta>=SPOT_MAP_SPAN){
        //above spot, in block level. 
        NSLog(@"Block:currSpan: %f\n", myMapView.region.span.latitudeDelta);
        [self enterBlockLevel];
    }else{
        NSLog(@"currSpan: %f\n", myMapView.region.span.latitudeDelta);
        //below block, in spot level.  
        [self enterSpotLevel];
    }
}

/***
 * Preset UIColors 
 * black, darkGray, lightGray, white, gray, red, green, 
 * blue,cyan,yellow,magenta,orange,purple,brown
 ***/

-(MKOverlayView *)mapView:(MKMapView *)myMapView viewForOverlay:(id<MKOverlay>)overlay{
    if ([overlay isKindOfClass:[MKPolygon class]]) {
        MKPolygon* polygon = (MKPolygon*) overlay;
        MKPolygonView *view = [[MKPolygonView alloc] initWithOverlay:polygon];
        view.lineWidth=1;
        view.strokeColor = [UIColor whiteColor];
        //grab the color from the subclass
        int color = polygon.color;
        switch (color) {
            case 0:
                view.fillColor = [[UIColor veryLowAvailabilityColor] colorWithAlphaComponent:0.2];
                break;
            case 1:
                view.fillColor = [[UIColor lowAvailabilityColor] colorWithAlphaComponent:0.2];
                break;
            case 2:
                view.fillColor = [[UIColor mediumAvailabilityColor] colorWithAlphaComponent:0.2];
                break;
            case 3:
                view.fillColor = [[UIColor highAvailabilityColor] colorWithAlphaComponent:0.2];
                break;
            case 4:
                view.fillColor = [[UIColor veryHighAvailabilityColor] colorWithAlphaComponent:0.2];
                break;
        }
        return view;
        
    } else if ([overlay isKindOfClass:[MKPolyline class]]) {
        MKPolylineView *view = [[MKPolylineView alloc] initWithOverlay:overlay];
        view.lineWidth = 8;
        MKPolyline *polyline = (MKPolyline *)overlay;
        int color = polyline.color;
        switch (color) {
            case 0:
                view.strokeColor = [UIColor veryLowAvailabilityColor];
                break;
            case 1:
                view.strokeColor = [UIColor lowAvailabilityColor];
                break;
            case 2:
                view.strokeColor = [UIColor mediumAvailabilityColor];
                break;
            case 3:
                view.strokeColor = [UIColor highAvailabilityColor];
                break;
            case 4:
                view.strokeColor = [UIColor veryHighAvailabilityColor];
                break;
        }
        return view;
    }else if([overlay isKindOfClass:[MKCircle class]]) {
        MKCircleView *circleView = [[MKCircleView alloc] initWithCircle:(MKCircle *)overlay];
        MKCircle* casted = (MKCircle*) overlay;
        if(casted.color==0){
            //taken
            circleView.fillColor = [[UIColor redColor] colorWithAlphaComponent:0.5];
            circleView.strokeColor = [UIColor redColor];
            
        }else{
            //free
            circleView.fillColor = [[UIColor greenColor] colorWithAlphaComponent:0.5];
            circleView.strokeColor = [UIColor greenColor];
            
        }
        circleView.lineWidth = 2;
        return circleView;
    }
    return nil;
}

- (void)handleGesture:(UIGestureRecognizer *)gestureRecognizer
{
    if (gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;
    
    
    //grab the point the user tapped, but in the mapView's perspective.  
    CGPoint touchPoint = [gestureRecognizer locationInView:mapView];
    //convert that to lat/lon in the map view's perspective.  
    CLLocationCoordinate2D coord = [self.mapView convertPoint:touchPoint toCoordinateFromView:self.mapView];
    //convert those lat/lon into an MKmapPoint.  
    MKMapPoint mapPoint = MKMapPointForCoordinate(coord);
    
    if(zoomState==ZOOM_GRID){        
        for(id gridOverlay in self.mapView.overlays){
            //get reference to the MKPolygonView object
            id view = [self.mapView viewForOverlay:gridOverlay];
            MKPolygonView *polyView = (MKPolygonView*)view;
            //get the touch point in the polygon view's perspective?
            CGPoint polygonViewPoint = [polyView pointForMapPoint:mapPoint];
            //now that it's in the polygon view's perspective, we can check if it's contained.  
            
            BOOL mapCoordinateIsInPolygon = CGPathContainsPoint(polyView.path, NULL, polygonViewPoint, NO);   
            //BUG this contains thing is including an area even above the polygon  
            if (mapCoordinateIsInPolygon) {
                //thus if contained...
                MKCoordinateRegion reg = [mapView convertRect:polyView.bounds toRegionFromView:polyView];
                [self zoomFromGridToBLockWithRegion:&reg];
                
                
                //[mapView setRegion:[mapView regionThatFits:reg] animated:YES];
                
                //[mapView setRegion:MKCoordinateRegionMakeWithDistance(mapView.centerCoordinate, METERS_PER_MILE/2, METERS_PER_MILE/2) animated:YES];
            }
        }
    }else if(zoomState==ZOOM_BLOCK){
        //if user clicked near a block, zoom to spot level
        [self zoomToSpotLevelWithCoordinates:&coord];

        /* so the decision bit is missing, since i don't yet nkow how to detect which block the user meant, and then from that extract the bounding regions of that block, so we ensure all spots in a block are shown.  */
        
    }else if (zoomState==ZOOM_SPOT){
        //detect nearest spot, 
        
        //circleView.path may or may not contain it;
        
        destLat= coord.latitude;
        destLat= coord.longitude;
        //ask user if that's desired destination
        UIAlertView* warningAlertView = [[UIAlertView alloc] initWithTitle:@"Spot 4102 Selected!" message:@"Launch GPS to this spot?" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Launch" , nil];
        warningAlertView.tag = GPS_LAUNCH_ALERT;
        [warningAlertView show];
    }else{
        //bad state.  return 
        return;
    }
    
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

        //        [mapView setRegion:region animated:TRUE];

        [locationManager stopUpdatingLocation];
    }
}

#pragma mark - Search bar and UISearchBarDelegate methods

- (void)setSearchBar:(UISearchBar *)searchBar active:(BOOL)visible {
    [searchBar setShowsScopeBar:visible];
    if (visible) {
        [self.view addSubview:self.disableViewOverlay];

        [UIView animateWithDuration:0.25 animations:^{
            self.navigationBar.leftBarButtonItem = Nil;
            self.disableViewOverlay.alpha = 0.8;
            searchBar.frame = CGRectMake(0, 0, 320, 88);
        }];
    } else {
        [searchBar resignFirstResponder];

        [UIView animateWithDuration:0.25 animations:^{
            self.navigationBar.leftBarButtonItem = self.leftBarButton;
            self.disableViewOverlay.alpha = 0;
            searchBar.frame = CGRectMake(45, 0, 275, 44);
        } completion:^(BOOL s){
            [self.disableViewOverlay removeFromSuperview];
        }];
    }
    [searchBar setShowsCancelButton:visible animated:YES];
}

- (void)handleSingleTap:(UIGestureRecognizer *)sender {
    [self setSearchBar:(UISearchBar *)self.topSearchBar active:NO];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    [mapView removeOverlays:mapView.overlays];

    [geocoder geocodeAddressString:searchBar.text inRegion:nil completionHandler:^(NSArray *placemarks, NSError * error){
        CLLocation* locationObject = [[placemarks objectAtIndex:0] location];

        MKCoordinateRegion viewRegion = [mapView regionThatFits:MKCoordinateRegionMakeWithDistance(locationObject.coordinate, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE)];
        [mapView setRegion:viewRegion animated:YES];
    }];

    [self setSearchBar:searchBar active:NO];
}

- (void)searchBar:(UISearchBar *)searchBar selectedScopeButtonIndexDidChange:(NSInteger)selectedScope {
    if (selectedScope == 1) {
        searchBar.keyboardType = UIKeyboardTypeNumberPad;
    } else {
        searchBar.keyboardType = UIKeyboardTypeDefault;
    }
    [searchBar resignFirstResponder];
    [searchBar becomeFirstResponder];
}

- (void)searchBarBookmarkButtonClicked:(UISearchBar *)searchBar {
    //load recently parked spots.  
    
}

- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar {
    [self setSearchBar:searchBar active:YES];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    [self setSearchBar:searchBar active:NO];
}

#pragma mark - Debug button actions

- (IBAction)gridButtonPressed:(id)sender {
    NSLog(@"Grid Button Pressed\n" );
    zoomState = ZOOM_GRID;
    [mapView removeOverlays:mapView.overlays];

    //set the zoom to fit 12 grids perfectly
    CLLocationCoordinate2D point = CLLocationCoordinate2DMake(42.365045,-71.118965);
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, METERS_PER_MILE, METERS_PER_MILE);  //this is essentially zoom level in android
    
    MKCoordinateRegion adjustedRegion = [mapView regionThatFits:viewRegion]; //is this a check?               
    
    [mapView setRegion:adjustedRegion animated:YES];  //this is animateTo or w/e. 
    NSArray* data = [self loadGridData];
    
    for (id element in data) {
        NSArray *array = [[element objectForKey:@"se_corner"] componentsSeparatedByString:@","];
        
        CLLocationCoordinate2D nw_point = CLLocationCoordinate2DMake([[array objectAtIndex:0] floatValue], [[array objectAtIndex:1] floatValue]);    
        array = [[element objectForKey:@"nw_corner"]componentsSeparatedByString:@","];
        CLLocationCoordinate2D se_point = CLLocationCoordinate2DMake([[array objectAtIndex:0] floatValue], [[array objectAtIndex:1] floatValue]);
        
        //calculate actual se_point using haversine.  
        
        int color = [[element objectForKey:@"color"] intValue];
        
        CLLocationCoordinate2D ne_point = CLLocationCoordinate2DMake(nw_point.latitude ,se_point.longitude);
        CLLocationCoordinate2D sw_point = CLLocationCoordinate2DMake(se_point.latitude ,nw_point.longitude);
        
        
        CLLocationCoordinate2D testLotCoords[5]={nw_point, ne_point, se_point, sw_point, nw_point};
        
        MKPolygon *commuterPoly1 = [MKPolygon polygonWithCoordinates:testLotCoords count:5];
        [commuterPoly1 setColor:color];
        [self.mapView addOverlay:commuterPoly1];
    }
}

     
- (IBAction)blockButtonPressed:(id)sender {
    [mapView removeOverlays:mapView.overlays];

    CLLocationCoordinate2D point = {42.364854,-71.109438};

    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, METERS_PER_MILE/2, METERS_PER_MILE/2);
    [mapView setRegion:[mapView regionThatFits:viewRegion] animated:YES];
    NSArray* data = [self loadBlockData];

    for (id line in data) {
        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[raw_waypoints.count];
        int i=0;
        for (id raw_waypoint in raw_waypoints) {
            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
            CLLocationCoordinate2D coord = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
            waypoints[i++] = coord;
        }

        MKPolyline *routeLine = [MKPolyline polylineWithCoordinates:waypoints count:raw_waypoints.count];

        int color = [[line objectForKey:@"color"] intValue];
        [routeLine setColor:color];

        [self.mapView addOverlay:routeLine];
    }
}
- (IBAction)spotButtonPressed:(id)sender {
    
    [mapView removeOverlays:mapView.overlays];
    CLLocationCoordinate2D point = {42.365354, -71.110843};
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, METERS_PER_MILE/16, METERS_PER_MILE/16);
    [mapView setRegion:[mapView regionThatFits:viewRegion] animated:YES];
    
    NSArray* data = [self loadSpotData];
    for(id spot in data){
        NSArray* point = [spot componentsSeparatedByString:@","];
        CLLocationCoordinate2D coord = CLLocationCoordinate2DMake([[point objectAtIndex:0] floatValue], [[point objectAtIndex:1] floatValue]);
        int color = [[point objectAtIndex:2] intValue];
        MKCircle* circle = [MKCircle circleWithCenterCoordinate:coord radius:2];
        [circle setColor:color];
        [self.mapView addOverlay:circle];
    }
}
- (IBAction)noneButtonPressed:(id)sender {
    [mapView removeOverlays:mapView.overlays];
    
    [locationManager startUpdatingLocation];
}

#pragma mark - Memory

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad{
    
    //check the current zoom level to set the ZOOM_STATE integer.  
    float zoomWidth = mapView.bounds.size.width;
    if(zoomWidth > BLOCK_MAP_SPAN){
        zoomState = ZOOM_GRID;
        //above middle zoom level        
    }else if(SPOT_MAP_SPAN){
        //above spot zoom level
        zoomState = ZOOM_BLOCK;
    }else{
        //inside spot zoom level.  
        zoomState = ZOOM_SPOT;
    }

    self.disableViewOverlay = [[UIView alloc]
                               initWithFrame:CGRectMake(0.0f,88.0f,320.0f,372.0f)];
    self.disableViewOverlay.backgroundColor=[UIColor blackColor];
    self.disableViewOverlay.alpha = 0;
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [self.disableViewOverlay addGestureRecognizer:singleTap];

    ((UISearchBar *)self.topSearchBar).scopeButtonTitles = [[NSArray alloc] initWithObjects:@"Place name", @"Spot number", nil];

    self.leftBarButton = self.navigationBar.leftBarButtonItem;

    [super viewDidLoad];
    
	
    // Do any additional setup after loading the view, typically from a nib.
    mapView.delegate=self;
    mapView.showsUserLocation = YES;
    
    //setup gesture recognizer for grids and blocks and spots.  
    UITapGestureRecognizer *tgr = [[UITapGestureRecognizer alloc] 
                                   initWithTarget:self action:@selector(handleSingleTap:)];
    tgr.numberOfTapsRequired = 1;
    tgr.numberOfTouchesRequired = 1;
    [mapView addGestureRecognizer:tgr];
    
    //SETUP LOCATION manager
    locationManager=[[CLLocationManager alloc] init];
    locationManager.delegate=self;
    locationManager.desiredAccuracy=kCLLocationAccuracyNearestTenMeters;
    
    [locationManager startUpdatingLocation];
}

- (void)viewDidUnload
{
    [self setMapView:nil];
    [self setNavigationBar:nil];
    [self setTopSearchBar:nil];
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
    if(geocoder ==nil){
        geocoder = [[CLGeocoder alloc] init];
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
