//
//  Server.h
//  Parq
//
//  Created by Michael Xia on 6/1/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <MapKit/MapKit.h>
#import <Foundation/Foundation.h>
#import "DataLayer.h"
#import <RestKit/RestKit.h>
#import "JSONKit.h"
#import "BoundingBox.h"
#import "SpotInfo.h"
#import "User.h"

//using @class to avoid cycle, which breaks the compiler.  sigh
@class PQMapViewController;

@protocol PQNetworkLayerDelegate <NSObject>

@optional
- (void)afterFetchingBalanceOnBackend:(NSInteger)balance;
- (void)afterParkingOnBackend:(BOOL)success endTime:(NSDate *)endTime parkingReference:(NSString *)parkingRef;
- (void)afterUnparkingOnBackend:(BOOL)success;
- (void)afterExtendingOnBackend:(BOOL)success endTime:(NSDate *)endTime parkingReference:(NSString *)parkingReference;

@end

@interface NetworkLayer : NSObject <RKRequestDelegate>{
    DataLayer* dataLayer;
}

@property (nonatomic,retain) DataLayer* dataLayer;
@property (nonatomic,retain) PQMapViewController* mapController;

//returns two dictionaries nested, one for updates the other for adding.  
-(void) addOverlayOfType:(EntityType) entityType ToMapForIDs:(NSArray*) newIDs AndUpdateForIDs:(NSArray*) updateIDs;

//gets updates for overlays already on map
-(void) updateOverlayOfType:(EntityType) entityType WithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;

//calculates MID's based on lat/long. 
-(NSMutableArray*) getMBIDsWithType:(EntityType) entityType NE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;
-(SpotInfo*) getSpotInfoForId:(NSNumber*)spotId SpotNumber:(NSNumber*)spotNum GPS:(CLLocationCoordinate2D*)coord;
-(BOOL) parkUserWithSpotInfo:(SpotInfo*) spotInfo AndDuration:(int)duration;

-(BOOL)submitAvailablilityInformation:(NSArray*)value;

-(User*) loginEmail:(NSString*) email AndPassword:(NSString*) pass;
-(User*) registerEmail:(NSString*) email AndPassword:(NSString*) pass AndPlate:(NSString*) plate;

//user points
+ (void)fetchUserPointsBalanceWithUid:(unsigned long long)uid andDelegate:(id<PQNetworkLayerDelegate>)delegate;
-(BOOL) userEarnedPoints:(NSNumber*) earnedPoints;
-(BOOL) userLostPoints:(NSNumber*) lostPoints;
+ (void)parkPaygWithSpotId:(unsigned long long)spotId delegate:(id<PQNetworkLayerDelegate>)delegate;
+ (void)parkPrepaidWithDuration:(NSTimeInterval) durationSeconds spotId:(unsigned long long)spotId delegate:(id<PQNetworkLayerDelegate>)delegate;
+ (void)unparkWithDelegate:(id<PQNetworkLayerDelegate>)delegate;
+ (void)extendWithDuration:(NSTimeInterval)durationMinutes andDelegate:(id<PQNetworkLayerDelegate>)delegate;

//network status
-(BOOL) isRecheableViaWifi;
-(BOOL) isRecheableVia3G;

//DEBUG
-(void) testAsync;
-(void) loadSpotData;
-(void) sendLogs;
@end
