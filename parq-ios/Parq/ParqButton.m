//
//  ParqButton.m
//  Parq
//
//  Created by Mark Yen on 1/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqButton.h"

@interface ParqButton()
@property (strong, nonatomic) CAGradientLayer *gradientLayer;
@end

@implementation ParqButton
@synthesize highColor = _highColor;
@synthesize lowColor = _lowColor;
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
  
  _highColor = [UIColor whiteColor];
  _lowColor = [UIColor grayColor];
}

- (void)drawRect:(CGRect)rect
{
  // Set the colors for the gradient to the 
  // two colors specified for high and low
  [self.gradientLayer setColors:
   [NSArray arrayWithObjects:
    (id)[self.highColor CGColor], 
    (id)[self.lowColor CGColor], nil]];

  [super drawRect:rect];
}

- (void)setHighColor:(UIColor*)color;
{
  // Set the high color and repaint
  _highColor = color;
  [[self layer] setNeedsDisplay];
}

- (void)setLowColor:(UIColor*)color;
{
  // Set the low color and repaint
  _lowColor = color;
  [[self layer] setNeedsDisplay];
}
@end
