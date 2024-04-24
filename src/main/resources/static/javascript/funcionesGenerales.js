function getNotAvailableDates(date) {
	$.ajax({
		url: 'http://localhost:8010/api-nikelao/dates',
		method: 'GET',
		data: { date: date },
		success: function(data) {
			if (data?.results != null && data?.results.length > 0) {
				data.results.forEach(function(dates) {
					deleteRowDataTable(dates.horary.id);
				});
			} else {
				console.log('No hay datos.', data);
			}
		},
		error: function(xhr, status, error) {
			console.error('getNotAvailableDates', error);
		}
	});
}

function addDateToClient(userid, horary, date, stateDateId) {
	$.ajax({
		url: 'http://localhost:8010/api-nikelao/clients',
		method: 'GET',
		data: { userid: userid },
		success: function(data) {
			if (data?.results != null && data?.results.length > 0) {
				var clientid = data.results[0].id;
				addDate(clientid, horary, date, stateDateId);
			} else {

				console.log('No se encontraron datos.', data);
			}
		},
		error: function(xhr, status, error) {
			console.error('addDateToClient:', error);
		}
	});
}

function addDate(clientid, horary, date, stateDateId) {
	var jsonData = JSON.stringify({
		clientId: clientid,
		horary: horary,
		date: date,
		stateDateId: stateDateId
	});

	$.ajax({
		url: 'http://localhost:8010/api-nikelao/dates',
		method: 'POST', // Cambiamos a POST para enviar datos en el cuerpo de la solicitud
		contentType: 'application/json', // Establecemos el tipo de contenido como JSON
		data: jsonData,
		success: function(data) {
			if (data?.results != null && data?.results.length > 0) {
				console.log("Se inserto la fecha correctamente");
				deleteRowDataTable(horary.id);
				alert("Se ha reservado la cita.");
			} else {

				console.log('No se encontraron datos.', data);
			}
		},
		error: function(xhr, status, error) {
			console.error('addDate', error);
		}
	});
}

function cancelDateToClient(date) {
	let jsonData = JSON.stringify({ date: date })
	$.ajax({
		url: 'http://localhost:8010/api-nikelao/canceldates',
		method: 'PUT',
		contentType: 'application/json', // Indica que se está enviando JSON
		data: jsonData,
		success: function(data) {
			if (data?.results != null && data?.results.length > 0) {
				let dateid = data.results[0].horary.id;
				let state = data.results[0].stateDate.name;
				updateRowDataTableToday(dateid, state);
			} else {

				console.log('No se encontraron datos.', data);
			}
		},
		error: function(xhr, status, error) {
			console.error('cancelDateToClient:', error);
		}
	});
}

function finishDateToClient(date) {
	let jsonData = JSON.stringify({ date: date })
	$.ajax({
		url: 'http://localhost:8010/api-nikelao/finishdates',
		method: 'PUT',
		contentType: 'application/json', // Indica que se está enviando JSON
		data: jsonData,
		success: function(data) {
			if (data?.results != null && data?.results.length > 0) {
				let dateid = data.results[0].horary.id;
				let state = data.results[0].stateDate.name;
				updateRowDataTableToday(dateid, state);
			} else {

				console.log('No se encontraron datos.', data);
			}
		},
		error: function(xhr, status, error) {
			console.error('cancelDateToClient:', error);
		}
	});
}

function initializeDataTable() {
	$.ajax({
		url: 'http://localhost:8010/api-nikelao/horary',
		method: 'GET',
		success: function(data) {
			if (data?.results != null && data?.results.length > 0) {
				createDataTable(data?.results);
				saveHorarysInputs(data.results);
			} else {
				console.log('No se encontraron datos.', data);
			}
		},
		error: function(xhr, status, error) {
			console.error('initializeDataTable', error);
		}
	});
}

function createDataTable(data) {
	var table = $('#tableAvailableDates').DataTable({});
	table.clear().draw();

	data.forEach(function(horary) {
		var row = table.row.add([horary.hour, '']).draw().node();
		$(row).attr('horaryid', horary.id);

		var button = document.createElement('button');
		button.textContent = 'Reservar';
		button.className = 'btn btn-primary';
		$(button).on('click', function() {
			var clientid = document.getElementById('user').getAttribute('userid');
			var date = $('#selectedDate').val();
			var stateDateId = 1;
			addDateToClient(clientid, horary, date, stateDateId);
		});

		$(row).find('td:eq(1)').html(button);
	});

}

