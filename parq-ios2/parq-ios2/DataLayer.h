//
//  DataLayer.h
//  Parq
//
//  Created by Michael Xia on 5/31/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "Segment.h"
#define GRID_LENGTH 0.005
@class PQMapViewController;
@interface DataLayer : NSObject {
        NSManagedObjectContext *managedObjectContext;
}

typedef enum {
    kSpotEntity,
    kGridEntity,
    kStreetEntity
} EntityType;

@property (nonatomic,retain) PQMapViewController* mapController;
@property (nonatomic, retain) NSManagedObjectContext *managedObjectContext;

-(BOOL) objExistsInCoreData:(NSObject*)object entityType:(EntityType) entityType;
-(BOOL) mbIdExistsInCoreData:(NSObject*)object entityType:(EntityType) entityType;
- (NSSet *)fetchObjectsForEntityName:(NSString *)newEntityName
                       withPredicate:(id)stringOrPredicate;

//call after server responds.  
-(void) storeSpotData:(NSArray*)spotList;
-(void) storeStreetData:(NSArray*)streetList;
-(void) storeGridData:(NSArray*)gridList;

-(void) fetchGridsForIDs:(NSArray*) microBlockIDs;
-(void) fetchStreetsForIDs:(NSArray*) microBlockIDs;
-(void) fetchSpotsForIDs:(NSArray*) microBlockIDs;

-(void) testFetch:(EntityType)entityType Microblocks:(NSArray*) mbids;

@end
