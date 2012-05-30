//
//  PQMapViewController.m
//  parq-ios2
//
//  Created by Mark Yen on 3/16/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQMapViewController.h"
#import "PQParkingViewController.h"
#import "MKShape+Color.h"
#import "UIColor+Parq.h"
#import "CalloutMapAnnotation.h"
#import "CalloutMapAnnotationView.h"

#define METERS_PER_MILE 1609.344

#define STREET_MAP_SPAN 0.0132
#define SPOT_MAP_SPAN 0.0017

#define GPS_LAUNCH_ALERT 10
#define MAX_CALLOUTS 8
#define GREY_CIRCLE_R 12
#define annotation_offset_y 0.000045
#define annotation_offset_x 0.00012
#define CALLOUT_LINE_LENGTH 0.00000023
//if the action was a pan, we are to erase the grey circle.  
#define panAction(x) x>0 ? 0:1
#define CALLOUT_TAPPED 1

typedef enum {
    kGridZoomLevel,
    kStreetZoomLevel,
    kSpotZoomLevel
} ZoomLevel;

@interface PQMapViewController ()
@property (strong, nonatomic) UIView *disableViewOverlay;
@property (strong, nonatomic) UIBarButtonItem *leftBarButton;
@property (nonatomic) ZoomLevel zoomState;
@end

@implementation PQMapViewController
@synthesize gCircle;
@synthesize callouts;
@synthesize calloutLines;
@synthesize map;
@synthesize topSearchBar;
@synthesize availabilitySelectionView;
@synthesize bottomSpotSelectionView;
@synthesize topSpotSelectionView;
@synthesize navigationBar;
@synthesize bottomSpotSelectionBar;
@synthesize topSpotSelectionBar;
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
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365399,-71.110897;42.364751,-71.110771", @"line", [NSNumber numberWithInt:4], @"color", nil], nil];
    
}

-(NSArray*) loadSpotData {
    return [NSArray arrayWithObjects:
            @"42.365354, -71.110843, 1, 1410",
            @"42.365292, -71.110835, 1, 1412",
            @"42.365239, -71.110825, 1, 1414",
            @"42.365187, -71.110811, 0, 1416",
            @"42.365140, -71.110806, 1, 1418",
            @"42.365092, -71.110798, 0, 1420",
            @"42.365045, -71.110790, 1, 1422",
            @"42.364995, -71.110782, 0, 1424",
            @"42.364947, -71.110768, 0, 1426",
            @"42.364896, -71.110766, 0, 1428",
            @"42.364846, -71.110752, 0, 1430",
            @"42.364797, -71.110739, 0, 1432",
            
            @"42.365348, -71.110924, 1, 1411",
            @"42.365300, -71.110916, 0, 1413",
            @"42.365251, -71.110905, 0, 1415",
            @"42.365203, -71.110900, 0, 1417",
            @"42.365154, -71.110892, 1, 1419",
            @"42.365104, -71.110876, 0, 1421",
            @"42.365049, -71.110868, 1, 1423",
            @"42.364993, -71.110860, 1, 1425",
            @"42.364943, -71.110849, 1, 1427",
            @"42.364894, -71.110846, 1, 1429",
            @"42.364846, -71.110835, 0, 1431",
            @"42.364799, -71.110830, 1, 1433",
            nil];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(alertView.tag==GPS_LAUNCH_ALERT && alertView.firstOtherButtonIndex==buttonIndex){
        //this URL kinda works.  http://maps.google.com/maps?daddr=Spot+1412@42,-73&saddr=Current+Location@42,-72
        
        //if yes, store the destination's lat/lon for return launch and start gps app.  
        NSString* destination =[NSString stringWithFormat:@"http://maps.google.com/maps?daddr=Spot+%d@%1.2f,%1.2f&saddr=Current+Location@%1.2f,%1.2f", 1412, 41.343, -74.115, 43.124, -72.31552];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:destination]];
    }else if(alertView.tag == CALLOUT_TAPPED && alertView.firstOtherButtonIndex==buttonIndex){
        
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
        UINavigationController *vc = [storyboard instantiateViewControllerWithIdentifier:@"ParkingController"];
        [vc setModalPresentationStyle:UIModalPresentationFullScreen];
        PQParkingViewController *vcTop = [[vc viewControllers] objectAtIndex:0];
        vcTop.parent = self;
        [self presentModalViewController:vc animated:YES];
        
        
    }
}

