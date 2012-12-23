INSERT INTO notification_settings SELECT
                                    pid,
                                    `playground.game.started` != 0,
                                    `playground.game.turn` != 0,
                                    `playground.game.finished` != 0,
                                    `playground.game.expiration.day` != 0,
                                    `playground.game.expiration.half` != 0,
                                    `playground.game.expiration.hour` != 0,
                                    `playground.challenge.initiated` != 0,
                                    `playground.challenge.rejected` != 0,
                                    `playground.challenge.repudiated` != 0,
                                    `playground.challenge.terminated` != 0,
                                    `playground.challenge.expiration.days` != 0,
                                    `playground.challenge.expiration.day` != 0,
                                    `playground.message.received` != 0,
                                    `playground.tourney.announced` != 0,
                                    `playground.tourney.finished` != 0
                                  FROM notification_timestamp;