//
//  EditSettingsViewController.h
//  Parq
//
//  Created by Michael Xia on 7/25/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UserInfo.h"

@class PQSettingsViewController;
@interface EditSettingsViewController : UIViewController <UITextFieldDelegate>{
    
}

@property (weak, nonatomic) IBOutlet UILabel* currentValue;
@property (weak, nonatomic) IBOutlet UITextField* editNewValue;
@property (weak, nonatomic) IBOutlet UITextField* confirmNewValue;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* saveButton;
@property UserInfo* vehicle;
@property PQSettingsViewController* parent;

@end
