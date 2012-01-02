from django.core.exceptions import ObjectDoesNotExist,MultipleObjectsReturned
from django.http import HttpResponseBadRequest
from django.shortcuts import render_to_response
from django.template import RequestContext
from django.contrib.auth.decorators import login_required

from parqweb.main.models import *


@login_required
def account(request):
	user = User.objects.get(user_id=request.user.parq_user.user_id)
	parking_history = Parkinginstance.objects.filter(user=request.user).order_by("-park_began_time")
	payments = Payment.objects.filter(account__user=user)
	accounts = user.accounts.all()
        return render_to_response("account.html",{'parq_user':user,
                                                  'parking_history':parking_history,
                                                  'accounts':accounts},
                                  RequestContext(request))

	

@login_required
def renew(request):
	return render_to_response("renew.html",{},RequestContext(request))

def view_location(request):
	location_name = request.GET.get("location")
	if not location_name:
		return HttpResponseBadRequest()
	try:
		location = Parkinglocation.objects.get(location_name=location_name)
		return render_to_response("view_location.html",{'location':location},RequestContext(request))
	except ObjectDoesNotExist:
		return HttpResponseBadRequest("Location %s does not exist in our database" % location_name)
	except MultipleObjectsReturned:
		return HttpResponseBadRequest("Location %s could not be resolved" % location_name)
