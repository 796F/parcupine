//
//  BoundingBox.h
//  Parq
//
//  Created by Michael Xia on 6/25/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Mapkit/Mapkit.h>
@interface BoundingBox : NSObject

@property (nonatomic) CLLocationCoordinate2D topRight;
@property (nonatomic) CLLocationCoordinate2D botLeft;

@end
