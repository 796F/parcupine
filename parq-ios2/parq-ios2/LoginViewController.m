//
//  LoginViewController.m
//  Parq
//
//  Created by Michael Xia on 7/21/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "LoginViewController.h"
#import "PQAppDelegate.h"
@interface LoginViewController ()

@end

@implementation LoginViewController
@synthesize parent;
@synthesize passwordField;
@synthesize emailField;
@synthesize entireScreen;


-(BOOL) tryLoggingIn{
    //call network layer to login.  
    return YES;
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    if (textField.tag==0) {
        //this is email field.  simply go next
        [passwordField becomeFirstResponder];
    }else{
        //hide the email/password box. 
        passwordField.hidden = YES;
        emailField.hidden = YES;
        //display a LOADING CIRCLE thing.  
        
        
        BOOL loginResp = [self tryLoggingIn];
        if(loginResp){
            self.parent.view.hidden = NO;
            NSLog(@"reveal map via login\n");
            [self dismissModalViewControllerAnimated:YES];
            [dataLayer setLoggedIn:YES];
        }else{
            //unhide email/password box.  
            passwordField.hidden = NO;
            emailField.hidden = NO;
            [dataLayer setLoggedIn:NO];
        }
    }
    return YES;
}

-(void) textFieldDidBeginEditing:(UITextField *)textField{
    //started editing, shift the view upwards
    [UIView animateWithDuration:.25 animations:^{
        //x y width height
        self.entireScreen.frame = CGRectMake(0, -95, 320, 460);
        
    }];
}

- (void)removeKeyboard:(UIGestureRecognizer *)sender {
    [self.passwordField resignFirstResponder];
    [self.emailField resignFirstResponder];
    [UIView animateWithDuration:.25 animations:^{
        //x y width height
        self.entireScreen.frame = CGRectMake(0, 20, 320, 460);
        
    }];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void) viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    if(!networkLayer){
        networkLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).networkLayer;
    }
    if(!dataLayer){
        dataLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).dataLayer;
    }
    UITapGestureRecognizer* singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(removeKeyboard:)];
    [self.entireScreen addGestureRecognizer:singleTap];

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
