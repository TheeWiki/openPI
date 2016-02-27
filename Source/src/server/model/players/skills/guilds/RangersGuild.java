package server.model.players.skills.guilds;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.EquipmentListener;
import server.model.players.Player;
import server.util.Misc;

/**
 * Rangers Guild
 * 
 * @author Aintaro
 * @edit Haile N.
 *
 */
public class RangersGuild {

	public final int
		ARROWS_REQ = 882,
		HIT_CHANCE = 55,
		RANGED_LV = 4;
	
	public final String
		MISSED = "Missed!",
		BLACK = "Hit Black!",
		YELLOW = "Hit Yellow!",
		BLUE = "Hit Blue!",
		RED = "Hit Red!",
		BULLSEYE = "Bulls-Eye";

	public int
		arrowsLeft = 0,
		playerScore = 0,
		hitChance;
	
	public boolean isFiringTarget;

	private Player player;

	public RangersGuild(Player player) {
		this.player = player;
	}

	public void fireAtTarget() {
		if(isFiringTarget) {
			return;
		}
		hitChance = Misc.random(HIT_CHANCE)+Misc.random(player.playerLevel[RANGED_LV]);
		if (arrowsLeft != 0) {
			for (int bowId : player.BOWS) {
				if(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == bowId) {
					player.usingBow = true;
			if (player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] == ARROWS_REQ && player.usingBow) {
				if (isInTargetArea()) {
					if (hitChance >= 10) {
						player.getPA().removeAllWindows();
						player.startAnimation(426);
						isFiringTarget = true;
						//c.getPlayerAction().setAction(true);
						//c.getPlayerAction().canWalk(false);
						CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer container) {
								if (hitChance >= 10 && hitChance <= 20) {
									playerScore += 10;
									sendConfiguration(80 + Misc.random(10), -60 + Misc.random(90));
									player.getPA().sendFrame126(BLACK, 567);
									player.getPA().addSkillXP(5, 4);
									player.getItems().deleteArrow();
									//c.getPlayerAction().setAction(false);
									//c.getPlayerAction().canWalk(true);
								} else if (hitChance >= 20 && hitChance < 30) {
									playerScore += 20;
									sendConfiguration(-70 + Misc.random(10),10 - Misc.random(40));
									player.getPA().sendFrame126(BLUE,567);
									player.getPA().addSkillXP(10, 4);
									player.getItems().deleteArrow();
									//c.getPlayerAction().setAction(false);
									//c.getPlayerAction().canWalk(true);
								} else if (hitChance >= 30 && hitChance < 50) {
									playerScore += 30;
									sendConfiguration(-30 - Misc.random(15),10 - Misc.random(25));
									player.getPA().sendFrame126(RED, 567);
									player.getPA().addSkillXP(15, 4);
									player.getItems().deleteArrow();
									//c.getPlayerAction().setAction(false);
									//c.getPlayerAction().canWalk(true);
								} else if (hitChance >= 50 && hitChance < 75) {
									playerScore += 50;
									sendConfiguration(5 - Misc.random(20),0 - Misc.random(20));
									player.getPA().sendFrame126(YELLOW, 567);
									player.getPA().addSkillXP(15, 4);
									player.getItems().deleteArrow();
									//c.getPlayerAction().setAction(false);
									//c.getPlayerAction().canWalk(true);
								} else if (hitChance >= 75) {
									playerScore += 100;
									sendConfiguration(0, 0);
									player.getPA().sendFrame126(BULLSEYE, 567);
									player.getPA().addSkillXP(50, 4);
									player.getItems().deleteArrow();
									//c.getPlayerAction().setAction(false);
									//c.getPlayerAction().canWalk(true);
								} else {
									player.getPA().sendFrame126(MISSED, 567);
									sendConfiguration(1200, 1200);
									player.getItems().deleteArrow();
									//c.getPlayerAction().setAction(false);
									//c.getPlayerAction().canWalk(true);
								}
								container.stop();
							}
							@Override
							public void stop() {
								isFiringTarget = false;
							}
						}, 3);
					}
				} else {
					player.getDH().sendStatement("You can't shoot from here.");
				}
			} else {
				player.getDH().sendStatement("You need some bronze arrows and a bow to shoot the target.");
				player.nextChat = 0;
			}}}
		} else {
			player.getDH().sendStatement("You should talk to the competition judge.");
			player.nextChat = 0;
		}
	}
	
	private void sendConfiguration(int xPos, int yPos) {
		switch (arrowsLeft) {
		case 1:
			arrowsLeft--;
			player.getPA().sendFrame70(1200, 1200, 538); 
			player.getPA().sendFrame70(1200, 1200, 557); 
			player.getPA().sendFrame70(1200, 1200, 559); 
			player.getPA().sendFrame70(1200, 1200, 560); 
			player.getPA().sendFrame70(1200, 1200, 561);
			player.getPA().sendFrame70(1200, 1200, 562); 
			player.getPA().sendFrame70(1200, 1200, 563); 
			player.getPA().sendFrame70(1200, 1200, 564);
			player.getPA().sendFrame70(1200, 1200, 565); 
			player.getPA().sendFrame70(1200, 1200, 566);
			player.getPA().sendFrame126("" + playerScore, 551); 
			player.getPA().sendFrame70(xPos, yPos, 536); 
			player.getPA().showInterface(446); 
			break;

		case 2:
			arrowsLeft--;
			player.getPA().sendFrame70(0, 0, 538); 
			player.getPA().sendFrame70(1200, 1200, 557); 
			player.getPA().sendFrame70(1200, 1200, 559); 
			player.getPA().sendFrame70(1200, 1200, 560); 
			player.getPA().sendFrame70(1200, 1200, 561);
			player.getPA().sendFrame70(1200, 1200, 562); 
			player.getPA().sendFrame70(1200, 1200, 563);
			player.getPA().sendFrame70(1200, 1200, 564); 
			player.getPA().sendFrame70(1200, 1200, 565); 
			player.getPA().sendFrame70(1200, 1200, 566); 
			player.getPA().sendFrame126("" + playerScore, 551);
			player.getPA().sendFrame70(xPos, yPos, 536); 
			player.getPA().showInterface(446);
			break;

		case 3:
			arrowsLeft--;
			player.getPA().sendFrame70(0, 0, 538);
			player.getPA().sendFrame70(0, 0, 557);
			player.getPA().sendFrame70(1200, 1200, 559); 
			player.getPA().sendFrame70(1200, 1200, 560); 
			player.getPA().sendFrame70(1200, 1200, 561); 
			player.getPA().sendFrame70(1200, 1200, 562); 
			player.getPA().sendFrame70(1200, 1200, 563); 
			player.getPA().sendFrame70(1200, 1200, 564); 
			player.getPA().sendFrame70(1200, 1200, 565); 
			player.getPA().sendFrame70(1200, 1200, 566); 
			player.getPA().sendFrame126("" + playerScore, 551); 
			player.getPA().sendFrame70(xPos, yPos, 536); 
			player.getPA().showInterface(446); 
			break;

		case 4:
			arrowsLeft--;
			player.getPA().sendFrame70(0, 0, 538); 
			player.getPA().sendFrame70(0, 0, 557);
			player.getPA().sendFrame70(0, 0, 559); 
			player.getPA().sendFrame70(1200, 1200, 560); 
			player.getPA().sendFrame70(1200, 1200, 561);
			player.getPA().sendFrame70(1200, 1200, 562);
			player.getPA().sendFrame70(1200, 1200, 563);
			player.getPA().sendFrame70(1200, 1200, 564); 
			player.getPA().sendFrame70(1200, 1200, 565); 
			player.getPA().sendFrame70(1200, 1200, 566); 
			player.getPA().sendFrame126("" + playerScore, 551); 
			player.getPA().sendFrame70(xPos, yPos, 536);
			player.getPA().showInterface(446);
			break;

		case 5:
			arrowsLeft--;
			player.getPA().sendFrame70(0, 0, 538); 
			player.getPA().sendFrame70(0, 0, 557); 
			player.getPA().sendFrame70(0, 0, 559); 
			player.getPA().sendFrame70(0, 0, 560); 
			player.getPA().sendFrame70(1200, 1200, 561); 
			player.getPA().sendFrame70(1200, 1200, 562);
			player.getPA().sendFrame70(1200, 1200, 563); 
			player.getPA().sendFrame70(1200, 1200, 564); 
			player.getPA().sendFrame70(1200, 1200, 565); 
			player.getPA().sendFrame70(1200, 1200, 566); 
			player.getPA().sendFrame126("" + playerScore, 551); 
			player.getPA().sendFrame70(xPos, yPos, 536); 
			player.getPA().showInterface(446); 
			break;

		case 6:
			arrowsLeft--;
			player.getPA().sendFrame70(0, 0, 538); 
			player.getPA().sendFrame70(0, 0, 557);
			player.getPA().sendFrame70(0, 0, 559); 
			player.getPA().sendFrame70(0, 0, 560); 
			player.getPA().sendFrame70(0, 0, 561); 
			player.getPA().sendFrame70(1200, 1200, 562);
			player.getPA().sendFrame70(1200, 1200, 563); 
			player.getPA().sendFrame70(1200, 1200, 564); 
			player.getPA().sendFrame70(1200, 1200, 565);
			player.getPA().sendFrame70(1200, 1200, 566); 
			player.getPA().sendFrame126("" + playerScore, 551);
			player.getPA().sendFrame70(xPos, yPos, 536); 
			player.getPA().showInterface(446);
			break;

		case 7:
			arrowsLeft--;
			player.getPA().sendFrame70(0, 0, 538);
			player.getPA().sendFrame70(0, 0, 557); 
			player.getPA().sendFrame70(0, 0, 559); 
			player.getPA().sendFrame70(0, 0, 560); 
			player.getPA().sendFrame70(0, 0, 561);
			player.getPA().sendFrame70(0, 0, 562); 
			player.getPA().sendFrame70(1200, 1200, 563); 
			player.getPA().sendFrame70(1200, 1200, 564); 
			player.getPA().sendFrame70(1200, 1200, 565); 
			player.getPA().sendFrame70(1200, 1200, 566); 
			player.getPA().sendFrame126("" + playerScore, 551); 
			player.getPA().sendFrame70(xPos, yPos, 536); 
			player.getPA().showInterface(446); 
			break;

		case 8:
			arrowsLeft--;
			player.getPA().sendFrame70(0, 0, 538); 
			player.getPA().sendFrame70(0, 0, 557); 
			player.getPA().sendFrame70(0, 0, 559); 
			player.getPA().sendFrame70(0, 0, 560); 
			player.getPA().sendFrame70(0, 0, 561); 
			player.getPA().sendFrame70(0, 0, 562); 
			player.getPA().sendFrame70(0, 0, 563); 
			player.getPA().sendFrame70(1200, 1200, 564);
			player.getPA().sendFrame70(1200, 1200, 565); 
			player.getPA().sendFrame70(1200, 1200, 566);
			player.getPA().sendFrame126("" + playerScore, 551); 
			player.getPA().sendFrame70(xPos, yPos, 536); 
			player.getPA().showInterface(446); 
			break;

		case 9:
			arrowsLeft--;
			player.getPA().sendFrame70(0, 0, 538); 
			player.getPA().sendFrame70(0, 0, 557); 
			player.getPA().sendFrame70(0, 0, 559); 
			player.getPA().sendFrame70(0, 0, 560); 
			player.getPA().sendFrame70(0, 0, 561); 
			player.getPA().sendFrame70(0, 0, 562); 
			player.getPA().sendFrame70(0, 0, 563); 
			player.getPA().sendFrame70(0, 0, 564); 
			player.getPA().sendFrame70(1200, 1200, 565); 
			player.getPA().sendFrame70(1200, 1200, 566); 
			player.getPA().sendFrame126("" + playerScore, 551); 
			player.getPA().sendFrame70(xPos, yPos, 536); 
			player.getPA().showInterface(446); 
			break;

		case 10:
			arrowsLeft--;
			player.getPA().sendFrame70(0, 0, 538); 
			player.getPA().sendFrame70(0, 0, 557); 
			player.getPA().sendFrame70(0, 0, 559); 
			player.getPA().sendFrame70(0, 0, 560); 
			player.getPA().sendFrame70(0, 0, 561); 
			player.getPA().sendFrame70(0, 0, 562); 
			player.getPA().sendFrame70(0, 0, 563); 
			player.getPA().sendFrame70(0, 0, 564); 
			player.getPA().sendFrame70(0, 0, 565); 
			player.getPA().sendFrame126("" + playerScore, 551); 
			player.getPA().sendFrame70(xPos, yPos, 536); 
			player.getPA().showInterface(446); 
			break;
		}
	}

	public boolean isInTargetArea() {
		if (player.absX >= 2669 && player.absX <= 2674 && player.absY >= 3415 && player.absY <= 3421) {
			return true;
		}
		return false;
	}

	public void exchangePoints() {
		if (arrowsLeft == 0 && playerScore > 0) {
			if(player.getItems().freeSlots() > 0) {
				int ticketsAmt = playerScore / 10;
				player.getItems().addItem(1464, ticketsAmt);
				player.getDH().sendNpcChat2("Well done. Your score is : " + playerScore + ".", "You have earned " + ticketsAmt + " Archery tickets.",485, "Tutor");
				playerScore = 0;
				player.nextChat = 0;
			} else {
				player.getDH().sendStatement("You need 1 free slot to exchange tickets.");
				player.nextChat = 0;
			}
		} else {
			player.getDH().sendStatement("You still got " + arrowsLeft + " shots left");
			player.nextChat = 0;
		}
	}

	public void howAmIDoing() {
		if (playerScore == 0) {
			player.getDH().sendNpcChat2("You haven't started yet. Stand behind the hay bales and", "shoot those arrows at the targets.", 485, "Tutor");
			player.nextChat = 0;
		} else {
			player.getDH().sendNpcChat2("Your score is : " + playerScore, "Your doing very well !",485, "Tutor");
			player.nextChat = 0;
		}
	}

	public void buyArrows() {
		if (arrowsLeft == 0) {
			if (player.getItems().playerHasItem(995, 200)) {
				if (player.getItems().freeSlots() > 1) {
					arrowsLeft = 10;
					player.getItems().deleteItem2(995, 200);
					player.getItems().addItem(ARROWS_REQ, 10);
					player.getItems().addItem(841, 1);
					player.getDH().sendStatement("The archer hands you 10 bronze arrows and a bow.");
					player.nextChat = 0;
				} else {
					player.getDH().sendStatement("You need 2 free slots to receive arrows and a bow.");
				}
			} else {
				player.getDH().sendStatement("You need at least 200 gp to buy 10 arrows and a bow.");
				player.nextChat = 0;
			}
		} else {
			player.getDH().sendStatement("You still got " + arrowsLeft + " shots left.");
			player.nextChat = 0;
		}
	}
}