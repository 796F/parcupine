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
#import "NetworkLayer.h"
#import "Segment.h"
#import "Spot.h"

//calculation constants
#define METERS_PER_MILE 1609.344
#define MAX_CALLOUTS 8
#define GREY_CIRCLE_R 12
#define annotation_offset_y 0.000045
#define annotation_offset_x 0.00012
#define CALLOUT_LINE_LENGTH 0.00000023
#define CALLOUT_WIDTH 0.00008
#define CALLOUT_HEIGHT 0.00015
#define STREET_MAP_SPAN 0.0132
#define SPOT_MAP_SPAN 0.0017
#define ACCURACY_LIMIT 200
#define USER_DISTANCE_FROM_SPOT_THRESHOLD 0.01


//alert view tags
#define GPS_LAUNCH_ALERT 0
#define CALLOUT_TAPPED 1

typedef enum {
    kGridZoomLevel,
    kStreetZoomLevel,
    kSpotZoomLevel
} ZoomLevel;

typedef struct{
    CLLocationCoordinate2D A;
    CLLocationCoordinate2D B;
} SegTwo;

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
@synthesize grids;
@synthesize streets;
@synthesize spots;
@synthesize managedObjectContext;
@synthesize user_loc;
@synthesize user_loc_isGood;
@synthesize desired_spot;
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
    NSArray* data = [NSArray arrayWithObjects:
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364551,-71.113099;42.364753,-71.110776", @"line", [NSNumber numberWithInt:0], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36643,-71.111047;42.363285,-71.110543", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.364904,-71.112343", @"line", [NSNumber numberWithInt:2], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364904,-71.112343;42.364618,-71.112311", @"line", [NSNumber numberWithInt:0], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.365294,-71.111857", @"line", [NSNumber numberWithInt:4], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365294,-71.111857;42.365383,-71.110889", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36532,-71.111565;42.366043,-71.111667", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366043,-71.111667;42.36622,-71.111839", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36622,-71.111839;42.366392,-71.112826", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366788,-71.11193;42.366412,-71.111031", @"line", [NSNumber numberWithInt:0], @"color", nil],nil];
    
    NSMutableArray* segList = [[NSMutableArray alloc] initWithCapacity:2];
    for(id line in data){
        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[raw_waypoints.count];
        int i=0;
        for (id raw_waypoint in raw_waypoints) {
            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
            waypoints[i++] = coordinate;
        }
        Segment* x =[[Segment alloc] initWithPointsA:&waypoints[0] andB:&waypoints[1] andColor:[[line objectForKey:@"color"] intValue]];
        [segList addObject:x];
    }
    return segList;
}

