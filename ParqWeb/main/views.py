from django.http import HttpResponse
from django.shortcuts import render_to_response
from django.template import RequestContext

from parqweb.main.models import Parkinglocation
from parqweb.main.forms import SearchParqForm

def home(request):
    form = SearchParqForm()
    locations = Parkinglocation.objects.all()
    return render_to_response("main.html",{'form':form,'locations':locations},RequestContext(request))

def about(request):
    return render_to_response("about.html",{},RequestContext(request))

def faq(request):
    return render_to_response("faq.html",{},RequestContext(request))

def features(request):
    return render_to_response("features.html",{},RequestContext(request))
