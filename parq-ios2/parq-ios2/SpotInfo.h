//
//  SpotInfo.h
//  Parq
//
//  Created by Michael Xia on 7/19/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SpotInfo : NSObject <NSCoding>{
    
}

@property (nonatomic, retain) NSNumber* spotId;
@property (nonatomic, retain) NSNumber* spotNumber;
@property (nonatomic, retain) NSNumber* minTime;
@property (nonatomic, retain) NSNumber* maxTime;
@property (nonatomic, retain) NSNumber* rateCents;
@property (nonatomic, retain) NSNumber* minuteInterval;
@property (nonatomic, retain) NSString* streetName;
@property (nonatomic, retain) NSNumber* latitude;
@property (nonatomic, retain) NSNumber* longitude;


@end
