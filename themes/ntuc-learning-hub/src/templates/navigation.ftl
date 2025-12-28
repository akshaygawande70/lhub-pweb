<div class="${nav_blog} box-menu-blog">
<nav class="menu ${nav_css_class} navbar blog-status" id="navigation" role="navigation" style="position:relative;">

	<ul aria-label="<@liferay.language key="site-pages" />" role="menubar" class="navbar-nav">	
		<#list nav_items as nav_item>
			<#assign
				nav_item_attr_has_popup = ""
				nav_item_css_class = ""
				nav_other_child_css =""
				nav_item_layout = nav_item.getLayout()
			/>

			<#if nav_item.isSelected()>
				<#assign
					nav_item_attr_has_popup = "aria-haspopup='true'"
					nav_item_css_class = "selected"
				/>
			</#if>
			<#if nav_item.hasChildren()>
				<#assign nav_item_has_class = "has-child"/>
				<#assign no_href=nav_item.getURL()/>
				<#--  <#assign no_href="javascript:void(0)"/>  -->
			<#else>
				<#assign nav_item_has_class = ""/>
				<#assign no_href=nav_item.getURL()/>
			</#if>

			<#if nav_item.getName() == "Courses" || nav_item.getName() == "Certification &amp; Exams" || nav_item.getName() == "Consultancy Services"  || nav_item.getName() == "Promotions and Packages" >
				<#assign no_href="javascript:void(0)"/>
			</#if>
			
			<#if nav_item.getName() != "Courses">
				<#assign nav_other_child_css ="Others"/>
			</#if>

			<li class="${nav_item_css_class} ${nav_item.getName()} ${nav_item_has_class} list-menu" id="layout_${nav_item.getLayoutId()}" role="presentation">
				<a aria-labelledby="layout_${nav_item.getLayoutId()}" ${nav_item_attr_has_popup} href="${no_href}" ${nav_item.getTarget()} role="menuitem" class="nav-link">
					<span><@liferay_theme["layout-icon"] layout=nav_item_layout /> ${nav_item.getName()}</span>

					<#if nav_item.hasChildren()>
						<a href="${no_href} " class="asub-menu">
						<span class="hidden show_child subarrow menusub"></span>	
					</a>	
					</#if>

				</a>

				<#if nav_item.hasChildren()>
					<#--  <div class="box-child-menu">
						<div class="container CX-wrapper">
							<div class="child-menu" role="menu">  -->
							<div class="mn-sub colsub-1">
        								<div class="sub-inner">
										<ul class="lv1">
								<#list nav_item.getChildren() as nav_child>
									<#assign
										nav_child_css_class = ""
									/>

									<#if nav_child.isSelected()>
										<#assign
											nav_child_css_class = "selected"
										/>
									</#if>

									<#if nav_child.hasChildren()>
										<#assign nav_child_has_class = "hasSub"/>
										<#--  <#assign no_href="javascript:void(0)"/>  -->
										<#assign no_href=nav_child.getURL()/>
									<#else>
										<#assign nav_child_has_class = ""/>
										<#assign no_href=nav_child.getURL()/>
									</#if>

									<li class="${nav_child_css_class} ${nav_other_child_css}" id="layout_${nav_child.getLayoutId()}" role="presentation">
										<a aria-labelledby="layout_${nav_child.getLayoutId()} ${nav_child_has_class}" class="${nav_child_has_class}" href="${no_href}" ${nav_child.getTarget()} role="menuitem">
											${nav_child.getName()}
											<#if nav_child.hasChildren()>
												<span class="hidden show_child2 subarrow"></span>	
											</#if>
										</a>
									
										
										<#--  children level 2  -->
										<#if nav_child.hasChildren()>
											<ul class="child-menu lv2" role="menu">
												<#list nav_child.getChildren() as nav_child2>
													<#assign
														nav_child2_css_class = ""
													/>

													<#if nav_child2.isSelected()>
														<#assign
															nav_child2_css_class = "selected"
														/>
													</#if>

													<li class="${nav_child2_css_class}" id="layout_${nav_child2.getLayoutId()}" role="presentation">
														<a aria-labelledby="layout_${nav_child2.getLayoutId()} ${nav_child_has_class}" href="${nav_child2.getURL()}" ${nav_child2.getTarget()} role="menuitem">
														${nav_child2.getName()}
														<#if nav_child.hasChildren()>
															<span class="subarrow"></span>	
														</#if>
														</a>
													
													
													<#--Childen level 3 -->
															<#--  <#if nav_child.hasChildren()>
																<ul class="child-menu lev-3" role="menu">
																	<#list nav_child.getChildren() as nav_child3>
																		<#assign
																			nav_child2_css_class = ""
																		/>

																		<#if nav_child3.isSelected()>
																			<#assign
																				nav_child2_css_class = "selected"
																			/>
																		</#if>

																		<li class="${nav_child2_css_class}" id="layout_${nav_child3.getLayoutId()}" role="presentation">
																			<a aria-labelledby="layout_${nav_child3.getLayoutId()}" href="${nav_child3.getURL()}" ${nav_child3.getTarget()} role="menuitem">${nav_child3.getName()}</a>
																		</li>
																	</#list>
																</ul>
															</#if>  -->
													<#--end-->
													
													<#--  </li>  -->
												</#list>
											</ul>
										</#if>
										<#--  children level 2  -->
										

									
									</li>
								</#list>
								</ul>
								</div>
										</div>
							<#--  </div>					
						</div>
					</div>  -->

				</#if>
			</li>
		</#list>
	</ul>
</nav>


<#--FOR Blog-->
<#--  <nav class="menu menu-blog">
    <ul>
        <li><a href="/home"><i class="fas fa-home"></i></a></li>  -->
		<@embedJournalArticle journalArticleTitle="Blog" portletInstanceId="blog" />
<#--  		
        <li><a href="/lhub-news">LHUB News</a></li>
        <li><a href="/stories">Stories</a></li>
        <li><a href="/for-learners">For Learners</a></li>
        <li><a href="/for-business-leaders">For Business Leaders</a></li>  -->
    <#--  </ul>
</nav>  -->
<#--End for blog-->
</div>