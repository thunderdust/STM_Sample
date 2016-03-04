$(document).ready(function() {
    var initPage = function() {
        switchActiveTab('nav-book');

        BookStore.dataTable = $('#book-table').DataTable({
            'serverSide' : true,
            'ajax' : {
                url : 'books/books',
                type : 'POST',
                contentType : "application/json",
                data: function ( d ) {
                    // Modify data into what is required by backend API
                    d.searchParam = d.search.value;
                    delete(d.search);
                    delete(d.columns);
                    delete(d.order);
                  return JSON.stringify(d);
                },
                dataSrc: "bookEntries"
            },
            columns: [
              { data: 'id' },
              { data: 'isbn' },
              { data: 'title' },
              { data: 'publisherId',
                  visible: false },
              { data: 'publisher' },
              { data: 'seriesId',
                  visible: false },
              { data: 'series' },
              { data: 'authorIds',
                  visible: false },
              { data: 'authorList' },
              { data: 'price' }
            ],
            select: 'single',
            filter: true
        });

        $('#book-add-button').click(BookStore.addEditBook);
        $('#book-delete-button').click(BookStore.deleteBook);
        $('#add-new-ok-button').click(BookStore.addNewAuthorPublisherOrSeries);
        
        // When edit button is clicked initialize the dialog
        $('#book-edit-modal-btn').on('click', function() {
            var selectedData = BookStore.dataTable.row('.selected').data();
            $('#book-id').show();
            $('#book-add-modal').attr('data-edit-mode', 'true');
            $('#book-add-modal #addBookLabel').hide();
            $('#book-add-modal #editBookLabel').show();
            $('#id-name').val(selectedData.id);
            $('#isbn-name').val(selectedData.isbn);
            $('#book-name').val(selectedData.title);
            $('#price').val(selectedData.price);
            var bookSeriesSelect = $('.book-series-selector');
            bookSeriesSelect.append(BookStore.createDynamicOptionHtml_(selectedData.seriesId, selectedData.series));
            bookSeriesSelect.val(selectedData.seriesId).trigger('change');
            var bookPublisherSelect = $('.book-publisher-selector');
            bookPublisherSelect.append(BookStore.createDynamicOptionHtml_(selectedData.publisherId, selectedData.publisher));
            bookPublisherSelect.val(selectedData.publisherId).trigger('change');
            var bookAuthorSelect = $('.book-author-selector');
            for (var i = 0; i < selectedData.authorIds.length; i++) {
                var authorId = selectedData.authorIds[i];
                var authorName = selectedData.authorList[i];
                bookAuthorSelect.append(BookStore.createDynamicOptionHtml_(authorId, authorName));
            }
            bookAuthorSelect.val(selectedData.authorIds).trigger('change');
        });
        
        // Disable delete button if nothing selected
        BookStore.dataTable.on('select', function () {
            $('#book-open-delete-modal-btn').prop('disabled', false);
            $('#book-edit-modal-btn').prop('disabled', false);
        });
        
        BookStore.dataTable.on('deselect', function () {
            $('#book-open-delete-modal-btn').prop('disabled', true);
            $('#book-edit-modal-btn').prop('disabled', true);
        });
        
        BookStore.dataTable.on('draw', function() {
            $('#book-open-delete-modal-btn').prop('disabled', true);
            $('#book-edit-modal-btn').prop('disabled', true);
        });

        // Reset data when modal hides
        $('#book-add-modal').on('hidden.bs.modal', function() {
            $('#book-add-modal').attr('data-edit-mode', 'false');
            $('#book-add-modal #editBookLabel').hide();
            $('#book-add-modal #addBookLabel').show();
            $('#book-id').hide();
            $('#id-name, #isbn-name, #book-name, #price').val('');
            $('#insert-new-author, #insert-new-publisher, #insert-new-series').val('');
            $('.book-series-selector option').remove();
            $('.book-publisher-selector option').remove();
            $('.book-author-selector option').remove();
            $('.book-series-selector').trigger('change');
            $('.book-publisher-selector').trigger('change');
            $('.book-author-selector').trigger('change');

            // Clear all validation errors
            BookStore.manageValidationError($('#book-form .validate-ele'), false);
        });
        
        // focus on first input when model is shown
        $('#book-add-modal').on('shown.bs.modal', function() {
            $('#isbn-name').focus();
        });
    };
    initPage();
    
    // Initialize series author and publisher selection
    var initSelectorComponent = function(url, selectorClass, titleClass, addNewUrl) {
        var addNewFunction = function(titleClass, addNewUrl) {
            BookStore.addNewForm_.attr('data-add-new-url', addNewUrl);
            BookStore.addNewForm_.attr('data-dropdown-selector', selectorClass);
            return '<a class="add-new-anchor" onclick="BookStore.addNewAuthorPublisherOrSeries();">Add this ' + titleClass + '</a>';
        };
        addNewFunction = addNewFunction.bind(null, titleClass, addNewUrl)
        return $(selectorClass).select2({
            ajax: {
                url: url,
                dataType: 'json',
                cache: true,
                delay: 250,
                processResults: function(data) {
                    return {
                        results: $.map(data, function(val, idx) {
                            val['text'] = val['name'];
                            return val;
                        })
                    };
                }
            },
            theme: 'bootstrap',
            language: {
                noResults: addNewFunction
            },
            escapeMarkup: function(markup) {
                return markup;
            },
            tags: true,
            createTag: function(termObj) {
                // Set the new data so that it can be added later
                BookStore.addNewForm_.find('input').val(termObj.term);
            }
        });
    };
    
    BookStore.addNewForm_ = $('#add-new-form');
    BookStore.seriesSelector_ = initSelectorComponent('series', '.book-series-selector', 'series', 'series');
    BookStore.publisherSelector_ = initSelectorComponent('publishers', '.book-publisher-selector', 'publisher', 'publisher');
    BookStore.authorSelector_ = initSelectorComponent('authors', '.book-author-selector', 'author', 'author');
});

