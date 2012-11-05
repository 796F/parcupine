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

#define STATUSCODE_REPORTING_SUCCESS 0
#define STATUSCODE_REPORTING_TWICE -5
#define STATUSCODE_REPORTING_BAD_TIME -10

//using @class to avoid cycle, which breaks the compiler.  sigh
@class PQMapViewController;

@protocol PQNetworkLayerDelegate <NSObject>

@optional
- (void)afterFetchingBalanceOnBackend:(NSInteger)balance;
- (void)afterParkingOnBackend:(BOOL)success endTime:(NSDate *)endTime parkingReference:(NSString *)parkingRef;
- (void)afterUnparkingOnBackend:(BOOL)success;
- (void)afterReportingOnBackend:(NSInteger)statusCode;
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

-(BOOL)submitAvailablilityInformation:(NSArray*)value;

-(User*) loginEmail:(NSString*) email AndPassword:(NSString*) pass;
-(User*) registerEmail:(NSString*) email AndPassword:(NSString*) pass AndPlate:(NSString*) plate;

+ (void)fetchUserPointsBalanceWithUid:(unsigned long long)uid andDelegate:(id<PQNetworkLayerDelegate>)delegate;
+ (void)parkPaygWithSpotId:(unsigned long long)spotId delegate:(id<PQNetworkLayerDelegate>)delegate;
+ (void)parkPrepaidWithDuration:(NSTimeInterval) durationSeconds spotId:(unsigned long long)spotId delegate:(id<PQNetworkLayerDelegate>)delegate;
+ (void)unparkWithDelegate:(id<PQNetworkLayerDelegate>)delegate;
+ (void)extendWithDuration:(NSTimeInterval)durationMinutes andDelegate:(id<PQNetworkLayerDelegate>)delegate;
+ (void)reportAvailability:(NSDictionary *)availability delegate:(id<PQNetworkLayerDelegate>)delegate;

//network status
-(BOOL) isRecheableViaWifi;
-(BOOL) isRecheableVia3G;

//DEBUG
-(void) testAsync;
-(void) loadSpotData;
-(void) sendLogs;
@end
