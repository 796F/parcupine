//
//  SelfReportingViewController.h
//  Parq
//
//  Created by Michael Xia on 7/23/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "NetworkLayer.h"
@interface SelfReportingViewController : UIViewController <MKMapViewDelegate, UIGestureRecognizerDelegate>{
    
}

@property (weak, nonatomic) IBOutlet MKMapView* mapView;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* leftButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* rightButton;
@property (weak, nonatomic) NetworkLayer* networkLayer;
@property BOOL showTapMe;
@property int UIType;
@end
