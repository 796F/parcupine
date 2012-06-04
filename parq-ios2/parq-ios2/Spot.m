//
//  Spot.m
//  Parq
//
//  Created by Michael Xia on 6/4/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "Spot.h"
#import "Street.h"


@implementation Spot

@dynamic lat;
@dynamic lon;
@dynamic segNumber;
@dynamic spotNumber;
@dynamic spotId;
@dynamic parentStreet;

-(id) makeWithSpotNumber:(int)spotNum segNumber:(int) segNum spotId:(long)spotIdIn Latitude:(double)latIn Longitude:(double)lonIn{
    Spot* spot = [Spot alloc];
    spot.spotNumber = [NSNumber numberWithInt:spotNum];
    spot.segNumber = [NSNumber numberWithInt:segNum];
    spot.spotId = [NSNumber numberWithLong:spotIdIn];
    spot.lat = [NSNumber numberWithDouble:latIn];
    spot.lon = [NSNumber numberWithDouble:lonIn];
    return spot;
}

@end
