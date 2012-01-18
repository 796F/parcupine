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
@property (nonatomic, retain) NSNumber* lat;
@property (nonatomic, retain) NSNumber* lon;
@property (nonatomic, retain) NSNumber* spot;
@property (nonatomic, retain) NSNumber* minTime;
@property (nonatomic, retain) NSNumber* maxTime;
@property (nonatomic, retain) NSNumber* defaultRate;
@property (nonatomic, retain) NSNumber* minIncrement;
@property (nonatomic, retain) NSString* description;
@end
