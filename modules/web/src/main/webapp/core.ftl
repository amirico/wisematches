<#ftl encoding="UTF-8">

<#import "spring.ftl" as spring />
<#import "/content/layout.ftl" as wisematches />
<#import "/ext/custom-element.ftl" as ext />

<#include "/content/messages.ftl"/>

<#assign locale="${springMacroRequestContext.getMessage('locale')}"/>

<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />