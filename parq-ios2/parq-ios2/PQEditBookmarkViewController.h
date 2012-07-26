//
//  PQEditBookmarkViewController.h
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Mapkit/Mapkit.h>

@interface PQEditBookmarkViewController : UIViewController <MKMapViewDelegate>{
    
}

@property (weak, nonatomic) IBOutlet MKMapView *map;
@property (nonatomic) CLLocationCoordinate2D bookmarkCoordinate;
@property (weak, nonatomic) UIViewController* parent;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* saveButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* cancelButton;


@end
