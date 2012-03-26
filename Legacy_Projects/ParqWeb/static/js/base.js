PARQ = {
    'insert_data' : function(url,elem_id) {
        $.ajax({
                'type':'GET',
                'url':url,
                'success': function(data,textStatus,jqXHR) {		    
                    if (data!=="BAD") {
                        $("#"+elem_id).html(data);
			$("#" + elem_id + " form").each(function() {
				PARQ.ajaxify_form(this);
			    });			
                    } else {
                        alert("Your last action didn't work. Sorry");
                    }
		},
		'error': function(data,textStatus,jqXHR) {
		    $('#modal').modal('hide');
		    $('#message').html(data);
                },
                'dataType':'html'
            });
    },
    'ajaxify_form' : function(formElem) {
	$(formElem).submit(function(e) {
		e.preventDefault();
		var data = $(formElem).serialize();
		var action = $(formElem).attr('action');
		var method = $(formElem).attr('method');
		$.ajax({'type':method,
			    'url':action,
			    'cache':false,
			    'data':data,
			    'success': function(data,textStatus,jqXHR) {
			       $("#modal").html(data);
			       setTimeout('window.location.reload()',500);
			    },
			    'error': function(jqXHR,textStatus,errorThrown) {
				$("#modal").html(jqXHR.responseText); 	     
				$("#modal form").each(function() {
					PARQ.ajaxify_form(this);
				    });			
			    },
			    'dataType':'html'
		      });		
	    });			
    },
    'set_map_height': function() {
	$('#map_canvas').css('height',$(window).height()*.45);
    },
    'init' :function() {
	PARQ.set_map_height();
        $('.loginButton').click(function() {
                PARQ.insert_data("/actions/login/",'modal');
            });
        $('.changePassword').click(function() {
                PARQ.insert_data("/actions/change_password/",'modal');
            });
        $('.addPayment').click(function() {
                PARQ.insert_data("/actions/add_payment_account/",'modal');
            });
        $('.signupButton').click(function() {
                PARQ.insert_data("/actions/sign_up/",'modal');
            });

        // $('#query').autocomplete({
        //         'serviceUrl':'/ajax/search/type/'
	// 	    });
    }

}
    $(document).ready(PARQ.init);
