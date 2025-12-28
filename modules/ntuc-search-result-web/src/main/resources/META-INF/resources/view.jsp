<%@ include file="init.jsp"%>

<portlet:resourceURL id="<%=MVCCommandNames.SEARCH_DATA_RESOURCES %>" var="searchResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="keyword" value="${keyword}" />
	<portlet:param name="course" value="${dataCourse}" />
	
</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.SEARCH_FILTER_RESOURCES %>" var="filterResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="keyword" value="${keyword}" />
	<portlet:param name="course" value="${dataCourse}" />
</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.SEARCH_DATA_COUNT_RESOURCES %>" var="countResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="keyword" value="${keyword}" />
	<portlet:param name="course" value="${dataCourse}" />
</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.SEARCH_BLOG_DATA_RESOURCES %>" var="searchBlogResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="keyword" value="${keyword}" />
	<portlet:param name="course" value="${dataCourse}" />
</portlet:resourceURL>
<portlet:resourceURL id="<%=MVCCommandNames.SEARCH_NEWS_DATA_RESOURCES %>" var="searchNewsResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="keyword" value="${keyword}" />
	<portlet:param name="course" value="${dataCourse}" />
</portlet:resourceURL>
<portlet:resourceURL id="<%=MVCCommandNames.SEARCH_PRESS_DATA_RESOURCES %>" var="searchPressResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="keyword" value="${keyword}" />
	<portlet:param name="course" value="${dataCourse}" />
</portlet:resourceURL>
<portlet:resourceURL id="<%=MVCCommandNames.SEARCH_EXAM_DATA_RESOURCES %>" var="searchExamResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="keyword" value="${keyword}" />
	<portlet:param name="course" value="${dataCourse}" />
</portlet:resourceURL>

<section class="cx-bread-result courses-filter mt-5">
    <div class="container">
    	<div class="row">
            <div class="col-md-12">
                <ul class="breadcrumb mb-3 mt-3">
                    <li class="breadcrumb-item"><a href="/">Home</a></li>
                    <li class="breadcrumb-item">Search Results</li>
                    <li class="breadcrumb-item active">"${keyword}"</li>
                </ul>
            </div>
            <div class="col-md-8">
                <h3 class="title-1 center-mb mb-5 mt-3">Search Results for "${keyword}"</h3>
            </div>
            <div class="col-md-4">
                <aui:input class="form-control bdr-15" autocomplete="false" placeholder="Search..." title="Search" type="text" 
                	label="" value="${keyword}" name="researchText" id="researchText"/>
            </div>
        </div>
    </div>
</section>

<c:if test="${dataCourse!=0 }">
	<style>
		.tab-search .slick-track{
			width: 100% !important;
		}
		.box-item{
			width: 49% !important;
		}
		.slick-next{
			display: none !important;
		}
		.slick-prev{
			display: none !important;
		}
	</style>
</c:if>

