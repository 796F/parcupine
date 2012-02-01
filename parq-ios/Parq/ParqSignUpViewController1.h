//
//  ParqSignUpViewController1.h
//  Parq
//
//  Created by Mark Yen on 1/30/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ParqLoginViewController.h"

@interface ParqSignUpViewController1 : UITableViewController
@property (weak, nonatomic) IBOutlet UITextField *nameField;
@property (weak, nonatomic) IBOutlet UITextField *emailField;
@property (weak, nonatomic) IBOutlet UITextField *passwordField;
@property (weak, nonatomic) IBOutlet UITextField *verifyField;
@property (weak, nonatomic) ParqLoginViewController *delegate;

@end
