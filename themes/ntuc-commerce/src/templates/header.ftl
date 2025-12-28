<#--  wajib ada  -->
<div class="d-none">
  <@liferay_commerce_ui["search-bar"] id="search-bar" />
</div>
<#--  wajib ada end -->
<header class="header-container">
<div class="box-header">
<@embedJournalArticle journalArticleTitle="Notice Header" portletInstanceId="notice header" />

        <div class="logo">
            <a data-senna-off="true" href="${base_url}">
                <img class="logo-1" src="${images_folder}/ntuc-learning-white.png" alt="NTUC LearningHub" />
                <img class="logo-2" src="${images_folder}/logo-ntuc.png" alt="NTUC LearningHub" />
            </a>
        </div>
        
        <#--  <div class="mobile-cart cart-header">
           <@liferay_commerce_ui["mini-cart"] spritemap="${themeDisplay.getPathThemeImages()}/icons.svg" /> 
        </div>        -->
        <div class="mobile-cart btn-cart-custom">
            <i class="fas fa-shopping-cart"></i>
            <span class="text-count"></span>
        </div>

       <#if is_signed_in>
          <span class="btn-login-mb show-xl gotouser" ><i class="fas fa-user"></i></span>
        <#else>
          <a href="/login-landing">   
            <span class="btn-login-mb show-xl no-gotouser" ><i class="fas fa-user"></i></span>
          </a>
        </#if>
        <#--  <a href="#menu" class="control-page btn-menu">
            <span></span>
            <span></span>
            <span></span>
        </a>  -->
        <div class="menu-mobile">
            <i class="fas fa-bars"></i>
        </div>
        <div id="menu" class="mn-wrap">
        
            <div class="link-wrap">
            <div class="overlay-menu-open"></div>
            <div class="close-menu-mobile">
            <i class="fas fa-times"></i>
        </div>
                <!--                <a class="btn-login hide-xl" href="#login-pp" data-toggle="modal"><i class="fas fa-user"></i> Login</a>-->
                <div class="btn-login hide-xl custom-user" >
                <#if is_signed_in>
                  <div class="user-header">
                      <div class="box-user">
                        <img src="${user_portrait}">
                        <span>${htmlUtil.escape(user_name)}</span>
                      </div>
                  </div>
                <#else>
                <a href="/login-landing" style="color:#fff">
                <div class="no-user-header">
                    <i class="fas fa-user"></i>
                </div>
                </a>
                </#if>
                

                    <#--  <div class="cart-header">
                        <@liferay_commerce_ui["mini-cart"] spritemap="${themeDisplay.getPathThemeImages()}/icons.svg" /> 
                     <span class="text-cart">Cart<span>
                    </div>  -->
                   <div class="btn-cart-custom">
                      <i class="fas fa-shopping-cart"></i>
                      <div class="box-count">
                        <span class="text-count"></span>
                      </div>
                      <span class="text-cart">Cart</span>
                  </div>
              
                 
                </div>
                
                <ul>
                  <@embedJournalArticle journalArticleTitle="Header" portletInstanceId="header" /> 
            </ul>            
          </div>
            
		<#include "${full_templates_path}/navigation.ftl" />
            
        </div>


        <!---start-->
                <div class="overlay-login"></div>
                <div class="box-login-user">
                  <#if is_signed_in>
                  <div class="list-menu-user">
                    <div class="text-blue">Welcome, <span>${htmlUtil.escape(user_name)}</span></div>
                    <div class="list-item"><a href="/account/profile">My Account</a></div>
                    <div class="list-item"><a href="/account/course_listing">Registered Course</a></div>
                    <div class="list-item"><a href="/account/courseinvoices">Invoices</a></div>
                    <div class="list-item"><a href="${logout_url}">Logout</a></div>
                  </div>
                  <#else>
                      <a href="/registration">Login</a>
                      <a href="https://ntuc.instructure.com/login/canvas">Canvas Login</a>
                  </#if>
                </div>
                <!---end-->
<#include "${full_templates_path}/global_search.ftl" />

        </div>
    </header>

    <!---End HAEDER -->
