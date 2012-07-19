//
//  PQSpotAnnotation.m
//  Parq
//
//  Created by Mark Yen on 6/8/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQSpotAnnotation.h"

@implementation PQSpotAnnotation
@synthesize coordinate = _coordinate;
@synthesize available = _available;
@synthesize name = _name;
@synthesize objId = _objId;

- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate
               available:(BOOL)available name:(int)name objId:(long)objId{
	if (self = [super init]) {
		_coordinate = coordinate;
        _available = available;
        _name = name;
        _objId = objId;
	}
    return self;
}
@end
