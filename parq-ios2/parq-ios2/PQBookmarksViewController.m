//
//  PQBookmarksViewController.m
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQBookmarksViewController.h"
#import "BookmarkCell.h"
#import "RecentCell.h"
#import "ContactCell.h"
@interface PQBookmarksViewController ()

@end

@implementation PQBookmarksViewController
@synthesize table;
@synthesize bookmarkSelectionBar;
@synthesize bookmarks = _bookmarks;
@synthesize recentSearches = _recentSearches;
@synthesize contacts = _contacts;
@synthesize leftButton;
@synthesize midButton;
@synthesize userIsEditing;
@synthesize rightButton;
-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex==0){
        //clear the recents
    }
    //else cancel.  
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    int displayType = bookmarkSelectionBar.selectedSegmentIndex;
    if(displayType == 0){
        return 1;
    }else if (displayType == 1){
        return 1;
        //split recent by area? time of search?
    }else{
        //split contacts up into alphabetical sections ???
        return 1;
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    // Return the number of rows in the section.
    int displayType = bookmarkSelectionBar.selectedSegmentIndex;
    if(displayType == 0){
        if (self.bookmarks==nil){
            self.bookmarks = [self loadBookmarks];
        }
        return [self.bookmarks count] + 1;
    }else if(displayType == 1){
        if(self.recentSearches==nil){
            self.recentSearches = [self loadRecent];
        }
        return [self.recentSearches count];
    }else{
        if(self.contacts==nil){
            self.contacts = [self loadContacts];
        }
        return [self.contacts count];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{

    static NSString *CellIdentifier;
    int displayType = bookmarkSelectionBar.selectedSegmentIndex;

    if(displayType == 0){
        if(indexPath.row==0){
            UITableViewCell *cell = [tableView
                                     dequeueReusableCellWithIdentifier:@"currLocCell"];
            if (cell == nil) {
                cell = [[UITableViewCell alloc]
                        initWithStyle:UITableViewCellStyleDefault 
                        reuseIdentifier:CellIdentifier];
            }
            return cell;
        }else{
            BookmarkCell *cell = [tableView
                                     dequeueReusableCellWithIdentifier:@"bookmarkCell"];
            if (cell == nil) {
                cell = [[BookmarkCell alloc]
                        initWithStyle:UITableViewCellStyleDefault 
                        reuseIdentifier:CellIdentifier];
            }
            if (self.bookmarks==nil){
                self.bookmarks = [self loadBookmarks];
            }
            cell.bookmarkName.text = [self.bookmarks objectAtIndex:[indexPath row]-1];
            return cell;
        }
    }else if(displayType == 1){
        RecentCell *cell = [tableView dequeueReusableCellWithIdentifier:@"recentCell"];
        if (cell == nil) {
            cell = [[RecentCell alloc]
                    initWithStyle:UITableViewCellStyleDefault 
                    reuseIdentifier:CellIdentifier];
        }
        if(self.recentSearches==nil){
            self.recentSearches = [self loadRecent];   
        }
        cell.recentSearchName.text = [self.recentSearches objectAtIndex:[indexPath row]];
        cell.recentSearchDescription.text = @"IM A MISSING DESC LAWL";
        return cell;
    }else{
        ContactCell* cell = [tableView dequeueReusableCellWithIdentifier:@"contactCell"];
        if (cell == nil) {
            cell = [[ContactCell alloc]
                    initWithStyle:UITableViewCellStyleDefault 
                    reuseIdentifier:CellIdentifier];
        }
        if(self.contacts==nil){
            self.contacts = [self loadContacts];
        }
        cell.contactName.text = [self.contacts objectAtIndex:[indexPath row]];
        return cell;
    }
    
}

-(void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSLog(@"index tapped %d\n", indexPath.row);
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (IBAction)bottomBarTapped {
    switch (self.bookmarkSelectionBar.selectedSegmentIndex) {
        case 0:
            leftButton.title = @"Edit";
            midButton.title = @"Bookmarks";
            [table reloadData];
            break;
        case 1:
            leftButton.title = @"Clear";
            midButton.title = @"All Recent";
            [table reloadData];
            break;
        case 2:
            leftButton.title = @"Edit";
            midButton.title = @"Contacts";
            [table reloadData];
            break;
    }
}


-(IBAction)editButtonPressed:(id)sender{
    switch(self.bookmarkSelectionBar.selectedSegmentIndex){
        case 0:
            //this is all a place holder really, best case is to immitate ios bookmarks.
            if(userIsEditing){
                leftButton.title = @"Edit";
                leftButton.tintColor = [UIColor grayColor];
                rightButton.title = @"Done";
                rightButton.tintColor = [UIColor grayColor];
                userIsEditing = NO;
            }else{
                leftButton.title = @"Done";
                leftButton.tintColor = [UIColor blueColor];
                rightButton.title = @"Delete";
                rightButton.tintColor = [UIColor redColor];
                userIsEditing = YES;
            }
            break;
        case 1:{
            
            UIActionSheet *clearRecentActionSheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:nil otherButtonTitles:@"Clear All Recents", nil];
            clearRecentActionSheet.tag = 1;
            [clearRecentActionSheet showInView:self.view];
            //clear the recent searches
            break;
            
            }
        case 2:
            //edit contacts
            
            break;
    }
    
    //shift all labels to the right a small bit.
    
    //show red minus signs in the shited space
    
    //also make accessory view on right side appear.  
    
}

-(IBAction)doneButtonPressed:(id)sender{
    [self dismissModalViewControllerAnimated:YES];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(NSArray*) loadBookmarks{
    return [[NSArray alloc] initWithObjects:@"Work",@"Gym",@"Lab",@"School", @"Girl",@"Bars", nil];
}

-(NSArray*) loadRecent{
    return [[NSArray alloc] initWithObjects:@"House of Blues", @"SENSEable Labs", @"Baltimore Aquarium", nil];
}
-(NSArray*) loadContacts{
    return [[NSArray alloc] initWithObjects:@"Baczuk, Eric", @"Biderman, Assaf", @"Bukauskus, Aurimas", @"Long, Sunny", @"Ratti, Carlo", @"Xia, Michael", @"Yen, Mark", @"Zheng, Gordon", nil];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    //load the array needed from core data.  
    int arrayToLoad = bookmarkSelectionBar.selectedSegmentIndex;
    if(arrayToLoad==0){
        self.bookmarks = [self loadBookmarks];
    }else if(arrayToLoad==1){
        self.recentSearches = [self loadRecent];
        //screw descriptions.  
    }else{
        self.contacts = [self loadContacts];
    }
    userIsEditing = NO;
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
