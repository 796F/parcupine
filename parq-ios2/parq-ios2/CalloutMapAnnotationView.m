#import "CalloutMapAnnotationView.h"
#import "CalloutMapAnnotation.h"
#import <CoreGraphics/CoreGraphics.h>
#import <QuartzCore/QuartzCore.h>

#define CalloutMapAnnotationViewBottomShadowBufferSize 6.0f
#define CalloutMapAnnotationViewContentHeightBuffer 8.0f

@interface CalloutMapAnnotationView()

@property (nonatomic, readonly) CGFloat yShadowOffset;
@property (nonatomic) BOOL animateOnNextDrawRect;
@property (nonatomic) CGRect endFrame;

- (void)prepareContentFrame;
- (void)prepareFrameSize;
- (void)prepareOffset;

@end


@implementation CalloutMapAnnotationView

@synthesize mapView = _mapView;
@synthesize contentView = _contentView;
@synthesize animateOnNextDrawRect = _animateOnNextDrawRect;
@synthesize endFrame = _endFrame;
@synthesize yShadowOffset = _yShadowOffset;
@synthesize contentHeight = _contentHeight;

- (id) initWithAnnotation:(id <MKAnnotation>)annotation reuseIdentifier:(NSString *)reuseIdentifier {
	if (self = [super initWithAnnotation:annotation reuseIdentifier:reuseIdentifier]) {
        self.contentHeight = 22.0f;
        self.backgroundColor = [UIColor clearColor];
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(5, 2, 32, 17)];
        label.text = annotation.title;
        label.textColor = [UIColor whiteColor];
        label.textAlignment = UITextAlignmentCenter;
        label.font = [UIFont boldSystemFontOfSize:12];
        label.backgroundColor = [UIColor clearColor];
        label.shadowColor = [UIColor blackColor];
        [self.contentView addSubview:label];
	}
	return self;
}

- (void)setAnnotation:(id <MKAnnotation>)annotation {
	[super setAnnotation:annotation];
	[self prepareFrameSize];
	[self prepareOffset];
	[self prepareContentFrame];
	[self setNeedsDisplay];
}

- (void)prepareFrameSize {
	CGRect frame = self.frame;
	CGFloat height =	self.contentHeight +
	CalloutMapAnnotationViewContentHeightBuffer +
	CalloutMapAnnotationViewBottomShadowBufferSize;
	
    frame.size = CGSizeMake(62, height);
	self.frame = frame;
}

- (void)prepareContentFrame {
	CGRect contentFrame = CGRectMake(self.bounds.origin.x + 10, 
									 self.bounds.origin.y + 3, 
									 self.bounds.size.width - 20, 
									 self.contentHeight);

	self.contentView.frame = contentFrame;
}

- (void)prepareOffset {
    switch(((CalloutMapAnnotation *)self.annotation).corner) {
        case kTopRightCorner:
            self.centerOffset = CGPointMake(-self.bounds.size.width/2+8.0, self.bounds.size.height/2);
            break;
        case kTopLeftCorner:
            self.centerOffset = CGPointMake(self.bounds.size.width/2-7.0, self.bounds.size.height/2);
            break;
        case kBottomLeftCorner:
            self.centerOffset = CGPointMake(self.bounds.size.width/2-7.0, -self.bounds.size.height/2+7.0);
            break;
        case kBottomRightCorner:
            self.centerOffset = CGPointMake(-self.bounds.size.width/2+8.0, -self.bounds.size.height/2+7.0);
            break;
    }
}

- (CGFloat)xTransformForScale:(CGFloat)scale {
	CGFloat xDistanceFromCenter = self.endFrame.size.width / 2;
    const CalloutCorner corner = ((CalloutMapAnnotation *)self.annotation).corner;
    if (corner == kTopRightCorner || corner == kBottomRightCorner) {
        xDistanceFromCenter = -xDistanceFromCenter;
    }
	return (xDistanceFromCenter * scale) - xDistanceFromCenter;
}

