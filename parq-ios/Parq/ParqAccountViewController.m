//
//  ParqAccountsViewController.m
//  Parq
//
//  Created by Mark Yen on 1/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqAccountViewController.h"
#import "SavedInfo.h"
#import "EditEmailViewController.h"
#import "TDSemiModal.h"
#import "ServerCalls.h"
#import "ParqLoginViewController.h"
#define UNPARK_AND_LOGOUT 1

@implementation ParqAccountViewController

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}
-(void) syncOptions{
    
    UITableView* tableView = [self tableView];
    NSIndexPath* path = [NSIndexPath indexPathForRow:2 inSection:1];
    UITableViewCell* cell = [tableView cellForRowAtIndexPath:path];
    if([SavedInfo ringEnable]){
        cell.accessoryType = UITableViewCellAccessoryCheckmark;    
    }else{
        cell.accessoryType = UITableViewCellAccessoryNone;
    }
    path = [NSIndexPath indexPathForRow:1 inSection:1];
    cell = [tableView cellForRowAtIndexPath:path];
    if([SavedInfo vibrateEnable]){
        cell.accessoryType = UITableViewCellAccessoryCheckmark;    
    }else{
        cell.accessoryType = UITableViewCellAccessoryNone;        
    }
    path = [NSIndexPath indexPathForRow:0 inSection:1];
    cell = [tableView cellForRowAtIndexPath:path];
    if([SavedInfo autoRefill]){
        cell.accessoryType = UITableViewCellAccessoryCheckmark;            
    }else{
        cell.accessoryType = UITableViewCellAccessoryNone;
    }
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self syncOptions];
    emailDisplayLabel.text = [SavedInfo getEmail];
    cardDisplayLabel.text = [@"\u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 " stringByAppendingString:[SavedInfo getCardStub]];
    editEmailView = [[EditEmailViewController alloc]initWithNibName:@"EditEmailViewController" bundle:nil];
    editEmailView.delegate = self;
	// Do any additional setup after loading the view, typically from a nib.
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
  return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{   
    
    UITableViewCell * cell = [tableView cellForRowAtIndexPath:indexPath];
    NSLog(@"\nINDEXPATH=%@\n", indexPath.description );
    if(indexPath.section==0){
        switch (indexPath.row) {
            case 0:{ 
                NSLog(@"change email");
                cell.selected=NO;
                [self presentSemiModalViewController:editEmailView];
                [editEmailView.editEmail becomeFirstResponder];
                break;
                }
            case 1:
                NSLog(@"change cc");
                cell.selected=NO;
                break;
        }
        //user info part
    }else if(indexPath.section==1){
        //warning/refill settings
        switch (indexPath.row) {
            case 0:
                if(cell.accessoryType==UITableViewCellAccessoryCheckmark){
                    cell.accessoryType = UITableViewCellAccessoryNone;
                    cell.selected=NO;
                    [SavedInfo toggleRefill];
                }else{
                    cell.accessoryType = UITableViewCellAccessoryCheckmark;
                    cell.selected=NO;
                    [SavedInfo toggleRefill];
                }
                break;
            case 1:
                if(cell.accessoryType==UITableViewCellAccessoryCheckmark){
                    cell.accessoryType = UITableViewCellAccessoryNone;
                    cell.selected=NO;
                    [SavedInfo toggleVibrate];
                }else{
                    cell.accessoryType = UITableViewCellAccessoryCheckmark;
                    cell.selected=NO;
                    [SavedInfo toggleVibrate];
                }
                break;
            case 2:
                if(cell.accessoryType==UITableViewCellAccessoryCheckmark){
                    cell.accessoryType = UITableViewCellAccessoryNone;
                    cell.selected=NO;
                    [SavedInfo toggleRing];
                }else{
                    cell.accessoryType = UITableViewCellAccessoryCheckmark;
                    cell.selected=NO;
                    [SavedInfo toggleRing];
                }
                break;
        }
    }else if(indexPath.section==2){
        cell.selected = NO;
        //if user is parked, shoot off dialog, wait for yes/no
        if([SavedInfo isParked]){
            UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"You are parked!" message:@"logging out will unpark you" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Yes", nil];
            alertView.tag = UNPARK_AND_LOGOUT;
            [alertView show];
        }else{
            [SavedInfo logOut];
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
            ParqLoginViewController *vc = [storyboard instantiateViewControllerWithIdentifier:@"LoginViewController"];
            vc.parent = self;
            [vc setModalPresentationStyle:UIModalPresentationFullScreen];
            
            [self presentModalViewController:vc animated:YES];
            return;
            
        }
    }
}

-(void) alertView:(UIAlertView*)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(alertView.tag==UNPARK_AND_LOGOUT){
        if(buttonIndex==1){
            if([ServerCalls unparkUserWithSpotId:[SavedInfo spotId] ParkRefNum:[SavedInfo parkRefNum]]){
                [SavedInfo logOut];
                UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
                ParqLoginViewController *vc = [storyboard instantiateViewControllerWithIdentifier:@"LoginViewController"];
                vc.parent = self;
                [vc setModalPresentationStyle:UIModalPresentationFullScreen];
                [self presentModalViewController:vc animated:YES];
            }
        }else{
            
        }   
    }
}

-(void)editEmailSetEmail:(EditEmailViewController*)viewController {
    NSString* newEmail = viewController.editEmail.text;
    NSString* confirmNewEmail = viewController.confirmNewEmail.text;
    NSString* oldEmail = viewController.oldEmail.text;
    NSString* confirmOldEmail = [SavedInfo getEmail];
    BOOL comparison = [newEmail isEqualToString:confirmNewEmail] && [oldEmail isEqualToString:confirmOldEmail];
    if( comparison ){
        BOOL result = [ServerCalls  editUserEmail:newEmail Password:@"" PhoneNumber:@""];
        if(result){
            emailDisplayLabel.text = newEmail;
            [SavedInfo setEmail:newEmail];
            [self dismissSemiModalViewController:editEmailView];
        }else{
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error updating profile"
                                                            message:@"Please try again later"
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
            //popup "could not change.  check fields"
        }
    }else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Your fields did not match"
                                                        message:@"Please check and try again"
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
            //popup "did not match, check your fields"
    }
}
-(void)editEmailClearEmail:(EditEmailViewController*)viewController{   
    viewController.editEmail.text=@"";
    viewController.oldEmail.text=@"";
    viewController.confirmNewEmail.text=@"";
    
}

-(void)editEmailCancel:(EditEmailViewController*)viewController{
    viewController.editEmail.text=@"";
    viewController.oldEmail.text=@"";
    viewController.confirmNewEmail.text=@"";
	[self dismissSemiModalViewController:editEmailView];
}

@end
