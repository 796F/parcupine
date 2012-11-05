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
@interface SelfReportingStaticViewController : UIViewController <UIGestureRecognizerDelegate, UIAlertViewDelegate, PQNetworkLayerDelegate>{
    
}

@property (weak, nonatomic) IBOutlet UILabel* userLabel;

@property (weak, nonatomic) IBOutlet UIButton* spot0;
@property (weak, nonatomic) IBOutlet UIButton* spot1;
@property (weak, nonatomic) IBOutlet UIButton* spot2;
@property (weak, nonatomic) IBOutlet UIButton* spot3;
@property (weak, nonatomic) IBOutlet UIButton* spot4;
@property (weak, nonatomic) IBOutlet UIButton* spot5;
@property (nonatomic, assign) NSInteger spotNumber;

@property (weak, nonatomic) IBOutlet UIBarButtonItem* leftButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* rightButton;
@property (weak, nonatomic) NetworkLayer* networkLayer;
@property (weak, nonatomic) UIViewController* parent;

@end
