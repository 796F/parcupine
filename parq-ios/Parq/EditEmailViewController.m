//
//  EditEmailViewController.m
//  Parq
//
//  Created by Michael Xia on 2/4/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "EditEmailViewController.h"

@implementation EditEmailViewController
@synthesize editEmail;
@synthesize delegate;
@synthesize confirmNewEmail;
@synthesize oldEmail;
@synthesize displayCC;
@synthesize displayEmail;

-(IBAction)cancelEmailEdit:(id)sender{
    if([self.delegate respondsToSelector:@selector(editEmailCancel:)]) {
		[self.delegate editEmailCancel:self];
	} else {
		// just dismiss the view automatically?
	}
}

-(IBAction)saveEmailEdit:(id)sender {
	if([self.delegate respondsToSelector:@selector(editEmailSetEmail:)]) {
		[self.delegate editEmailSetEmail:self];
	}
}

-(IBAction)clearEmailEdit:(id)sender {
	if([self.delegate respondsToSelector:@selector(editEmailClearEmail:)]) {
		[self.delegate editEmailClearEmail:self];
	}
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

-(BOOL)textFieldShouldReturn:(UITextField*)textField{
    NSInteger nextTagNumber = textField.tag+1;
    UIResponder* nextResponder = [textField.superview viewWithTag:nextTagNumber];
    if(nextResponder){
        [nextResponder becomeFirstResponder];

    }else{
        [textField resignFirstResponder];

    }
    return NO;
}

-(IBAction)submitEmailEdit :(id)sender{
    if([self.delegate respondsToSelector:@selector(editEmailSetEmail:)]) {
		[self.delegate editEmailSetEmail:self];
	}
    [self dismissSemiModalViewController:self];
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    [confirmNewEmail addTarget:self action:@selector(submitEmailEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}



@end
