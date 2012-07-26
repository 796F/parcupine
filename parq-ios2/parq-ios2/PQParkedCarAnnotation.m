//
//  PQParkedCarAnnotation.m
//  Parq
//
//  Created by Mark Yen on 5/31/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQParkedCarAnnotation.h"

@implementation PQParkedCarAnnotation
@synthesize coordinate = _coordinate;
@synthesize title = _title;

- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate addressDictionary:(NSDictionary *)addressDictionary {
	if ((self = [super initWithCoordinate:coordinate addressDictionary:addressDictionary])) {
		_coordinate = coordinate;
	}
	return self;
}


@end
