//
//  PQMapViewController.m
//  parq-ios2
//
//  Created by Mark Yen on 3/16/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQMapViewController.h"
#import "PQParkingViewController.h"
#import "PQSettingsViewController.h"
#import "PQSpotAnnotation.h"

//calculation constants
#define METERS_PER_MILE 1609.344
#define MAX_CALLOUTS 8
#define GREY_CIRCLE_R 12
#define CALLOUT_LINE_LENGTH 0.00000018
#define CALLOUT_WIDTH 0.00008
#define CALLOUT_HEIGHT 0.00015

/* 
 grid to street  dlon 0.009102
 street to spot  dlon 0.003932
 */

//map level span control constnats.  
#define GRID_SPAN_UPPER_LIMIT 0.035748
#define GRID_TO_STREET_SPAN_LAT 0.014040
#define STREET_TO_SPOT_SPAN_LAT 0.003 
//2387
#define SPOT_LEVEL_SPAN 0.001649

#define ACCURACY_LIMIT 100
#define USER_DISTANCE_FROM_SPOT_THRESHOLD 0.0001
#define GRID_LENGTH 0.005
#define GRID_LEVEL_REGION_METERS 1150
#define STREET_LEVEL_REGION_METERS 500
#define SPOT_LEVEL_REGION_METERS 100



//alert view tags
#define GPS_LAUNCH_ALERT 0

typedef enum {
    kGridZoomLevel,
    kStreetZoomLevel,
    kSpotZoomLevel
} ZoomLevel;

typedef enum {
    kAvailabilityData,
    kPriceData,
    kNoneData
} DataType;

typedef struct{
    CLLocationCoordinate2D A;
    CLLocationCoordinate2D B;
} SegTwo;

@interface PQMapViewController ()
@property (strong, nonatomic) UIView *disableViewOverlay;
@property (strong, nonatomic) UIBarButtonItem *leftBarButton;
@property (nonatomic) ZoomLevel zoomState;
@property (nonatomic) DataType displayedData;
@end

@implementation PQMapViewController
@synthesize gCircle;
@synthesize callouts;
@synthesize calloutLines;
@synthesize map;
@synthesize topSearchBar;
@synthesize availabilitySelectionView;
@synthesize bottomSpotSelectionView;
@synthesize topSpotSelectionView;
@synthesize gradientIcon;
@synthesize navigationBar;
@synthesize bottomSpotSelectionBar;
@synthesize topSpotSelectionBar;
@synthesize geocoder;
@synthesize disableViewOverlay;
@synthesize leftBarButton;
@synthesize zoomState;
@synthesize displayedData;
@synthesize grids;
@synthesize streets;
@synthesize spots;
@synthesize managedObjectContext;
@synthesize user_loc;
@synthesize user_loc_isGood;
@synthesize desired_spot;
@synthesize availabilitySelectionBar;
@synthesize oldStreetLevelRegion;
@synthesize shouldNotClearOverlays;

-(void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSLog(@"hello\n");
}

-(NSArray*)loadCambridgeBlockData{
        NSArray* data = [NSArray arrayWithObjects:
                         
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.377235,-71.11056;42.37505,-71.11112", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374928,-71.11007;42.377625,-71.109436", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374775,-71.10885;42.37763,-71.10812", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.374706,-71.10835", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.375122,-71.11163;42.374706,-71.10835", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37439,-71.105896;42.374706,-71.10835", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37738,-71.11063;42.3779,-71.10813;42.377975,-71.10781", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.376755,-71.10657;42.377975,-71.10781", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.378284,-71.10638;42.378235,-71.106316;42.377125,-71.10495", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.377125,-71.10495;42.374928,-71.10229", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.375214,-71.10415;42.37417,-71.103035", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37439,-71.105896;42.37624,-71.105446;42.376278,-71.10542", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372902,-71.108215;42.374527,-71.10706", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37268,-71.107506;42.37441,-71.10612", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37718,-71.104866;42.377632,-71.10418;42.3776,-71.104225", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.376625,-71.10436;42.377186,-71.10354", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.376747,-71.102776;42.376114,-71.10372", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37323,-71.10557;42.373863,-71.105", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.373417,-71.103935;42.374065,-71.103424", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372925,-71.106316;42.37326,-71.10702", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372986,-71.10499;42.37371,-71.10665", @"line", [NSNumber numberWithInt:3], @"color", nil],                   
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.373196,-71.110825", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.3779,-71.10813;42.37738,-71.11063", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37738,-71.11063;42.37695,-71.112656", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37695,-71.112656", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37695,-71.112656;42.376312,-71.11561", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.3791,-71.11478;42.37669,-71.11379", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37637,-71.11573;42.378574,-71.11655", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.378315,-71.113106;42.37695,-71.112656", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374928,-71.11007;42.377625,-71.109436", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37505,-71.11112;42.377235,-71.11056", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.375263,-71.11274;42.377007,-71.11236", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374928,-71.11007;42.37536,-71.11342", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374775,-71.10885;42.374928,-71.11007", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372894,-71.11496;42.374832,-71.11446", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372692,-71.11407;42.374348,-71.1136", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37384,-71.11245;42.37282,-71.112946", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.374706,-71.10835", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37397,-71.11278;42.37451,-71.11239;42.375202,-71.11227;42.3752,-71.11224", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374348,-71.1136;42.37536,-71.11342", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.376625,-71.11409;42.37585,-71.11429", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374775,-71.10885;42.37763,-71.10812", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.37416,-71.11327", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.373196,-71.110825;42.371445,-71.11201", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.371117,-71.11081", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37232,-71.10861;42.370808,-71.10961", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.372055,-71.10795", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372055,-71.10795;42.37119,-71.105736", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371445,-71.11201;42.37005,-71.10692", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369576,-71.11186;42.371117,-71.11081", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374706,-71.10835;42.372772,-71.109726", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374527,-71.10706;42.37232,-71.10861", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37441,-71.10612;42.373062,-71.10718;42.37268,-71.107506;42.372055,-71.10795", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371613,-71.106834;42.37162,-71.10683;42.37323,-71.10557;42.373863,-71.105;42.374233,-71.10471", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37119,-71.105736;42.373417,-71.103935;42.374065,-71.103424", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37381,-71.10213;42.37133,-71.10429;42.370815,-71.10479", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37151,-71.109024;42.370678,-71.10649", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372055,-71.10795;42.37133,-71.108444", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37184,-71.1074;42.37115,-71.107895", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371613,-71.106834;42.37162,-71.10683;42.37094,-71.10728", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.3704,-71.103745;42.373707,-71.10126", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37371,-71.10665;42.37323,-71.10557;42.373276,-71.10552", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369995,-71.110176;42.370808,-71.10961", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369205,-71.10783;42.37005,-71.10692", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36975,-71.10586;42.369164,-71.1042", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.3694,-71.1034;42.369865,-71.10471", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374706,-71.10835;42.372772,-71.109726", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37232,-71.10861;42.370808,-71.10961", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370808,-71.10961;42.369175,-71.11073", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371117,-71.11081;42.372772,-71.109726", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.373196,-71.110825;42.371445,-71.11201;42.371517,-71.11195", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371834,-71.11337;42.37384,-71.11245", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372074,-71.11425;42.374348,-71.1136", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372894,-71.11496;42.374832,-71.11446", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372074,-71.11425;42.3715,-71.112206;42.370808,-71.10961", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37232,-71.10861;42.373196,-71.110825", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370808,-71.10961;42.37053,-71.108665", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371086,-71.1161;42.369957,-71.11677", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371414,-71.11706;42.370266,-71.11775", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370495,-71.11448;42.37165,-71.1177", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369366,-71.11465;42.370495,-71.11448", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372604,-71.11723", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372524,-71.11727;42.37165,-71.1177", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37159,-71.11692;42.372326,-71.11656", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370266,-71.11775;42.3704,-71.11826;42.370632,-71.119286", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369583,-71.11812;42.370266,-71.11775", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370266,-71.11775;42.369957,-71.11677", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37013,-71.11558;42.370754,-71.11518", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370495,-71.11448;42.3701,-71.11335;42.36986,-71.11258;42.369045,-71.110374", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374348,-71.1136;42.37536,-71.11342", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374832,-71.11446;42.37547,-71.114296", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364551,-71.113099;42.364753,-71.110776", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36643,-71.111047;42.363285,-71.110543", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.364904,-71.112343", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364904,-71.112343;42.364618,-71.112311", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.365294,-71.111857", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365294,-71.111857;42.365383,-71.110889", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36532,-71.111565;42.366043,-71.111667", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366043,-71.111667;42.36622,-71.111839", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36622,-71.111839;42.366392,-71.112826", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366788,-71.11193;42.366412,-71.111031", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360075,-71.094794;42.359591,-71.094086", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.359591,-71.094086;42.358925,-71.093528", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.358925,-71.093528;42.357253,-71.092616", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370024,-71.113324;42.365998,-71.113539", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365998,-71.113539;42.364587,-71.113882", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369263,-71.113389;42.367788,-71.109439", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.367788,-71.109439;42.365838,-71.106027", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.368533,-71.113473;42.36759,-71.110651", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36759,-71.110651;42.365402,-71.106639", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.367915,-71.113527;42.367035,-71.110898", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.367035,-71.110898;42.366163,-71.109171", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366163,-71.109171;42.363896,-71.108828", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366623,-71.110158;42.363722,-71.109707", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.368938,-71.110308;42.367241,-71.111392", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366805,-71.110383;42.368375,-71.108806", @"line", [NSNumber numberWithInt:1], @"color", nil]
                         ,nil];

     

    NSMutableArray* segList = [[NSMutableArray alloc] initWithCapacity:data.count];
    for(id line in data){
        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[raw_waypoints.count];
        int i=0;
        for (id raw_waypoint in raw_waypoints) {
            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
            waypoints[i++] = coordinate;
        }
        for(i=0; i<raw_waypoints.count-1; i++){
            Segment* x =[[Segment alloc] initWithPointsA:&waypoints[i] andB:&waypoints[i+1] andColor:[[line objectForKey:@"color"] intValue]];
            [segList addObject:x];
        }
        
    }
    return segList;
}

