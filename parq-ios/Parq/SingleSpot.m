//
//  SingleSpot.m
//  Parq
//
//  Created by Michael Xia on 1/18/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "SingleSpot.h"

@implementation SingleSpot

- (SingleSpot*) initWithLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn Desc:(NSString*) descriptionIn{
    lat = latIn;
    lon = lonIn;
    description = descriptionIn;
    return self;
}
@synthesize lat;
@synthesize lon;
@synthesize description;
@end
