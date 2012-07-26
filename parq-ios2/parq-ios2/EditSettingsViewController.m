//
//  EditSettingsViewController.m
//  Parq
//
//  Created by Michael Xia on 7/25/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "EditSettingsViewController.h"
#import "PQSettingsViewController.h"

@interface EditSettingsViewController ()

@end

@implementation EditSettingsViewController
@synthesize saveButton;
@synthesize confirmNewValue;
@synthesize editNewValue;
@synthesize currentValue;
@synthesize vehicle;
@synthesize parent;

-(IBAction)saveButtonPressed:(id)sender{
    if([confirmNewValue.text isEqualToString:editNewValue.text]){
        //matching
        [vehicle setConfirmNewValue:confirmNewValue.text];
        [parent updateField:vehicle];
        [self.navigationController popToRootViewControllerAnimated:YES];
    }else{
        //show alert.  
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"Check your fields!" message:nil delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [alert show];
    }
    
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField{

    if(textField.tag==0){
        [confirmNewValue becomeFirstResponder];
    }else{
        //submit changes! SAME AS SAVE BUTTON.
        [self saveButtonPressed:self];
    }
    return YES;
}

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
    NSString* current = [NSString stringWithFormat:@"%s %s", vehicle.fieldName.UTF8String, vehicle.oldValue.UTF8String];
    currentValue.text = current;
    [editNewValue becomeFirstResponder];
    if([vehicle.fieldName isEqualToString:@"Email:"]){
        editNewValue.keyboardType = UIKeyboardTypeEmailAddress;
        confirmNewValue.keyboardType = UIKeyboardTypeEmailAddress;
    }else if([vehicle.fieldName isEqualToString:@"Address:"]){
//        editNewValue.keyboardType = UIKeyboardTypeNamePhonePad;
//        confirmNewValue.keyboardType = UIKeyboardTypeEmailAddress;
    }
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