- (NSArray*)loadBlockData {
    NSArray* data = [NSArray arrayWithObjects:
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364551,-71.113099;42.364753,-71.110776", @"line", [NSNumber numberWithInt:0], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36643,-71.111047;42.363285,-71.110543", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.364904,-71.112343", @"line", [NSNumber numberWithInt:2], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364904,-71.112343;42.364618,-71.112311", @"line", [NSNumber numberWithInt:0], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.365294,-71.111857", @"line", [NSNumber numberWithInt:4], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365294,-71.111857;42.365383,-71.110889", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36532,-71.111565;42.366043,-71.111667", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366043,-71.111667;42.36622,-71.111839", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36622,-71.111839;42.366392,-71.112826", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366788,-71.11193;42.366412,-71.111031", @"line", [NSNumber numberWithInt:0], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360075,-71.094794;42.359591,-71.094086", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.359591,-71.094086;42.358925,-71.093528", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.358925,-71.093528;42.357253,-71.092616", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     nil];
    
    NSMutableArray* segList = [[NSMutableArray alloc] initWithCapacity:2];
    for(id line in data){
        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[raw_waypoints.count];
        int i=0;
        for (id raw_waypoint in raw_waypoints) {
            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
            waypoints[i++] = coordinate;
        }
        Segment* x =[[Segment alloc] initWithPointsA:&waypoints[0] andB:&waypoints[1] andColor:[[line objectForKey:@"color"] intValue]];
        [segList addObject:x];
    }
    return segList;
}

-(NSArray*) loadSpotData {
    return [NSArray arrayWithObjects:
            @"42.365354,-71.110843,1,1410,0,1",
            @"42.365292,-71.110835,1,1412,0,2",
            @"42.365239,-71.110825,1,1414,0,3",
            @"42.365187,-71.110811,0,1416,0,4",
            @"42.365140,-71.110806,1,1418,0,5",
            @"42.365092,-71.110798,0,1420,0,6",
            @"42.365045,-71.110790,1,1422,0,7",
            @"42.364995,-71.110782,0,1424,0,8",
            @"42.364947,-71.110768,0,1426,0,9",
            @"42.364896,-71.110766,0,1428,0,10",
            @"42.364846,-71.110752,0,1430,0,11",
            @"42.364797,-71.110739,0,1432,0,12",
            
            @"42.365348,-71.110924,1,1411,0,13",
            @"42.365300,-71.110916,0,1413,0,14",
            @"42.365251,-71.110905,0,1415,0,15",
            @"42.365203,-71.110900,0,1417,0,16",
            @"42.365154,-71.110892,1,1419,0,17",
            @"42.365104,-71.110876,0,1421,0,18",
            @"42.365049,-71.110868,1,1423,0,19",
            @"42.364993,-71.110860,1,1425,0,20",
            @"42.364943,-71.110849,1,1427,0,21",
            @"42.364894,-71.110846,1,1429,0,22",
            @"42.364846,-71.110835,0,1431,0,23",
            @"42.364799,-71.110830,1,1433,0,24",
            //
            @"42.365311,-71.110994,0,1431,0,24",
            @"42.365291,-71.111118,0,1433,0,24",
            @"42.365279,-71.111253,0,1434,0,24",
            @"42.365267,-71.111388,0,1435,0,24",
            @"42.365255,-71.111523,0,1436,0,24",
            @"42.365243,-71.111658,0,1437,0,24",
            @"42.365231,-71.111793,0,1438,0,24",
            @"42.365219,-71.111928,0,1439,0,24",
            
            @"42.364769,-71.11101,0,1433,0,24",
            @"42.364759,-71.111133,0,1434,0,24",
            @"42.364749,-71.111256,0,1435,0,24",
            @"42.364739,-71.111379,0,1436,0,24",
            @"42.364729,-71.111502,0,1437,0,24",
            @"42.364719,-71.111625,0,1438,0,24",
            @"42.364709,-71.111748,0,1439,0,24",
            @"42.364699,-71.111871,0,1440,0,24",
            @"42.364795,-71.11093,0,1444,024",

            @"42.364689,-71.11101,0,1433,0,24",
            @"42.364679,-71.111133,0,1434,0,24",
            @"42.364669,-71.111256,0,1435,0,24",
            @"42.364659,-71.111379,0,1436,0,24",
            @"42.364649,-71.111502,0,1437,0,24",
            @"42.364639,-71.111625,0,1438,0,24",
            @"42.364629,-71.111748,0,1439,0,24",
            @"42.364619,-71.111871,0,1440,0,24",
            @"42.364684,-71.110887,0,1449,0,24",
            
            @"42.365555,-71.110825,1,1436,0,24",
            @"42.365733,-71.110851,1,1437,0,24",
            @"42.365911,-71.110877,1,1438,0,24",
            @"42.366089,-71.110903,1,1439,0,24",
            @"42.366267,-71.110929,1,1440,0,24",


            nil];
    
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (actionSheet.tag == GPS_LAUNCH_ALERT) {
        switch (buttonIndex) {
            case 0:
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://maps.google.com/maps?q=Spot+%d@%f,%f", desired_spot.name, desired_spot.coordinate.latitude, desired_spot.coordinate.longitude]]];
                break;
            case 1:
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://maps.google.com/maps?daddr=Spot+%d@%f,%f&saddr=Current+Location@%f,%f", desired_spot.name, desired_spot.coordinate.latitude, desired_spot.coordinate.longitude,user_loc.latitude, user_loc.longitude]]];
                break;
            case 2:
                [self parkNow];
                break;
        }
    }
}

#pragma mark - CALLOUT PLACEMENT

-(bool)A:(double)a sameSignAsB:(double) b{
    if(a>0){
        return b>0 ? true : false;
    }else{
        return b<0 ? true : false;
    }
}

//separates points for a segment to left and right, uses first segment if multiple exist.  
-(NSArray*) newCalloutPlacementWithSegment:(Segment*)seg andSpots:(NSArray*)spotList{
    double left_coords[2] = {0,0};
    double right_coords[2] = {0,0};
    int left_count = 0;
    int right_count = 0;
    bool error = false;
    double reference_dy = 0;
    double reference_dx = 0;
    NSMutableArray* leftSide = [[NSMutableArray alloc] init];
    NSMutableArray* rightSide = [[NSMutableArray alloc] init];
    
    for(MKCircle* spot in spotList){
        bool isRightSide = false;        
        //for each spot inside the grey circle, calculate the projected point.  
        CLLocationCoordinate2D x = spot.coordinate;
        CLLocationCoordinate2D segment[2] = {[seg A], [seg B]};
        CLLocationCoordinate2D point = [self getProjectedPoint:&x A:&segment[0] B:&segment[1] error:&error];
        //don't care about errors in this case.  known to be localized.  
        //determine from that, what side of the street a point lays.  
        if(reference_dy == 0){
            //first one, assign to a side, keep it's sign. 
            reference_dy = x.latitude - point.latitude;
            reference_dx = x.longitude - point.longitude;
            isRightSide = true;
            //NSLog(@"%d is right side!\n", [[spotArray objectAtIndex:3] intValue]);
            [rightSide addObject:spot];
        }else{
            //do a check to see if it's the same side as ref_sign
            //compare latitudes, if useful, go on.  
            double spot_dy = x.latitude - point.latitude;
            double spot_dx = x.longitude - point.longitude;
            if([self A:reference_dx sameSignAsB:spot_dx] && [self A:reference_dy sameSignAsB:spot_dy]){
                //same side as reference.  
                //NSLog(@"%d is right side!\n", [[spotArray objectAtIndex:3] intValue]);
                isRightSide = true;
                [rightSide addObject:spot];
            }else{
                //NSLog(@"%d is left side!\n", [[spotArray objectAtIndex:3] intValue]);
                //different side as reference.  
                isRightSide = false;
                [leftSide addObject:spot];
            }
        }
        //add it to the left or right value depending
        if(isRightSide){
            right_coords[0] += point.latitude;
            right_coords[1] += point.longitude;
            right_count++;
        }else{
            left_coords[0] += point.latitude;
            left_coords[1] += point.longitude;
            left_count++;
        }
    }
    right_coords[0] /= right_count;
    right_coords[1] /= right_count;
    left_coords[0] /= left_count;
    left_coords[1] /= left_count;
    CLLocationCoordinate2D averages[2];
    averages[0] = CLLocationCoordinate2DMake(left_coords[0], left_coords[1]);
    averages[1] = CLLocationCoordinate2DMake(right_coords[0], right_coords[1]);
    
    //now have averages for both left and right, as well as arrays.  
    NSMutableArray* calloutArr = [[NSMutableArray alloc] initWithCapacity:right_count+left_count];
    [calloutArr addObjectsFromArray:[self oneSidedCallout:leftSide aLat:averages[0].latitude aLon:averages[0].longitude]];
    [ calloutArr addObjectsFromArray:[self oneSidedCallout:rightSide aLat:averages[1].latitude aLon:averages[1].longitude]];
    
    return calloutArr;
}

- (NSArray *)calloutBubblePlacement:(CLLocationCoordinate2D *)selectionCenter withR:(CLLocationDistance) radius{
    //using the street information, snap to the street via geometric projection 
    
    CLLocation* center = [[CLLocation alloc] initWithLatitude:(*selectionCenter).latitude longitude:(*selectionCenter).longitude];
    
    //look through list of points, check spot distanceFromLocation (coord) vs radius.  
    //keep track of some stuff
    NSMutableArray* insideCircle = [[NSMutableArray alloc] init];
    float avgLat=0, avgLon=0, count=0;
    for(PQSpotAnnotation* spot in spots){
        CLLocation* spot_loc = [[CLLocation alloc] initWithLatitude:        spot.coordinate.latitude longitude: spot.coordinate.longitude];
        CLLocationDistance dist = [spot_loc distanceFromLocation:center];
        if(dist<radius-2 && spot.available == YES){
            //inside the circle
            [insideCircle addObject:spot];
            avgLat += spot_loc.coordinate.latitude;
            avgLon += spot_loc.coordinate.longitude;   
            count++;
        }
    }
    /* CALCULATE AVERAGES FOR BOTH SIDES */
    NSArray* segData = [self loadBlockData];
    return [self newCalloutPlacementWithSegment:[segData objectAtIndex:1] andSpots:insideCircle];
    
}

-(NSArray*) oneSidedCallout:(NSArray*)spotList aLat:(double)avgLat aLon:(double)avgLon{
    NSMutableArray* results = [[NSMutableArray alloc] initWithCapacity:spots.count];
    
    //project bubbles using this average and the spot's coordinates.  
    for(MKCircle* spot in spotList){
        CLLocation* spot_loc = [[CLLocation alloc] initWithLatitude:spot.coordinate.latitude longitude:spot.coordinate.longitude];
        //controls how far the callouts go.  
        double rsq = CALLOUT_LINE_LENGTH;
        
        //only make callout for those clearly in circle
        
        
        double mdx = avgLat - spot_loc.coordinate.latitude;
        double mdy = avgLon - spot_loc.coordinate.longitude;
        double dx = sqrt(rsq / (1 + (mdy * mdy)/(mdx * mdx)));
        double dy = sqrt(rsq / (1 + (mdx * mdx)/(mdy * mdy)));
        double callout_lat;
        double callout_lon;
        CalloutCorner corner;
        if(mdy>0){
            if(mdx>0){
                //bottom left of avg
                callout_lat = avgLat - dx;
                callout_lon = avgLon - dy;
                corner = kTopRightCorner;
            }else{
                //bottom right of avg
                callout_lat = avgLat + dx;
                callout_lon = avgLon - dy;
                corner = kBottomRightCorner;
            }
        }else{
            if(mdx>0){
                //top Left of avg
                callout_lat = avgLat - dx;
                callout_lon = avgLon + dy;
                corner = kTopLeftCorner;
            }else{
                //top right of avg
                callout_lat = avgLat + dx;
                callout_lon = avgLon + dy;
                corner = kBottomLeftCorner;
                
            }
        }
        NSString* title = [NSString stringWithFormat:@"%d", spot.name];
        NSDictionary* add = [[NSDictionary alloc] initWithObjectsAndKeys:[[CalloutMapAnnotation alloc] initWithLatitude:callout_lat                                         andLongitude:callout_lon
                                                                                                               andTitle:title
                                                                                                              andCorner:corner
                                                                                                              andCircle:spot], @"callout",spot_loc, @"spot", nil];
        
        [results addObject:add];
    }
    return results;
}

-(bool) pointA:(CLLocationCoordinate2D*)a isCloseToB:(CLLocationCoordinate2D*)b{
    double dx = a->longitude - b->longitude;
    double dy = a->latitude - b->latitude;
    double hsq = dx*dx+dy*dy;
    if(hsq < USER_DISTANCE_FROM_SPOT_THRESHOLD ){
        return true;
    }else{
        return false;
    }
}

- (bool) tappedCalloutAtCoords:(CLLocationCoordinate2D*) coords{
    for(int i=0; i< callouts.count; i++){
        CalloutMapAnnotation* c = [callouts objectAtIndex:i];
        double dx = c.latitude-(*coords).latitude; 
        double dy = c.longitude-(*coords).longitude;
        if(fabs(dx) < CALLOUT_WIDTH && fabs(dy) < CALLOUT_HEIGHT){
            //check where user's location is.  
            CLLocationCoordinate2D spot_loc = c.circle.coordinate;
            desired_spot = c.circle;
            //user distance from spot.  
            //user's gps reported accuracy. 
            
            if(user_loc_isGood && ![self pointA:&spot_loc isCloseToB:&user_loc]){
                UIActionSheet *directionsActionSheet = [[UIActionSheet alloc] initWithTitle:[NSString stringWithFormat:@"Spot %@", c.title] delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:nil otherButtonTitles:@"Open in Maps", @"Get Directions", @"Park Now", nil];
                directionsActionSheet.tag = GPS_LAUNCH_ALERT;
                [directionsActionSheet showInView:bottomSpotSelectionView];
                return true;
            }else{
                [self parkNow];
                return true;
            }
        }
    }
    return false;
}

- (void)parkNow {
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    UINavigationController *vc = [storyboard instantiateViewControllerWithIdentifier:@"ParkingController"];
    [vc setModalPresentationStyle:UIModalPresentationFullScreen];
    PQParkingViewController *vcTop = [[vc viewControllers] objectAtIndex:0];
    //also pass rate information for the selected spot here.  
    vcTop.parent = (PQMapViewController*)self;
    [self presentModalViewController:vc animated:YES];
}

-(void) clearCallouts{
    [self.map removeAnnotations:callouts];
    [self.map removeOverlays:calloutLines];   
    [calloutLines removeAllObjects];
    [callouts removeAllObjects];   
}

- (void)clearMap {
    [map removeOverlays:map.overlays];
    [map removeAnnotations:callouts];
    [map removeOverlays:calloutLines];
    [map removeAnnotations:spots];
    [grids removeAllObjects];
    [callouts removeAllObjects];
    [calloutLines removeAllObjects];
    [streets removeAllObjects];
    [spots removeAllObjects];
}

- (void)clearGrids {
    // For some reason, clearing just self.grids
    // is leaving residual stuff behind on the map.
    // A hack to get around this is to clear all
    // overlays and then add back the ones we want.
//    [self.map removeOverlays:self.grids];
//    [grids removeAllObjects];
    [self.map removeOverlays:self.map.overlays];
    [grids removeAllObjects];
    [self.map addOverlays:calloutLines];
    if (gCircle != nil) {
        [self.map addOverlay:gCircle];
    }
}

-(void) clearStreets{
    [self.map removeOverlays:self.streets];
    [streets removeAllObjects];
}
-(void) clearSpots{
    [self.map removeAnnotations:self.spots];
    [spots removeAllObjects];
}

- (void)showSelectionCircle:(CLLocationCoordinate2D *)coord {
    int radius = GREY_CIRCLE_R;
    
    NSArray *placement = [self calloutBubblePlacement:coord withR:radius];
    if(placement.count >0){
        [self.map setCenterCoordinate:*coord animated:YES];
    }
    MKCircle *greyCircle= [MKCircle circleWithCenterCoordinate:*coord radius:radius];
    [greyCircle setColor:-1];
    [self.map addOverlay:greyCircle];
    
    if(calloutLines==NULL || callouts == NULL){
        calloutLines = [[NSMutableArray alloc]initWithCapacity:MAX_CALLOUTS];
        callouts = [[NSMutableArray alloc] initWithCapacity:MAX_CALLOUTS];
        
    }else{
        [self clearCallouts];
        [self.map removeOverlay:gCircle];
    }
    gCircle = greyCircle;
    
    for (NSDictionary *bubble in placement) {
        CLLocationCoordinate2D endpoints[2];
        CalloutMapAnnotation *callout = [bubble objectForKey:@"callout"];
        double corner_lat = callout.coordinate.latitude;
        double corner_lon = callout.coordinate.longitude;
        
        endpoints[0] = CLLocationCoordinate2DMake(corner_lat, corner_lon);
        endpoints[1] = ((CLLocation *)[bubble objectForKey:@"spot"]).coordinate;
        MKPolyline *calloutLine = [MKPolyline polylineWithCoordinates:endpoints count:2];
        [calloutLine setColor:-1];
        [callouts addObject:callout];
        [calloutLines addObject:calloutLine];
        
        [self.map addAnnotation:callout];
        [self.map addOverlay:calloutLine];
    }
}

-(CLLocationCoordinate2D)topLeftOfMap{
    return CLLocationCoordinate2DMake(map.centerCoordinate.latitude + map.region.span.latitudeDelta/2,
                                      map.centerCoordinate.longitude - map.region.span.longitudeDelta/2);
}

-(CLLocationCoordinate2D)botRightOfMap{
    return CLLocationCoordinate2DMake(map.centerCoordinate.latitude - map.region.span.latitudeDelta/2,
                                      map.centerCoordinate.longitude + map.region.span.longitudeDelta/2);
}

- (void)showGridLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
    [self clearMap];
    CLLocationCoordinate2D ref = CLLocationCoordinate2DMake(42.412079, -71.168098);
    MKCircle* reference_point = [MKCircle circleWithCenterCoordinate:ref radius:20];
    [self.map addOverlay:reference_point];
    //NSArray* data = [self loadGridData];
    CLLocationCoordinate2D NW = [self topLeftOfMap];
    CLLocationCoordinate2D SE = [self botRightOfMap];
    NSArray* gridList = [networkLayer callGridWithNW:&NW SE:&SE];
    for (Grid* grid in gridList) {
        //calculate actual se_point using haversine.  
        int color = [[grid status] intValue];

        CLLocationCoordinate2D nw_point = CLLocationCoordinate2DMake(grid.lat.doubleValue, grid.lon.doubleValue);
        CLLocationCoordinate2D se_point = CLLocationCoordinate2DMake(nw_point.latitude - GRID_LENGTH, nw_point.longitude + GRID_LENGTH);
        CLLocationCoordinate2D ne_point = CLLocationCoordinate2DMake(nw_point.latitude ,se_point.longitude);
        CLLocationCoordinate2D sw_point = CLLocationCoordinate2DMake(se_point.latitude ,nw_point.longitude);
        CLLocationCoordinate2D testLotCoords[5]={nw_point, ne_point, se_point, sw_point, nw_point};

        MKPolygon *commuterPoly1 = [MKPolygon polygonWithCoordinates:testLotCoords count:5];
        [commuterPoly1 setColor:color];
        [grids addObject:commuterPoly1];
    }
    [self.map addOverlays:grids];
}

