//
//  Grid.m
//  Parq
//
//  Created by Michael Xia on 6/28/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "Grid.h"
#import "DataLayer.h"
#import "MKShape+Color.h"

@implementation Grid

@dynamic gridId;
@dynamic lat;
@dynamic lon;
@dynamic status;
@dynamic microblock;

-(MKPolygon*) generateOverlay{
    int color = self.status.intValue;
    CLLocationCoordinate2D nw_point = CLLocationCoordinate2DMake(self.lat.doubleValue, self.lon.doubleValue);
    CLLocationCoordinate2D se_point = CLLocationCoordinate2DMake(nw_point.latitude - GRID_LENGTH, nw_point.longitude + GRID_LENGTH);
    CLLocationCoordinate2D ne_point = CLLocationCoordinate2DMake(nw_point.latitude ,se_point.longitude);
    CLLocationCoordinate2D sw_point = CLLocationCoordinate2DMake(se_point.latitude ,nw_point.longitude);
    CLLocationCoordinate2D testLotCoords[5]={nw_point, ne_point, se_point, sw_point, nw_point};        
    MKPolygon *gridPoly = [MKPolygon polygonWithCoordinates:testLotCoords count:5];
    [gridPoly setColor:color];
    [gridPoly setObjId:self.gridId.longValue];
    return gridPoly;
}

@end
