//
//  ParqParkViewController.h
//  Parq
//
//  Created by Mark Yen on 1/25/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RateObject.h"

@interface ParqParkViewController : UIViewController <UIAlertViewDelegate>
@property (weak, nonatomic) IBOutlet UIDatePicker *timePicker;
@property (weak, nonatomic) IBOutlet UILabel *rate;
@property (weak, nonatomic) IBOutlet UILabel *total;
@property (weak, nonatomic) IBOutlet UILabel *lotNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *spotNumLabel;
@property (nonatomic) int spotNumber;
@property (strong, nonatomic) RateObject *rateObj;
- (void)timeUp;
- (void)unpark;
@end
