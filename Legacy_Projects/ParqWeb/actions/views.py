from django.core.exceptions import ObjectDoesNotExist, MultipleObjectsReturned
from django.http import HttpResponse, HttpResponseRedirect, HttpResponseBadRequest,HttpResponseForbidden,Http404
from django.shortcuts import render_to_response
from django.template import RequestContext

from django.contrib.auth import login as auth_login, logout as auth_logout
from django.contrib.auth.decorators import login_required

from parqweb.main.models import *
from parqweb.actions.forms import UserCreationForm, AddPaymentForm, PaymentAccountForm,AuthenticationForm, PasswordChangeForm
from parqweb.actions.constants import *
from parqweb.parqclient.decorators import admin_required

from datetime import datetime
import json

@login_required
def logout(request):
    auth_logout(request)
    return HttpResponseRedirect("/")

def login(request):
    context = {'form_button':"Login",
              'form_action':"/actions/login/",
              'form_title':"Login"}
    if request.method == "POST":
        form = AuthenticationForm(data=request.POST)
        if form.is_valid():
            auth_login(request, form.get_user())
            if request.session.test_cookie_worked():
                request.session.delete_test_cookie()
            return render_to_response("modal_message.html",
                                      LOGIN_SUCCESS_CONTEXT,
                                      RequestContext(request))
        else:
            context.update({'form':form})
            response = render_to_response("modal_form.html",context,RequestContext(request))
            response['status']=400
            return response
    else:
        request.session.set_test_cookie()
        form = AuthenticationForm()
        context.update({'form':form})
        return render_to_response("modal_form.html",context,RequestContext(request))

'''This is for non ajax requests'''
def login_page(request):
    context = {'form_button':"Login",
              'form_action':"/actions/login/",
              'form_title':"Login"}
    if request.method == "POST":
        form = AuthenticationForm(data=request.POST)
        if form.is_valid():
            auth_login(request, form.get_user())
            if request.session.test_cookie_worked():
                request.session.delete_test_cookie()
            return HttpResponseRedirect("/")
        else:
            context.update({'form':form})
            return render_to_response("form.html",context,RequestContext(request))
    else:
        request.session.set_test_cookie()
        form = AuthenticationForm()
        context.update({'form':form})
        return render_to_response("form.html",context,RequestContext(request))

@login_required
def change_password(request):
    context = {'form_button':'Change Password',
               'form_action':'/actions/change_password/',
               'form_title':'Change Password'}
    if request.method=="POST":
        form = PasswordChangeForm(request.user,request.POST)
        if form.is_valid():
            form.save()
            return render_to_response("modal_message.html",
                                      CHANGE_PASSWORD_SUCCESS_CONTEXT,
                                      RequestContext(request))
        else:
            context.update({'form':form})
            response = render_to_response("modal_form.html",context,RequestContext(request))
            response['status'] = 400
            return response
    else:        
        form = PasswordChangeForm(request.user)
        context.update({'form':form})
        return render_to_response("modal_form.html",context,RequestContext(request))

def sign_up(request):
    context = {'form_button':'Sign Up',
               'form_action':'/actions/sign_up/',
               'form_title':'Sign up'}
    if request.method=="POST":
        form = UserCreationForm(request.POST)
        if form.is_valid():
            user = form.save()
            
            parq_user = User(password=request.POST['password1'],
                             user_id = user.id,
                             email = request.POST['username'],
                             django_user = user,                           
                             lastupdatedatetime =  datetime.now(),
                             )
            parq_user.save()
            return render_to_response("modal_message.html",
                                      SIGN_UP_SUCCESS_CONTEXT,
                                      RequestContext(request))
        else:
            context.update({'form':form})            
            response = render_to_response("modal_form.html",context,RequestContext(request))
            response['status'] = 400
            return response
    else:
        form = UserCreationForm()
        context.update({'form':form})
        return render_to_response("modal_form.html",context,RequestContext(request))

    
@login_required
def add_payment_account(request):
    if request.method=="POST":
        form = AddPaymentForm(request.user,request.POST)
        if form.is_valid():
            pa = form.save()
            if not pa:
                return render_to_response("modal_message.html",ADD_PAYMENT_FAILURE_CONTEXT,RequestContext(request))
            return render_to_response("modal_message.html",ADD_PAYMENT_SUCCESS_CONTEXT,RequestContext(request))
        else:
            response = render_to_response("add_payment_form.html",{'form':form},RequestContext(request))
            response['status'] = 400
            return response
    else:
        form = AddPaymentForm(request.user,initial={"month_expiration":"mm","year_expiration":"yyyy"})
        return render_to_response("add_payment_form.html",{'form':form},RequestContext(request))

@login_required
def delete_payment_account(request):
    aid = request.GET.get('aid')
    if not aid:
        return HttpResponseBadRequest("No Account ID Specified")
    try:
        account = Paymentaccount.objects.get(account_id=aid)

        #only let user delete their own payment accounts
        if account.user != request.user.parq_user:
            raise Http404
        account.is_deleted = True
        account.save()
        return HttpResponseRedirect("/user/account/")
    except ObjectDoesNotExist:
        raise Http404
    except MultipleObjectsReturned:
        return HttpResponseBadRequest("Multiple Objects Returned. Sorry!")
    
                                      
@login_required
def set_default_payment_account(request):
    aid = request.GET.get("aid")
    if not aid:
        return HttpResponseBadRequest("No Account ID Specified")        
    try:
        account = Paymentaccount.objects.get(account_id=aid)
        if account.user != request.user.parq_user:
            raise Http404
        for acc in Paymentaccount.objects.filter(user=request.user.parq_user):
            acc.is_default_payment = False
            acc.save()
        account.is_default_payment = True
        account.save()
        return HttpResponseRedirect("/user/account/")
    except ObjectDoesNotExist:
        raise Http404

def autocomplete_location(request):
    query = request.GET.get("query")
    if not query:
        return HttpResponseBadRequest("Invalid Query")

    clean_query = query.lower()

    suggestions = list(Parkinglocation.objects.filter(location_name__istartswith=clean_query).values_list("location_name",flat=True))
    result = {'suggestions':suggestions,'query':query}
    return HttpResponse(json.dumps(result))
    
    
@admin_required
def filter_spaces(request):
    occupied = request.GET.get("occupied")
    client_id = request.GET.get("client_id")
    if not client_id or not occupied:
        return HttpResponseBadRequest("Parameters missing")
    try:
        client= Client.objects.get(client_id=client_id)
        if client not in Client.objects.filter(admin_relationships__admin=request.user.admin_user):
            return HttpResponseForbidden("Cannot access client")
        result = Parkingspace.objects.filter(location__client=client)
        if occupied=="true":            
            result = [space for space in result if space.is_occupied()]
        elif occupied=="false":
            result=  [space for space in result if not space.is_occupied()]
        result = [space.space_name for space in result]
        return HttpResponse(json.dumps(list(result)))
    except ObjectDoesNotExist:
        return HttpResponseBadRequest("Error. Client not found")
    except MultipleObjectsReturned:
        return HttpResponseBadRequest("Error. More than one client with id %s" % client_id)
    
        
