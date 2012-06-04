//
//  Segment.m
//  Parq
//
//  Created by Michael Xia on 6/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "Segment.h"

@implementation Segment
@synthesize A;
@synthesize B;

-(CLLocationCoordinate2D*) getSegment{
    CLLocationCoordinate2D* points = malloc(sizeof(CLLocationCoordinate2D)*2);
    points[0] = A;
    points[1] = B;
    return points;
}

-(id) initWithPointsA:(CLLocationCoordinate2D*) a andB:(CLLocationCoordinate2D*) b{
    A = *a;
    B = *b;
    return self;
}

@end
