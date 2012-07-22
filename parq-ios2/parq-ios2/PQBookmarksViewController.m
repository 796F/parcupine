//
//  PQBookmarksViewController.m
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQBookmarksViewController.h"
#import "BookmarkCell.h"

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

@synthesize parent;

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

-(void) tableView:(UITableView *)tableView willBeginEditingRowAtIndexPath:(NSIndexPath *)indexPath{
    
}
-(void) tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        //NSInteger row = [indexPath row];
        [UIView animateWithDuration:.7 animations:^{
            //x y width height
            tableView.frame = CGRectMake(320, 44, 320, 44);
        }];

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

    if(indexPath.row==0 && displayType == 0){
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
        if(displayType==0){
            //current location offsets the index.  
            cell.bookmarkName.text = [self.bookmarks objectAtIndex:[indexPath row]-1];
        }else if(displayType==1){
            //recents
            cell.bookmarkName.text = [self.recentSearches objectAtIndex:[indexPath row]];
        }else{
            //contacts
            cell.bookmarkName.text = [self.contacts objectAtIndex:[indexPath row]];
        }
        
        return cell;
    }
    
    
}

-(void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSLog(@"BOOKMARK index tapped %d\n", indexPath.row);
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    //find which bookmark that index corresponds to.  
//    BookmarkCell* cell = (BookmarkCell*) [table cellForRowAtIndexPath:indexPath];
    
    CLLocationCoordinate2D coord = CLLocationCoordinate2DMake(42.365102, -71.110841);
    //get rid of this view, and then zoom the map.  
    [self dismissModalViewControllerAnimated:YES];
    [self.parent showBookmarkWithLocation:&coord AndAnnotation:nil];    
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

-(void) toggleShowRedMinus:(NSArray*) cellList Row:(int) row Section:(int) section{
    int i=0;
    if(userIsEditing){
        userIsEditing = NO;
        //hide, currently editing.  
        for(i=0; i<cellList.count; i++){
            BookmarkCell* cell = (BookmarkCell*)[table cellForRowAtIndexPath:[NSIndexPath indexPathForRow:row+i inSection:section]];
            cell.selectionStyle = UITableViewCellSelectionStyleBlue;
            [UIView animateWithDuration:0.5 animations:^{
                cell.bookmarkName.center = CGPointMake(cell.bookmarkName.center.x-30, cell.bookmarkName.center.y);
                cell.minus.center = CGPointMake(-15, cell.minus.center.y);
            }];
        }

    }else{
        //show, not yet editing.  
        userIsEditing = YES;        
        for(i=0; i<cellList.count; i++){
            BookmarkCell* cell = (BookmarkCell*)[table cellForRowAtIndexPath:[NSIndexPath indexPathForRow:row+i inSection:section]];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            [UIView animateWithDuration:0.5 animations:^{
                cell.bookmarkName.center = CGPointMake(cell.bookmarkName.center.x+30, cell.bookmarkName.center.y);
                cell.minus.center = CGPointMake(30, cell.minus.center.y);
            }];
        }

    }
    
}

-(IBAction)editButtonPressed:(id)sender{
    
    switch(self.bookmarkSelectionBar.selectedSegmentIndex){
        case 0:
            //this is all a place holder really, best case is to immitate ios bookmarks.
            [self toggleShowRedMinus:self.bookmarks Row:1 Section:0];
            break;
        case 1:{
            
            UIActionSheet *clearRecentActionSheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:nil otherButtonTitles:@"Clear All Recents", nil];
            clearRecentActionSheet.tag = 1;
            [clearRecentActionSheet showInView:self.view];
            //clear the recent searches
            
            
            }
            break;
        case 2:
            //edit contacts
            [self toggleShowRedMinus:self.contacts Row:0 Section:2];
            break;
    }
    
    //shift all labels to the right a small bit.
    

    

    
    
    //show red minus signs in the shited space
    
    //also make accessory view on right side appear.  
    
}

-(IBAction)doneButtonPressed:(id)sender{
    [self dismissModalViewControllerAnimated:YES];
}

#pragma mark - view lifecycle
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