- (void)showStreetLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
    [self clearMap];
    
    NSArray* data = [self loadCambridgeBlockData];
    if(streets==NULL){
        streets = [[NSMutableArray alloc] init];
    }
    
    for (id line in data) {
        //        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[2] = {[line A], [line B]};
        
        //        int i=0;
        //        for (id raw_waypoint in raw_waypoints) {
        //            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
        //            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
        //            waypoints[i++] = coordinate;
        //        }
        //        
        MKPolyline *routeLine = [MKPolyline polylineWithCoordinates:waypoints count:2];
        
        int color = [((Segment*)line) color];
        [routeLine setColor:color];
        [streets addObject:routeLine];
        
    }
    [self.map addOverlays:streets];
}


- (void)showSpotLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
    [self clearStreets];
    [self clearGrids];
    [self clearSpots];
    
    NSArray* data = [self loadSpotData];
    if(spots==NULL){
        spots = [[NSMutableArray alloc] init];
    }
    for(id spot in data){
        NSArray* point = [spot componentsSeparatedByString:@","];
        CLLocationCoordinate2D coord = CLLocationCoordinate2DMake([[point objectAtIndex:0] floatValue], [[point objectAtIndex:1] floatValue]);
        PQSpotAnnotation *annotation = [[PQSpotAnnotation alloc] initWithCoordinate:coord available:[[point objectAtIndex:2] intValue] name:[[point objectAtIndex:3] intValue]];
        [spots addObject:annotation];
    }
    [self.map addAnnotations:spots];
}

