<%@ include file="init.jsp"%>

<portlet:resourceURL id="<%=MVCCommandNames.COURSES_DATA_RESOURCE%>"
	var="coursesResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="theme" value="${dataTheme}" />
	<portlet:param name="topic" value="${dataTopic}" />

</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.COURSES_FILTER_RESOURCE%>"
	var="filterResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="theme" value="${dataTheme}" />
	<portlet:param name="topic" value="${dataTopic}" />
</portlet:resourceURL>

<portlet:resourceURL
	id="<%=MVCCommandNames.COURSES_DATA_COUNT_RESOURCE%>"
	var="countResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="theme" value="${dataTheme}" />
	<portlet:param name="topic" value="${dataTopic}" />
</portlet:resourceURL>

<div class="cx-main-wrap courses-filter">
	<div class="container sp-main-bot sp-main-top">
	<c:choose>
	<c:when test="${(dataTopic != 0)}">
		<h3 class="title-1 center-mb">All ${dataTopicName} Courses</h3>
	</c:when>
	<c:otherwise>
		<h3 class="title-1 center-mb">All ${dataThemeName} Courses</h3>
	</c:otherwise>
	</c:choose>
		<div class="row filter-wrap-2">
			<div class="col-mdauto">
				<div class="show-control">
					<a href="#" class="control btn-4"><em class="fas fa-filter"></em>
						Filter</a>
					<div class="sub-content">
						<div class="row">
							<div class="col-6">Advance Filters</div>
							<div class="col-6 text-right">
								<a class="clear-btn" data-form-id="filter" href="#"
									onclick="resetFilter(); return false;">Clear All</a>
							</div>
						</div>
						<form id="filter">
							<div class="lb-1 mb-2">Theme</div>
							<div class="row sp-row-1 break-425" id="filter-theme">
								<!-- INSERTED BY JS -->
							</div>
							<div class="lb-1 mb-2">Topic</div>
							<div class="row sp-row-1 break-425" id="filter-topic">
								<!-- INSERTED BY JS -->
							</div>
							<div class="lb-1 mb-2">Funded/Non Funded</div>
							<div class="row sp-row-1 break-425" id="filter-funded">
								<!-- INSERTED BY JS -->
							</div>
							<div class="lb-1 mb-2">Price</div>
							<div class="container">
								<div class="row sp-row-1 break-425">
									<div class="col-xl-5 col-lg-4 col-6 bcol">
										<div id="slider-range"></div>
									</div>
								</div>
								<div class="row sp-row-1 break-425 slider-labels">
									<div class="col-xl-2 caption">
										<strong>Min:</strong> <span id="slider-range-value1"></span>
									</div>
									<div class="col-xl-3 text-right caption">
										<strong>Max:</strong> <span id="slider-range-value2"></span>
									</div>
								</div>
								<div class="row sp-row-1 break-425">
									<div class="col-xl-5 col-lg-4 col-6 bcol">
										<input type="hidden" name="min-value" value=""> <input
											type="hidden" name="max-value" value="">
									</div>
								</div>
							</div>

						</form>
					</div>
				</div>
			</div>
			<div class="col-md last">
				<div class="select-wrap">
					<div class="dropdown">
						<select class="filter-price" id="price"
							onchange="triggerChange(); return false;">
							<option value="asc" id="low-to-high">Price (Low to High)</option>
							<option value="desc" id="high-to-low">Price (High to
								Low)</option>
						</select>
					</div>
				</div>
			</div>
		</div>
		<div id="course-list" class="row sp-row-2 grid-1 break-480">
			<!-- INSERTED BY JS -->
			<div class="loader"></div>
		</div>
		<div id="courses-pagination" class="pager"></div>
		<!-- CONDITION FOR ALL RESULT-->
		<br>
		<c:if test="${(dataCourse == 0)}">
			<h2 class="title-1">All Results</h2>
			<div id="all-result">
				<div class="loader"></div>
				<!-- INSERTED BY JS -->
			</div>
		</c:if>
		<!-- END CONDITION FOR ALL RESULT -->
	</div>
