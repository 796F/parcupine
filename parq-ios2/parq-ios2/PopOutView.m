//
//  PopOutView.m
//  Parq
//
//  Created by Michael Xia on 7/24/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "PopOutView.h"

@implementation PopOutView
@synthesize parent;
@synthesize outputText;
@synthesize inputText;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
