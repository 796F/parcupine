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
    editEmailView = [[EditEmailViewController alloc]initWithNibName:@"EditEmailViewController" bundle:nil];
    editEmailView.delegate = self;
    reference = self;
	// Do any additional setup after loading the view, typically from a nib.
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
  return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{   
    
    UITableViewCell * cell = [tableView cellForRowAtIndexPath:indexPath];
    
    if(indexPath.section==0){
        switch (indexPath.row) {
            case 0:{ 
                NSLog(@"change email");
                cell.selected=NO;
                [reference presentSemiModalViewController:editEmailView];
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
        //log user out
        NSLog(@"log me out!!"  );
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
            //popup "could not change.  check fields"
        }
    }else{
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
