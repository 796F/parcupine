//
//  SpotInfo.m
//  Parq
//
//  Created by Michael Xia on 7/19/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "SpotInfo.h"

@implementation SpotInfo

@synthesize spotId;
@synthesize spotNumber;
@synthesize minTime;
@synthesize maxTime;
@synthesize rateCents;
@synthesize minuteInterval;
@synthesize streetName;
@synthesize latitude;
@synthesize longitude;

#pragma mark NSCoding

-(void) encodeWithCoder: (NSCoder*) coder {
    [coder encodeObject:spotId forKey:@"spotId"];
    [coder encodeObject:spotNumber forKey:@"spotNumber"];
    [coder encodeObject:minTime forKey:@"minTime"];
    [coder encodeObject:maxTime forKey:@"maxTime"];
    [coder encodeObject:rateCents forKey:@"rateCents"];
    [coder encodeObject:minuteInterval forKey:@"minuteInterval"];
    [coder encodeObject:streetName forKey:@"streetName"];
    [coder encodeObject:latitude forKey:@"latitude"];
    [coder encodeObject:longitude forKey:@"longitude"];
}

-(id) initWithCoder: (NSCoder*) coder {
    self = [super init];
    if ( ! self) return nil;
    spotId = [coder decodeObjectForKey: @"spotId"];
    spotNumber =[coder decodeObjectForKey: @"spotNumber"];
    minTime = [coder decodeObjectForKey: @"minTime"];
    maxTime = [coder decodeObjectForKey: @"maxTime"];
    rateCents = [coder decodeObjectForKey: @"rateCents"];
    minuteInterval = [coder decodeObjectForKey: @"minuteInterval"];
    streetName = [coder decodeObjectForKey: @"streetName"];
    latitude = [coder decodeObjectForKey: @"latitude"];
    longitude = [coder decodeObjectForKey: @"longitude"];
    return self;
}

@end
