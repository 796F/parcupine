//
//  EditEmailViewController.h
//  Parq
//
//  Created by Michael Xia on 2/4/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TDSemiModal.h"

@interface EditEmailViewController : TDSemiModalViewController {
    id delegate;
    IBOutlet UITextField* editEmail;
    IBOutlet UITextField* oldEmail;
    IBOutlet UITextField* confirmNewEmail;
}


@property (nonatomic, retain) IBOutlet id delegate;
@property (nonatomic, retain) UITextField * editEmail;
@property (nonatomic, retain) UITextField * oldEmail;
@property (nonatomic, retain) UITextField * confirmNewEmail;

@property (nonatomic,retain) UILabel* displayEmail;
@property(nonatomic,retain) UILabel* displayCC;

-(IBAction)saveEmailEdit:(id)sender;
-(IBAction)clearEmailEdit:(id)sender;
-(IBAction)cancelEmailEdit:(id)sender;

@end


@interface NSObject (EditEmailViewControllerDelegate)
-(void)editEmailSetEmail:(EditEmailViewController*)viewController;
-(void)editEmailClearEmail:(EditEmailViewController*)viewController;
-(void)editEmailCancel:(EditEmailViewController*)viewController;
@end

