//
//  PQSettingsViewController.m
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQSettingsViewController.h"
#import "PQAppDelegate.h"
#define SURE_TO_RESET 1

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
@synthesize emailLabel;
@synthesize emailCellView;
@synthesize user;
@synthesize parent;
@synthesize table;
@synthesize userInfo;

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
        DLog(@"");
    if(alertView.tag == SURE_TO_RESET && buttonIndex==1){
        //log user out. 
        [dataLayer setLoggedIn:NO];
        //crash this bitch.  
        NSMutableArray* forCrash = [[NSMutableArray alloc] initWithObjects:@"uno",@"dos", nil];
        for(NSString* numero in forCrash){
            [forCrash removeObject:numero];
        }
    }
}
-(void) alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex{
    DLog(@"");
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

-(void) updateField:(UserInfo*) updateInfo{

    //call back by edit controller.  
    NSString* name = updateInfo.fieldName;
    if([name hasPrefix:@"Name:"]){
        nameLabel.text = updateInfo.confirmNewValue;
    }else if([name hasPrefix:@"Address:"]){
        addrLabel.text = updateInfo.confirmNewValue;
    }else if([name hasPrefix:@"Email:"]){
        emailLabel.text = updateInfo.confirmNewValue;
    }else if([name hasPrefix:@"License:"]){
        plateLabel.text = updateInfo.confirmNewValue;
    }else{
        return;
    }
    
}

-(void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    EditSettingsViewController *vc = (EditSettingsViewController*) segue.destinationViewController;
    [vc setVehicle:userInfo];
    [vc setParent:self];
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    int section = indexPath.section;
    int row = indexPath.row;
    [dataLayer logString:[NSString stringWithFormat:@"%s sec %d row %d", __PRETTY_FUNCTION__, section, row]];
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if(section == 0){
        if(row==0){
            //payment options
            [self performSegueWithIdentifier:@"showPaymentOptions" sender:self];
        }else if(row==1){
            //account balance
        }else{
            UIAlertView* crashAlert = [[UIAlertView alloc] initWithTitle:@"Are you sure?" message:@"This application is in beta testing, and software bugs exist.  Only reset if you experience issues using the application." delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Reset", nil];
            crashAlert.tag = SURE_TO_RESET;
            [crashAlert show];
        }
    }else if (section==1){
        if(row==2){
            //change city
        }
    }else{
        //selected one of the account settings.
        switch (row) {
            case 0:{
                //name
                [userInfo setFieldName:@"Name:"];
                [userInfo setOldValue:nameLabel.text];
                [self performSegueWithIdentifier:@"editUser" sender:self];
            }
                break;
            case 1:
                //addr
                [userInfo setFieldName:@"Address:"];
                [userInfo setOldValue:addrLabel.text];
                [self performSegueWithIdentifier:@"editUser" sender:self];
                break;
            case 2:
                //email
                [userInfo setFieldName:@"Email:"];
                [userInfo setOldValue:emailLabel.text];
                [self performSegueWithIdentifier:@"editUser" sender:self];
                break;
            case 3:
                //license
                [userInfo setFieldName:@"License:"];
                [userInfo setOldValue:plateLabel.text];
                [self performSegueWithIdentifier:@"editUser" sender:self];
                break;
            default:
                //error
                break;
        }
    }
    
    //if section isn't 0, dont' do anything.  
}
#pragma mark - BUTTONS

-(IBAction)doneButtonPressed:(id)sender{
    //save information, then dismiss.
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    [self dismissModalViewControllerAnimated:YES];
}
-(IBAction)signOutButtonPressed:(id)sender{
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    [dataLayer setLoggedIn:NO];
    [[DataLayer managedObjectContext] deleteObject:[dataLayer fetchObjectsForEntityName:@"User" withPredicate:nil].allObjects.lastObject];
    self.parent.view.hidden = YES;
    [self dismissModalViewControllerAnimated:YES];
      
}

#pragma mark - PQNetworkLayerDelegate
- (void)afterFetchUserPointsBalance:(NSInteger)balance {
    balanceLabel.text = [NSString stringWithFormat:@"%ld", (long)balance];
    [DataLayer fetchUser].balance = [NSNumber numberWithInteger:balance];
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
    user = [DataLayer fetchUser];
    nameLabel.text = user.name;
    addrLabel.text = user.address;
    emailLabel.text = user.email;
    plateLabel.text = user.license;
    balanceLabel.text = [user.balance stringValue];
    userInfo = [[UserInfo alloc] init];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    [NetworkLayer fetchUserPointsBalanceWithUid:[user.uid unsignedLongLongValue] andDelegate:self];
    [networkLayer sendLogs];
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

@end
