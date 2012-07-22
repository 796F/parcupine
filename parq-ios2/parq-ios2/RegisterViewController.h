//
//  RegisterViewController.h
//  Parq
//
//  Created by Michael Xia on 7/22/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NetworkLayer.h"
#import "DataLayer.h"

@interface RegisterViewController : UIViewController<UITextFieldDelegate,
UIGestureRecognizerDelegate,
UIAlertViewDelegate>{
    NetworkLayer* networkLayer;
    DataLayer* dataLayer;
    
}
@property (weak, nonatomic) IBOutlet UITextField* emailField;
@property (weak, nonatomic) IBOutlet UITextField* passwordField;
@property (weak, nonatomic) IBOutlet UITextField* license;
@property (weak, nonatomic) IBOutlet UIView* entireScreen;

@property UIViewController* parent;

@end
