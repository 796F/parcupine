//
//  Waypoint.h
//  Parq
//
//  Created by Michael Xia on 7/11/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Spot, Street;

@interface Waypoint : NSManagedObject

@property (nonatomic, retain) NSNumber * lat;
@property (nonatomic, retain) NSNumber * lon;
@property (nonatomic, retain) NSNumber * microblock;
@property (nonatomic, retain) NSNumber * status;
@property (nonatomic, retain) Street *parentStreet;
@property (nonatomic, retain) NSOrderedSet *childrenSpots;
@end

@interface Waypoint (CoreDataGeneratedAccessors)

- (void)insertObject:(Spot *)value inChildrenSpotsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromChildrenSpotsAtIndex:(NSUInteger)idx;
- (void)insertChildrenSpots:(NSArray *)value atIndexes:(NSIndexSet *)indexes;
- (void)removeChildrenSpotsAtIndexes:(NSIndexSet *)indexes;
- (void)replaceObjectInChildrenSpotsAtIndex:(NSUInteger)idx withObject:(Spot *)value;
- (void)replaceChildrenSpotsAtIndexes:(NSIndexSet *)indexes withChildrenSpots:(NSArray *)values;
- (void)addChildrenSpotsObject:(Spot *)value;
- (void)removeChildrenSpotsObject:(Spot *)value;
- (void)addChildrenSpots:(NSOrderedSet *)values;
- (void)removeChildrenSpots:(NSOrderedSet *)values;



@end
