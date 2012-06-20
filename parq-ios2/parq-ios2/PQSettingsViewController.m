//
//  PQSettingsViewController.m
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQSettingsViewController.h"

@interface PQSettingsViewController ()

@end

@implementation PQSettingsViewController

@synthesize addrLabel;
@synthesize addrCellView;
@synthesize nameLabel;
@synthesize nameCellView;
@synthesize ssnLabel;
@synthesize ssnCellView;
@synthesize soundCellView;
@synthesize vibrateCellView;
@synthesize plateLabel;
@synthesize plateCellView;
@synthesize balanceLabel;
@synthesize balanceCellView;
@synthesize user;

#pragma mark - BUTTONS

-(IBAction)doneButtonPressed:(id)sender{
    //save information, then dismiss.  
    [self dismissModalViewControllerAnimated:YES];
}

#pragma mark - VIEW LIFECYCLE

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}



-(void) viewDidLoad{

    [super viewDidLoad];

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)viewDidUnload{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source


//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    static NSString *CellIdentifier = @"Cell";
//    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
//    if (cell == nil) {
//        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
//    }
//    return cell;
//}
#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(indexPath.section==0){
        //selected one of the account settings.  
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
    }
    //if section isn't 0, dont' do anything.  
}

@end
