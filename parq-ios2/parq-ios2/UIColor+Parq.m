//
//  UIColor+Parq.m
//  Parq
//
//  Created by Mark Yen on 4/4/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "UIColor+Parq.h"

// Convert colors easily from here http://www.touch-code-magazine.com/web-color-to-uicolor-convertor/
@implementation UIColor (Parq)
+ (UIColor *)veryLowAvailabilityColor {
    return [UIColor colorWithRed:0.933 green:0.11 blue:0.141 alpha:0.75]; /*#ee1c24*/
}
+ (UIColor *)lowAvailabilityColor {
    return [UIColor colorWithRed:0.949 green:0.396 blue:0.133 alpha:0.75]; /*#f26522*/
}
+ (UIColor *)mediumAvailabilityColor {
    return [UIColor colorWithRed:1 green:0.949 blue:0 alpha:0.75]; /*#fff200*/
}
+ (UIColor *)highAvailabilityColor {
    return [UIColor colorWithRed:0.553 green:0.776 blue:0.247 alpha:0.75]; /*#8dc63f*/
}
+ (UIColor *)veryHighAvailabilityColor {
    return [UIColor colorWithRed:0.224 green:0.71 blue:0.29 alpha:0.75]; /*#39b54a*/
}
+ (UIColor *)disabledTextColor {
    return [UIColor colorWithRed:0.761 green:0.753 blue:0.753 alpha:1]; /*#c2c0c0*/
}
+ (UIColor *)activeTextColor {
    return [UIColor colorWithRed:0.22 green:0.329 blue:0.529 alpha:1]; /*#385487*/
}
@end
