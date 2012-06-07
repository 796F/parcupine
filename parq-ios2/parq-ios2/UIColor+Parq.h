//
//  UIColor+Parq.h
//  Parq
//
//  Created by Mark Yen on 4/4/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIColor (Parq)
+ (UIColor *)veryLowAvailabilityColor;
+ (UIColor *)lowAvailabilityColor;
+ (UIColor *)mediumAvailabilityColor;
+ (UIColor *)highAvailabilityColor;
+ (UIColor *)veryHighAvailabilityColor;
+ (UIColor *)veryHighPriceColor;
+ (UIColor *)highPriceColor;
+ (UIColor *)mediumPriceColor;
+ (UIColor *)lowPriceColor;
+ (UIColor *)veryLowPriceColor;
+ (UIColor *)disabledTextColor;
+ (UIColor *)activeTextColor;
@end
