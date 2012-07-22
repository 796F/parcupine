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

-(User*) loginEmail:(NSString*) email AndPassword:(NSString*) pass;
//network status
-(BOOL) isRecheableViaWifi;
-(BOOL) isRecheableVia3G;

//DEBUG
-(void) testAsync;
-(void) loadSpotData;
@end