- (void)showAvailabilitySelectionView {
    self.availabilitySelectionView.hidden = NO;
    self.topSpotSelectionView.hidden = YES;
    self.bottomSpotSelectionView.hidden = YES;
}

- (void)showSpotSelectionViews {
    self.topSpotSelectionView.hidden = NO;
    self.bottomSpotSelectionView.hidden = NO;
    self.availabilitySelectionView.hidden = YES;
}

- (void)mapView:(MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated {
    oldStreetLevelRegion.center = map.centerCoordinate;    
    if (displayedData != kNoneData) {
        double currentSpan = mapView.region.span.latitudeDelta;
        CLLocationCoordinate2D center = mapView.centerCoordinate;
        if(currentSpan >= GRID_SPAN_UPPER_LIMIT){
            //above the limit, clear map.  
            zoomState = kGridZoomLevel;
            [self clearMap];
        }else if (currentSpan >= GRID_TO_STREET_SPAN_LAT) {
            //within grid level, show grids.  
            zoomState = kGridZoomLevel;
            [self showGridLevelWithCoordinates:&center];
            [self showAvailabilitySelectionView];
        }else if(currentSpan >= STREET_TO_SPOT_SPAN_LAT){
            //within street level, show streets.  
            zoomState = kStreetZoomLevel;
            [self showStreetLevelWithCoordinates:&center];
            [self showAvailabilitySelectionView];
        }else if(currentSpan < STREET_TO_SPOT_SPAN_LAT){
            if(!shouldNotClearOverlays){
                //if we're allowed to clear the overlays, remove them.  
                if(callouts.count >0){
                    //remove overlays on pan
                    [self clearCallouts]; 
                }
                [self.map removeOverlay:gCircle];
            }
            shouldNotClearOverlays = false;
            
            //below the street canyon, fall down into spot level.  
            zoomState = kSpotZoomLevel;
            [mapView setRegion:MKCoordinateRegionMake(center, MKCoordinateSpanMake(SPOT_LEVEL_SPAN, SPOT_LEVEL_SPAN)) animated:YES];
            [self showSpotLevelWithCoordinates:&center];
            [self showSpotSelectionViews];            
        }

    }
}

- (MKOverlayView *)mapView:(MKMapView *)myMapView viewForOverlay:(id<MKOverlay>)overlay {
    if ([overlay isKindOfClass:[MKPolygon class]]) {
        MKPolygon* polygon = (MKPolygon*) overlay;
        MKPolygonView *view = [[MKPolygonView alloc] initWithOverlay:polygon];
        view.lineWidth=1;
        view.strokeColor = [UIColor whiteColor];
        switch (displayedData) {
            case kAvailabilityData:
                switch (polygon.color) {
                    case 0:
                        view.fillColor = [[UIColor veryLowAvailabilityColor] colorWithAlphaComponent:0.2];
                        break;
                    case 1:
                        view.fillColor = [[UIColor lowAvailabilityColor] colorWithAlphaComponent:0.2];
                        break;
                    case 2:
                        view.fillColor = [[UIColor mediumAvailabilityColor] colorWithAlphaComponent:0.2];
                        break;
                    case 3:
                        view.fillColor = [[UIColor highAvailabilityColor] colorWithAlphaComponent:0.2];
                        break;
                    case 4:
                        view.fillColor = [[UIColor veryHighAvailabilityColor] colorWithAlphaComponent:0.2];
                        break;
                }
                break;
            case kPriceData:
                switch (polygon.color) {
                    case 0:
                        view.fillColor = [[UIColor veryHighPriceColor] colorWithAlphaComponent:0.2];
                        break;
                    case 1:
                        view.fillColor = [[UIColor highPriceColor] colorWithAlphaComponent:0.2];
                        break;
                    case 2:
                        view.fillColor = [[UIColor mediumPriceColor] colorWithAlphaComponent:0.2];
                        break;
                    case 3:
                        view.fillColor = [[UIColor lowPriceColor] colorWithAlphaComponent:0.2];
                        break;
                    case 4:
                        view.fillColor = [[UIColor veryLowPriceColor] colorWithAlphaComponent:0.2];
                        break;
                }
                break;
            case kNoneData:
                return nil;
        }
        return view;
    } else if ([overlay isKindOfClass:[MKPolyline class]]) {
        MKPolylineView *view = [[MKPolylineView alloc] initWithOverlay:overlay];
        view.lineWidth = 8;
        MKPolyline *polyline = (MKPolyline *)overlay;
        switch (displayedData) {
            case kAvailabilityData:
                switch (polyline.color) {
                    case -1:
                        view.strokeColor = [UIColor blackColor];
                        view.lineWidth = 1;
                        break;
                    case 0:
                        view.strokeColor = [UIColor veryLowAvailabilityColor];
                        break;
                    case 1:
                        view.strokeColor = [UIColor lowAvailabilityColor];
                        break;
                    case 2:
                        view.strokeColor = [UIColor mediumAvailabilityColor];
                        break;
                    case 3:
                        view.strokeColor = [UIColor highAvailabilityColor];
                        break;
                    case 4:
                        view.strokeColor = [UIColor veryHighAvailabilityColor];
                        break;
                }
                break;
            case kPriceData:
                switch (polyline.color) {
                    case -1:
                        view.strokeColor = [UIColor blackColor];
                        view.lineWidth = 1;
                        break;
                    case 0:
                        view.strokeColor = [UIColor veryHighPriceColor];
                        break;
                    case 1:
                        view.strokeColor = [UIColor highPriceColor];
                        break;
                    case 2:
                        view.strokeColor = [UIColor mediumPriceColor];
                        break;
                    case 3:
                        view.strokeColor = [UIColor lowPriceColor];
                        break;
                    case 4:
                        view.strokeColor = [UIColor veryLowPriceColor];
                        break;
                }
                break;
            case kNoneData:
                return nil;
        }
        return view;
    } else if ([overlay isKindOfClass:[MKCircle class]]) {
        MKCircleView *circleView = [[MKCircleView alloc] initWithCircle:(MKCircle *)overlay];
        // Grey selection circle
        circleView.fillColor = [[UIColor blackColor] colorWithAlphaComponent:0.3];
        circleView.strokeColor = [UIColor whiteColor];

        circleView.lineWidth = 6;
        return circleView;
    }
    return nil;
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
	if ([annotation isKindOfClass:[CalloutMapAnnotation class]]) {
        
        //reusing annotation views causes the displayed numberes to be incorrect 
        //ie.  1412's callout is 1422 or something.  temporarily removed, not too much slower.
		CalloutMapAnnotationView *calloutMapAnnotationView;// = (CalloutMapAnnotationView *)[self.map dequeueReusableAnnotationViewWithIdentifier:@"CalloutAnnotation"];
		if (!calloutMapAnnotationView) {
			calloutMapAnnotationView = [[CalloutMapAnnotationView alloc] initWithAnnotation:annotation 
                                                                            reuseIdentifier:@"CalloutAnnotation"];
            
		}
		calloutMapAnnotationView.mapView = self.map;
		return calloutMapAnnotationView;
	} else if ([annotation isKindOfClass:[PQSpotAnnotation class]]) {
        static NSString *availableSpotIdentifier = @"AvailableSpotIdentifier";
        static NSString *occupiedSpotIdentifier = @"OccupiedSpotIdentifier";
        MKAnnotationView *annotationView;
        if(((PQSpotAnnotation *)annotation).available) {
            annotationView = [self.map dequeueReusableAnnotationViewWithIdentifier:availableSpotIdentifier];
            if (!annotationView) {
                annotationView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:availableSpotIdentifier];
                annotationView.image = [UIImage imageNamed:@"spot_free.png"];
                annotationView.opaque = NO;
            }
        } else {
            annotationView = [self.map dequeueReusableAnnotationViewWithIdentifier:occupiedSpotIdentifier];
            if (!annotationView) {
                annotationView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:occupiedSpotIdentifier];
                annotationView.image = [UIImage imageNamed:@"spot_occupied.png"];
                annotationView.opaque = NO;
            }
        }
        return annotationView;
    }
    
	return nil;
}

