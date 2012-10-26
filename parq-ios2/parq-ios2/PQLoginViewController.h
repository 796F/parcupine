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
@property (weak, nonatomic) IBOutlet UITextField* confirmEmailField;
@property (weak, nonatomic) IBOutlet UITextField* licensePlateField;
@property (weak, nonatomic) IBOutlet UIButton* whiteButton;
@property (weak, nonatomic) IBOutlet UIButton* registerButton;
@property (weak, nonatomic) IBOutlet UIButton *goButton;
@property (weak, nonatomic) IBOutlet UIButton* submitButton;
@property (weak, nonatomic) IBOutlet UILabel *backLabel;
@property UIViewController* parent;


@end