<section class="section-tab-search">
    <div class="container">
        <div class="row">
            <div class="tab-search">
                <c:if test="${dataCourse!=0 }">
                	<div class="box-item active" data-id="course">COURSES</div>
	
	                <div class="box-item" data-id="exams">EXAMS</div>
                </c:if>

				<c:if test="${dataCourse==0 }">
					<div class="box-item active" data-id="course">COURSES</div>
	
	                <div class="box-item" data-id="exams">EXAMS</div>
	                
	                <div class="box-item" data-id="blogs">BLOGS</div>
	
	                <div class="box-item" data-id="pressreleases">PRESS RELEASES</div>
	
	                <div class="box-item" data-id="news">NEWS</div>
                </c:if>
            </div>

            <div class="box-content w-100 mt-5">
                <div class="content-data active" data-content="course">

                    <section class="cx-bread-result courses-filter">
                        <div class="container">
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
														<div class="col-xl-6 col-lg-4 col-6 bcol">
															<div id="slider-range"></div>
														</div>
														<div class="col-xl-6 col-lg-4 col-6 bcol">
															<btn type="submit" class="control btn-4 float-right"><em class="fas fa-paper-plane "></em>
											Submit</btn>
														</div>
													</div>
													<div class="row sp-row-1 break-425 slider-labels">
														<div class="col-xl-2 caption">
															<strong>Min:</strong> <span id="slider-range-value1"></span>
														</div>
														<div class="col-xl-4 text-right caption">
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
												onchange="triggerChange();return false;">
												<option value="asc" id="low-to-high">Price (Low to High)</option>
												<option value="desc" id="high-to-low">Price (High to
													Low)</option>
											</select>
					
										</div>
									</div>
								</div>
							</div>
							<div id="courses-result" class="row sp-row-2 grid-1 break-480">
								<!-- INSERTED BY JS -->
								<div class="loader"></div>
							</div>
                            <div id="courses-pagination" class="pager">
                                
                            </div>
                        </div>
                    </section>
                </div>
                
                <div class="content-data" data-content="exams">

                    <section class="cx-bread-result courses-filter">
                        <div class="container">
                            <div class="row filter-wrap-2" style="display: none" id="block-filter-exams">
								<div class="col-mdauto">
									<div class="show-control">
										<a href="#" class="control btn-4"><em class="fas fa-filter"></em>
											Filter</a>
										<div class="sub-content">
											<div class="row">
												<div class="col-6">Advance Filters</div>
												<div class="col-6 text-right">
													<a class="clear-btn" data-form-id="filter" href="#"
														onclick="resetFilterExams(); return false;">Clear All</a>
												</div>
											</div>
											<form id="filter">
												<div class="lb-1 mb-2">Category</div>
												<div class="row sp-row-1 break-425" id="filter-category">
													
												</div>												
											</form>
										</div>
									</div>
								</div>
								<div class="col-md last">
									<div class="select-wrap">
										<div class="dropdown">
											<select class="filter-price" id="priceFilterExams"
												onchange="triggerChangeExams();return false;">
												<option value="asc" id="low-to-high">Price (Low to High)</option>
												<option value="desc" id="high-to-low">Price (High to
													Low)</option>
											</select>
					
										</div>
									</div>
								</div>
							</div>
							<div id="all-result-exams" class="row sp-row-2 grid-1 break-480">
								<!-- INSERTED BY JS -->
								<div class="loader" style="display: none;"></div>
							</div>
                            <div id="exams-pagination" class="pager">
                                
                            </div>
                        </div>
                    </section>
                </div>

				<c:if test="${dataCourse==0 }">
					<div class="content-data" data-content="blogs">
	                    <div id="all-result-blogs" class="row sp-row-2 grid-1 break-480">
							<!-- INSERTED BY JS -->
							<div class="loader" style="display: none;"></div>
						</div>
	                    <div id="blogs-pagination" class="pager">
	                              
	                   	</div>
	                </div>
	
	                <div class="content-data" data-content="news">
	                    <div id="all-result-news" class="row sp-row-2 grid-1 break-480">
							<!-- INSERTED BY JS -->
							<div class="loader" style="display: none;"></div>
						</div>
	                    <div id="news-pagination" class="pager">
	                              
	                   	</div>
	                </div>
	
	                <div class="content-data" data-content="pressreleases">
	                    <div id="all-result-pressreleases" class="row sp-row-2 grid-1 break-480">
							<!-- INSERTED BY JS -->
							<div class="loader" style="display: none;"></div>
						</div>
	                    <div id="pressreleases-pagination" class="pager">
	                              
	                   	</div>
	                </div>
				</c:if>
            </div>
        </div>
    </div>
