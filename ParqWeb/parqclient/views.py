from django.http import HttpResponse, HttpResponseBadRequest
from django.template import RequestContext
from django.shortcuts import render_to_response
from django.core.exceptions import ObjectDoesNotExist

from parqweb.parqclient.decorators import admin_required
from parqweb.main.models import *

from datetime import datetime

@admin_required
def account(request):
    admin_user = request.user.admin_user
    clients = Client.objects.filter(admin_relationships__admin=admin_user)
    parking_spaces_by_client = {}
    for c in clients:
        parking_spaces_by_client[c] = Parkingspace.objects.filter(location__client=c)

    parking_reports = Payment.objects.filter(parkinginst__space__location__client__in=clients).order_by("-payment_datetime")
    current_activity = Parkinginstance.objects.filter(park_end_time__gt=datetime.now())

    return render_to_response("client_account.html",{'admin_user':admin_user,
                                                'parking_spaces_by_client':parking_spaces_by_client,
                                                'parking_reports':parking_reports,
                                                'current_activity':current_activity},
                              RequestContext(request))

@admin_required
def space_activity(request):
    sid = request.GET.get('sid')
    if not sid:
        raise Http404
    try:
        space = Parkingspace.objects.get(space_id=sid)
        if not Adminclientrelationship.objects.filter(
            admin=request.user.admin_user,
            client=space.location.client).exists():
            return HttpResponseBadRequest("You do not have permissions to view this parking space")
        parking_history = Parkinginstance.objects.filter(space=space)
        return render_to_response("space_activity.html",
                                  {'space':space,
                                   'parking_history':parking_history},
                                  RequestContext(request))
    except ObjectDoesNotExist:
        return HttpResponseBadRequest("Space with id %s not found" % sid)
    

