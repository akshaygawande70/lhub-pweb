# NTUC API Modules – JUnit Coverage Suitability

> Binary Excel artifacts are no longer checked into the repository; regenerate locally via
> `python reports/scripts/generate_notification_api_coverage.py` when you need a fresh download.

The following table lists each class under the `ntuc-common-api` and `ntuc-content-api` modules with notes about their responsibilities, dependencies, and suitability for achieving >85% JUnit coverage (green = straightforward, amber = moderate effort, red = higher effort).

| Module | Class | Responsibility | Coverage Outlook | Suggested Test Focus |
| --- | --- | --- | --- | --- |
| ntuc-common-api | `api.ntuc.common.dto.ResultDto` | POJO carrying course/content metadata fields with getters/setters. | Easy (green) – simple constructor and accessors. | Instantiate with sample data, verify getters/setters, equals-like behavior if added. |
| ntuc-common-api | `api.ntuc.common.dto.UserOtpDto` | DTO for OTP payload data fields. | Easy (green) – only state and accessors. | Validate field assignment and default constructor behavior. |
| ntuc-common-api | `api.ntuc.common.CommonApi` | OSGi marker interface for API exposure. | Easy (green) – no logic. | Interface presence; can be covered by simple instantiation in tests using implementing classes. |
| ntuc-common-api | `api.ntuc.common.api.NtucCommonApi` | Service interface defining portal utility methods. | Medium (amber) – interface methods rely on portal context. | Use mocks to validate default behaviors or stub implementations in tests. |
| ntuc-common-api | `api.ntuc.common.constants.CourseAdminConstant` | Constants for course admin keys. | Easy (green) – static fields only. | Static constant assertions. |
| ntuc-common-api | `api.ntuc.common.constants.PortletCommandConstant` | Constants for portlet command names. | Easy (green). | Static constant assertions. |
| ntuc-common-api | `api.ntuc.common.constants.OtpConstant` | OTP-related constant values. | Easy (green). | Static constant assertions. |
| ntuc-common-api | `api.ntuc.common.constants.PortletDisplayCategoryConstant` | Category constants for portlets. | Easy (green). | Static constant assertions. |
| ntuc-common-api | `api.ntuc.common.constants.SearchConstant` | Search parameter constants. | Easy (green). | Static constant assertions. |
| ntuc-common-api | `api.ntuc.common.exception.NtucException` | Custom runtime exception for NTUC. | Easy (green) – thin subclass. | Verify message/causes propagate. |
| ntuc-common-api | `api.ntuc.common.filter.NtucActionFilter` | Liferay action filter wrapper delegating to provided logic. | Medium (amber) – interacts with portal filter chain. | Use Mockito for ActionRequest/Response to verify delegation and attribute handling. |
| ntuc-common-api | `api.ntuc.common.filter.NtucRenderFilter` | Render-phase filter similar to action filter. | Medium (amber). | Mock render request/response and chain execution. |
| ntuc-common-api | `api.ntuc.common.filter.NtucResourceFilter` | Resource-phase filter wrapper. | Medium (amber). | Mock resource request/response and chain; assert attribute behavior. |
| ntuc-common-api | `api.ntuc.common.override.OverrideActionRequestParam` | Wrapper to override action request parameters. | Medium (amber) – extends Liferay request wrapper. | Mock underlying request to confirm overridden parameter map. |
| ntuc-common-api | `api.ntuc.common.override.OverrideRenderRequestParam` | Wrapper for render requests overriding parameters. | Medium (amber). | Mock render request parameter map access. |
| ntuc-common-api | `api.ntuc.common.override.OverrideResourceRequestParam` | Wrapper for resource request parameters. | Medium (amber). | Mock resource request parameter override behavior. |
| ntuc-common-api | `api.ntuc.common.util.AESEncryptUtil` | AES encryption/decryption helper using JCE. | Hard (red) – static state, cryptography, logging. | Use deterministic key/inputs; verify encrypt/decrypt, assert null handling on exceptions. |
| ntuc-common-api | `api.ntuc.common.util.AfiCaptchaUtil` | CAPTCHA verification helper hitting remote API. | Hard (red) – HTTP dependency. | Mock HTTP client/URL connections; test success/failure parsing. |
| ntuc-common-api | `api.ntuc.common.util.ComparationUtil` | Equality helpers for strings/objects. | Easy (green). | Parameterized tests for equality checks and null safety. |
| ntuc-common-api | `api.ntuc.common.util.CompressImageUtil` | Image compression helper using external library. | Hard (red) – file/I/O and image processing. | Use temp files and sample images; mock dependencies if possible. |
| ntuc-common-api | `api.ntuc.common.util.CSRFValidationUtil` | CSRF validation using token comparisons. | Medium (amber) – depends on portal utilities. | Mock PortalUtil/HttpServletRequest to cover success/failure branches. |
| ntuc-common-api | `api.ntuc.common.util.CurrencyUtil` | Currency formatting helper. | Easy (green). | Validate formatting for sample inputs/locales. |
| ntuc-common-api | `api.ntuc.common.util.DateUtil` | Date/time formatting and parsing helpers. | Medium (amber) – timezone/format branches. | Parameterized tests over patterns and null handling. |
| ntuc-common-api | `api.ntuc.common.util.DocumentMediaUtil` | Media/document type detection and URL helpers. | Medium (amber) – uses portal classes. | Mock DLFileEntry/ServiceContext to assert behavior. |
| ntuc-common-api | `api.ntuc.common.util.DownloadInvoiceUtil` | Builds invoice download URLs and tokens. | Medium (amber). | Mock PortalUtil/themeDisplay to verify URL construction. |
| ntuc-common-api | `api.ntuc.common.util.EmailUtil` | Email composition and sending helpers. | Hard (red) – JavaMail dependency. | Inject/mocks for MailServiceUtil; verify content building branches. |
| ntuc-common-api | `api.ntuc.common.util.ExportUtil` | CSV/XLS export utility. | Hard (red) – file streams and encoding. | Use temp streams; assert headers/rows produced. |
| ntuc-common-api | `api.ntuc.common.util.GsonUtil` | Gson serializer/deserializer helpers. | Easy (green). | Round-trip serialization tests and null handling. |
| ntuc-common-api | `api.ntuc.common.util.HttpApiUtil` | HTTP client helper for API calls. | Hard (red) – networking and IO. | Mock HttpURLConnection/responses; test timeout/error branches. |
| ntuc-common-api | `api.ntuc.common.util.HttpGetWithEntity` | Custom HttpGet supporting request entity. | Medium (amber) – extends Apache HttpGet. | Verify entity storage and headers; no real network calls. |
| ntuc-common-api | `api.ntuc.common.util.PermissionUtil` | Permission checking helper using Liferay services. | Hard (red) – heavy portal dependencies. | Use PowerMockito/Mockito for static portal utilities; branch coverage on roles. |
| ntuc-common-api | `api.ntuc.common.util.PortletCommandUtil` | Builds/executes portlet commands. | Hard (red) – depends on portal request context. | Mock Portlet/ThemeDisplay interactions. |
| ntuc-common-api | `api.ntuc.common.util.RedirectUtil` | Redirect helper building URLs. | Medium (amber). | Mock HttpServletRequest/ThemeDisplay to verify URL outputs. |
| ntuc-common-api | `api.ntuc.common.util.RequestParameterUtil` | Safely reading request parameters. | Medium (amber). | Mock PortletRequest/HttpServletRequest to cover null/default branches. |
| ntuc-common-api | `api.ntuc.common.util.RoleUtil` | Role lookup helper via portal APIs. | Hard (red) – portal user/role dependencies. | Mock RoleLocalServiceUtil/UserLocalServiceUtil; test missing roles cases. |
| ntuc-common-api | `api.ntuc.common.util.XSSValidationUtil` | XSS detection using regex/Jsoup checks. | Medium (amber). | Parameterized tests for safe/unsafe inputs. |
| ntuc-common-api | `api.ntuc.common.myinfo.connector.ApplicationConstant` | Constants for MyInfo integration. | Easy (green). | Assert constant values remain unchanged. |
| ntuc-common-api | `api.ntuc.common.myinfo.connector.CertUtil` | Certificate loader for MyInfo integration. | Hard (red) – file/keystore I/O. | Use in-memory streams or temp files for keystore loading. |
| ntuc-common-api | `api.ntuc.common.myinfo.connector.MyInfoConnector` | Handles OAuth token and person API calls to MyInfo. | Hard (red) – network + cryptography. | Mock HTTP responses, JSON parsing, and exception paths. |
| ntuc-common-api | `api.ntuc.common.myinfo.connector.MyInfoException` | Custom exception wrapper for MyInfo errors. | Easy (green). | Assert message/causes. |
| ntuc-common-api | `api.ntuc.common.myinfo.connector.MyInfoSecurityHelper` | Signing/encryption helper for MyInfo requests. | Hard (red) – cryptographic operations. | Use known fixtures to verify signatures and error handling. |
| ntuc-common-api | `api.ntuc.common.myinfo.connector.StringUtil` | String helper for MyInfo (base64, signing helpers). | Medium (amber). | Unit test encoding/decoding and null handling. |
| ntuc-content-api | `api.ntuc.nlh.content.api.NtucContentApi` | Public interface for content search/render helpers. | Medium (amber) – depends on portal context. | Mock portal search services to exercise methods. |
| ntuc-content-api | `api.ntuc.nlh.content.ContentApi` | OSGi marker interface. | Easy (green). | Interface presence; covered via implementing class tests. |
| ntuc-content-api | `api.ntuc.nlh.content.constant.ContentConstants` | Static constants for content keys. | Easy (green). | Constant assertions. |
| ntuc-content-api | `api.ntuc.nlh.content.dto.ConfigListDto` | DTO representing configuration items. | Easy (green). | Getter/setter coverage. |
| ntuc-content-api | `api.ntuc.nlh.content.dto.FilterCoursesDto` | DTO carrying course filter criteria. | Easy (green). | Getter/setter coverage. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.AssetRendererFactoryLookup` | Interface for locating asset renderer factories. | Easy (green). | Provide stub implementation in tests. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.DocumentFormPermissionChecker` | Interface for permission checks. | Easy (green). | Stub implementation coverage. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.DocumentFormPermissionCheckerImpl` | Default permission checker using portal services. | Medium (amber) – portal dependencies. | Mock Role/Permission services to cover allow/deny branches. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.PortletRequestThemeDisplaySupplier` | Supplies ThemeDisplay from portlet request. | Medium (amber). | Mock ThemeDisplay extraction and null handling. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.PortletURLFactory` | Interface for creating portlet URLs. | Easy (green). | Stub implementations in tests. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.PortletURLFactoryImpl` | Implementation building render/resource/action URLs. | Medium (amber). | Mock PortletRequest/Response to verify URL creation branches. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.SearchPortletSearchResultPreferences` | Reads search preferences from portlet configuration. | Medium (amber) – depends on PortletPreferences. | Mock preferences for defaults and overrides. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.SearchResultFieldDisplayContext` | Value object describing how search fields should render. | Easy (green). | Constructor/getter coverage. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.SearchResultPreferences` | Aggregates preferences for search rendering. | Medium (amber). | Construct with mock preferences and assert derived values. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.SearchResultSummaryDisplayBuilder` | Builder assembling summary display contexts. | Medium (amber). | Use mocked dependencies and sample search documents to validate output. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.SearchResultSummaryDisplayContext` | POJO for search summary display settings. | Easy (green). | Getter coverage. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.SearchResultViewURLSupplier` | Supplies URLs for search result views. | Medium (amber). | Mock asset renderer and theme display interactions. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.SearchUtil` | Helper to build search queries and summaries. | Hard (red) – integrates with Liferay search APIs. | Use mocks for SearchContext/Indexer; cover null/error paths. |
| ntuc-content-api | `api.ntuc.nlh.content.engine.ThemeDisplaySupplier` | Functional interface supplying ThemeDisplay. | Easy (green). | Lambda-based tests verifying supply behavior. |
| ntuc-content-api | `api.ntuc.nlh.content.util.CommonDateResult` | Date wrapper for search results. | Easy (green). | Getter/setter coverage with sample date. |
| ntuc-content-api | `api.ntuc.nlh.content.util.ContentUtil` | Utility for content extraction and URL handling. | Medium (amber) – portal/search dependencies. | Mock AssetEntry/SearchDocument interactions; verify null handling. |
