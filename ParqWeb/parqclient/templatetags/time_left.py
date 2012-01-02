from django import template
from datetime import datetime

register = template.Library()

def time_left(value):
    try:
        output = ""
        left = value-datetime.now()
        days = left.days
        hours = left.seconds/3600
        minutes = left.seconds/minutes
        if days:
            output+="%s days" % days
        if hours:
            output+=",%sh " % hours
        output+="%sm ago" % minutes 
        return output
    except:
        return None
    
register.filter('time_left',time_left)
