//
//  RateObject.h
//  hello
//
//  Created by Michael Xia on 1/14/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RateObject : NSObject{
    NSNumber* lat;
    NSNumber* lon;
    NSNumber* spot;
    NSNumber* minTime;
    NSNumber* maxTime;
    NSNumber* defaultRate;
    NSNumber* minIncrement;
    NSString* description;
}
-(id)initWithLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn Spot:(NSNumber*)spotIn Min:(NSNumber*)minIn Max:(NSNumber*)maxIn DefRate:(NSNumber*)defRateIn minInc:(NSNumber*)minIncIn desc: (NSString*) descIn;
@property (retain) NSNumber* lat;
@property (retain) NSNumber* lon;
@property (retain) NSNumber* spot;
@property (retain) NSNumber* minTime;
@property (retain) NSNumber* maxTime;
@property (retain) NSNumber* defaultRate;
@property (retain) NSNumber* minIncrement;
@property (retain) NSString* description;
@end
