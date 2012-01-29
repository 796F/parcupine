//
//  ParqTimeRemainingViewController.h
//  Parq
//
//  Created by Mark Yen on 1/27/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ParqTimeBox.h"
#import "RateObject.h"

@interface ParqTimeRemainingViewController : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *lotNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *spotNumLabel;
@property (weak, nonatomic) IBOutlet ParqTimeBox *hours;
@property (weak, nonatomic) IBOutlet ParqTimeBox *minutes;
@end
