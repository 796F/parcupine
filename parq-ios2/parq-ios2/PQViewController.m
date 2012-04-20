//
//  PQViewController.m
//  parq-ios2
//
//  Created by Mark Yen on 3/16/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQViewController.h"
#import "MKShape+Color.h"
#import "UIColor+Parq.h"
#import "CalloutMapAnnotation.h"
#import "CalloutMapAnnotationView.h"

#define METERS_PER_MILE 1609.344

#define STREET_MAP_SPAN 0.0132
#define SPOT_MAP_SPAN 0.0017

#define GPS_LAUNCH_ALERT 10

typedef enum {
    kGridZoomLevel,
    kStreetZoomLevel,
    kSpotZoomLevel
} ZoomLevel;

@interface PQViewController ()
@property (strong, nonatomic) UIView *disableViewOverlay;
@property (strong, nonatomic) UIBarButtonItem *leftBarButton;
@property (nonatomic) ZoomLevel zoomState;
@end

@implementation PQViewController
@synthesize map;
@synthesize topSearchBar;
@synthesize navigationBar;
@synthesize geocoder;
@synthesize disableViewOverlay;
@synthesize leftBarButton;
@synthesize zoomState;

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
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.350393,-71.124159", @"nw_corner", @"42.360393,-71.134159", @"se_corner", [NSNumber numberWithInt:1], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360393,-71.124159", @"nw_corner", @"42.370393,-71.134159", @"se_corner", [NSNumber numberWithInt:2], @"color",nil],
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
            @"42.364995, -71.110782, 0",
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

- (NSArray *)calloutBubblePlacement:(CLLocationCoordinate2D *)selectionCenter {
    return [[NSArray alloc] initWithObjects:
            [[NSDictionary alloc] initWithObjectsAndKeys:
             [[CalloutMapAnnotation alloc] initWithLatitude:selectionCenter->latitude+0.0002
                                               andLongitude:selectionCenter->longitude-0.0005
                                                   andTitle:@"1104"
                                                  andCorner:kBottomRightCorner], @"callout",
             [[CLLocation alloc] initWithLatitude:42.365154 longitude:-71.110892], @"spot", nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:
             [[CalloutMapAnnotation alloc] initWithLatitude:selectionCenter->latitude-0.0001
                                               andLongitude:selectionCenter->longitude-0.0005
                                                   andTitle:@"1106"
                                                  andCorner:kTopRightCorner], @"callout",
             [[CLLocation alloc] initWithLatitude:42.365049 longitude:-71.110868], @"spot", nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:
             [[CalloutMapAnnotation alloc] initWithLatitude:selectionCenter->latitude-0.0004
                                               andLongitude:selectionCenter->longitude-0.0005
                                                   andTitle:@"1108"
                                                  andCorner:kTopRightCorner], @"callout",
             [[CLLocation alloc] initWithLatitude:42.364993 longitude:-71.110860], @"spot", nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:
             [[CalloutMapAnnotation alloc] initWithLatitude:selectionCenter->latitude+0.0003
                                               andLongitude:selectionCenter->longitude+0.0005
                                                   andTitle:@"1101"
                                                  andCorner:kBottomLeftCorner], @"callout",
             [[CLLocation alloc] initWithLatitude:42.365140 longitude:-71.110806], @"spot", nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:
             [[CalloutMapAnnotation alloc] initWithLatitude:selectionCenter->latitude
                                               andLongitude:selectionCenter->longitude+0.0005
                                                   andTitle:@"1105"
                                                  andCorner:kBottomLeftCorner], @"callout",
             [[CLLocation alloc] initWithLatitude:42.365045 longitude:-71.110790], @"spot", nil], nil];
}

