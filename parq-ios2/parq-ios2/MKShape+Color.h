//
//  MKShape+Color.h
//  Parq
//
//  Created by Mark Yen on 4/6/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import <objc/runtime.h>

@interface MKShape (Color)

@property (nonatomic) int color;
@property (nonatomic) int name;
@property (nonatomic) long objId;

@end