- (double) dot_prodX1:(double)x1 Y1:(double)y1 X2:(double)x2 Y2:(double)y2 {
    return x1*x2 + y1*y2;
}

-(double) dot_relative_to_P:(CLLocationCoordinate2D*)p V:(CLLocationCoordinate2D*)v W:(CLLocationCoordinate2D*)w{
    return [self dot_prodX1:(*p).latitude - (*v).latitude 
                         Y1:(*p).longitude - (*v).longitude 
                         X2:(*v).latitude - (*w).latitude 
                         Y2:(*v).longitude - (*w).longitude];
}

- (double) l_sqrdV:(CLLocationCoordinate2D*)v W:(CLLocationCoordinate2D*) w{
    return ((*v).latitude-(*w).latitude)*((*v).latitude-(*w).latitude) 
    + ((*v).longitude - (*w).longitude)*((*v).longitude - (*w).longitude);
}

- (NSArray *)calloutBubblePlacement:(CLLocationCoordinate2D *)selectionCenter withR:(CLLocationDistance) radius{
    //using the street information, snap to the street via geometric projection 
    
    
    CLLocation* center = [[CLLocation alloc] initWithLatitude:(*selectionCenter).latitude longitude:(*selectionCenter).longitude];
    
    //look through list of points, check spot distanceFromLocation (coord) vs radius.  
    NSArray* data = [self loadSpotData];
    //keep track of some stuff
    float avgLat=0, avgLon=0, count=0;
    
    for(id spot in data){
        NSArray* point = [spot componentsSeparatedByString:@","];
        CLLocation* spot_loc = [[CLLocation alloc] initWithLatitude:[[point objectAtIndex:0] floatValue] longitude:[[point objectAtIndex:1] floatValue]];
        
        CLLocationDistance dist = [spot_loc distanceFromLocation:center];
        
        if(dist<radius-2){
            //inside the circle
            avgLat += spot_loc.coordinate.latitude;
            avgLon += spot_loc.coordinate.longitude;   
            count++;
        }
    }
    
    
    //compute the average point of those inside the selection circle.  
    avgLat /= count;
    avgLon /= count;
    NSMutableArray* results = [[NSMutableArray alloc] initWithCapacity:count];
    if(count==1){
        //if only one circle was within radius, do not callout.  
        //as it breaks the callout line algorithm.  
        return results;
    }
    //project bubbles using this average and the spot's coordinates.  
    for(id spot in data){
        NSArray* point = [spot componentsSeparatedByString:@","];
        CLLocation* spot_loc = [[CLLocation alloc] initWithLatitude:[[point objectAtIndex:0] floatValue] longitude:[[point objectAtIndex:1] floatValue]];
        
        CLLocationDistance dist = [spot_loc distanceFromLocation:center];
        
        
        //controls how far the callouts go.  
        double rsq = CALLOUT_LINE_LENGTH;

        //only make callout for those clearly in circle
        //using 2 because the radius of small red circles is 2.  aka don't call it out
        //unless the red circle is clearly inside selection circle.  
        if(dist<radius-2){ 
            
            double mdx = avgLat - [[point objectAtIndex:0] floatValue];
            double mdy = avgLon - [[point objectAtIndex:1] floatValue];
            double dx = sqrt(rsq / (1 + (mdy * mdy)/(mdx * mdx)));
            double dy = sqrt(rsq / (1 + (mdx * mdx)/(mdy * mdy)));
            
            if(mdy>0){
                if(mdx>0){
                    //bottom left of avg
                    NSDictionary* add = [[NSDictionary alloc] initWithObjectsAndKeys:[[CalloutMapAnnotation alloc]                                                                                       initWithLatitude:avgLat - dx                                         andLongitude:avgLon - dy 
                                                                                                                                                                                                                 andTitle:[point objectAtIndex:3]
                                                                                                                                                                                                                andCorner:kTopRightCorner], @"callout",
                                         spot_loc, @"spot", nil];
                    [results addObject:add];
                }else{
                    //bottom right of avg
                    NSDictionary* add = [[NSDictionary alloc] initWithObjectsAndKeys:[[CalloutMapAnnotation alloc] 
                        initWithLatitude:avgLat + dx                                               andLongitude:avgLon - dy
                                                                                                                andTitle:[point objectAtIndex:3]
                                                                                                                          andCorner:kBottomRightCorner], @"callout",
                                         spot_loc, @"spot", nil];
                    [results addObject:add];
                }
            }else{
                if(mdx>0){
                    //top Left of avg
                    NSDictionary* add = [[NSDictionary alloc] initWithObjectsAndKeys:[[CalloutMapAnnotation alloc] initWithLatitude:avgLat - dx                                          andLongitude:avgLon + dy
                                                                                                                           andTitle:[point objectAtIndex:3]
                                                                                                                          andCorner:kTopLeftCorner], @"callout",
                                         spot_loc, @"spot", nil];
                    [results addObject:add];
                }else{
                    //top right of avg
                    NSDictionary* add = [[NSDictionary alloc] initWithObjectsAndKeys:[[CalloutMapAnnotation alloc] initWithLatitude:avgLat + dx                                          andLongitude:avgLon + dy
                                                                                                                           andTitle:[point objectAtIndex:3]
                                                                                                                          andCorner:kBottomLeftCorner], @"callout",
                                         spot_loc, @"spot", nil];
                    [results addObject:add];
                }
            }
        }
        
    }//dx = dy/dx
    return results;
    
}