- (void)showSelectionCircle:(CLLocationCoordinate2D *)coord {
    // Assumes overlays and annotations were cleared in the calling function
    
    MKCircle *greyCircle = [MKCircle circleWithCenterCoordinate:*coord radius:12];
    [greyCircle setColor:-1];
    [self.map addOverlay:greyCircle];
    
    NSArray *placement = [self calloutBubblePlacement:coord];
    for (NSDictionary *bubble in placement) {
        CLLocationCoordinate2D endpoints[2];
        CalloutMapAnnotation *callout = [bubble objectForKey:@"callout"];
        endpoints[0] = callout.coordinate;
        endpoints[1] = ((CLLocation *)[bubble objectForKey:@"spot"]).coordinate;
        MKPolyline *calloutLine = [MKPolyline polylineWithCoordinates:endpoints count:2];
        [calloutLine setColor:-1];
        [self.map addOverlay:calloutLine];

        [self.map addAnnotation:callout];
    }
}

- (void)showGridLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
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
        [self.map addOverlay:commuterPoly1];
    }
}

- (void)showStreetLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
    
    //remove all overlays, store them somewhere for when the user zooms out?
    [self.map removeOverlays:self.map.overlays];
    [self.map removeAnnotations:self.map.annotations];

    NSArray* data = [self loadBlockData];
    
    for (id line in data) {
        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[raw_waypoints.count];
        int i=0;
        for (id raw_waypoint in raw_waypoints) {
            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
            waypoints[i++] = coordinate;
        }
        
        MKPolyline *routeLine = [MKPolyline polylineWithCoordinates:waypoints count:raw_waypoints.count];
        
        int color = [[line objectForKey:@"color"] intValue];
        [routeLine setColor:color];
        
        [self.map addOverlay:routeLine];
    }
}


- (void)showSpotLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
    [self.map removeOverlays:self.map.overlays];
    [self.map removeAnnotations:self.map.annotations];

    [self showSelectionCircle:coord];
    NSArray* data = [self loadSpotData];
    for(id spot in data){
        NSArray* point = [spot componentsSeparatedByString:@","];
        CLLocationCoordinate2D coord = CLLocationCoordinate2DMake([[point objectAtIndex:0] floatValue], [[point objectAtIndex:1] floatValue]);
        int color = [[point objectAtIndex:2] intValue];
        MKCircle* circle = [MKCircle circleWithCenterCoordinate:coord radius:2];
        [circle setColor:color];
        [self.map addOverlay:circle];
    }
}

