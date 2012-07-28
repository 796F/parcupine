//
//  PQParkedCarAnnotation.h
//  Parq
//
//  Created by Mark Yen on 5/31/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>

@interface PQParkedCarAnnotation : MKPlacemark
@property (retain, nonatomic) NSString *title;
@end
