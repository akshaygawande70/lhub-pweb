<#--  <div class="commerce-topbar ${speedwell_topbar_css_class}">
	<div class="speedwell-topbar__actions">  -->
	<#if is_setup_complete || !is_signed_in>
		<#--  <div class="speedwell-topbar__search speedwell-search">  -->
			<#--  <div class="speedwell-topbar__button js-toggle-search">
				<svg class="lexicon-icon lexicon-icon-search">
					<use href="${themeDisplay.getPathThemeImages()}/icons.svg#search" />
				</svg>
			</div>  -->

			<#--  <div class="speedwell-search__bar-wrapper">
				<div class="speedwell-search__bar">
					<button class="speedwell-topbar__button" disabled>
						<svg class="lexicon-icon lexicon-icon-search">
							<use href="${themeDisplay.getPathThemeImages()}/icons.svg#search" />
						</svg>
					</button>  -->
					<@liferay_commerce_ui["search-bar"] id="search-bar" />
<#--  
					<button class="speedwell-topbar__button js-toggle-search">
						<svg class="lexicon-icon lexicon-icon-times">
							<use href="${themeDisplay.getPathThemeImages()}/icons.svg#times" />
						</svg>
					</button>
				</div>
			</div>  -->


		<#--  </div>  -->



		<div class="speedwell-topbar__cart-wrapper speedwell-cart">
			<@liferay_commerce_ui["mini-cart"] spritemap="${themeDisplay.getPathThemeImages()}/icons.svg" />
		</div>
	</#if>
		<@liferay_commerce_ui["mini-cart"] spritemap="${themeDisplay.getPathThemeImages()}/icons.svg" />
	<#--  </div>
</div>  -->