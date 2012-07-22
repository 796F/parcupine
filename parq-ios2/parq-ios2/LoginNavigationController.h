//
//  LoginViewController.h
//  Parq
//
//  Created by Michael Xia on 7/22/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NetworkLayer.h"
#import "DataLayer.h"
#import "PQMapViewController.h"

@interface LoginNavigationController : UINavigationController <UITextFieldDelegate, UIGestureRecognizerDelegate,UIAlertViewDelegate>{

}
@property (weak, nonatomic) UIViewController* parent;


@end