#pragma mark - LINE PROJECTION

//given a point p, and a segment a to b, find the orthogonally projected point on the segment.
-(CLLocationCoordinate2D) getProjectedPoint: (CLLocationCoordinate2D*) p A:(CLLocationCoordinate2D*)a B:(CLLocationCoordinate2D*) b error:(bool*)err{
    double x1 = b->latitude;
    double y1 = b->longitude;
    double x2 = a->latitude;
    double y2 = a->longitude;
    double dy = y1 - y2;
    double dx = x1- x2;
    double x3 = p->latitude;
    double y3 = p->longitude;
    
    double m = dy/dx; 
    double b1 = y1 - x1*m;
    
    double new_x = (m*y3 + x3 - m*y1 + m*m*x1) / (m*m+1);
    double new_y = m*new_x + b1;
    //check validity of projected point.  
    
    if((new_y > y1 && new_y > y2) || (new_y < y1 && new_y < y2)){
        *err = true;
    }
    return CLLocationCoordinate2DMake(new_x, new_y);
}

-(CLLocationCoordinate2D) snapFromCoord:(CLLocationCoordinate2D*) coord toSegments:(NSArray*) segments{
    // each segment is CLLocationCoordinate2D points[2];
    double min_dist = 180*180;
    CLLocationCoordinate2D ret_coord;
    
    for(id line in segments){
        bool error = false;
        CLLocationCoordinate2D seg[2] = {[line A], [line B]};
        CLLocationCoordinate2D snaploc = [self getProjectedPoint:coord A:&seg[0] B:&seg[1] error:&error];
        if(error){
            continue;
        }
        double d_lat = snaploc.latitude - (*coord).latitude;
        double d_lon = snaploc.longitude - (*coord).longitude;
        double dist = d_lat*d_lat + d_lon*d_lon;
        if(dist < min_dist){
            min_dist = dist;
            ret_coord = snaploc;
        }
    }
    return ret_coord;
}

