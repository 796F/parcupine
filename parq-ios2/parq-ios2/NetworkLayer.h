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

//using @class to avoid cycle, which breaks the compiler.  sigh
@class PQMapViewController;
@interface NetworkLayer : NSObject <RKRequestDelegate>{
    DataLayer* dataLayer;
}

@property (nonatomic,retain) DataLayer* dataLayer;
@property (nonatomic,retain) PQMapViewController* mapController;

//returns two dictionaries nested, one for updates the other for adding.  
-(void) addGridsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs;
-(void) addStreetsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs;
-(void) addSpotsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs;

//gets updates for overlays already on map
-(void) updateGridsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;
-(void) updateStreetsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;
-(void) updateSpotsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;

//calculates MID's based on lat/long. 
-(NSMutableArray*) getGridLevelMicroBlockIDListWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;
-(NSMutableArray*) getStreetLevelMicroBlockIDListWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;
-(NSMutableArray*) getSpotLevelMicroBlockIDListWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;

//network status
-(BOOL) isRecheableViaWifi;
-(BOOL) isRecheableVia3G;

//DEBUG
-(void) testAsync;
-(void) insertTestData;
@end
