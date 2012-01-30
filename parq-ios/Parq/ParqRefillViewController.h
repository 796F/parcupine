//
//  ParqRefillViewController.h
//  Parq
//
//  Created by Mark Yen on 1/29/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqParkViewController.h"
#import "ParqTimeRemainingViewController.h"

@interface ParqRefillViewController : UIViewController <UIAlertViewDelegate>
@property (weak, nonatomic) IBOutlet UIDatePicker *timePicker;
@property (weak, nonatomic) IBOutlet UILabel *rate;
@property (weak, nonatomic) IBOutlet UILabel *total;
@property (weak, nonatomic) IBOutlet UILabel *lotNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *spotNumLabel;
@property (weak, nonatomic) ParqTimeRemainingViewController *delegate;

@end
