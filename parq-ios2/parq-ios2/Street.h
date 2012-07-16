//
//  Street.h
//  Parq
//
//  Created by Michael Xia on 7/11/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>
#import <MapKit/MapKit.h>
#import "MKShape+Color.h"

@class Waypoint;

@interface Street : NSManagedObject

@property (nonatomic, retain) NSNumber * microblock;
@property (nonatomic, retain) NSNumber * streetId;
@property (nonatomic, retain) NSString * streetName;
@property (nonatomic, retain) NSOrderedSet *childrenWaypoints;
@end

@interface Street (CoreDataGeneratedAccessors)

- (void)insertObject:(Waypoint *)value inChildrenWaypointsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromChildrenWaypointsAtIndex:(NSUInteger)idx;
- (void)insertChildrenWaypoints:(NSArray *)value atIndexes:(NSIndexSet *)indexes;
- (void)removeChildrenWaypointsAtIndexes:(NSIndexSet *)indexes;
- (void)replaceObjectInChildrenWaypointsAtIndex:(NSUInteger)idx withObject:(Waypoint *)value;
- (void)replaceChildrenWaypointsAtIndexes:(NSIndexSet *)indexes withChildrenWaypoints:(NSArray *)values;
- (void)addChildrenWaypointsObject:(Waypoint *)value;
- (void)removeChildrenWaypointsObject:(Waypoint *)value;
- (void)addChildrenWaypoints:(NSOrderedSet *)values;
- (void)removeChildrenWaypoints:(NSOrderedSet *)values;
-(MKPolyline*) generateOverlay;
@end
