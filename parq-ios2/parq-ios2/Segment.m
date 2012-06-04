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
@synthesize color;

-(Segment*) initWithPointsA:(CLLocationCoordinate2D*) a andB:(CLLocationCoordinate2D*) b andColor:(int) colorIn{
    A = *a;
    B = *b;
    color = colorIn;
    return self;
}

@end