- (void)clearMap {
    [self.map removeOverlays:self.map.overlays];
    [self.map removeAnnotations:self.map.annotations];
}

- (void)showSelectionCircle:(CLLocationCoordinate2D *)coord {
    int radius = GREY_CIRCLE_R;
    
    NSArray *placement = [self calloutBubblePlacement:coord withR:radius];
    if(placement.count >0){
        [self.map setCenterCoordinate:*coord animated:YES];
    }
    MKCircle *greyCircle= [MKCircle circleWithCenterCoordinate:*coord radius:radius];
    [greyCircle setColor:-1];
    [self.map addOverlay:greyCircle];
    
    if(calloutLines==NULL || callouts == NULL){
        calloutLines = [[NSMutableArray alloc]initWithCapacity:MAX_CALLOUTS];
        callouts = [[NSMutableArray alloc] initWithCapacity:MAX_CALLOUTS];
        
    }else{
        [self.map removeAnnotations:callouts];
        [self.map removeOverlays:calloutLines];
        [self.map removeOverlay:gCircle];
        
        [calloutLines removeAllObjects];
        [callouts removeAllObjects];
    }
    gCircle = greyCircle;
    
    for (NSDictionary *bubble in placement) {
        CLLocationCoordinate2D endpoints[2];
        CalloutMapAnnotation *callout = [bubble objectForKey:@"callout"];
        double corner_lat = callout.coordinate.latitude;
        double corner_lon = callout.coordinate.longitude;
        switch (callout.corner) {
                /*
                 The callout Line currently goes to an off-center of annotation, 
                 for future aesthetics, maybe rearrange?
                 */
                
//            case kBottomRightCorner:
//                NSLog(@"%s has corner bottom right\n", [callout.title cString]);
//                corner_lat-=annotation_offset_y;
//                corner_lon-=annotation_offset_x;
//                break;
//            case kBottomLeftCorner:
//                corner_lat-=annotation_offset_y;
//                corner_lon-=annotation_offset_x;
//                break;
//            case kTopLeftCorner:
//                corner_lat+=annotation_offset_y;
//                corner_lon+=annotation_offset_x;
//                break;
//            case kTopRightCorner:
//                corner_lat+=annotation_offset_y;
//                corner_lon+=annotation_offset_x;
//                break;
            default:
                //NSLog(@"error, corner type unknown\n");
                corner_lat = callout.coordinate.latitude;
                corner_lon = callout.coordinate.longitude;
                break;
        }

        endpoints[0] = CLLocationCoordinate2DMake(corner_lat, corner_lon);
        endpoints[1] = ((CLLocation *)[bubble objectForKey:@"spot"]).coordinate;
        MKPolyline *calloutLine = [MKPolyline polylineWithCoordinates:endpoints count:2];
        [calloutLine setColor:-1];
        [callouts addObject:callout];
        [calloutLines addObject:calloutLine];

        [self.map addAnnotation:callout];
        [self.map addOverlay:calloutLine];        
    }
}

- (void)showGridLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
    [self clearMap];
    
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
    [self clearMap];
    
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
    [self clearMap];
    
    NSArray* data = [self loadSpotData];
    for(id spot in data){
        NSArray* point = [spot componentsSeparatedByString:@","];
        CLLocationCoordinate2D coord = CLLocationCoordinate2DMake([[point objectAtIndex:0] floatValue], [[point objectAtIndex:1] floatValue]);
        int color = [[point objectAtIndex:2] intValue];
        MKCircle* circle;
        if(color==0){
            circle  = [MKCircle circleWithCenterCoordinate:coord radius:2];
        }else if(color==1){
            circle = [MKCircle circleWithCenterCoordinate:coord radius:3];   
        }
        [circle setColor:color];
        [self.map addOverlay:circle];
    }
}

