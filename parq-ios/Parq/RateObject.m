//
//  RateObject.m
//  hello
//
//  Created by Michael Xia on 1/14/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import "RateObject.h"

@implementation RateObject
-(id)initWithLat:(NSNumber*)latIn 
             Lon:(NSNumber*)lonIn 
            Spot:(NSNumber*)spotIn 
             Min:(NSNumber*)minIn 
             Max:(NSNumber*)maxIn 
         DefRate:(NSNumber*)defRateIn 
          minInc:(NSNumber*)minIncIn 
            desc: (NSString*) descIn{
    lat = latIn;
    lon=lonIn;
    spot = spotIn;
    minTime=minIn;
    maxTime=maxIn;
    defaultRate = defRateIn;
    minIncrement=minIncIn;
    description=descIn;
    return self;
}
@synthesize lat;
@synthesize lon;
@synthesize spot;
@synthesize minTime;
@synthesize maxTime;
@synthesize defaultRate;
@synthesize minIncrement;
@synthesize description;

@end
