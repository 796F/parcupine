//
//  PQSpotAnnotation.h
//  Parq
//
//  Created by Mark Yen on 6/8/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface PQSpotAnnotation : NSObject <MKAnnotation>
@property (nonatomic) BOOL available;
@property (nonatomic) int name;

- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate
               available:(BOOL)available name:(int)name;
@end
