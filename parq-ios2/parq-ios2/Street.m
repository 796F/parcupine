//
//  Street.m
//  Parq
//
//  Created by Michael Xia on 7/11/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "Street.h"
#import "Waypoint.h"


@implementation Street

@dynamic microblock;
@dynamic streetId;
@dynamic streetName;
@dynamic childrenWaypoints;

-(MKPolyline*) generateOverlay {
    //grab the waypoints that belong to this street
    int count = self.childrenWaypoints.count;
    CLLocationCoordinate2D waypoints[count];
    for(Waypoint* point in self.childrenWaypoints){
        //itereate, and set them as the points of the polyline
        
    }
    //create and return the view.  
    MKPolyline* line = [MKPolyline polylineWithCoordinates:waypoints count:count];
    return line;
}

@end
