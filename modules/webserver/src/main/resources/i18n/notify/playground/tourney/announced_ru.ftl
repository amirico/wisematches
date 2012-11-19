<#-- @ftlvariable name="context.tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#import "../../utils.ftl" as util>

<p>
    Следующий, ${context.tourney.number?string}${gameMessageSource.getNumeralEnding(context.tourney.number, locale)},
    WiseMatches турнир
    начинает через ${gameMessageSource.formatRemainedTime(context.tourney.scheduledDate, locale)}
    (${gameMessageSource.formatDate(context.tourney.scheduledDate, locale)}).
</p>

<p>
    По нашим сведениям вы еще не подписались на участие в этом турнире. Вы можете сделать это в любое время
    на странице всех ваших активных турниров: <@util.link href="playground/tourney"/>.
</p>