</div>

<script type="text/javascript">

    var courses = "";
    var maxPrice = "";
    var minSliderPrice = 0;
    var maxSliderPrice = 0;
    var maximumPrice = 0;
    var paginationSize = 16;

    $(document).ready(function() {
    	var url_string = window.location.href;
		var url = new URL(url_string);
		var searchParam = new URLSearchParams(url.search);
		var course = searchParam.get('course');
		loadFilter("<%=filterResourceURL.toString()%>",initSlider);
    });
    
    function triggerChange() {
		loadCount('<%=countResourceURL.toString()%>');
	}
    
    function generatePagination(url,total, filters){
   		var container = $('#courses-pagination');
   		container.pagination({
   			dataSource: url,
   			locator: 'courses',
   			totalNumber: total,
   			ulClassName: 'justify-content-center',
   		    pageSize: paginationSize,
	   		alias: {
	   		    pageNumber: '<portlet:namespace/>pageNum',
	   		    pageSize: '<portlet:namespace/>limit'
	   		},
   		    ajax: {
   		    	type : "POST",
   		    	data : {
   		    		<portlet:namespace/>filter : filters
   		    	},
   		    	cache : false,
   		        beforeSend: function() {
   		        	container.prev().html(pageTransition());
   		        }
   		    },
            callback: function(response, pagination){   	
            	var htmlCourses = '';
              	if(response.length == 0) {
              		htmlCourses += generateNullResult();
              	} else {
              		$.each(response, function (index, course) {
	   					htmlCourses += generateCourseResult(course); 
	               	});	
              	}
               	container.prev().html(htmlCourses);
               	$('html, body').animate({
	                 scrollTop: container.prev().offset().top - 300
	            }, 1000);
              }
   		})
 }
    
    function loadCount(url) {
	 	let filters = generateFilters();
		return jQuery.ajax({
			url : url,
			type : "POST",
			dataType : "json",
			data : {
		    		<portlet:namespace/>filter : filters
		    	},
			async : "true",
			cache : "true",
			beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
                $('#course-list .loader').removeClass('hidden')
        	},
	        success : function(data,textStatus,XMLHttpRequest){
                generatePagination('<%=coursesResourceURL.toString()%>',data.total, filters);
			},
			complete: function () { // Set our complete callback, adding the .hidden class and hiding the spinner.
            	$('#course-list .loader').hide();
            },
			error : function(data,textStatus,XMLHttpRequest){
				
			}
		});
	}
    
    function loadFilter(url, callback) {
		return jQuery.ajax({
			url : url,
			type : "POST",
			dataType : "json",
			async : "true",
			cache : "true",			
	        success : function(data,textStatus,XMLHttpRequest){
	        	var htmlThemeList = '';
	            var htmlTopicList = '';
	            var htmlFundedList = '';
	            maxPrice = data.maxPrice;
	            callback(maxPrice);
	            
	            if (data.filterTheme.length > 0) {
                    data.filterTheme.forEach(function(filterTheme) {
                        htmlThemeList += generateFilterList(filterTheme);
                    });
                }

                $('#filter-theme').html(htmlThemeList);

                if (data.filterTopic.length > 0) {
                    data.filterTopic.forEach(function(filterTopic) {
                        htmlTopicList += generateFilterList(filterTopic);
                    });
                }

                $('#filter-topic').html(htmlTopicList);

                if (data.fundedList.length > 0) {
                    data.fundedList.forEach(function(fundedList) {
                        htmlFundedList += generateOtherList(fundedList);
                    });
                }

                $('#filter-funded').html(htmlFundedList);
			},
			error : function(data,textStatus,XMLHttpRequest){
				
			}
		});
	}

    
    function initSlider(data) {
    	 // Price Slider
    	maximumPrice = data;
        maxSliderPrice = Number((parseFloat(maximumPrice)).toFixed(1)) + 1;
    	loadCount('<%=countResourceURL.toString()%>');
        $('.noUi-handle').on('click', function() {
        	$(this).width(50);
        });
        
        var rangeSlider = document.getElementById('slider-range');
        var moneyFormat = wNumb({
			decimals: 0,
            thousand: ',',
            prefix: '$'
		});
        document.getElementById('slider-range-value1').innerHTML = minSliderPrice;
        document.getElementById('slider-range-value2').innerHTML = maxSliderPrice;
		noUiSlider.create(rangeSlider, {
            start: [0, maxSliderPrice],
            step: 10,
            range: {
            	'min': [0],
              	'max': [maxSliderPrice]
            },
            format: moneyFormat,
            connect: true
		});
          
       	// Set visual min and max values and also update value hidden form inputs
       	rangeSlider.noUiSlider.on('change', function(values, handle) {
            minSliderPrice = moneyFormat.from(values[0]);
            maxSliderPrice = moneyFormat.from(values[1]);
            
       		document.getElementById('slider-range-value1').innerHTML = values[0];
            document.getElementById('slider-range-value2').innerHTML = values[1];
            document.getElementsByName('min-value').value = moneyFormat.from(values[0]);
            document.getElementsByName('max-value').value = moneyFormat.from(values[1]);
            loadCount('<%=countResourceURL.toString()%>');
       	});
    }
    
    function generateNullResult() {
		var html = '<div class="col-xl-12 col-lg-12 col-12 bcol">';
		html += '<div class="card inner imgeffect hoverpp">'
		html += '<div class="card-body">'
		html += '<p class="text-center"> No Data Found </p>'
		html += '</div>';
		html += '</div>';
		html += '</div>';
		return html;
	}
    
    function pageTransition() {
		var html = '<div class="col-xl-12 col-lg-12 col-12 bcol" style="height:200vh;">';
		html += '<div class="card inner imgeffect hoverpp">'
		html += '<div class="card-body">'
		html += '<p class="text-center"> Loading Data ... </p>'
		html += '</div>';
		html += '</div>';
		html += '</div>';
		return html;
	}

    function generateCourseResult(data) {
        var html = '<div class="col-xl-3 col-lg-4 col-6 bcol">';
        html += '<div class="inner imgeffect hoverpp">';
        html += '<figure class="imgwrap" style="height: 176.083px;">';
        html += '<div class="bg" style="background-image: url(' + data.urlImage +
            ');">';
        html += '<img src="' + data.urlImage + '" style="height: 200px;" class="bgimg"/>';
        html += '</div>'
        html += '</figure>';
        html += '<div class="box-content" style="height: 150px;">'
        html += '<div class="box-status">';

        if (data.popular == "POPULAR") {
            html += '<span class="popular">' + data.popular + '</span>'
        }

        if (data.funded == "FUNDED") {
            html += '<span class="funded">' + data.funded + '</span>'
        }

        html += '</div>'
        html += '<p class="title mb-1" style="height: 61.3333px;">' +
            data.title + '</p>';

        if (data.price != "0") {
        	var floatPrice = data.price;
        	html += '<div class="float-right small-text">' + data.status;
            html += '<strong class="price" style="margin-right:20px;">$ ' + floatPrice.toFixed(2) + '</strong>';
        	html += '</div>';
        }else{
            html += '<strong class="price float-right" style="margin-right:20px;">&nbsp;</strong>';
        }
        
        if(data.whatInIt != ""){
	        html += '<div class="hidden-pp"><div class="hovercontent"><h4>What' + "'" + 's in it for me?</h4>';
	        html += "" + data.whatInIt + "";
	        html += '</div></div>';        	
        }

        html += '</div>';
        var url = ''+data.urlMore;
        html += '<a href="' + url.split('?')[0] + '" class="fxlink">View detail</a>';
        html += '</div>';
        html += '</div>';
        return html;
    }

    function generateFilterList(data) {
        var valueTemp = data.category + '|' + data.filterId;
        
        var html = '<div class="col-lg-4 col-md-4 col-6 bcol">';
        html += '<div class="checkbox checktype">';
        html += '<input type="checkbox" name="' + data.filterName + '" onclick="triggerChange()" id="' + data.filterName + '" value="' + valueTemp + '" /> <label for="' + data.filterName + '">' +
            data.filterName + '</label>';
        html += '</div>'
        html += '</div>'
        html += '</div>'
        return html;
    }

    function generateOtherList(data) {
        var valueTemp = data.category + '|' + data.filterName.toUpperCase();
        var html = '<div class="col-lg-4 col-md-4 col-6 bcol">';
        html += '<div class="checkbox checktype">';
        html += '<input type="checkbox" name="' + data.filterName + '" onclick="triggerChange()" id="' + data.filterName + '" value="' + valueTemp + '" /> <label for="' + data.filterName + '">' +
            data.filterName + '</label>';
        html += '</div>'
        html += '</div>'
        html += '</div>'
        return html;
    }

    function generateFilters() {
        var sortIdx = document.getElementById("price").selectedIndex;
        var sortId = document.getElementById("price").options[sortIdx].value;
        //		var priceSortValue = priceSort.selectElement.options[priceSort.selectedIndex].value;
        //
        var filters = [];
        $(':checkbox:checked').each(function(i) {
            //
			filters[i] = $(this).val();   	 		
        });
       
        filters.push('minPrice|' + minSliderPrice);
        filters.push('maxPrice|' + maxSliderPrice);
       // getFilteredCourses(data, sortId);
        var coursesFilter = courses;
        var htmlCourses = '';
	    var minPriceFilter = '';
	    var maxPriceFilter = '';
	    var fundedFilter = [];
	    var priceFilter = {};	
	    var topicIdsFilter = [];
	    var themeIdsFilter = [];
	    var filterList = {};
	    for(var i = 0; i < filters.length; i++){
        	//
        	var temp = filters[i].split("|");
			var keyTemp = temp[0];
            var valueTemp = temp[1];
            if(keyTemp == "topicIds"){
              	topicIdsFilter.push(parseInt(valueTemp));
            }else if(keyTemp == "themeIds"){
               	themeIdsFilter.push(parseInt(valueTemp));
            }else if(keyTemp == "funded"){
               	fundedFilter.push(valueTemp);
            }else if(keyTemp == "minPrice"){
               	priceFilter['min'] = parseFloat(valueTemp);	
            }else if(keyTemp == "maxPrice"){;
            	priceFilter['max'] = parseFloat(valueTemp);
            } 
       	}
            
        filterList['topicIds'] = topicIdsFilter;
        filterList['themeIds'] = themeIdsFilter;
        filterList['funded'] = fundedFilter;
        filterList['price'] = priceFilter;
        filterList['sort'] = sortId;
        return JSON.stringify(filterList);
    }

    function resetFilter() {
    	$('input[type=checkbox]').prop('checked',false); 
    	
    	var rangeSlider = document.getElementById('slider-range');
    	minSliderPrice = 0;
    	maxSliderPrice = maximumPrice;
    	var moneyFormat = wNumb({
			decimals: 0,
            thousand: ',',
            prefix: '$'
		});
    	document.getElementById('slider-range-value1').innerHTML = minSliderPrice;
        document.getElementById('slider-range-value2').innerHTML = maxSliderPrice;
        document.getElementsByName('min-value').value = moneyFormat.from(minSliderPrice);
        document.getElementsByName('max-value').value = moneyFormat.from(maxSliderPrice);
        rangeSlider.noUiSlider.set([0, maximumPrice]);
    	
        $("#price").val('asc');
        loadCount('<%=countResourceURL.toString()%>');
	}
</script>