-(NSArray*) loadSpotData {
    return [NSArray arrayWithObjects:
            @"42.365354,-71.110843,1,1410,0,1",
            @"42.365292,-71.110835,1,1412,0,2",
            @"42.365239,-71.110825,1,1414,0,3",
            @"42.365187,-71.110811,0,1416,0,4",
            @"42.365140,-71.110806,1,1418,0,5",
            @"42.365092,-71.110798,0,1420,0,6",
            @"42.365045,-71.110790,1,1422,0,7",
            @"42.364995,-71.110782,0,1424,0,8",
            @"42.364947,-71.110768,0,1426,0,9",
            @"42.364896,-71.110766,0,1428,0,10",
            @"42.364846,-71.110752,0,1430,0,11",
            @"42.364797,-71.110739,0,1432,0,12",
            
            @"42.365348,-71.110924,1,1411,0,13",
            @"42.365300,-71.110916,0,1413,0,14",
            @"42.365251,-71.110905,0,1415,0,15",
            @"42.365203,-71.110900,0,1417,0,16",
            @"42.365154,-71.110892,1,1419,0,17",
            @"42.365104,-71.110876,0,1421,0,18",
            @"42.365049,-71.110868,1,1423,0,19",
            @"42.364993,-71.110860,1,1425,0,20",
            @"42.364943,-71.110849,1,1427,0,21",
            @"42.364894,-71.110846,1,1429,0,22",
            @"42.364846,-71.110835,0,1431,0,23",
            @"42.364799,-71.110830,1,1433,0,24",
            nil];
    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(alertView.tag==GPS_LAUNCH_ALERT && alertView.firstOtherButtonIndex==buttonIndex){
        //this URL kinda works.  http://maps.google.com/maps?daddr=Spot+1412@42,-73&saddr=Current+Location@42,-72
        //if yes, store the destination's lat/lon for return launch and start gps app.  
        NSString* destination =[NSString stringWithFormat:@"http://maps.google.com/maps?daddr=Spot+%d@%1.2f,%1.2f&saddr=Current+Location@%1.2f,%1.2f", desired_spot.name, user_loc.latitude, user_loc.longitude, desired_spot.coordinate.latitude, desired_spot.coordinate.longitude];
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
// unused now.  originally intended for snap to street.  
//- (double) dot_prodX1:(double)x1 Y1:(double)y1 X2:(double)x2 Y2:(double)y2 {
//    return x1*x2 + y1*y2;
//}
//
//-(double) dot_relative_to_P:(CLLocationCoordinate2D*)p V:(CLLocationCoordinate2D*)v W:(CLLocationCoordinate2D*)w{
//    return [self dot_prodX1:(*p).latitude - (*v).latitude 
//                         Y1:(*p).longitude - (*v).longitude 
//                         X2:(*v).latitude - (*w).latitude 
//                         Y2:(*v).longitude - (*w).longitude];
//}
//
//- (double) l_sqrdV:(CLLocationCoordinate2D*)v W:(CLLocationCoordinate2D*) w{
//    return ((*v).latitude-(*w).latitude)*((*v).latitude-(*w).latitude) 
//    + ((*v).longitude - (*w).longitude)*((*v).longitude - (*w).longitude);
//}
#pragma mark - CALLOUT PLACEMENT
-(bool)A:(double)a sameSignAsB:(double) b{
    if(a>0){
        return b>0 ? true : false;
    }else{
        return b<0 ? true : false;
    }
}

//separates points for a segment to left and right, uses first segment if multiple exist.  
-(NSArray*) newCalloutPlacementWithSegment:(Segment*)seg andSpots:(NSArray*)spotList{
    double left_coords[2] = {0,0};
    double right_coords[2] = {0,0};
    int left_count = 0;
    int right_count = 0;
    bool error = false;
    double reference_dy = 0;
    double reference_dx = 0;
    //    NSLog(@"TOTAL OF %d IN CIRCLE\n", spotList.count);
    NSMutableArray* leftSide = [[NSMutableArray alloc] init];
    NSMutableArray* rightSide = [[NSMutableArray alloc] init];
    
    for(MKCircle* spot in spotList){
        bool isRightSide = false;        
        //for each spot inside the grey circle, calculate the projected point.  
        CLLocationCoordinate2D x = spot.coordinate;
        CLLocationCoordinate2D segment[2] = {[seg A], [seg B]};
        CLLocationCoordinate2D point = [self getProjectedPoint:&x A:&segment[0] B:&segment[1] error:&error];
        //don't care about errors in this case.  known to be localized.  
        //determine from that, what side of the street a point lays.  
        if(reference_dy == 0){
            //first one, assign to a side, keep it's sign. 
            reference_dy = x.latitude - point.latitude;
            reference_dx = x.longitude - point.longitude;
            isRightSide = true;
            //NSLog(@"%d is right side!\n", [[spotArray objectAtIndex:3] intValue]);
            [rightSide addObject:spot];
        }else{
            //do a check to see if it's the same side as ref_sign
            //compare latitudes, if useful, go on.  
            double spot_dy = x.latitude - point.latitude;
            double spot_dx = x.longitude - point.longitude;
            if([self A:reference_dx sameSignAsB:spot_dx] && [self A:reference_dy sameSignAsB:spot_dy]){
                //same side as reference.  
                //NSLog(@"%d is right side!\n", [[spotArray objectAtIndex:3] intValue]);
                isRightSide = true;
                [rightSide addObject:spot];
            }else{
                //NSLog(@"%d is left side!\n", [[spotArray objectAtIndex:3] intValue]);
                //different side as reference.  
                isRightSide = false;
                [leftSide addObject:spot];
            }
        }
        //add it to the left or right value depending
        if(isRightSide){
            right_coords[0] += point.latitude;
            right_coords[1] += point.longitude;
            right_count++;
        }else{
            left_coords[0] += point.latitude;
            left_coords[1] += point.longitude;
            left_count++;
        }
    }
    right_coords[0] /= right_count;
    right_coords[1] /= right_count;
    left_coords[0] /= left_count;
    left_coords[1] /= left_count;
    CLLocationCoordinate2D averages[2];
    averages[0] = CLLocationCoordinate2DMake(left_coords[0], left_coords[1]);
    averages[1] = CLLocationCoordinate2DMake(right_coords[0], right_coords[1]);
    
    //now have averages for both left and right, as well as arrays.  
    NSMutableArray* calloutArr = [[NSMutableArray alloc] initWithCapacity:right_count+left_count];
    [calloutArr addObjectsFromArray:[self test:leftSide aLat:averages[0].latitude aLon:averages[0].longitude]];
    [ calloutArr addObjectsFromArray:[self test:rightSide aLat:averages[1].latitude aLon:averages[1].longitude]];
    
    return calloutArr;
}

- (NSArray *)calloutBubblePlacement:(CLLocationCoordinate2D *)selectionCenter withR:(CLLocationDistance) radius{
    //using the street information, snap to the street via geometric projection 
    
    CLLocation* center = [[CLLocation alloc] initWithLatitude:(*selectionCenter).latitude longitude:(*selectionCenter).longitude];
    
    //look through list of points, check spot distanceFromLocation (coord) vs radius.  
    //keep track of some stuff
    NSMutableArray* insideCircle = [[NSMutableArray alloc] init];
    float avgLat=0, avgLon=0, count=0;
    for(MKCircle* spot in spots){
        CLLocation* spot_loc = [[CLLocation alloc] initWithLatitude:        spot.coordinate.latitude longitude: spot.coordinate.longitude];
        CLLocationDistance dist = [spot_loc distanceFromLocation:center];
        if(dist<radius-2 && spot.color == 1){
            //inside the circle
            [insideCircle addObject:spot];
            avgLat += spot_loc.coordinate.latitude;
            avgLon += spot_loc.coordinate.longitude;   
            count++;
        }
    }
    /* CALCULATE AVERAGES FOR BOTH SIDES */
    NSArray* segData = [self loadBlockData];
    return [self newCalloutPlacementWithSegment:[segData objectAtIndex:1] andSpots:insideCircle];
    
}

-(NSArray*) test:(NSArray*)spotList aLat:(double)avgLat aLon:(double)avgLon{
    NSMutableArray* results = [[NSMutableArray alloc] initWithCapacity:spots.count];
    
    //project bubbles using this average and the spot's coordinates.  
    for(MKCircle* spot in spotList){
        CLLocation* spot_loc = [[CLLocation alloc] initWithLatitude:spot.coordinate.latitude longitude:spot.coordinate.longitude];
        //controls how far the callouts go.  
        double rsq = CALLOUT_LINE_LENGTH;
        
        //only make callout for those clearly in circle
        
        
        double mdx = avgLat - spot_loc.coordinate.latitude;
        double mdy = avgLon - spot_loc.coordinate.longitude;
        double dx = sqrt(rsq / (1 + (mdy * mdy)/(mdx * mdx)));
        double dy = sqrt(rsq / (1 + (mdx * mdx)/(mdy * mdy)));
        double callout_lat;
        double callout_lon;
        CalloutCorner corner;
        if(mdy>0){
            if(mdx>0){
                //bottom left of avg
                callout_lat = avgLat - dx;
                callout_lon = avgLon - dy;
                corner = kTopRightCorner;
            }else{
                //bottom right of avg
                callout_lat = avgLat + dx;
                callout_lon = avgLon - dy;
                corner = kBottomRightCorner;
            }
        }else{
            if(mdx>0){
                //top Left of avg
                callout_lat = avgLat - dx;
                callout_lon = avgLon + dy;
                corner = kTopLeftCorner;
            }else{
                //top right of avg
                callout_lat = avgLat + dx;
                callout_lon = avgLon + dy;
                corner = kBottomLeftCorner;
                
            }
        }
        NSString* title = [NSString stringWithFormat:@"%d", spot.name];
        NSDictionary* add = [[NSDictionary alloc] initWithObjectsAndKeys:[[CalloutMapAnnotation alloc] initWithLatitude:callout_lat                                         andLongitude:callout_lon
                                                                                                               andTitle:title
                                                                                                              andCorner:corner
                                                                                                              andCircle:spot], @"callout",spot_loc, @"spot", nil];
        
        [results addObject:add];
    }
    return results;
}

-(bool) pointA:(CLLocationCoordinate2D*)a isCloseToB:(CLLocationCoordinate2D*)b{
    double dx = (*a).longitude - (*b).longitude;
    double dy = (*a).latitude - (*b).longitude;
    double hsq = dx*dx+dy*dy;
    NSLog(@"user distance from spot is %f\n", hsq);
    if(hsq < USER_DISTANCE_FROM_SPOT_THRESHOLD ){
        return true;
    }else{
        return false;
    }
    
}

- (bool) tappedCalloutAtCoords:(CLLocationCoordinate2D*) coords{
    for(int i=0; i< callouts.count; i++){
        CalloutMapAnnotation* c = [callouts objectAtIndex:i];
        double dx = c.latitude-(*coords).latitude; 
        double dy = c.longitude-(*coords).longitude;
        if(fabs(dx) < CALLOUT_WIDTH && fabs(dy) < CALLOUT_HEIGHT){
            //check where user's location is.  
            CLLocationCoordinate2D spot_loc = c.circle.coordinate;
            desired_spot = c.circle;
            if(user_loc_isGood && [self pointA:&spot_loc isCloseToB:&user_loc]){

                    //if user is close to location, ask user if that's desired destination
                    NSString* destination =[NSString stringWithFormat:@"Park at %s?",     [c.title UTF8String]];
                    UIAlertView* warningAlertView = [[UIAlertView alloc] initWithTitle:destination message:nil delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Park" , nil];
                    warningAlertView.tag = CALLOUT_TAPPED;
                    [warningAlertView show];
                    return true;
            }else{
                //location isn't good, OR we're far away.  launch gps.  
                NSString* destination =[NSString stringWithFormat:@"Launch GPS to %s?",     [c.title UTF8String]];
                UIAlertView* warningAlertView = [[UIAlertView alloc] initWithTitle:destination message:nil delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Launch" , nil];
                warningAlertView.tag = GPS_LAUNCH_ALERT;
                [warningAlertView show];
                return true;
            }
        }
    }
    return false;
}

-(void) clearCallouts{
    [self.map removeAnnotations:callouts];
    [self.map removeOverlays:calloutLines];   
    [calloutLines removeAllObjects];
    [callouts removeAllObjects];   
}

- (void)clearMap {
    [self clearGrids];
    [self clearStreets];
    [self clearSpots];
    [self clearCallouts];
    [self.map removeOverlay:gCircle];
}

- (void)clearGrids {
    [self.map removeOverlays:self.grids];
    [grids removeAllObjects];
}

-(void) clearStreets{
    [self.map removeOverlays:self.streets];
    [streets removeAllObjects];
}
-(void) clearSpots{
    [self.map removeOverlays:self.spots];
    [spots removeAllObjects];
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
    if(grids==NULL){
        grids = [[NSMutableArray alloc] init];
    }
    
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
        [grids addObject:commuterPoly1];
    }
    [self.map addOverlays:grids];
}

- (void)showStreetLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
    [self clearMap];
    
    NSArray* data = [self loadBlockData];
    if(streets==NULL){
        streets = [[NSMutableArray alloc] init];
    }
    
    for (id line in data) {
        //        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[2] = {[line A], [line B]};
        
        //        int i=0;
        //        for (id raw_waypoint in raw_waypoints) {
        //            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
        //            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
        //            waypoints[i++] = coordinate;
        //        }
        //        
        MKPolyline *routeLine = [MKPolyline polylineWithCoordinates:waypoints count:2];
        
        int color = [((Segment*)line) color];
        [routeLine setColor:color];
        [streets addObject:routeLine];
        
    }
    [self.map addOverlays:streets];
}


- (void)showSpotLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
    [self clearStreets];
    [self clearGrids];
    [self clearSpots];
    
    NSArray* data = [self loadSpotData];
    if(spots==NULL){
        spots = [[NSMutableArray alloc] init];
    }
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
        [circle setName:[[point objectAtIndex:3] intValue]];
        [circle setColor:color];
        [spots addObject:circle];
    }
    [self.map addOverlays:spots];
    //make circle appear on top.
    if(gCircle!=NULL){
        [self.map removeOverlay:gCircle];
        [self.map removeOverlays:calloutLines];
        [self.map addOverlay:gCircle];
        [self.map addOverlays:calloutLines];
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
    
    double currentSpan = mapView.region.span.latitudeDelta;
    CLLocationCoordinate2D center = mapView.centerCoordinate;
    if (currentSpan>=STREET_MAP_SPAN) {
        //NSLog(@"GRID:currSpan: %f\n", mapView.region.span.latitudeDelta);
        zoomState = kGridZoomLevel;
        [self showGridLevelWithCoordinates:&center];
        [self showAvailabilitySelectionView];
    } else if (currentSpan>=SPOT_MAP_SPAN) {
        //NSLog(@"Block:currSpan: %f\n", mapView.region.span.latitudeDelta);
        zoomState = kStreetZoomLevel;
        [self showStreetLevelWithCoordinates:&center];
        [self showAvailabilitySelectionView];
    } else {        
        //NSLog(@"currSpan: %f\n", mapView.region.span.latitudeDelta);
        zoomState = kSpotZoomLevel;
        [self showSpotLevelWithCoordinates:&center];
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

#pragma mark - LINE PROJECTION

//given a point p, and a segment a to b, find the orthogonally projected point on the segment.
-(CLLocationCoordinate2D) getProjectedPoint: (CLLocationCoordinate2D*) p A:(CLLocationCoordinate2D*)a B:(CLLocationCoordinate2D*) b error:(bool*)err{
    double x1 = (*b).latitude;
    double y1 = (*b).longitude;
    double x2 = (*a).latitude;
    double y2 = (*a).longitude;
    double dy = y1 - y2;
    double dx = x1- x2;
    double x3 = (*p).latitude;
    double y3 = (*p).longitude;
    
    double m = dy/dx; 
    double b1 = y1 - x1*m;
    
    double new_x = (m*y3 + x3 - m*y1 + m*m*x1) / (m*m+1);
    double new_y = m*new_x + b1;
    //check validity of projected point.  
    
    if((new_y > y1 && new_y > y2) || (new_y < y1 && new_y < y2)){
        *err = true;
    }
    return CLLocationCoordinate2DMake(new_x, new_y);
}

-(CLLocationCoordinate2D) snapFromCoord:(CLLocationCoordinate2D*) coord toSegments:(NSArray*) segments{
    // each segment is CLLocationCoordinate2D points[2];
    double min_dist = 180*180;
    CLLocationCoordinate2D ret_coord;
    
    for(id line in segments){
        bool error = false;
        CLLocationCoordinate2D seg[2] = {[line A], [line B]};
        CLLocationCoordinate2D snaploc = [self getProjectedPoint:coord A:&seg[0] B:&seg[1] error:&error];
        if(error){
            continue;
        }
        double d_lat = snaploc.latitude - (*coord).latitude;
        double d_lon = snaploc.longitude - (*coord).longitude;
        double dist = d_lat*d_lat + d_lon*d_lon;
        if(dist < min_dist){
            min_dist = dist;
            ret_coord = snaploc;
        }
    }
    return ret_coord;
}

#pragma mark - GESTURE HANDLERS

-(void)handlePanGesture:(UIGestureRecognizer*)gestureRecognizer{
    if(gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;
    if(zoomState == kGridZoomLevel){
        //ping server for new data with coordinates.  
    }else if(zoomState == kStreetZoomLevel){
        
        //ping server for new street data with coordinates
    }else{
        if(callouts.count >0){
            //remove overlays on pan
            [self clearCallouts]; 
        }
        
        //ping server for new spot data
        
        //load the new data to the map
        //asynchronously ping server for street data
        [self.map removeOverlay:gCircle];
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
            NSArray* segList = [self loadBlockData];
            CLLocationCoordinate2D snaploc = [self snapFromCoord:&coord toSegments:segList];
            /* end snap stuff */
            if([self tappedCalloutAtCoords:&coord]){
            }else{
                //if the result returned is valid
                [self showSelectionCircle:&snaploc];                
            }
            
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
        [self.map setCenterCoordinate:locationObject.coordinate animated:YES];
        //        MKCoordinateRegion viewRegion = [self.map regionThatFits:MKCoordinateRegionMakeWithDistance(locationObject.coordinate, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE)];
        //        [self.map setRegion:viewRegion animated:YES];
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
    
}
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {   
    return YES;
}

#pragma mark - LOCATION
- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation{
    
    user_loc = newLocation.coordinate;
    if (MAX(newLocation.horizontalAccuracy, newLocation.verticalAccuracy) < ACCURACY_LIMIT) {
        //if we get a decent read store it and stop updating.  
        user_loc_isGood = true;
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
    if(!dataLayer){
        //prepare the data layer for fetching from core data
        NSLog(@"setting data layer...\n");    
        dataLayer = [((PQAppDelegate*)[[UIApplication sharedApplication] delegate]) getDataLayer];
    }
    if(dataLayer.managedObjectContext.persistentStoreCoordinator.managedObjectModel!=nil){
        NSLog(@"dl set complete.\n");
    }
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
    
    //SETUP LOCATION manager
    locationManager=[[CLLocationManager alloc] init];
    locationManager.delegate=self;
    locationManager.desiredAccuracy=kCLLocationAccuracyNearestTenMeters;
    
    [locationManager startUpdatingLocation];
    user_loc_isGood = false;
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
