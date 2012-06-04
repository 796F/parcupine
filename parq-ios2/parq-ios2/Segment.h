//
//  Segment.h
//  Parq
//
//  Created by Michael Xia on 6/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface Segment : NSObject{

}

@property (nonatomic) CLLocationCoordinate2D A;
@property (nonatomic) CLLocationCoordinate2D B;

-(CLLocationCoordinate2D*) getSegment;
-(Segment*) initWithPointsA:(CLLocationCoordinate2D*) a andB:(CLLocationCoordinate2D*) b;
@end
