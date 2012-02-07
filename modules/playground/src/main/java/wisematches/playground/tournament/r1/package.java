/**
 * <p>
 *     This chapter describes tournament use cases for players and how its are mapped to Java code.
 * </p>
 *
 * <p>
 *     Each players are divided into five sections according to player's rating:
 * <ul>
 *     <li><strong>Grandmaster</strong> - any rating</li>
 *     <li><strong>Expert</strong> - top rating 2000 or less</li>
 *     <li><strong>Advanced</strong> - top rating 1700 or less</li>
 *     <li><strong>Intermediate</strong> - top rating 1500 or less</li>
 *     <li><strong>Casual</strong> - top rating 1300 or less</li>
 * </ul>
 * "Top" rating means top rating of player for past 90 days.
 * </p>
 * <p>
 *     It means that player with highye rating can't play in lower section (Grandmaster can't play in Expert, Advanced and
 *     lower sections).
 * </p>
 *
 * <p>
 *     If by the time the tournament starts player's rating exceeds the rating limit for the section,
 *     he/she will be automatically moved to the next higher section.
 * </p>
 *
 * <p>
 *     Player can be subscribed only for one category. Player also can unsubscribe at any time while tournament is not
 *     started.
 * </p>
 *
 * <p>
 *     All players in each section are divided in groups by 4 players each and games of such groups called "round".
 *     The round is finished when games in this round are finished. The player who won games in the group is take a
 *     place in next round (if game was drawn when all players).
 * </p>
 *
 * <p>
 *     Player at any time can see information about any tournament, sections or groups.
 * </p>
 */
package wisematches.playground.tournament.r1;