- (CGFloat)yTransformForScale:(CGFloat)scale {
	CGFloat yDistanceFromCenter = (((self.endFrame.size.height) / 2) + CalloutMapAnnotationViewBottomShadowBufferSize);
    const CalloutCorner corner = ((CalloutMapAnnotation *)self.annotation).corner;
    if (corner == kTopLeftCorner || corner == kTopRightCorner) {
        yDistanceFromCenter = -yDistanceFromCenter;
    }
	return yDistanceFromCenter - yDistanceFromCenter * scale;
}

- (void)animateIn {
	self.endFrame = self.frame;
	CGFloat scale = 0.001f;
	self.transform = CGAffineTransformMake(scale, 0.0f, 0.0f, scale, [self xTransformForScale:scale], [self yTransformForScale:scale]);
	[UIView beginAnimations:@"animateIn" context:nil];
	[UIView setAnimationCurve:UIViewAnimationCurveEaseOut];
	[UIView setAnimationDuration:0.075];
	[UIView setAnimationDidStopSelector:@selector(animateInStepTwo)];
	[UIView setAnimationDelegate:self];
	scale = 1.1;
	self.transform = CGAffineTransformMake(scale, 0.0f, 0.0f, scale, [self xTransformForScale:scale], [self yTransformForScale:scale]);
	[UIView commitAnimations];
}

- (void)animateInStepTwo {
	[UIView beginAnimations:@"animateInStepTwo" context:nil];
	[UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];
	[UIView setAnimationDuration:0.1];
	[UIView setAnimationDidStopSelector:@selector(animateInStepThree)];
	[UIView setAnimationDelegate:self];
	
	CGFloat scale = 0.95;
	self.transform = CGAffineTransformMake(scale, 0.0f, 0.0f, scale, [self xTransformForScale:scale], [self yTransformForScale:scale]);
	
	[UIView commitAnimations];
}

- (void)animateInStepThree {
	[UIView beginAnimations:@"animateInStepThree" context:nil];
	[UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];
	[UIView setAnimationDuration:0.075];
	
	CGFloat scale = 1.0;
	self.transform = CGAffineTransformMake(scale, 0.0f, 0.0f, scale, [self xTransformForScale:scale], [self yTransformForScale:scale]);
	
	[UIView commitAnimations];
}

- (void)didMoveToSuperview {
	[self animateIn];
}

