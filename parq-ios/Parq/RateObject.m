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

-(NSNumber*) lat{
    return lat;
}
-(NSNumber*) lon{
    return lon;
}
-(NSNumber*) spot{
    return spot;
}
-(NSNumber*) minTime{
    return minTime;
}
-(NSNumber*) maxTime{
    return maxTime;
}
-(NSNumber*) defaultRate{
    return defaultRate;
}
-(NSNumber*) minIncrement{
    return minIncrement;
}
-(NSString*) description{
    return description;
}
@end
