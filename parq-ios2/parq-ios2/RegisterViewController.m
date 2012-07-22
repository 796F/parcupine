//
//  RegisterViewController.m
//  Parq
//
//  Created by Michael Xia on 7/22/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "RegisterViewController.h"

@interface RegisterViewController ()

@end

@implementation RegisterViewController
@synthesize emailField;
@synthesize passwordField;
@synthesize license;
@synthesize parent;

@synthesize entireScreen;
-(void) textFieldDidBeginEditing:(UITextField *)textField{
    //correct the screen placement.  
}
-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    //next should move stuff down.  
    return YES;
}
-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    //are you sure this is correct stuff, then yes = submit.  
}
-(void) removeKeyboard:(UIGestureRecognizer*) sender{
    //called to put screen back to normal.  
}


#pragma mark - VIEW LIFECYCLE
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    //reveal the nav bar
    self.navigationController.navigationBar.hidden = NO;
	// Do any additional setup after loading the view.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
