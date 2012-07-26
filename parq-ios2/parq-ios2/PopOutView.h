//
//  PopOutView.h
//  Parq
//
//  Created by Michael Xia on 7/24/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PopOutView : UIView <UITextFieldDelegate>

@property (weak, nonatomic) UIViewController* parent;
@property (weak, nonatomic) IBOutlet UITextField* inputText;
@property (weak, nonatomic) IBOutlet UILabel* outputText;

@end