- (void)showAvailabilitySelectionView {
    self.availabilitySelectionView.hidden = NO;
    self.topSpotSelectionView.hidden = YES;
    self.bottomSpotSelectionView.hidden = YES;
}

- (void)showSpotSelectionViews {
    self.topSpotSelectionView.hidden = NO;
    self.bottomSpotSelectionView.hidden = NO;
    self.availabilitySelectionView.hidden = YES;
}

- (void)mapView:(MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated {
    //no longer getting called after user taps callout bubble....
    CLLocationCoordinate2D coord = mapView.centerCoordinate;
    if (mapView.region.span.latitudeDelta>=STREET_MAP_SPAN) {
        NSLog(@"GRID:currSpan: %f\n", mapView.region.span.latitudeDelta);
        zoomState = kGridZoomLevel;
        [self showGridLevelWithCoordinates:&coord];
        [self showAvailabilitySelectionView];
    } else if (mapView.region.span.latitudeDelta>=SPOT_MAP_SPAN) {
        NSLog(@"Block:currSpan: %f\n", mapView.region.span.latitudeDelta);
        zoomState = kStreetZoomLevel;
        [self showStreetLevelWithCoordinates:&coord];
        [self showAvailabilitySelectionView];
    } else {        
        
        //NSLog(@"currSpan: %f\n", mapView.region.span.latitudeDelta);
        zoomState = kSpotZoomLevel;
        [self showSpotSelectionViews];
        
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
                //51 204 0
                
                circleView.lineWidth = 6;
                break;
            case 0:
                //taken
                circleView.fillColor = [[UIColor redColor] colorWithAlphaComponent:0.9];
                circleView.strokeColor = [UIColor whiteColor];
                circleView.lineWidth = 2;
                break;
            case 1:
                //free
                circleView.fillColor = [UIColor colorWithRed:51.0/255 green:224.0/255 blue:0 alpha:0.8];;
                circleView.strokeColor = [UIColor whiteColor];
                circleView.lineWidth = 2;
                break;
        }
        return circleView;
    }
    return nil;
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
	if ([annotation isKindOfClass:[CalloutMapAnnotation class]]) {
        
        //reusing annotation views causes the displayed numberes to be incorrect 
        //ie.  1412's callout is 1422 or something.  temporarily removed, not too much slower.
		CalloutMapAnnotationView *calloutMapAnnotationView;// = (CalloutMapAnnotationView *)[self.map dequeueReusableAnnotationViewWithIdentifier:@"CalloutAnnotation"];
		if (!calloutMapAnnotationView) {
			calloutMapAnnotationView = [[CalloutMapAnnotationView alloc] initWithAnnotation:annotation 
                                                                            reuseIdentifier:@"CalloutAnnotation"];
            
		}
		calloutMapAnnotationView.mapView = self.map;
		return calloutMapAnnotationView;
	}
    
	return nil;
}

