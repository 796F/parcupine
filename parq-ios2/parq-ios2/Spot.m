//
//  Spot.m
//  Parq
//
//  Created by Michael Xia on 7/11/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "Spot.h"
#import "Waypoint.h"


@implementation Spot

@dynamic lat;
@dynamic lon;
@dynamic microblock;
@dynamic segNumber;
@dynamic spotId;
@dynamic spotNumber;
@dynamic status;
@dynamic parentWaypoint;

-(PQSpotAnnotation*) generateOverlay{
    //allocate an annotation, fill it with the info provided
    CLLocationCoordinate2D coord = CLLocationCoordinate2DMake([self.lat doubleValue], [self.lon doubleValue]);
    PQSpotAnnotation *annotation = [[PQSpotAnnotation alloc] initWithCoordinate:coord available:[self.status intValue] name:[self.spotNumber intValue] objId:[self.spotId longValue]];
    return annotation;
}

@end
