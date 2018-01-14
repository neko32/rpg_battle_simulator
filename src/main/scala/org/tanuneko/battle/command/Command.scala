package org.tanuneko.battle.command

import org.scalacheck.commands.Commands
import org.tanuneko.battle.model.CommandType

case class Command(fromTeamName: String,
                      fromEntName: String,
                      toTeamName: String,
                      toEntName: String,
                      cmdType: CommandType,
                      attrName: String,
                      value: Any,
                      expiry: Int = 0)
