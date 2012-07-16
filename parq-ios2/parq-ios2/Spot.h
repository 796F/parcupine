//
//  Spot.h
//  Parq
//
//  Created by Michael Xia on 7/11/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>
#import <MapKit/MapKit.h>
//#import "MKShape+Color.h"
#import "PQSpotAnnotation.h"

@class Waypoint;

@interface Spot : NSManagedObject

@property (nonatomic, retain) NSNumber * lat;
@property (nonatomic, retain) NSNumber * lon;
@property (nonatomic, retain) NSNumber * microblock;
@property (nonatomic, retain) NSNumber * segNumber;
@property (nonatomic, retain) NSNumber * spotId;
@property (nonatomic, retain) NSNumber * spotNumber;
@property (nonatomic, retain) NSNumber * status;
@property (nonatomic, retain) Waypoint *parentWaypoint;

-(PQSpotAnnotation*) generateOverlay;

@end
