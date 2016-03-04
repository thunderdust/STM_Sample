$(document).ready(function() {
	var initPage = function() {
		switchActiveTab('nav-employee');

		BookStore.dataTable = $('#employee-table').DataTable({
			'serverSide' : true,
			'ajax' : {
				url : 'employeemanagement/findUserByOfficeId',
				type : 'POST',
				contentType : "application/json",
				data: function ( d ) {
					// send only data required by backend API
					delete(d.columns);
					delete(d.order);
					delete(d.search);
					d.officeId = $('#employee-office-filter').val();
			      return JSON.stringify(d);
			    },
			    dataSrc: "userAccountEntities",
				xhrFields: {
				      withCredentials: true
				   }
			},
			columns: [
	          { data: 'id' },
	          { data: 'firstName' },
	          { data: 'lastName' },
	          { data: 'email' },
	          { data: 'officeName' }
			],
			select: "single"
			
		});
		
		$.ajax({
			url : 'offices',
			type : 'POST',
			contentType : "application/json",
			data: JSON.stringify({ draw: 0, start: 0, length: 10})
		}).done(function(data) {
			var officeSelects = $('.office-selects');
			$.each(data.offices, function(i, office) {
				$.each(officeSelects, function(i, select) {
					$(select).append($('<option data-display = "' + office.name + '" value="' + office.id + '">' + office.name + '</li>'));
				});
			});
		});

		$('#employee-add-button').click(BookStore.addEmployee);
		$('#employee-delete-button').click(BookStore.deleteEmployee);
		$('#employee-office-filter').change(function() { $('#employee-table').dataTable().fnReloadAjax(); });
		
		// disable delete button if nothing selected
		BookStore.dataTable.on('select', function () {
			$('#employee-open-delete-modal-btn').prop('disabled', false);
			$('#employee-edit-modal-btn').prop('disabled', false);
	    });
		
		BookStore.dataTable.on('deselect', function () {
			$('#employee-open-delete-modal-btn').prop('disabled', true);
			$('#employee-edit-modal-btn').prop('disabled', true);
	    });
		
		BookStore.dataTable.on('draw', function() {
			$('#employee-open-delete-modal-btn').prop('disabled', true);
			$('#employee-edit-modal-btn').prop('disabled', true);
		});

		// When edit button is clicked initialize the dialog
        $('#employee-edit-modal-btn').on('click', function() {
            var selectedData = BookStore.dataTable.row('.selected').data();
            $('#id').prop('readonly',true)
            $('#employee-add-modal #myModalLabel').data().mode = 'update';
            $('#employee-add-modal #myModalLabel').html('Edit employee');
            $('#id').val(selectedData.id);
            $('#first-name').val(selectedData.firstName);
            $('#last-name').val(selectedData.lastName);
            $('#email').val(selectedData.email);
            $('#roles').val(selectedData.roles);
            $('#office option[data-display='+selectedData.officeName+']').attr('selected', 'selected');
            // $('#password').val(selectedData.password);
        });
        
        // reset data when modal hides
        $('#employee-add-modal').on('hidden.bs.modal', function() {
        	$('#id').prop('readonly',false);
            $('#employee-add-modal #myModalLabel').data().mode = 'add';
            $('#employee-add-modal #myModalLabel').html('Add new employee');
            $('#employee-form')[0].reset();
        });

        $('#employee-add-modal #myModalLabel').data().mode = 'add';
	};

	initPage();
});

BookStore.addEmployee = function(evt) {
	var formData = $('#employee-form').serializeObject();
	// modify role object to become an object if only one is selected
	if (typeof(formData.roles) === 'string') {
		formData.roles = [formData.roles];
	}
	
	var url = 'employeemanagement/' + $('#employee-add-modal #myModalLabel').data().mode + 'UserAccount';
	
	$.ajax({
		url : url,
		data : JSON.stringify(formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#employee-add-modal').modal('hide');
		$('#employee-table').dataTable().fnReloadAjax();
		
	});
};

BookStore.deleteEmployee = function(evt) {
	var selectedId = BookStore.dataTable.data()[BookStore.dataTable.row('.selected')[0]].id;
	$.ajax({
		url : "employeemanagement/deleteUserAccount?id=" + selectedId,
		type : 'DELETE',
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#employee-delete-modal').modal('hide');
		$('#employee-table').dataTable().fnReloadAjax();
		
	});
};