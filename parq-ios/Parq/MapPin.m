//
//  MapPin.m
//  Parq
//
//  Created by Michael Xia on 1/21/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "MapPin.h"

@implementation MapPin
@synthesize coordinate;

-(id) initWithCoordinate:(CLLocationCoordinate2D)c Title:(NSString*)t subTitle:(NSString*)s{
    coordinate = c;
    mTitle = t;
    mSubTitle = s;
    return self;
}
-(NSString*) subtitle{
    return mSubTitle;
}
-(NSString*) title{
    return mTitle;
}

@end
