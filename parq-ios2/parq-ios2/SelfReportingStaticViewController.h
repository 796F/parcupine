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
@interface SelfReportingStaticViewController : UIViewController <UIGestureRecognizerDelegate, UIAlertViewDelegate>{
    
}

@property (weak, nonatomic) IBOutlet UILabel* userLabel;

@property (weak, nonatomic) IBOutlet UISegmentedControl* spot0;
@property (weak, nonatomic) IBOutlet UISegmentedControl* spot1;
@property (weak, nonatomic) IBOutlet UISegmentedControl* spot2;
@property (weak, nonatomic) IBOutlet UISegmentedControl* spot3;
@property (weak, nonatomic) IBOutlet UISegmentedControl* spot4;
@property (weak, nonatomic) IBOutlet UISegmentedControl* spot5;

@property (nonatomic, assign) NSInteger spotNumber;

@property (weak, nonatomic) IBOutlet UIImageView* bottomImage;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* leftButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* rightButton;
@property (weak, nonatomic) NetworkLayer* networkLayer;
@property BOOL showTapMe;
@property int UIType;
@property BOOL isNotParking;
@property (weak, nonatomic) UIViewController* parent;

@end
