//
//  Waypoint.h
//  Parq
//
//  Created by Michael Xia on 6/28/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Street;

@interface Waypoint : NSManagedObject

@property (nonatomic, retain) NSNumber * lat;
@property (nonatomic, retain) NSNumber * lon;
@property (nonatomic, retain) NSNumber * status;
@property (nonatomic, retain) NSNumber * microblock;
@property (nonatomic, retain) Street *parentStreet;

@end
