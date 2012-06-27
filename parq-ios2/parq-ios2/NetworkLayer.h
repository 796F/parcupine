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
@interface NetworkLayer : NSObject{
    DataLayer* dataLayer;
}

@property (nonatomic,retain) DataLayer* dataLayer;
@property (nonatomic,retain) PQMapViewController* parent;

//returns two dictionaries nested, one for updates the other for adding.  
-(NSDictionary*) addGridsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs;
-(NSDictionary*) addStreetsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs;
-(NSDictionary*) addSpotsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs;

//gets updates for overlays already on map
-(NSDictionary*) updateGridsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;
-(NSDictionary*) updateStreetsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;
-(NSDictionary*) updateSpotsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;

//calculates MID's based on lat/long. 
-(NSMutableArray*) getGridLevelMicroBlockIDListWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;
-(NSMutableArray*) getStreetLevelMicroBlockIDListWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;
-(NSMutableArray*) getSpotLevelMicroBlockIDListWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft;

//DEBUG
-(void) testAsync;
@end
