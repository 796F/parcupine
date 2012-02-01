//
//  ParqSignUpViewController2.h
//  Parq
//
//  Created by Mark Yen on 1/30/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ParqLoginViewController.h"

@interface ParqSignUpViewController2 : UITableViewController
@property (weak, nonatomic) IBOutlet UITextField *ccNumField;
@property (weak, nonatomic) IBOutlet UITextField *cscField;
@property (weak, nonatomic) IBOutlet UITextField *zipField;
@property (weak, nonatomic) IBOutlet UILabel *expMonthLabel;
@property (weak, nonatomic) IBOutlet UILabel *expYearLabel;
@property (strong, nonatomic) NSString *name;
@property (strong, nonatomic) NSString *email;
@property (strong, nonatomic) NSString *password;
@property (weak, nonatomic) ParqLoginViewController *delegate;

@end
