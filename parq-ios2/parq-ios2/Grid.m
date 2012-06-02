//
//  Grid.m
//  Parq
//
//  Created by Michael Xia on 6/1/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "Grid.h"


@implementation Grid

@dynamic gridId;
@dynamic lat;
@dynamic lon;

-(id) initWithGridId:(long) gid Latitude:(double) mylat Longitude:(double)mylon{
    gridId = [NSNumber numberWithLong:gid];
    lat = [NSNumber numberWithDouble:mylat];
    lon = [NSNumber numberWithDouble:mylon];
    return self;
}

@end
