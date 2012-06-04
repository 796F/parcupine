//
//  Server.m
//  Parq
//
//  Created by Michael Xia on 6/1/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

/* 
 The network layer handles all interactions with the server.  these methods can be accessed by
 retrieving the data layer object from the app delegate, which can be reached by all classes.
 All pushes to, and fetches from, will go thorugh this layer.  There is a single instance.  
 */

#import "NetworkLayer.h"
#import "Grid.h"

@implementation NetworkLayer

#define gridLength 0.01

+(NSArray*) callGridWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight{    
    NSArray* grids = [NSArray arrayWithObjects:
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"1", @"gridId", @"42.350393", @"lat", @"-71.104159", @"lon", @"0", @"fillRate" , nil],
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"2", @"gridId", @"42.360393", @"lat", @"-71.104159", @"lon", @"4", @"fillRate" , nil],
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"3", @"gridId", @"42.370393", @"lat", @"-71.104159", @"lon", @"2", @"fillRate" , nil],
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"4", @"gridId", @"42.350393", @"lat", @"-71.114159", @"lon", @"3", @"fillRate" , nil],
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"5", @"gridId", @"42.360393", @"lat", @"-71.114159", @"lon", @"3", @"fillRate" , nil],
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"6", @"gridId", @"42.370393", @"lat", @"-71.114159", @"lon", @"0", @"fillRate" , nil],
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"7", @"gridId", @"42.350393", @"lat", @"-71.124159", @"lon", @"1", @"fillRate" , nil],
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"8", @"gridId", @"42.360393", @"lat", @"-71.124159", @"lon", @"2", @"fillRate" , nil],
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"9", @"gridId", @"42.370393", @"lat", @"-71.124159", @"lon", @"3", @"fillRate" , nil],
                      nil];
    NSMutableArray* gridArr = [[NSMutableArray alloc] initWithCapacity:grids.count];
    for(id dic in grids){
//        Grid* t = [[Grid alloc] initWithGridId:[[dic objectForKey:@"gridId"] longLongValue]
//                                      Latitude:[[dic objectForKey:@"lat"] doubleValue] 
//                                     Longitude:[[dic objectForKey:@"lon"] doubleValue]];
//        [gridArr addObject:t];
    }
    return gridArr; 
}

+(NSArray*) callSpotWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight{
    return nil;
}

+(NSArray*) callStreetWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight{
    return nil;
}


@end