BookStore.addNewAuthorPublisherOrSeries = function() {
    var addNewUrl = BookStore.addNewForm_.attr('data-add-new-url');
    var select2Dropdown = $(BookStore.addNewForm_.attr('data-dropdown-selector'));
    var dataObj = BookStore.addNewForm_.serializeObject();
    
    $.ajax({
        url: addNewUrl,
        data: JSON.stringify(dataObj),
        type: 'POST',
        contentType: 'application/json',
        xhrFields: {
            withCredentials: true
        }
    }).done(function(result) {
        // Do something
        var resultId = result.id;
        var resultText = result.name;
        select2Dropdown.append(BookStore.createDynamicOptionHtml_(resultId, resultText));
        select2Dropdown.val(resultId).trigger('change');
        select2Dropdown.select2("close");
    });
};

BookStore.addEditBook = function(event) {
    var isEditMode = $('#book-add-modal').attr('data-edit-mode') === 'true';

    // Validation
    var allFormGroups = $('#book-form .validate-ele');
    var isValid = isEditMode ? BookStore.validateFormGroup(allFormGroups) : BookStore.validateFormGroup(allFormGroups.slice(1));
    if (!isValid) return;

    var formData = {};
    var isbnInput = $('#isbn-name');
    var bookInput = $('#book-name');
    var priceInput = $('#price');
    
    formData['publisherId'] = BookStore.publisherSelector_.val();
    formData['seriesId'] = BookStore.seriesSelector_.val();
    formData['authorIdList'] = BookStore.authorSelector_.val();
    formData['isbn'] = isbnInput.val();
    formData['title'] = bookInput.val();
    formData['price'] = priceInput.val();
    
    if (isEditMode) {
        formData['id'] = $('#id-name').val();
    }
    
    $.ajax({
        url : isEditMode ? "books/update" : "books/add",
        data : JSON.stringify(formData),
        type : 'POST',
        contentType : 'application/json',
        xhrFields: {
          withCredentials: true
       }
    }).done(function() {
        $('#book-add-modal').modal('hide');
        $('#book-table').dataTable().fnReloadAjax();
    });
};

BookStore.deleteBook = function(evt) {
    var selectedId = BookStore.dataTable.row('.selected').data().id;
    $.ajax({
        url : "books?id=" + selectedId,
        type : 'DELETE',
        xhrFields: {
          withCredentials: true
       }
    }).done(function() {
        $('#book-delete-modal').modal('hide');
        $('#book-table').dataTable().fnReloadAjax();
    });
};

BookStore.validateFormGroup = function(eleOrEleArray) {
    var validationStatus = true;
    var validateFn = function(ele) {
        ele = $(ele);
        if (!ele.val()) {
            validationStatus = validationStatus && !BookStore.manageValidationError(ele, true);
        } else {
            validationStatus = validationStatus && !BookStore.manageValidationError(ele, false);
        }
    }
    if (eleOrEleArray.length > 1) {
        $.each(eleOrEleArray, function(idx, ele) {
            validateFn(ele);
        });
    } else {
        validateFn(eleOrEleArray);
    }
    return validationStatus;
}

/*
 * If status true set validation error else remove it. 
 */
BookStore.manageValidationError = function(eleOrEleArray, status) {
    if (eleOrEleArray.length > 1) {
        $.each(eleOrEleArray, function(idx, ele) {
            var ele = $(ele);
            status ? ele.parent().parent().addClass('has-error') : ele.parent().parent().removeClass('has-error');
        });
    } else {
        status ? eleOrEleArray.parent().parent().addClass('has-error') : eleOrEleArray.parent().parent().removeClass('has-error');
    }
    return status;
};

BookStore.createDynamicOptionHtml_ = function(id, value) {
    return '<option value="' + id + '" + class="dynamic-option"' + '>' + value + '</option>';
}