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
+ (UIColor *)veryHighPriceColor {
    return [UIColor colorWithRed:0.925 green:0.004 blue:0.91 alpha:0.75]; /*#ec01e8*/
}
+ (UIColor *)highPriceColor {
    return [UIColor colorWithRed:0.753 green:0.161 blue:0.918 alpha:0.75]; /*#c029ea*/
}
+ (UIColor *)mediumPriceColor {
    return [UIColor colorWithRed:0.494 green:0.416 blue:0.953 alpha:0.75]; /*#7e6af3*/
}
+ (UIColor *)lowPriceColor {
    return [UIColor colorWithRed:0.188 green:0.675 blue:0.937 alpha:0.75]; /*#30acef*/
}
+ (UIColor *)veryLowPriceColor {
    return [UIColor colorWithRed:0.004 green:0.89 blue:1 alpha:0.75]; /*#01e3ff*/
}
+ (UIColor *)disabledTextColor {
    return [UIColor colorWithRed:0.761 green:0.753 blue:0.753 alpha:1]; /*#c2c0c0*/
}
+ (UIColor *)activeTextColor {
    return [UIColor colorWithRed:0.22 green:0.329 blue:0.529 alpha:1]; /*#385487*/
}
@end
