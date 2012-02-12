//
//  ParqLoginViewController.h
//  Parq
//
//  Created by Mark Yen on 1/6/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ParqSpotViewController.h"

@interface ParqLoginViewController : UITableViewController

@property (weak, nonatomic) IBOutlet UITextField *emailControl;
@property (weak, nonatomic) IBOutlet UITextField *passwordControl;
@property (weak, nonatomic) UIViewController *parent;
- (void)logInAfterSigningUp:(NSString*)email password:(NSString*)password;
-(void)logUserIn;
@end