#pragma mark - GESTURE HANDLERS


-(void)handlePanGesture:(UIGestureRecognizer*)gestureRecognizer{
    if(gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;
    
    
    
}
-(void)handleDoubleTapGesture:(UIGestureRecognizer *)gestureRecognizer{

    if (gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;

    CGPoint touchPoint = [gestureRecognizer locationInView:self.map];
    CLLocationCoordinate2D coord = [self.map convertPoint:touchPoint toCoordinateFromView:self.map];
    MKMapPoint mapPoint = MKMapPointForCoordinate(coord);
    if (zoomState==kGridZoomLevel) {
        for (id gridOverlay in self.map.overlays) {
            MKPolygonView *polyView = (MKPolygonView*)[self.map viewForOverlay:gridOverlay];
            CGPoint polygonViewPoint = [polyView pointForMapPoint:mapPoint];
            
            if (CGPathContainsPoint(polyView.path, NULL, polygonViewPoint, NO)) {
                MKCoordinateRegion reg = [self.map convertRect:polyView.bounds toRegionFromView:polyView];
                //save this for when user wants to zoom out of spot level.  
                oldStreetLevelRegion = [self.map regionThatFits:reg];
                
                [self.map setRegion:oldStreetLevelRegion animated:YES];
                [self showStreetLevelWithCoordinates:&(reg.center)];
            }
        }
    } else if (zoomState==kStreetZoomLevel) { 
        MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(coord,SPOT_LEVEL_REGION_METERS ,SPOT_LEVEL_REGION_METERS);
        [self.map setRegion:[self.map regionThatFits:viewRegion] animated:YES];
        [self showSpotLevelWithCoordinates:&coord];
    } else if (zoomState==kSpotZoomLevel) {
        if(oldStreetLevelRegion.center.longitude==0){
            oldStreetLevelRegion.center = map.centerCoordinate;
        }
        if(oldStreetLevelRegion.span.longitudeDelta==0){
            oldStreetLevelRegion.span.latitudeDelta = 0.005000;
            oldStreetLevelRegion.span.longitudeDelta =  0.005000;
        }
        [self.map setRegion:[self.map regionThatFits:oldStreetLevelRegion] animated:YES];            
        [self showStreetLevelWithCoordinates:&(oldStreetLevelRegion.center)];
    }

}

- (void)handleSingleTapGesture:(UIGestureRecognizer *)gestureRecognizer
{

    if (gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;
    
    CGPoint touchPoint = [gestureRecognizer locationInView:self.map];
    CLLocationCoordinate2D coord = [self.map convertPoint:touchPoint toCoordinateFromView:self.map];
    if(zoomState == kSpotZoomLevel){
        //snap the circle to the closest polyline.
        NSArray* segList = [self loadBlockData];
        CLLocationCoordinate2D snaploc = [self snapFromCoord:&coord toSegments:segList];
        /* end snap stuff */
        if(![self tappedCalloutAtCoords:&coord]){
            //if the result returned is valid
            [self showSelectionCircle:&snaploc];
            //since you just summoned them, tell region change handler they shouldn't be cleared.  
            shouldNotClearOverlays = true;
            
        }
    }
}


#pragma mark - Search bar and UISearchBarDelegate methods

- (void)setSearchBar:(UISearchBar *)searchBar active:(BOOL)visible {
    if (visible) {
        if (zoomState == kSpotZoomLevel) {
            self.disableViewOverlay.frame = CGRectMake(0.0f,88.0f,320.0f,372.0f);
        } else {
            self.disableViewOverlay.frame = CGRectMake(0.0f,44.0f,320.0f,416.0f);
        }
        [self.view addSubview:self.disableViewOverlay];
        
        [UIView animateWithDuration:0.25 animations:^{
            self.navigationBar.leftBarButtonItem = nil;
            self.disableViewOverlay.alpha = 0.8;
            if (zoomState == kSpotZoomLevel) {
                searchBar.frame = CGRectMake(0, 0, 320, 88);
            } else {
                searchBar.frame = CGRectMake(0, 0, 320, 44);
            }
        }];
    } else {
        [searchBar resignFirstResponder];
        
        [UIView animateWithDuration:0.25 animations:^{
            self.navigationBar.leftBarButtonItem = self.leftBarButton;
            self.disableViewOverlay.alpha = 0;
            searchBar.frame = CGRectMake(45, 0, 275, 44);
        } completion:^(BOOL s){
            [self.disableViewOverlay removeFromSuperview];
        }];
    }
    [searchBar setShowsScopeBar:(visible && zoomState==kSpotZoomLevel)];
    [searchBar setShowsCancelButton:visible animated:YES];
}

- (void)handleSingleTap:(UIGestureRecognizer *)sender {
    //close search bar on tap anywhere.
    [self setSearchBar:(UISearchBar *)self.topSearchBar active:NO];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    [self clearMap];
    
    [geocoder geocodeAddressString:searchBar.text inRegion:nil completionHandler:^(NSArray *placemarks, NSError * error){
        CLLocation* locationObject = [[placemarks objectAtIndex:0] location];
        [self.map setCenterCoordinate:locationObject.coordinate animated:YES];
        //        MKCoordinateRegion viewRegion = [self.map regionThatFits:MKCoordinateRegionMakeWithDistance(locationObject.coordinate, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE)];
        //        [self.map setRegion:viewRegion animated:YES];
    }];
    
    [self setSearchBar:searchBar active:NO];
}

- (void)searchBar:(UISearchBar *)searchBar selectedScopeButtonIndexDidChange:(NSInteger)selectedScope {
    if (selectedScope == 1) {
        searchBar.keyboardType = UIKeyboardTypeNumberPad;
    } else {
        searchBar.keyboardType = UIKeyboardTypeDefault;
    }
    [searchBar resignFirstResponder];
    [searchBar becomeFirstResponder];
}

- (void)searchBarBookmarkButtonClicked:(UISearchBar *)searchBar {
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    UIViewController *vc = [storyboard instantiateViewControllerWithIdentifier:@"BookmarksController"];
    [vc setModalPresentationStyle:UIModalPresentationFullScreen];
    [self presentModalViewController:vc animated:YES];
    
}

- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar {
    [self setSearchBar:searchBar active:YES];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    [self setSearchBar:searchBar active:NO];
}

#pragma mark - TOOLBAR BUTTON ACTIONS
- (IBAction)availabilityBarTapped {
    switch (self.availabilitySelectionBar.selectedSegmentIndex) {
        case 0:
            gradientIcon.image = [UIImage imageNamed:@"gradient_avail"];
            displayedData = kAvailabilityData;
            [self mapView:map regionDidChangeAnimated:NO];
            break;
        case 1:
            gradientIcon.image = [UIImage imageNamed:@"gradient_price"];
            displayedData = kPriceData;
            [self mapView:map regionDidChangeAnimated:NO];
            break;
        case 2:
            gradientIcon.image = [UIImage imageNamed:@"gradient_none"];
            displayedData = kNoneData;
            [self clearMap];
            break;
    }
}

-(IBAction)topSpotSegControlIndexChanged:(id)sender{
    int index = self.topSpotSelectionBar.selectedSegmentIndex;
    NSString* test = [sender titleForSegmentAtIndex:index];
    NSLog(@"%s\n", test.UTF8String );
}

-(IBAction)botSpotSegControlIndexChanged:(id)sender{
    int index = self.bottomSpotSelectionBar.selectedSegmentIndex;
    NSString* test = [sender titleForSegmentAtIndex:index];
    NSLog(@"%s\n", test.UTF8String );
}

-(IBAction)settingsButtonPressed :(id)sender{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    UIViewController *vc = [storyboard instantiateViewControllerWithIdentifier:@"SettingsController"];
    [vc setModalPresentationStyle:UIModalPresentationFullScreen];
    [self presentModalViewController:vc animated:YES];
}

#pragma mark - Debug button actions

- (IBAction)gridButtonPressed:(id)sender {
    zoomState = kGridZoomLevel;
    
    //set the zoom to fit 12 grids perfectly
    CLLocationCoordinate2D point = CLLocationCoordinate2DMake(42.37369,-71.10976);
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, GRID_LEVEL_REGION_METERS, GRID_LEVEL_REGION_METERS);  
    //this is essentially zoom level in android
    
    MKCoordinateRegion adjustedRegion = [map regionThatFits:viewRegion];
    
    [self.map setRegion:adjustedRegion animated:YES];
    //DEBUG BELOW
    [self showGridLevelWithCoordinates:&point];
    [self showAvailabilitySelectionView];
}

- (IBAction)streetButtonPressed:(id)sender {
    CLLocationCoordinate2D point = {42.37369,-71.10976};
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, STREET_LEVEL_REGION_METERS, STREET_LEVEL_REGION_METERS);
    [self.map setRegion:[self.map regionThatFits:viewRegion] animated:YES];
    [self showStreetLevelWithCoordinates:&point];
    [self showAvailabilitySelectionView];
}

- (IBAction)spotButtonPressed:(id)sender {
    CLLocationCoordinate2D point = {42.37369,-71.10976};
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, SPOT_LEVEL_REGION_METERS, SPOT_LEVEL_REGION_METERS);
    [map setRegion:[map regionThatFits:viewRegion] animated:YES];
    [self showSpotLevelWithCoordinates:&point];
    [self showAvailabilitySelectionView];
    
}


