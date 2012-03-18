//
//  RateObject.m
//  hello
//
//  Created by Michael Xia on 1/14/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import "RateObject.h"

@implementation RateObject
@synthesize spotNumber;
@synthesize minTime;
@synthesize rateCents;
@synthesize minuteInterval;
@synthesize lotName;
@synthesize latitude;
@synthesize longitude;

-(id)initWithLat:(NSNumber*)latIn
             lon:(NSNumber*)lonIn
            spot:(NSNumber*)spotIn
             min:(NSNumber*)minIn
             max:(NSNumber*)maxIn
         defRate:(NSNumber*)defRateIn
          minInc:(NSNumber*)minIncIn
            desc:(NSString*)descIn
{
    spotNumber = spotIn;
    minTime=minIn;
    rateCents = defRateIn;
    minuteInterval=minIncIn;
    lotName=descIn;
    latitude=latIn;
    longitude=lonIn;
    return self;
}
@end
