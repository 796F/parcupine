//
//  MapPin.h
//  Parq
//
//  Created by Michael Xia on 1/21/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface MapPin : NSObject <MKAnnotation> {
    CLLocationCoordinate2D coordinate;
    NSString* mTitle;
    NSString * mSubTitle;
}

-(id) initWithCoordinate:(CLLocationCoordinate2D)c  Title:(NSString*)t subTitle:(NSString*)s;

@end
