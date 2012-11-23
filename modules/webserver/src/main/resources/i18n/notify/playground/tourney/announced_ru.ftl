<#-- @ftlvariable name="context" type="wisematches.playground.tourney.regular.Tourney" -->
<#import "../../utils.ftl" as util>

<p>
    Следующий, ${context.number?string}${gameMessageSource.getNumeralEnding(context.number, locale)}
    WiseMatches турнир
    начинает через ${gameMessageSource.formatRemainedTime(context.scheduledDate, locale)}
    (${gameMessageSource.formatDate(context.scheduledDate, locale)}).
</p>

<p>
    По нашим сведениям вы еще не подписались на участие в этом турнире. Вы можете сделать это в любое время
    на странице всех ваших активных турниров: <@util.link href="playground/tourney"/>.
</p>