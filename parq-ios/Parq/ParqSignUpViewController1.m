//
//  ParqSignUpViewController1.m
//  Parq
//
//  Created by Mark Yen on 1/30/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqSignUpViewController1.h"
#import "ParqSignUpViewController2.h"


@implementation ParqSignUpViewController1
@synthesize nameField;
@synthesize emailField;
@synthesize passwordField;
@synthesize verifyField;
@synthesize parent;

-(BOOL)textFieldShouldReturn:(UITextField*)textField{
    NSInteger tagNumber = textField.tag +1;
    UIResponder* nextResponder = [[textField.superview.superview viewWithTag:tagNumber] viewWithTag:tagNumber];
    if(nextResponder){
        [nextResponder becomeFirstResponder];
    }else{
        [textField resignFirstResponder];
    }
    return NO;
    
}

- (IBAction)nextButton:(id)sender {
    [self performSegueWithIdentifier:@"showPaymentMethod" sender:sender];
}

- (IBAction)cancelButton:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}

/**
 * Returns the error string, or nil if there is no error
 */
- (NSString*)validate {
    return nil;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    NSString *errorString = [self validate];
    if (errorString.length == 0) {
        ParqSignUpViewController2 *vc = [segue destinationViewController];
        vc.name = nameField.text;
        vc.email = emailField.text;
        vc.password = passwordField.text;
        vc.parent = parent;
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Check your input"
                                                        message:errorString
                                                       delegate:nil 
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
    }
}

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)viewDidUnload
{
    [self setNameField:nil];
    [self setEmailField:nil];
    [self setPasswordField:nil];
    [self setVerifyField:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
}

@end
