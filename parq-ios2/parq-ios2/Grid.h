//
//  Grid.h
//  Parq
//
//  Created by Michael Xia on 6/1/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Grid : NSManagedObject{
    NSNumber* gridId;
    NSNumber* lat;
    NSNumber* lon;
}

@property (nonatomic, retain) NSNumber * gridId;
@property (nonatomic, retain) NSNumber * lat;
@property (nonatomic, retain) NSNumber * lon;

-(id) initWithGridId:(long) gid Latitude:(double)lat Longitude:(double)lon;

@end