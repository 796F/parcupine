//
//  LoginViewController.h
//  Parq
//
//  Created by Michael Xia on 7/21/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NetworkLayer.h"
#import "DataLayer.h"

@interface LoginViewController : UIViewController 
<UITextFieldDelegate,
UIGestureRecognizerDelegate>{
    NetworkLayer* networkLayer;
    DataLayer* dataLayer;
}
@property (weak, nonatomic) IBOutlet UITextField* emailField;
@property (weak, nonatomic) IBOutlet UITextField* passwordField;
@property (weak, nonatomic) IBOutlet UIView* entireScreen;
@property (weak, nonatomic) UIViewController* parent;
//callback from network layer.  
//-(void) loginResponseIs:(BOOL) resp;

@end
