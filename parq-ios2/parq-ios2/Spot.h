//
//  Spot.h
//  Parq
//
//  Created by Michael Xia on 6/4/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Street;

@interface Spot : NSManagedObject{
    NSNumber* lat;
    NSNumber* lon;
    NSNumber* segNumber;
    NSNumber* spotNumber;
    NSNumber* spotId;
    Street* parentStreet;
}

@property (nonatomic, retain) NSNumber * lat;
@property (nonatomic, retain) NSNumber * lon;
@property (nonatomic, retain) NSNumber * segNumber;
@property (nonatomic, retain) NSNumber * spotNumber;
@property (nonatomic, retain) NSNumber * spotId;
@property (nonatomic, retain) Street *parentStreet;

-(id) makeWithSpotNumber:(int)spotNum segNumber:(int) segNum spotId:(long)spotIdIn Latitude:(double)latIn Longitude:(double)lonIn;

@end
