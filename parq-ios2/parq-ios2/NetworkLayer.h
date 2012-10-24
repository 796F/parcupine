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
- (void)afterFetchUserPointsBalance:(NSInteger)balance;
- (void)afterUnpark:(BOOL)success;

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

+ (void)unparkUserWithDelegate:(id<PQNetworkLayerDelegate>)delegate;

//network status
-(BOOL) isRecheableViaWifi;
-(BOOL) isRecheableVia3G;
-(void) decideUIType;

//DEBUG
-(void) testAsync;
-(void) loadSpotData;
-(void) sendLogs;
@end