function updateDataTable() {
	// Obtener la lista de horarios desde el campo oculto
	var data = retrieveHoraryList();

	var table = $('#tableAvailableDates').DataTable();
	table.clear().draw();

	data.forEach(function(horary) {
		var row = table.row.add([horary.hour, '']).draw().node();
		$(row).attr('horaryid', horary.id);

		var button = document.createElement('button');
		button.textContent = 'Reservar';
		button.className = 'btn btn-primary';
		$(button).on('click', function() {
			var clientid = document.getElementById('user').getAttribute('userid');
			var date = $('#selectedDate').val();
			var stateDateId = 1;
			addDateToClient(clientid, horary, date, stateDateId);
		});

		$(row).find('td:last').html(button);

	});
}


function saveHorarysInputs(data) {
	// Almacenar los datos de horarios en una variable global
	var horarys = data;

	// Construir la lista de horarios
	var horaryList = [];
	horarys.forEach(function(horary) {
		horaryList.push("[" + horary.hour + "," + horary.id + "]");
	});

	// Convertir la lista en formato de cadena separada por comas
	var horaryString = horaryList.join(';');

	// Asignar la cadena de horarios al valor del campo oculto
	$('#listHorary').val(horaryString);
}


function retrieveHoraryList() {
	// Obtener el valor del campo oculto
	var horaryString = $('#listHorary').val();

	// Dividir la cadena en una lista de horarios
	var horarys = horaryString.split(';');

	var horaryList = []
	horarys.forEach(function(horary) {
		// Suponiendo que cada horario tiene el formato "hour,id"
		var parts = horary.trim().substring(1, horary.length - 1).split(',');
		var horaryObj = {
			hour: parts[0],
			id: parts[1]
		};
		horaryList.push(horaryObj);
	});

	// Ahora horaryList contiene la lista de horarios que puedes utilizar
	console.log(horaryList);
	return horaryList;
}

function deleteRowDataTable(horaryid) {
	var row = document.querySelector('[horaryid="' + horaryid + '"]');
	if (row) {
		var table = $('#tableAvailableDates').DataTable();
		table.row(row).remove().draw(false);
	}

}

function updateRowDataTableToday(dateid, state) {
	var row = document.querySelector('[dateid="' + dateid + '"]');
	if (row) {
		var table = $('#tableDatesToday').DataTable();

		// Deshabilitar el botón dentro de la fila
		$(row).find('button.btn').prop('disabled', true);
	}

}

function initializeDataTableToday(date) {
	$.ajax({
		url: 'http://localhost:8010/api-nikelao/datestoday',
		method: 'GET',
		data: { date: date },
		success: function(data) {
			if (data?.results != null && data?.results.length > 0) {
				createDataTableToday(data?.results);
			} else {
				console.log('No se encontraron datos.', data);
			}
		},
		error: function(xhr, status, error) {
			console.error('initializeDataTable', error);
		}
	});
}

function createDataTableToday(data) {
	var table = $('#tableDatesToday').DataTable({});
	table.clear().draw();

	data.forEach(function(date) {
		var name = date.client.name + " " + date.client.surname;
		var row = table.row.add([date.horary.hour, name, date.client.phone, date.stateDate.stateName, '', '']).draw().node();
		$(row).attr('dateid', date.horary.id);

		if (date.stateDate.stateName !== "CANCELADA" && date.stateDate.stateName !== "FINALIZADA") {
			var cancel = document.createElement('button');
			cancel.textContent = 'Cancelar';
			cancel.className = 'btn btn-danger';
			$(cancel).on('click', function() {
				cancelDateToClient(date);
			});

			$(row).find('td:last').prev().html(cancel);
			
			var finish = document.createElement('button');
			finish.textContent = 'Finalizar';
			finish.className = 'btn btn-success';
			$(finish).on('click', function() {
				finishDateToClient(date);
			});

			$(row).find('td:last').html(finish);
		}
	});

}

