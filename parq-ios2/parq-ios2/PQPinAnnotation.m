//
//  PQPinAnnotation.m
//  Parq
//
//  Created by Michael Xia on 7/18/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "PQPinAnnotation.h"

@implementation PQPinAnnotation

@synthesize coordinate = _coordinate;
@synthesize title = _title;
@synthesize subtitle = _subtitle;

-(id) initWithCoordinate:(CLLocationCoordinate2D)coordinateIn
                   title:(NSString*) titleIn
                subTitle:(NSString*) subIn{
    if (self = [super init]) {
		_coordinate = coordinateIn;
        _title = titleIn;
        _subtitle = subIn;
	}
    return self;
}


@end


