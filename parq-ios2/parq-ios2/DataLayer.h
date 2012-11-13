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
#import "SpotInfo.h"
#define GRID_LENGTH 0.005
@class PQMapViewController;
@interface DataLayer : NSObject

typedef enum {
    kSpotEntity,
    kGridEntity,
    kStreetEntity
} EntityType;

typedef enum {
    kUnparkedParkMode,
    kPrepaidParkMode,
    kPaygParkMode
} ParkMode;

@property (nonatomic,retain) PQMapViewController* mapController;

- (NSSet *)fetchObjectsForEntityName:(NSString *)newEntityName
                       withPredicate:(id)stringOrPredicate;

-(User*) saveUserWithEmail:(NSString*)email Pass:(NSString*)pass License:(NSString*)license UID:(NSNumber*) uid Balance:(NSNumber*) balance;
-(BOOL) userAddPoints:(NSNumber*) earnedPoints;
-(BOOL) userDecPoints:(NSNumber*) decreasePoints;

//checks if an object, or if an mbid, is already stored in core data.  
-(BOOL) objExistsInCoreData:(NSObject*)object EntityType:(EntityType) entityType;
-(BOOL) mbIdExistsInCoreData:(NSNumber*)object EntityType:(EntityType) entityType;

//call after server responds.  
-(void) store:(EntityType)entityType WithData:(NSArray*)overlayList;
-(void) fetch:(EntityType) entityType ForIDs:(NSArray*) microBlockIDs;

//settings stuff using plist
-(BOOL) isFirstLaunch;


-(BOOL) isLoggedIn;
-(void) setLoggedIn:(BOOL) yesORno;
-(void) setLastReportTime:(NSDate*) lastReportTime;
-(NSDate*) getLastReportTime;
+ (void)setEndTime:(NSDate*)endTime;
+ (NSDate *)endTime;
+ (void)setStartTime:(NSDate*)startTime;
+ (NSDate *)startTime;
+ (void)setParkingMode:(ParkMode)parkingMode;
+ (ParkMode)parkingMode;
+ (void)setSpotInfo:(SpotInfo*) spotInfo;
+ (SpotInfo *)spotInfo;
+ (void)setParkingReference:(NSString*) ref;
+ (NSString*)parkingReference;
+ (void)clearSavedParkingSession;
-(NSNumber*) getSpotId;
-(void) setSpotId:(NSNumber*) spotId;
-(NSNumber*) getAppVersion;
-(void) setAppVersion:(NSNumber*)appVersion;

-(void) logString:(NSString*) string;
//debug
-(void) testFetch:(EntityType)entityType Microblocks:(NSArray*) microBlockIDs;
-(void) loadMockData;
-(BOOL) hasMockData;

+ (User *)fetchUser;
+ (NSManagedObjectContext *)managedObjectContext;
@end
