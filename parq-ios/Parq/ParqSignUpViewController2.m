//
//  ParqSignUpViewController2.m
//  Parq
//
//  Created by Mark Yen on 1/30/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqSignUpViewController2.h"
#import "ServerCalls.h"

@interface ParqSignUpViewController2 ()
@property (nonatomic) int expMonth;
@property (nonatomic) int expYear;
@end

@implementation ParqSignUpViewController2
@synthesize ccNumField;
@synthesize cscField;
@synthesize zipField;
@synthesize expMonthLabel;
@synthesize expYearLabel;
@synthesize name;
@synthesize email;
@synthesize password;
@synthesize expMonth;
@synthesize expYear;
@synthesize delegate;

/**
 * Returns the error string, or nil if there is no error
 */
- (NSString*)validate {
    return nil;
}

- (IBAction)doneButton:(id)sender {
    NSString *errorString = [self validate];
    if (errorString.length == 0) {
        const BOOL success = [ServerCalls registerEmail:email Password:password CreditCard:ccNumField.text cscNumber:cscField.text HolderName:name BillingAddress:@"" ExpMonth:[NSString stringWithFormat:@"%d", expMonth] ExpYear:[NSString stringWithFormat:@"%d", expYear] Zipcode:zipField.text];
        if (success) {
            [self dismissModalViewControllerAnimated:YES];
            [delegate logInAfterSigningUp:email password:password];
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Couldn't sign up"
                                                            message:@"Please try again."
                                                           delegate:nil 
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
        }
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
    [self setCcNumField:nil];
    [self setCscField:nil];
    [self setZipField:nil];
    [self setExpMonthLabel:nil];
    [self setExpYearLabel:nil];
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