- (IBAction)noneButtonPressed:(id)sender {
//    CLLocationCoordinate2D topleft = [self topLeftOfMap];
//    CLLocationCoordinate2D botright = [self botRightOfMap];
//    [networkLayer getGridLevelMicroBlockIdListWithNW:&topleft SE:&botright];

    NSLog(@"dlat %f dlon %f\n", map.region.span.latitudeDelta, map.region.span.longitudeDelta);
    
}


- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {   
    return YES;
}

#pragma mark - LOCATION
- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation{
    user_loc = newLocation.coordinate;
    if (MAX(newLocation.horizontalAccuracy, newLocation.verticalAccuracy) < ACCURACY_LIMIT) {
        if (!user_loc_isGood) {
            manager.distanceFilter = 30.0;
            MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(newLocation.coordinate, SPOT_LEVEL_REGION_METERS, SPOT_LEVEL_REGION_METERS);
            [self.map setRegion:[map regionThatFits:viewRegion] animated:YES];

            user_loc_isGood = true;
        }
    }
}

#pragma mark - Memory

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad{
    
    [super viewDidLoad];
    if(!dataLayer){
        //prepare the data layer for fetching from core data
        dataLayer = [((PQAppDelegate*)[[UIApplication sharedApplication] delegate]) getDataLayer];
    }
    if(!networkLayer){
        networkLayer = [((PQAppDelegate*)[[UIApplication sharedApplication] delegate])  getNetworkLayerWithDataLayer:dataLayer];
    }
    
    grids = [[NSMutableArray alloc] init];
    spots = [[NSMutableArray alloc] init];
    streets = [[NSMutableArray alloc] init];
    
    //check the current zoom level to set the ZOOM_STATE integer.  
    if (self.map.bounds.size.width >= GRID_TO_STREET_SPAN_LAT) {
        zoomState = kGridZoomLevel;
        //above middle zoom level        
    } else if (self.map.bounds.size.width >= STREET_TO_SPOT_SPAN_LAT) {
        //above spot zoom level
        zoomState = kStreetZoomLevel;
    } else {
        //inside spot zoom level.  
        zoomState = kSpotZoomLevel;
    }
    self.disableViewOverlay = [[UIView alloc]
                               initWithFrame:CGRectMake(0.0f,88.0f,320.0f,372.0f)];
    self.disableViewOverlay.backgroundColor=[UIColor blackColor];
    self.disableViewOverlay.alpha = 0;
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [self.disableViewOverlay addGestureRecognizer:singleTap];
    
    
    ((UISearchBar *)self.topSearchBar).scopeButtonTitles = [[NSArray alloc] initWithObjects:@"Place name", @"Spot number", nil];
    
    self.leftBarButton = self.navigationBar.leftBarButtonItem;
    
    [self.topSpotSelectionBar setWidth:36 forSegmentAtIndex:0];
    [self.topSpotSelectionBar setWidth:36 forSegmentAtIndex:5];
    [self.bottomSpotSelectionBar setWidth:36 forSegmentAtIndex:0];
    [self.bottomSpotSelectionBar setWidth:36 forSegmentAtIndex:5];
    
    // Do any additional setup after loading the view, typically from a nib.
    self.map.delegate=self;
    self.map.showsUserLocation = YES;
    //setup gesture recognizer for grids and blocks and spots.  
    UITapGestureRecognizer *doubleTapRecognizer = [[UITapGestureRecognizer alloc]
                                   initWithTarget:self action:@selector(handleDoubleTapGesture:)];
    UITapGestureRecognizer * singleTapRecognizer = [[UITapGestureRecognizer alloc]
                                                    initWithTarget:self action:@selector(handleSingleTapGesture:)];

    [doubleTapRecognizer setNumberOfTapsRequired:2];
    [singleTapRecognizer setNumberOfTapsRequired:1];
    
    [singleTapRecognizer requireGestureRecognizerToFail:doubleTapRecognizer];

    [self.map addGestureRecognizer:singleTapRecognizer];
    [self.map addGestureRecognizer:doubleTapRecognizer];
    
    //add a pan gesture to map on TOP of already existing one.  
    UIPanGestureRecognizer* pgr = [[UIPanGestureRecognizer alloc]
                                   initWithTarget:self action:@selector(handlePanGesture:)];
    pgr.delegate = self;
    //[self.map addGestureRecognizer:pgr];
}


