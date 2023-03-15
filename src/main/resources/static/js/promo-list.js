// Efeito infinit scroll

var pageNumber = 0;

$(document).ready(function() {
	$("#loader-img").hide();
	$("#fim-btn").hide();
});


$(window).scroll(function() {
	var scrollTop = $(this).scrollTop();
	var conteudo = $(document).height() - $(window).height();
	
	//console.log('scrollTop: ', scrollTop, ' | ', 'Conteudo', conteudo);
	
	if(scrollTop >= conteudo) {
		pageNumber++;
		
		setTimeout(function() {
			loadByScrollBar(pageNumber);
		}, 200);
	}
});


function loadByScrollBar(PageNumber) {
	
	var site = $("#autocomplete-input").val();
	
	$.ajax({
		method: "GET",
		url: "/promocao/list/ajax",
		data: {
			page : PageNumber,
			site: site
		},
		beforeSend:function() {
			$("#loader-img").show();
		},
		success: function(response) {
			//console.log("Resposta > ", response);
			
			if(response.length > 150) {
				$(".row").fadeIn(250, function() {
					$(this).append(response);
				})
			} else {
				$("#fim-btn").show();
				$("#loader-img").removeClass("loader");
			}
			
			
		},
		error:function(xhr) {
			alert("Ops, ocorreu um erro: " + xhr.statud + " - " + xhr.statusText);
		},
		complete: function() {
			$("#loader-img").hide();
		}
	});
	
}

// Autocomplete
$("#autocomplete-input").autocomplete({
	source: function(request, response) {
		$.ajax({
			method: "GET",
			url: "/promocao/site",
			data: {
				termo: request.term
			},
			success: function(result) {
				response(result);
			}
		});
	}
});


//Pesquisa do site
$("#autocomplete-submit").on("click", function() {
	
	var site = $("#autocomplete-input").val();
	
	$.ajax({
		method: "GET",
		url: "/promocao/site/list",
		data: {
			site: site
		},
		beforeSend: function() {
			pageNumber = 0;
			$("#fim-btn").hide();
			$(".row").fadeOut(400, function() {
				$(this).empty();
			});
		},
		success: function(response) {
			$(".row").fadeIn(250, function() {
				$(this).append(response);
			})
		},
		error: function(xhr) {
			alert("Ops, algo deu errado: " + xhr.status + ", " + xhr.statusText);
		}
	});
});


// Adicionar Likes
$(document).on("click", "button[id*='likes-btn-']", function() {
	
	var id = $(this).attr("id").split("-")[2];
	
	//console.log("id: " + id);
	
	$.ajax({
		method: "POST",
		url: "/promocao/like/" + id,
		success: function(response) {
			$("#likes-count-" + id).text(response);
		},
		error: function(xhr) {
			alert("Ops, ocorreu um erro: " + xhr.status + ", " + xhr.statusText);
		}
	});
	
});



// SSE 

window.onload = init();

var totalOfertas = new Number(0);

function init() {
	
	const evtSource = new EventSource("/promocao/notificacao");
	
	evtSource.onopen = (event) => {
		console.log("A conexão foi estabelecida");
	}
	
	evtSource.onmessage = (event) => {
		const count = event.data;
		
		if(count > 0) showButton(count);
	}
	
}

function showButton(count) {
	totalOfertas = totalOfertas + new Number(count);
	$("#btn-alert").show(function() {
		$(this)
			.attr("style", "display: block;")
			.text("Veja " + totalOfertas + " novas(s) ofertas(s)!");
	});
}

$("#btn-alert").click(function() {
	$.ajax({
		method: "GET",
		url: "/promocao/list/ajax",
		data: {
			page: 0,
			site: ''
		},
		beforeSend: function() {
			pageNumber = 0;
			totalOfertas = 0;
			$("#fim-btn").hide();
			$("#loader-img").addClass("loader");
			$("#btn-alert").attr("style", "display: none;");
			$(".row").fadeOut(400, function(){
				$(this).empty();
			});
		},
		success: function(response, status, xhr) {
			//console.log(status);
			$("#loader-img").hide();
			$(".row").fadeIn(250, function() {
				$(this).append(response);
			});
		},
		error: function(error) {
			console.log("error: ", error);
		}
	});
});









