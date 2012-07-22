//
//  PQLoginViewController.h
//  Parq
//
//  Created by Michael Xia on 7/22/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NetworkLayer.h"
#import "DataLayer.h"
#import "PQMapViewController.h"

@interface PQLoginViewController : UITableViewController
<UITextFieldDelegate,
UIGestureRecognizerDelegate,
UIAlertViewDelegate>{
    NetworkLayer* networkLayer;
    DataLayer* dataLayer;

}
@property (weak, nonatomic) IBOutlet UITextField* emailField;
@property (weak, nonatomic) IBOutlet UITextField* passwordField;
@property (weak, nonatomic) IBOutlet UITextField* confirmPasswordField;
@property (weak, nonatomic) IBOutlet UITextField* licensePlateField;
@property (weak, nonatomic) IBOutlet UIView* entireScreen;
@property (weak, nonatomic) IBOutlet UIButton* whiteButton;
@property (weak, nonatomic) IBOutlet UIButton* registerButton;
@property (weak, nonatomic) IBOutlet UIButton* submitButton;
@property UIViewController* parent;
@property (weak, nonatomic) IBOutlet UINavigationBar* navBar;


@end