- (void)viewDidUnload
{
    [self setMap:nil];
    [self setNavigationBar:nil];
    [self setTopSearchBar:nil];
    [self setBottomSpotSelectionBar:nil];
    [self setTopSpotSelectionBar:nil];
    [self setAvailabilitySelectionView:nil];
    [self setBottomSpotSelectionView:nil];
    [self setTopSpotSelectionView:nil];
    [self setGradientIcon:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];

    locationManager=[[CLLocationManager alloc] init];
    locationManager.delegate=self;
    locationManager.desiredAccuracy=kCLLocationAccuracyNearestTenMeters;

    [locationManager startUpdatingLocation];
    user_loc_isGood = false;
}

- (void)viewDidAppear:(BOOL)animated
{
    
    [super viewDidAppear:animated];
    //prepare geocoder upon view load.  
    if(geocoder ==nil){
        geocoder = [[CLGeocoder alloc] init];
    }
    
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
    
    [locationManager stopUpdatingLocation];
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

@end

#pragma mark - IDEA DUMP
////this overwrites the current grid colors, doesn't 
//for( MKPolygon* overlay in grids){
//    MKPolygonView* view = (MKPolygonView*) [map viewForOverlay:overlay];
//    view.fillColor = [[UIColor veryLowAvailabilityColor] colorWithAlphaComponent:0.2];
//    //we can edit already existing overlays, so NO need to remove them.  
//    
//    //maintain a hash map of ID to OVERLAY object, server responds with ID's, find and modify the color.  EZ.
//}
