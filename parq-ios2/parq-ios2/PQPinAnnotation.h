//
//  PQPinAnnotation.h
//  Parq
//
//  Created by Michael Xia on 7/18/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface PQPinAnnotation : NSObject <MKAnnotation>

@property (nonatomic, ) CLLocationCoordinate2D coordinate;
@property (nonatomic, copy) NSString *subtitle;
@property (nonatomic, copy) NSString *title;

-(id) initWithCoordinate:(CLLocationCoordinate2D)coordinate
                   title:(NSString*) titleIn
                subTitle:(NSString*) subIn;

@end
