//
//  Street.h
//  Parq
//
//  Created by Michael Xia on 6/4/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Spot;

@interface Street : NSManagedObject

@property (nonatomic, retain) NSNumber * streetId;
@property (nonatomic, retain) NSString * streetName;
@property (nonatomic, retain) NSSet *childrenSpots;
@property (nonatomic, retain) NSOrderedSet *childrenWaypoints;
@end

@interface Street (CoreDataGeneratedAccessors)

- (void)addChildrenSpotsObject:(Spot *)value;
- (void)removeChildrenSpotsObject:(Spot *)value;
- (void)addChildrenSpots:(NSSet *)values;
- (void)removeChildrenSpots:(NSSet *)values;

- (void)insertObject:(NSManagedObject *)value inChildrenWaypointsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromChildrenWaypointsAtIndex:(NSUInteger)idx;
- (void)insertChildrenWaypoints:(NSArray *)value atIndexes:(NSIndexSet *)indexes;
- (void)removeChildrenWaypointsAtIndexes:(NSIndexSet *)indexes;
- (void)replaceObjectInChildrenWaypointsAtIndex:(NSUInteger)idx withObject:(NSManagedObject *)value;
- (void)replaceChildrenWaypointsAtIndexes:(NSIndexSet *)indexes withChildrenWaypoints:(NSArray *)values;
- (void)addChildrenWaypointsObject:(NSManagedObject *)value;
- (void)removeChildrenWaypointsObject:(NSManagedObject *)value;
- (void)addChildrenWaypoints:(NSOrderedSet *)values;
- (void)removeChildrenWaypoints:(NSOrderedSet *)values;
@end
