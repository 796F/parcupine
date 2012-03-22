//
//  MKPolyline+Color.m
//  Parq
//
//  Created by Mark Yen on 3/22/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "MKPolyline+Color.h"

@implementation MKPolyline (Color)

static char colorKey;

- (void) setColor:(int)color {
    objc_setAssociatedObject(self, &colorKey, [NSNumber numberWithInt:color], OBJC_ASSOCIATION_RETAIN);
}

- (int) color {
    return [objc_getAssociatedObject( self, &colorKey ) intValue];
}

@end
