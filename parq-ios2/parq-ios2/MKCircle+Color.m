//
//  MKCircle+ColorTag.m
//  Parq2
//
//  Created by Michael Xia on 3/22/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "MKCircle+Color.h"
#import "objc/runtime.h"

@implementation MKCircle (Color)

static char colorKey;

- (void) setColor:(int)color {
    objc_setAssociatedObject(self, &colorKey, [NSNumber numberWithInt:color], OBJC_ASSOCIATION_RETAIN);
}

- (int) color {
    return [objc_getAssociatedObject( self, &colorKey ) intValue];
}
@end
