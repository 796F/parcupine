//
//  Server.h
//  Parq
//
//  Created by Michael Xia on 6/1/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <MapKit/MapKit.h>
#import <Foundation/Foundation.h>

@interface NetworkLayer : NSObject

+(NSArray*) callGridWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight;
+(NSArray*) callSpotWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight;
+(NSArray*) callStreetWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight;

@end
