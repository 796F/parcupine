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

@property (weak, nonatomic) IBOutlet UISwitch* spot101;
@property (weak, nonatomic) IBOutlet UISwitch* spot102;
@property (weak, nonatomic) IBOutlet UISwitch* spot103;
@property (weak, nonatomic) IBOutlet UISwitch* spot104;
@property (weak, nonatomic) IBOutlet UISwitch* spot105;
@property (weak, nonatomic) IBOutlet UISwitch* spot106;

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
