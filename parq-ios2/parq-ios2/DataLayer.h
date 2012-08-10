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
#import "User.h"
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

- (NSSet *)fetchObjectsForEntityName:(NSString *)newEntityName
                       withPredicate:(id)stringOrPredicate;

-(User*) getUser;

//checks if an object, or if an mbid, is already stored in core data.  
-(BOOL) objExistsInCoreData:(NSObject*)object EntityType:(EntityType) entityType;
-(BOOL) mbIdExistsInCoreData:(NSObject*)object EntityType:(EntityType) entityType;

//call after server responds.  
-(void) store:(EntityType)entityType WithData:(NSArray*)overlayList;
-(void) fetch:(EntityType) entityType ForIDs:(NSArray*) microBlockIDs;

//settings stuff using plist
-(int) UIType;
-(void) setUIType:(int) type;
-(BOOL) isFirstLaunch;
-(BOOL) isLoggedIn;
-(void) setLoggedIn:(BOOL) yesORno;
-(void) setEndTime:(NSDate*) endTime;
-(NSDate*) getEndTime;
-(NSNumber*) getSpotId;

-(void) logString:(NSString*) string;

//debug
-(void) testFetch:(EntityType)entityType Microblocks:(NSArray*) microBlockIDs;
-(void) loadMockData;
@end
