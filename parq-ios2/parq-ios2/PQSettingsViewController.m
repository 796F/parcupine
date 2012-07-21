//
//  PQSettingsViewController.m
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQSettingsViewController.h"
#import "PQAppDelegate.h"
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
-(IBAction)signOutButtonPressed:(id)sender{
    [dataLayer setLoggedIn:NO];
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    UIViewController* vc = [storyboard instantiateViewControllerWithIdentifier:@"LoginController"];
    [vc setModalPresentationStyle:UIModalPresentationFullScreen];
    [self presentModalViewController:vc animated:YES];    
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
    if(!networkLayer){
        networkLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).networkLayer;
    }
    if(!dataLayer){
        dataLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).dataLayer;
    }
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
    int section = indexPath.section;
    int row = indexPath.row;
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if(section == 0){
        //selected one of the account settings.  
        switch (row) {
            case 0:
                //name
                break;
            case 1:
                //addr
                break;
            case 2:
                //license
                break;
            case 3:
                //ssn
                break;
            default:
                //error
                break;
        }
    }else if (section==1){
        if(row==2){
            //change city
        }
    }else{
        if(row==0){
            //payment options
            [self performSegueWithIdentifier:@"showPaymentOptions" sender:self];
        }else{
            //account balance
        }
    }

    //if section isn't 0, dont' do anything.  
}
@end
