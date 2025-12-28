<header class="header-container "><!--old-user-->
<div class="box-header">
<@embedJournalArticle journalArticleTitle="Notice Header" portletInstanceId="notice header" />
        <div class="logo">
            <a data-senna-off="true" href="${base_url}">
                <img class="logo-1" src="${images_folder}/ntuc-learning-hub-logo.png" alt="NTUC LearningHub" />
                <img class="logo-2" src="${images_folder}/ntuc-learning-hub-logo-2.png" alt="NTUC LearningHub" />
            </a>
        </div>
        <#if is_signed_in>
          <span class="btn-login-mb show-xl gotouser" ><i class="fas fa-user"></i></span>
        <#else>
          <a href="/login-landing">   
            <span class="btn-login-mb show-xl no-gotouser" ><i class="fas fa-user"></i></span>
          </a>
        </#if>
        <a href="#menu" class="control-page btn-menu">
            <span></span>
            <span></span>
            <span></span>
        </a>
        <div id="menu" class="mn-wrap">
            <div class="link-wrap">
      <#--  <a class="btn-login hide-xl" href="https://ntuc.instructure.com/login/canvas" target="_blank"><i class="fas fa-user"></i> Canvas Login</a>  -->
                <div class="btn-login hide-xl btn-login-cs" href="#" target="_blank">
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
                  
                      <#if is_setup_complete || !is_signed_in>
                      <div class="cart-header">
                      </div>
                  </#if>
                  </div>


                <ul>
                   <@embedJournalArticle journalArticleTitle="Header" portletInstanceId="header" />
                  <#--  <li id="topmenu532" class="">
                      <a href='/about-us' >About Us</a>
                    <ul>
                      <li id="topmenu31658"><a href='/web/guest/overview' data-senna-off="true">Overview</a></li>
                      <li id="topmenu36784"><a href='/web/guest/awards-and-milestones' data-senna-off="true">Awards and Milestones</a></li>
                      <li id="topmenu646"><a href='/web/guest/board-of-director' data-senna-off="true">Board of Directors</a></li>
                      <li id="topmenu645"><a href='/web/guest/our-team' data-senna-off="true">Our Team</a></li>
                      <li id="topmenu644"><a href='/web/guest/academic-exam-board' data-senna-off="true">Academic & Exam Board</a></li>
                      <li id="topmenu36783"><a href='/web/guest/our-partners' data-senna-off="true">Our Partners</a></li>
                      <li id="topmenu31774"><a href='/web/guest/our-trainers' data-senna-off="true">Our Trainers</a></li>
                      <li id="topmenu38764"><a href='/web/guest/our-privacy-policy' data-senna-off="true">Our Privacy Policy</a></li>
                    </ul>
                  </li>
                <li id="topmenu5290" class="">
                    <a href='#'>Events</a>
                  <ul>
                    <li id="topmenu726"><a href='/web/guest/events' data-senna-off="true">Events</a></li>
                    <li id="topmenu5305"><a href='/web/guest/upcoming-events' data-senna-off="true">Upcoming Events</a></li>
                    <li id="topmenu5389"><a href='/web/guest/past-events' data-senna-off="true">Past Events</a></li>
                  </ul>
                </li>
                <li id="topmenu703" class="">
                    <a href='/web/guest/media' data-senna-off="true">Media</a>
                </li>
                <li id="topmenu668" class="">
                  <a href='/web/guest/blog' data-senna-off="true">Blog</a>
                </li>
                <li id="topmenu4140" class="">
                  <a href='/web/guest/contact-us' data-senna-off="true">Contact Us</a>
                  <ul>
                    <li id="topmenu23938"><a href='/web/guest/faq' data-senna-off="true">FAQs</a></li>
                    <li id="topmenu4142"><a href='/web/guest/contact-us-form' data-senna-off="true">Contact Us</a></li>
                    <li id="topmenu33048"><a href='/web/guest/careers' data-senna-off="true">Careers</a></li>
                    <li id="topmenu4839"><a href='/web/guest/gig-marketplace' data-senna-off="true">GIG Marketplace</a></li>
                  </ul>
                </li>  -->
            </ul>            </div>
            
		<#include "${full_templates_path}/navigation.ftl" />
            
        </div>
        <!--start-->
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
                  <!--End-->
        <#include "${full_templates_path}/global_search.ftl" />
        </div>
    </header>

    <!---End HAEDER -->