- (void)mapView:(MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated {
    CLLocationCoordinate2D coord = mapView.centerCoordinate;
    if (mapView.region.span.latitudeDelta>=STREET_MAP_SPAN) {
        NSLog(@"GRID:currSpan: %f\n", mapView.region.span.latitudeDelta);
        zoomState = kGridZoomLevel;
        [self showGridLevelWithCoordinates:&coord];
    } else if (mapView.region.span.latitudeDelta>=SPOT_MAP_SPAN) {
        NSLog(@"Block:currSpan: %f\n", mapView.region.span.latitudeDelta);
        zoomState = kStreetZoomLevel;
        [self showStreetLevelWithCoordinates:&coord];
    } else {
        NSLog(@"currSpan: %f\n", mapView.region.span.latitudeDelta);
        zoomState = kSpotZoomLevel;
        [self showSpotLevelWithCoordinates:&coord];
    }
}

- (MKOverlayView *)mapView:(MKMapView *)myMapView viewForOverlay:(id<MKOverlay>)overlay {
    if ([overlay isKindOfClass:[MKPolygon class]]) {
        MKPolygon* polygon = (MKPolygon*) overlay;
        MKPolygonView *view = [[MKPolygonView alloc] initWithOverlay:polygon];
        view.lineWidth=1;
        view.strokeColor = [UIColor whiteColor];
        switch (polygon.color) {
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
        switch (polyline.color) {
            case -1:
                view.strokeColor = [UIColor blackColor];
                view.lineWidth = 1;
                break;
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
    } else if ([overlay isKindOfClass:[MKCircle class]]) {
        MKCircleView *circleView = [[MKCircleView alloc] initWithCircle:(MKCircle *)overlay];
        MKCircle* circle = (MKCircle*) overlay;
        switch (circle.color) {
            case -1:
                // Grey selection circle
                circleView.fillColor = [[UIColor blackColor] colorWithAlphaComponent:0.3];
                circleView.strokeColor = [UIColor whiteColor];
                circleView.lineWidth = 6;
                break;
            case 0:
                //taken
                circleView.fillColor = [[UIColor redColor] colorWithAlphaComponent:0.5];
                circleView.strokeColor = [UIColor redColor];
                circleView.lineWidth = 2;
                break;
            case 1:
                //free
                circleView.fillColor = [[UIColor greenColor] colorWithAlphaComponent:0.5];
                circleView.strokeColor = [UIColor greenColor];
                circleView.lineWidth = 2;
                break;
        }
        return circleView;
    }
    return nil;
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
	if ([annotation isKindOfClass:[CalloutMapAnnotation class]]) {
		CalloutMapAnnotationView *calloutMapAnnotationView = (CalloutMapAnnotationView *)[self.map dequeueReusableAnnotationViewWithIdentifier:@"CalloutAnnotation"];
		if (!calloutMapAnnotationView) {
			calloutMapAnnotationView = [[CalloutMapAnnotationView alloc] initWithAnnotation:annotation 
																			 reuseIdentifier:@"CalloutAnnotation"];

		}
		calloutMapAnnotationView.mapView = self.map;
		return calloutMapAnnotationView;
	}

	return nil;
}

- (void)handleGesture:(UIGestureRecognizer *)gestureRecognizer
{
    if (gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;

    CGPoint touchPoint = [gestureRecognizer locationInView:self.map];
    CLLocationCoordinate2D coord = [self.map convertPoint:touchPoint toCoordinateFromView:self.map];
    MKMapPoint mapPoint = MKMapPointForCoordinate(coord);
    
    if (zoomState==kGridZoomLevel) {
        for (id gridOverlay in self.map.overlays) {
            MKPolygonView *polyView = (MKPolygonView*)[self.map viewForOverlay:gridOverlay];
            CGPoint polygonViewPoint = [polyView pointForMapPoint:mapPoint];

            //BUG this contains thing is including an area even above the polygon  
            if (CGPathContainsPoint(polyView.path, NULL, polygonViewPoint, NO)) {
                MKCoordinateRegion reg = [self.map convertRect:polyView.bounds toRegionFromView:polyView];
                [self.map setRegion:[self.map regionThatFits:reg] animated:YES];
                [self showStreetLevelWithCoordinates:&(reg.center)];
            }
        }
    } else if (zoomState==kStreetZoomLevel) {
        MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(coord, METERS_PER_MILE/16, METERS_PER_MILE/16);
        [self.map setRegion:[self.map regionThatFits:viewRegion] animated:YES];
        [self showSpotLevelWithCoordinates:&coord];
        /*
         //ask user if that's desired destination
         UIAlertView* warningAlertView = [[UIAlertView alloc] initWithTitle:@"Spot 4102 Selected!" message:@"Launch GPS to this spot?" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Launch" , nil];
         warningAlertView.tag = GPS_LAUNCH_ALERT;
         [warningAlertView show];
         */
    } else if (zoomState==kSpotZoomLevel) {
        [self showSpotLevelWithCoordinates:&coord];
        [self.map setCenterCoordinate:coord animated:YES];
    }
}

#pragma mark - Search bar and UISearchBarDelegate methods

- (void)setSearchBar:(UISearchBar *)searchBar active:(BOOL)visible {
    if (visible) {
        if (zoomState == kSpotZoomLevel) {
            self.disableViewOverlay.frame = CGRectMake(0.0f,88.0f,320.0f,372.0f);
        } else {
            self.disableViewOverlay.frame = CGRectMake(0.0f,44.0f,320.0f,416.0f);
        }
        [self.view addSubview:self.disableViewOverlay];

        [UIView animateWithDuration:0.25 animations:^{
            self.navigationBar.leftBarButtonItem = Nil;
            self.disableViewOverlay.alpha = 0.8;
            if (zoomState == kSpotZoomLevel) {
                searchBar.frame = CGRectMake(0, 0, 320, 88);
            } else {
                searchBar.frame = CGRectMake(0, 0, 320, 44);
            }
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
    [searchBar setShowsScopeBar:(visible && zoomState==kSpotZoomLevel)];
    [searchBar setShowsCancelButton:visible animated:YES];
}

- (void)handleSingleTap:(UIGestureRecognizer *)sender {
    [self setSearchBar:(UISearchBar *)self.topSearchBar active:NO];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    [self.map removeOverlays:self.map.overlays];

    [geocoder geocodeAddressString:searchBar.text inRegion:nil completionHandler:^(NSArray *placemarks, NSError * error){
        CLLocation* locationObject = [[placemarks objectAtIndex:0] location];

        MKCoordinateRegion viewRegion = [self.map regionThatFits:MKCoordinateRegionMakeWithDistance(locationObject.coordinate, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE)];
        [self.map setRegion:viewRegion animated:YES];
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
    zoomState = kGridZoomLevel;
    [self.map removeOverlays:map.overlays];
    [self.map removeAnnotations:self.map.annotations];

    //set the zoom to fit 12 grids perfectly
    CLLocationCoordinate2D point = CLLocationCoordinate2DMake(42.365045,-71.118965);
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, METERS_PER_MILE, METERS_PER_MILE);  //this is essentially zoom level in android
    
    MKCoordinateRegion adjustedRegion = [map regionThatFits:viewRegion];
    
    [self.map setRegion:adjustedRegion animated:YES];
    [self showGridLevelWithCoordinates:&point];
}

     
- (IBAction)streetButtonPressed:(id)sender {
    [self.map removeOverlays:self.map.overlays];
    [self.map removeAnnotations:self.map.annotations];

    CLLocationCoordinate2D point = {42.364854,-71.109438};

    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, METERS_PER_MILE/2, METERS_PER_MILE/2);
    [self.map setRegion:[self.map regionThatFits:viewRegion] animated:YES];
    [self showStreetLevelWithCoordinates:&point];
}
- (IBAction)spotButtonPressed:(id)sender {
    
    [map removeOverlays:map.overlays];
    CLLocationCoordinate2D point = {42.365077, -71.110838};

    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, METERS_PER_MILE/16, METERS_PER_MILE/16);
    [map setRegion:[map regionThatFits:viewRegion] animated:YES];

    [self showSpotLevelWithCoordinates:&point];
}
- (IBAction)noneButtonPressed:(id)sender {
    [self.map removeOverlays:map.overlays];
    [self.map removeAnnotations:self.map.annotations];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation{
    if (MAX(newLocation.horizontalAccuracy, newLocation.verticalAccuracy) < 100) {
        //One location is obtained.. just zoom to that location
        /*
         MKCoordinateRegion region;
         region.center=newLocation.coordinate;
         //Set Zoom level using Span
         MKCoordinateSpan span;
         span.latitudeDelta=.005;
         span.longitudeDelta=.005;
         region.span=span;
         
         [map setRegion:region animated:TRUE];
         */

        [self spotButtonPressed:Nil];

        [locationManager stopUpdatingLocation];
    }
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
    float zoomWidth = self.map.bounds.size.width;
    if(zoomWidth > STREET_MAP_SPAN){
        zoomState = kGridZoomLevel;
        //above middle zoom level        
    }else if(SPOT_MAP_SPAN){
        //above spot zoom level
        zoomState = kStreetZoomLevel;
    }else{
        //inside spot zoom level.  
        zoomState = kSpotZoomLevel;
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
    self.map.delegate=self;
    self.map.showsUserLocation = YES;
    
    //setup gesture recognizer for grids and blocks and spots.  
    UITapGestureRecognizer *tgr = [[UITapGestureRecognizer alloc]
                                   initWithTarget:self action:@selector(handleGesture:)];
    [map addGestureRecognizer:tgr];
    
    //SETUP LOCATION manager
    locationManager=[[CLLocationManager alloc] init];
    locationManager.delegate=self;
    locationManager.desiredAccuracy=kCLLocationAccuracyNearestTenMeters;
    
    [locationManager startUpdatingLocation];
}

- (void)viewDidUnload
{
    [self setMap:nil];
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
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