</section>
<script>
	var initBlogLoad = false;
	var initExamLoad = false;
	var initNewsLoad = false;
	var initPressLoad = false;
	var initCourse = true;
	
	$( "#<portlet:namespace/>researchText" ).blur(function() {
		if("${keyword}"!=$(this).val() && $(this).val().length>2){
			window.location.href = "/search-result?keyword="+$(this).val()+"&course="+"${dataCourse}";
		}
	});
	
	$( "#<portlet:namespace/>researchText" ).on('keypress',function(e) {
	    if(e.which == 13) {
	    	if("${keyword}"!=$(this).val() && $(this).val().length>2){
	    		window.location.href = "/search-result?keyword="+$(this).val()+"&course="+"${dataCourse}";
	    	}
	    }
	});

    $(".box-item").click(function () {
        const id = $(this).data('id');
        
        if(id=='exams' && !initExamLoad){
        	$("#all-result-exams .loader").show();
        	$("#block-filter-exams").show();
        	initExamLoad = true;
        	loadDataExams('<%=searchExamResourceURL.toString()%>');
        }else if(id=='blogs' && !initBlogLoad && '${dataCourse}'==0){
        	$("#all-result-blogs .loader").show();
        	initBlogLoad = true;
        	loadOtherResult("<%=searchBlogResourceURL.toString()%>","blogs");
        }else if(id=='pressreleases' && !initPressLoad && '${dataCourse}'==0){
        	$("#all-result-pressreleases .loader").show();
        	initPressLoad = true;
        	loadOtherResult("<%=searchPressResourceURL.toString()%>","pressreleases");
        }else if(id=='news' && !initNewsLoad && '${dataCourse}'==0){
        	$("#all-result-news .loader").show();
        	initNewsLoad = true;
        	loadOtherResult("<%=searchNewsResourceURL.toString()%>","news");
        }
        
        if (!$(this).hasClass('active')) {
            $(".box-item").removeClass('active');
            $(this).addClass('active');

            $('.content-data').hide();
            $("[data-content='"+id+"']").fadeIn();
        }
    });
    $(document).on("click", ".control, .sub-content", function (e) {
        e.stopPropagation();
    });
</script>

