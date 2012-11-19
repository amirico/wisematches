<#-- @ftlvariable name="context.tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#import "../../utils.ftl" as util>

<p>
    Next ${context.tourney.number?string}${gameMessageSource.getNumeralEnding(context.tourney.number, locale)}
    WiseMatches tourney is going to be
    started in ${gameMessageSource.formatRemainedTime(context.tourney.scheduledDate, locale)}
    (${gameMessageSource.formatDate(context.tourney.scheduledDate, locale)}).
</p>

<p>
    We found that you are not subscribed yet to the tourney. You can do that at any time
    on main tourneys page: <@util.link href="playground/tourney"/>.
</p>