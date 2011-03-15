<#ftl encoding="UTF-8">

<#assign locale="${springMacroRequestContext.getMessage('locale')}"/>
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<#macro message code args=[]><#if args?size == 0>${springMacroRequestContext.getMessage(code)}<#else>${springMacroRequestContext.getMessage(code, args)}</#if></#macro>

<#macro messageCapFirst code args=[]><#if args?size == 0>${springMacroRequestContext.getMessage(code)?cap_first}<#else>${springMacroRequestContext.getMessage(code, args)?cap_first}</#if></#macro>

<#macro messageCap code args=[]><#if args?size == 0>${springMacroRequestContext.getMessage(code)?capitalize}<#else>${springMacroRequestContext.getMessage(code, args)?capitalize}</#if></#macro>

<#import "spring.ftl" as spring />
<#import "/content/templates/common.ftl" as wm />
<#import "/content/templates/declension.ftl" as declension />