- (void)drawRect:(CGRect)rect {
	CGFloat stroke = 1.0;
	CGFloat radius = 7.0;
	CGMutablePathRef path = CGPathCreateMutable();
	UIColor *color;
	CGColorSpaceRef space = CGColorSpaceCreateDeviceRGB();
	CGContextRef context = UIGraphicsGetCurrentContext();
	
	//Determine Size
	rect = self.bounds;
	rect.size.width -= stroke + 14;
	rect.size.height -= stroke + CalloutMapAnnotationViewBottomShadowBufferSize;
	rect.origin.x += stroke / 2.0 + 7;
	rect.origin.y += stroke / 2.0;
	
	//Create Path For Callout Bubble
    const CalloutCorner corner = ((CalloutMapAnnotation *)self.annotation).corner;
    if (corner != kTopLeftCorner) {
        CGPathMoveToPoint(path, NULL, rect.origin.x, rect.origin.y + radius);
    } else {
        CGPathMoveToPoint(path, NULL, rect.origin.x, rect.origin.y);
    }
    if (corner != kBottomLeftCorner) {
        CGPathAddLineToPoint(path, NULL, rect.origin.x, rect.origin.y + rect.size.height - radius);
        CGPathAddArc(path, NULL, rect.origin.x + radius, rect.origin.y + rect.size.height - radius, 
                     radius, M_PI, M_PI / 2, 1);
    } else {
        CGPathAddLineToPoint(path, NULL, rect.origin.x, rect.origin.y + rect.size.height);
    }
    if (corner != kBottomRightCorner) {
        CGPathAddLineToPoint(path, NULL, rect.origin.x + rect.size.width - radius, 
                             rect.origin.y + rect.size.height);
        CGPathAddArc(path, NULL, rect.origin.x + rect.size.width - radius, 
                     rect.origin.y + rect.size.height - radius, radius, M_PI / 2, 0.0f, 1);
    } else {
        CGPathAddLineToPoint(path, NULL, rect.origin.x + rect.size.width, 
                             rect.origin.y + rect.size.height);
    }
    if (corner != kTopRightCorner) {
        CGPathAddLineToPoint(path, NULL, rect.origin.x + rect.size.width, rect.origin.y + radius);
        CGPathAddArc(path, NULL, rect.origin.x + rect.size.width - radius, rect.origin.y + radius, 
                     radius, 0.0f, -M_PI / 2, 1);
    } else {
        CGPathAddLineToPoint(path, NULL, rect.origin.x + rect.size.width, rect.origin.y);
    }
    if (corner != kTopLeftCorner) {
        CGPathAddLineToPoint(path, NULL, rect.origin.x + radius, rect.origin.y);
        CGPathAddArc(path, NULL, rect.origin.x + radius, rect.origin.y + radius, radius, 
                     -M_PI / 2, M_PI, 1);
    } else {
        CGPathAddLineToPoint(path, NULL, rect.origin.x, rect.origin.y);
    }
	CGPathCloseSubpath(path);
	
	//Fill Callout Bubble & Add Shadow
	color = [[UIColor blackColor] colorWithAlphaComponent:.6];
	[color setFill];
	CGContextAddPath(context, path);
	CGContextSaveGState(context);
	CGContextSetShadowWithColor(context, CGSizeMake (0, self.yShadowOffset), 6, [UIColor colorWithWhite:0 alpha:.5].CGColor);
	CGContextFillPath(context);
	CGContextRestoreGState(context);
	
	//Stroke Callout Bubble
	color = [[UIColor darkGrayColor] colorWithAlphaComponent:.9];
	[color setStroke];
	CGContextSetLineWidth(context, stroke);
	CGContextSetLineCap(context, kCGLineCapSquare);
	CGContextAddPath(context, path);
	CGContextStrokePath(context);
	
	//Determine Size for Gloss
	CGRect glossRect = self.bounds;
	glossRect.size.width = rect.size.width - stroke;
	glossRect.size.height = (rect.size.height - stroke) / 2;
	glossRect.origin.x = rect.origin.x + stroke / 2;
	glossRect.origin.y += rect.origin.y + stroke / 2;
	
	CGFloat glossTopRadius = radius - stroke / 2;
	CGFloat glossBottomRadius = radius / 1.5;
	
	//Create Path For Gloss
	CGMutablePathRef glossPath = CGPathCreateMutable();
    if (corner != kTopLeftCorner) {
        CGPathMoveToPoint(glossPath, NULL, glossRect.origin.x, glossRect.origin.y + glossTopRadius);
    } else {
        CGPathMoveToPoint(glossPath, NULL, glossRect.origin.x, glossRect.origin.y);
    }
    if (corner != kBottomLeftCorner) {
        CGPathAddLineToPoint(glossPath, NULL, glossRect.origin.x, glossRect.origin.y + glossRect.size.height - glossBottomRadius);
        CGPathAddArc(glossPath, NULL, glossRect.origin.x + glossBottomRadius, glossRect.origin.y + glossRect.size.height - glossBottomRadius, 
                     glossBottomRadius, M_PI, M_PI / 2, 1);
    } else {
        CGPathAddLineToPoint(glossPath, NULL, glossRect.origin.x, glossRect.origin.y + glossRect.size.height);
    }
    if (corner != kBottomRightCorner) {
        CGPathAddLineToPoint(glossPath, NULL, glossRect.origin.x + glossRect.size.width - glossBottomRadius, 
                             glossRect.origin.y + glossRect.size.height);
        CGPathAddArc(glossPath, NULL, glossRect.origin.x + glossRect.size.width - glossBottomRadius, 
                     glossRect.origin.y + glossRect.size.height - glossBottomRadius, glossBottomRadius, M_PI / 2, 0.0f, 1);
    } else {
        CGPathAddLineToPoint(glossPath, NULL, glossRect.origin.x + glossRect.size.width, 
                             glossRect.origin.y + glossRect.size.height);
    }
    if (corner != kTopRightCorner) {
        CGPathAddLineToPoint(glossPath, NULL, glossRect.origin.x + glossRect.size.width, glossRect.origin.y + glossTopRadius);
        CGPathAddArc(glossPath, NULL, glossRect.origin.x + glossRect.size.width - glossTopRadius, glossRect.origin.y + glossTopRadius, 
                     glossTopRadius, 0.0f, -M_PI / 2, 1);
    } else {
        CGPathAddLineToPoint(glossPath, NULL, glossRect.origin.x + glossRect.size.width, glossRect.origin.y);
    }
    if (corner != kTopLeftCorner) {
        CGPathAddLineToPoint(glossPath, NULL, glossRect.origin.x + glossTopRadius, glossRect.origin.y);
        CGPathAddArc(glossPath, NULL, glossRect.origin.x + glossTopRadius, glossRect.origin.y + glossTopRadius, glossTopRadius, 
                     -M_PI / 2, M_PI, 1);
    } else {
        CGPathAddLineToPoint(glossPath, NULL, glossRect.origin.x, glossRect.origin.y);
    }
	CGPathCloseSubpath(glossPath);
	
	//Fill Gloss Path	
	CGContextAddPath(context, glossPath);
	CGContextClip(context);
	CGFloat colors[] =
	{
		1, 1, 1, .3,
		1, 1, 1, .1,
	};
	CGFloat locations[] = { 0, 1.0 };
	CGGradientRef gradient = CGGradientCreateWithColorComponents(space, colors, locations, 2);
	CGPoint startPoint = glossRect.origin;
	CGPoint endPoint = CGPointMake(glossRect.origin.x, glossRect.origin.y + glossRect.size.height);
	CGContextDrawLinearGradient(context, gradient, startPoint, endPoint, 0);
	
	//Gradient Stroke Gloss Path	
	CGContextAddPath(context, glossPath);
	CGContextSetLineWidth(context, 2);
	CGContextReplacePathWithStrokedPath(context);
	CGContextClip(context);
	CGFloat colors2[] =
	{
		1, 1, 1, .3,
		1, 1, 1, .1,
		1, 1, 1, .0,
	};
	CGFloat locations2[] = { 0, .1, 1.0 };
	CGGradientRef gradient2 = CGGradientCreateWithColorComponents(space, colors2, locations2, 3);
	CGPoint startPoint2 = glossRect.origin;
	CGPoint endPoint2 = CGPointMake(glossRect.origin.x, glossRect.origin.y + glossRect.size.height);
	CGContextDrawLinearGradient(context, gradient2, startPoint2, endPoint2, 0);
	
	//Cleanup
	CGPathRelease(path);
	CGPathRelease(glossPath);
	CGColorSpaceRelease(space);
	CGGradientRelease(gradient);
	CGGradientRelease(gradient2);
}

- (CGFloat)yShadowOffset {
	if (!_yShadowOffset) {
		float osVersion = [[[UIDevice currentDevice] systemVersion] floatValue];
		if (osVersion >= 3.2) {
			_yShadowOffset = 6;
		} else {
			_yShadowOffset = -6;
		}
		
	}
	return _yShadowOffset;
}

- (UIView *)contentView {
	if (!_contentView) {
		_contentView = [[UIView alloc] init];
		self.contentView.backgroundColor = [UIColor clearColor];
		self.contentView.autoresizingMask = UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;
		[self addSubview:self.contentView];
	}
	return _contentView;
}

@end