//
//  RateObject.h
//  hello
//
//  Created by Michael Xia on 1/14/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RateObject : NSObject
@property (nonatomic, retain) NSNumber* spotNumber;
@property (nonatomic, retain) NSNumber* minTime;
@property (nonatomic, retain) NSNumber* rateCents;
@property (nonatomic, retain) NSNumber* minuteInterval;
@property (nonatomic, retain) NSString* lotName;

-(id)initWithLat:(NSNumber*)latIn lon:(NSNumber*)lonIn spot:(NSNumber*)spotIn min:(NSNumber*)minIn max:(NSNumber*)maxIn defRate:(NSNumber*)defRateIn minInc:(NSNumber*)minIncIn desc: (NSString*) descIn;
@end