<!-- SEPERATE -->

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
	
	function triggerChangeExams() {
		loadDataExams('<%=searchExamResourceURL.toString()%>');
	}
	
	function generatePaginationOthers(url,data, id){
   		var container = null;
   		var locators = null;
   		console.log('imp::::3010:::'+data);
   		
   		if(id=='blogs'){
   			container = $('#blogs-pagination');
   			locators="blogs";
        }else if(id=='pressreleases'){
        	container = $('#pressreleases-pagination');
        	locators="pressReleases";
        }else if(id=='news'){
        	container = $('#news-pagination');
        	locators="news";
        }
   		
   		container.pagination({
   			dataSource: url,
   			locator: locators,
   			totalNumber: data.resCount+1,
   			ulClassName: 'justify-content-center',
   		    pageSize: paginationSize,
	   		alias: {
	   			
	   		    pageNumber: '<portlet:namespace/>pageNum',
	   		    pageSize: '<portlet:namespace/>limit'
	   		},
   		    ajax: {
   		    	type : "POST",
   		    	cache : false,
   		        beforeSend: function() {
   		        	
   		        	container.prev().html(pageTransition());
   		        }
   		    },
            callback: function(response, pagination){                 	
				if(id=='exams'){
		        	
		        }else if(id=='blogs'){		        	
		        	var htmlBlogs = '';
	              	if(response.length == 0) {
	              		htmlBlogs += generateNullResult(false);
	              	} else {
	              		$.each(response, function (index, blogs) {
	              			htmlBlogs += generateOthersResult('blogs', blogs); 
	              			console.log('blog::::::::::::3010z::::::::::::::::'+ console.log (JSON.stringify (blogs)));
		               	});	
	              	}
	               	container.prev().html(htmlBlogs);
	               	$('#all-result-blogs .bcol.content').show();
		        }else if(id=='pressreleases'){		        	
		        	var htmlPressReleases = '';
	              	if(response.length == 0) {
	              		htmlPressReleases += generateNullResult(false);
	              	} else {
	              		$.each(response, function (index, pressReleases) {
	              			htmlPressReleases += generateOthersResult('press releases', pressReleases); 
		               	});	
	              	}
	               	container.prev().html(htmlPressReleases);
	               	$('#all-result-pressreleases .bcol.content').show();
		        }else if(id=='news'){
					var htmlNews = '';
	              	if(response.length == 0) {
	              		htmlNews += generateNullResult(false);
	              	} else {
	              		$.each(response, function (index, news) {
	              			htmlNews += generateOthersResult('news', news); 
		               	});	
	              	}
	               	container.prev().html(htmlNews);
	               	$('#all-result-news .bcol.content').show();
		        }
               	
               	$('html, body').animate({
	                scrollTop: container.prev().offset().top - 300
	            }, 1000);
              }
   		})
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
	              		htmlCourses += generateNullResult(true);
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
	 
	 function generatePaginationExams(data){
	   		var container = $('#exams-pagination');
	   		console.log('imp::::3010:::'+data);
	   		container.pagination({
	   			dataSource: data,
	   			locator: 'exams',
	   			totalNumber: data.length+1,
	   			ulClassName: 'justify-content-center',
	   		    pageSize: paginationSize,
		   		alias: {
		   		
		   		    pageNumber: '<portlet:namespace/>pageNum',
		   		    pageSize: '<portlet:namespace/>limit'
		   		},
	   		    ajax: {
	   		    	type : "POST",
	   		    	cache : false,
	   		        beforeSend: function() {
	   		        	
	   		        	container.prev().html(pageTransition());
	   		        }
	   		    },
	            callback: function(response, pagination){   	
	              	var htmlExams = '';
	              	if(response.length == 0) {
	              		htmlExams += generateNullResult(true);
	              	} else {
	              		$.each(response, function (index, exams) {
	              			htmlExams += generateCourseResultExams(exams); 
		               	});	
	              	}
	               	container.prev().html(htmlExams);
	               	
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
	                $('#courses-result .loader').removeClass('hidden')
        		},
		        success : function(data,textStatus,XMLHttpRequest){
	                generatePagination('<%=searchResourceURL.toString()%>',data.total, filters);
				},
				complete: function () { // Set our complete callback, adding the .hidden class and hiding the spinner.
	            	$('#courses-result .loader').hide();
	            },
				error : function(data,textStatus,XMLHttpRequest){
					
				}
			});
		}
	 
	 function loadDataExams(url) {
		 	var sortIdx = document.getElementById("priceFilterExams").selectedIndex;
	        var sortId = document.getElementById("priceFilterExams").options[sortIdx].value;
	        
	        var categoryIdsFilter = [];
	        $('.category-exams-filter:checkbox:checked').each(function(i) {
				categoryIdsFilter.push(parseInt($(this).val()));
	        });
	        	        
			return jQuery.ajax({
				url : url,
				type : "POST",
				dataType : "json",
				async : "true",
				cache : "true",
				data : {
   		    		<portlet:namespace/>orders: sortId,
   		    		<portlet:namespace/>categoryIds: categoryIdsFilter
   		    	},
				beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
	                $('#exams-result .loader').removeClass('hidden')
     			},
		        success : function(data,textStatus,XMLHttpRequest){
	                generatePaginationExams(data);
	                
	                var htmlCategoryList="";
	                if (data.categoryList.length > 0) {
	                    data.categoryList.forEach(function(categoryList) {
	                    	htmlCategoryList += generateCategoryExamsList(categoryList);
	                    });
	                }
	                if(categoryIdsFilter.length<=0){
	                	$('#filter-category').html(htmlCategoryList);
	                }
				},
				complete: function () { // Set our complete callback, adding the .hidden class and hiding the spinner.
	            	$('#exams-result .loader').hide();
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
	 
	function loadOtherResult(url, id) {
		return jQuery.ajax({
			url : url,
			type : "POST",
			dataType : "json",
			async : "false",
			cache : "false",
			beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
                $('#all-result-blogs .loader').removeClass('hidden');
                $('#all-result-news .loader').removeClass('hidden');
                $('#all-result-pressreleases .loader').removeClass('hidden');
                  
    		},
			success : function(data,textStatus,XMLHttpRequest){
				
		        	
				generatePaginationOthers(url,data, id);								
			},
			complete: function () { // Set our complete callback, adding the .hidden class and hiding the spinner.
            	$('#all-result-blogs .loader').hide();
                $('#all-result-news .loader').hide();
                $('#all-result-pressreleases .loader').hide();
            },
			error : function(data,textStatus,XMLHttpRequest){
				
			}
		});
	}
	
	function generateNullResult(course) {
		var html = '<div class="col-xl-12 col-lg-12 col-12 bcol">';
		html += '<div class="card inner imgeffect hoverpp">'
		html += '<div class="card-body">'
		if(course) {
			html += '<p class="text-center"> No pages found under <i>"${keyword}"</i> .</p>'	
		} else {
			html += '<p class="text-center"> No pages found under <i>"${keyword}"</i> .</p>'
		}
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
        html += '<img src="' + data.urlImage + '" class="bgimg"/>';
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
        html += '<a href="' + url.split('?')[0] + '" target="_blank" class="fxlink">View detail</a>';
        html += '</div>';
        html += '</div>';
        return html;
    }
	
	function generateCourseResultExams(data) {
        var html = '<div class="col-xl-3 col-lg-4 col-6 bcol">';
        html += '<div class="inner imgeffect hoverpp">';
        html += '<figure class="imgwrap" style="height: 176.083px;">';
        html += '<div class="bg" style="background-image: url(' + data.urlImage +
            ');">';
        html += '<img src="' + data.urlImage + '" class="bgimg"/>';
        html += '</div>'
        html += '</figure>';
        html += '<div class="box-content" style="height: 150px;">'
        html += '<div class="box-status">';
        html += '</div>'
        html += '<p class="title mb-1" style="height: 61.3333px;">' +
            data.title + '</p>';

        if (data.price != "0") {
        	var floatPrice = data.price;
        	html += '<div class="float-right small-text">';
        	if(data.originalPrice>0){
        		html += '<strong class="price-normal" style="margin-right:20px;">$ ' + (data.originalPrice).toFixed(2) + '</strong>';
        	}
            html += '<strong class="price" style="margin-right:20px;">$ ' + floatPrice.toFixed(2) + '</strong>';
        	html += '</div>';
        }else{
            html += '<strong class="price float-right" style="margin-right:20px;">&nbsp;</strong>';
        }

        html += '</div>';
        var url = ''+data.urlMore;
        html += '<a href="' + url.split('?')[0] + '" target="_blank" class="fxlink">View detail</a>';
        html += '</div>';
        html += '</div>';
        return html;
    }
	
	function generateOthersResult(name, data) {
		var html = '<div class="grid-7">';
		html += '<div class="bcol title">';
		html += '<h3>'+name+'</h3></div>';
		html += '<div class="bcol content">';
		html += '<a href="'+data.urlMore+'" target="_blank"><h4>'+data.title+'</h4></a>';
		html += '<p>'+data.desc+'</p>';
		html += '<br><h6>'+data.date+'</h6></div>';
		html += '</div>';
		return html;
	}
	
	function generateCategoryExamsList(data) {
		var obj = data.JSONObject;
        var html = '<div class="col-lg-4 col-md-4 col-6 bcol">';
        html += '<div class="checkbox checktype">';
        html += '<input class="category-exams-filter" type="checkbox" name="' + obj.id+ '" onclick="triggerChangeExams()" id="' + obj.id 
        	+ '" value="' + obj.id + '" /> <label for="' + obj.id + '">' +
        	obj.name + '</label>';
        html += '</div>'
        html += '</div>'
        html += '</div>'
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
	    
	    function resetFilterExams() {
	    	$('.category-exams-filter:checkbox').prop('checked',false); 
	    	
            loadDataExams('<%=searchExamResourceURL.toString()%>');
	    }

</script>

