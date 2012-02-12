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
@property (weak, nonatomic) IBOutlet UITextField *addressField;
@property (weak, nonatomic) IBOutlet UITextField *zipField;
@property (weak, nonatomic) IBOutlet UITextField *expMonthField;
@property (weak, nonatomic) IBOutlet UITextField *expYearField;
@property (strong, nonatomic) NSString *name;
@property (strong, nonatomic) NSString *email;
@property (strong, nonatomic) NSString *password;
@property (nonatomic) int expMonth;
@property (nonatomic) int expYear;
@property (weak, nonatomic) ParqLoginViewController *parent;

@end