-(void) mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view{
    
     //ask user if that's desired destination
   
    NSString* destination =[NSString stringWithFormat:@"Park at %s?",     [view.annotation.title UTF8String]];
    UIAlertView* warningAlertView = [[UIAlertView alloc] initWithTitle:destination message:nil delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Park" , nil];
     warningAlertView.tag = CALLOUT_TAPPED;
     [warningAlertView show];
     
     
}
-(void) mapView:(MKMapView *)mapView didDeselectAnnotationView:(MKAnnotationView *)view{
    NSLog(@"OMG unselected me\n");
}

-(void) mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control{
    NSLog(@"OMG second touched me\n");
    
}
//given a point p, and a segment a to b, find the orthogonally projected point on the segment.
-(CLLocationCoordinate2D) getProjectedPoint: (CLLocationCoordinate2D*) p A:(CLLocationCoordinate2D*)a B:(CLLocationCoordinate2D*) b{
    double x1 = (*b).latitude;
    double y1 = (*b).longitude;
    double dy = y1 - (*a).longitude;
    double dx = x1- (*a).latitude;
    double m = dy/dx; 
    double b1 = y1 - x1*m;
    double x2 = (*p).latitude;
    double y2 = (*p).longitude;
    double new_x = (m*y2 + x2 - m*y1 + m*m*x1) / (m*m+1);
    double new_y = m*new_x + b1;
    return CLLocationCoordinate2DMake(new_x, new_y);
}

-(void)handlePanGesture:(UIGestureRecognizer*)gestureRecognizer{
    if(gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;
    if(zoomState == kGridZoomLevel){
        
        //ping server for new data with coordinates.  
    }else if(zoomState == kStreetZoomLevel){
        
        //ping server for new data with coordinates
    }else{
        if(callouts.count >0){
            //remove overlays on pan
            [self.map removeAnnotations:callouts];
            [self.map removeOverlays:calloutLines];   
            [calloutLines removeAllObjects];
            [callouts removeAllObjects];    
        }
        [self.map removeOverlay:gCircle];
        //ping server for new data
    }
    
    
}

- (void)handleTapGesture:(UIGestureRecognizer *)gestureRecognizer
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
        
    } else if (zoomState==kSpotZoomLevel) {
        if([gestureRecognizer numberOfTouches]==1){
            //snap the circle to the closest polyline.  
                
            [self showSelectionCircle:&coord];
        }
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
            self.navigationBar.leftBarButtonItem = nil;
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
    //close search bar on tap anywhere.
    [self setSearchBar:(UISearchBar *)self.topSearchBar active:NO];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    [self clearMap];
    
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
    zoomState = kGridZoomLevel;
    
    //set the zoom to fit 12 grids perfectly
    CLLocationCoordinate2D point = CLLocationCoordinate2DMake(42.365045,-71.118965);
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, METERS_PER_MILE, METERS_PER_MILE);  //this is essentially zoom level in android
    
    MKCoordinateRegion adjustedRegion = [map regionThatFits:viewRegion];
    
    [self.map setRegion:adjustedRegion animated:YES];
    [self showGridLevelWithCoordinates:&point];
}

- (IBAction)streetButtonPressed:(id)sender {
    CLLocationCoordinate2D point = {42.364854,-71.109438};
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, METERS_PER_MILE/2, METERS_PER_MILE/2);
    [self.map setRegion:[self.map regionThatFits:viewRegion] animated:YES];
    [self showStreetLevelWithCoordinates:&point];
}
- (IBAction)spotButtonPressed:(id)sender {
    CLLocationCoordinate2D point = {42.365077, -71.110838};
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, METERS_PER_MILE/16, METERS_PER_MILE/16);
    [map setRegion:[map regionThatFits:viewRegion] animated:YES];
    
    [self showSpotLevelWithCoordinates:&point];
}
- (IBAction)noneButtonPressed:(id)sender {
    [self clearMap];
}
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {   
    return YES;
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
        
        [self spotButtonPressed:nil];
        
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
    [super viewDidLoad];
    
    //check the current zoom level to set the ZOOM_STATE integer.  
    if (self.map.bounds.size.width > STREET_MAP_SPAN) {
        zoomState = kGridZoomLevel;
        //above middle zoom level        
    } else if (self.map.bounds.size.width > SPOT_MAP_SPAN) {
        //above spot zoom level
        zoomState = kStreetZoomLevel;
    } else {
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
    
    [self.topSpotSelectionBar setWidth:36 forSegmentAtIndex:0];
    [self.topSpotSelectionBar setWidth:36 forSegmentAtIndex:5];
    [self.bottomSpotSelectionBar setWidth:36 forSegmentAtIndex:0];
    [self.bottomSpotSelectionBar setWidth:36 forSegmentAtIndex:5];
    
    // Do any additional setup after loading the view, typically from a nib.
    self.map.delegate=self;
    self.map.showsUserLocation = YES;
    
    //setup gesture recognizer for grids and blocks and spots.  
    UITapGestureRecognizer *tgr = [[UITapGestureRecognizer alloc]
                                   initWithTarget:self action:@selector(handleTapGesture:)];
    [self.map addGestureRecognizer:tgr];
    
    //add a pan gesture to map on TOP of already existing one.  
    UIPanGestureRecognizer* pgr = [[UIPanGestureRecognizer alloc]
                                   initWithTarget:self action:@selector(handlePanGesture:)];
    pgr.delegate = self;
    [self.map addGestureRecognizer:pgr];
        
    //[self gestureRecognizer:tgr shouldRecognizeSimultaneouslyWithGestureRecognizer:pgr];
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
    [self setBottomSpotSelectionBar:nil];
    [self setTopSpotSelectionBar:nil];
    [self setAvailabilitySelectionView:nil];
    [self setBottomSpotSelectionView:nil];
    [self setTopSpotSelectionView:nil];
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
