//
//  ParqTimeBox.m
//  Parq
//
//  Created by Mark Yen on 1/17/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqTimeBox.h"

@interface ParqTimeBox()
@property (strong, nonatomic) CAGradientLayer *gradientLayer;
@end

@implementation ParqTimeBox
@synthesize gradientLayer = _gradientLayer;

- (void)awakeFromNib
{
  // Initialize the gradient layer
  self.gradientLayer = [[CAGradientLayer alloc] init];
  // Set its bounds to be the same of its parent
  [self.gradientLayer setBounds:[self bounds]];
  // Center the layer inside the parent layer
  [self.gradientLayer setPosition:
   CGPointMake([self bounds].size.width/2,
               [self bounds].size.height/2)];
  
  // Insert the layer at position zero to make sure the 
  // text of the button is not obscured
  [[self layer] insertSublayer:self.gradientLayer atIndex:0];
  
  // Set the layer's corner radius
  [[self layer] setCornerRadius:8.0f];
  // Turn on masking
  [[self layer] setMasksToBounds:YES];
  // Display a border around the button 
  // with a 1.0 pixel width
  [[self layer] setBorderWidth:1.0f];

  [[self layer] setBorderColor:[[UIColor darkGrayColor] CGColor]];
}

- (void)drawRect:(CGRect)rect
{
  // Set the colors for the gradient to the 
  // two colors specified for high and low
  [self.gradientLayer setColors:
   [NSArray arrayWithObjects:
    (id)[[UIColor darkGrayColor] CGColor], 
    (id)[[UIColor blackColor] CGColor], nil]];
  
  [super drawRect:rect];
}
@end
