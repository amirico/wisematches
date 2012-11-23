<#-- @ftlvariable name="context" type="wisematches.playground.tourney.regular.Tourney" -->
<#import "../../utils.ftl" as util>

<p>
    Next ${context.number?string}${gameMessageSource.getNumeralEnding(context.number, locale)}
    WiseMatches tourney is going to be
    started in ${gameMessageSource.formatRemainedTime(context.scheduledDate, locale)}
    (${gameMessageSource.formatDate(context.scheduledDate, locale)}).
</p>

<p>
    We found that you are not subscribed yet to the tourney. You can do that at any time
    on main tourneys page: <@util.link href="playground/tourney"/>.
</p>