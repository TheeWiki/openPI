from server.util import Plugin

# Logout button
def clickButton_9154(player):
	player.logout()

# Activating Prayers in the Prayer book
def clickButton_21233(player):
	player.getCombat().activatePrayer(0)
def clickButton_21234(player):
	player.getCombat().activatePrayer(1)
def clickButton_21235(player):
	player.getCombat().activatePrayer(2)
def clickButton_70080(player):
	player.getCombat().activatePrayer(3)
def clickButton_70082(player):
	player.getCombat().activatePrayer(4)
def clickButton_21236(player):
	player.getCombat().activatePrayer(5)
def clickButton_21237(player):
	player.getCombat().activatePrayer(6)
def clickButton_21238(player):
	player.getCombat().activatePrayer(7)
def clickButton_21239(player):
	player.getCombat().activatePrayer(8)
def clickButton_21240(player):
	player.getCombat().activatePrayer(9)
def clickButton_21241(player):
	player.getCombat().activatePrayer(10)
def clickButton_70084(player):
	player.getCombat().activatePrayer(11)
def clickButton_70086(player):
	player.getCombat().activatePrayer(12)
def clickButton_21242(player):
	player.getCombat().activatePrayer(13)
def clickButton_21243(player):
	player.getCombat().activatePrayer(14)
def clickButton_21244(player):
	player.getCombat().activatePrayer(15)
def clickButton_21245(player):
	player.getCombat().activatePrayer(16)
def clickButton_21246(player):
	player.getCombat().activatePrayer(17)
def clickButton_21247(player):
	player.getCombat().activatePrayer(18)
def clickButton_70088(player):
	player.getCombat().activatePrayer(19)
def clickButton_70090(player):
	player.getCombat().activatePrayer(20)
def clickButton_2171(player):
	player.getCombat().activatePrayer(21)
def clickButton_2172(player):
	player.getCombat().activatePrayer(22)
def clickButton_2173(player):
	player.getCombat().activatePrayer(23)
def clickButton_70092(player):
	player.getCombat().activatePrayer(24)
def clickButton_70094(player):
	player.getCombat().activatePrayer(25)

# Brightness level 1
def clickButton_74201(player):
			player.getActionSender().sendConfig(505, 1)
			player.getActionSender().sendConfig(506, 0)
			player.getActionSender().sendConfig(507, 0)
			player.getActionSender().sendConfig(508, 0)
			player.getActionSender().sendConfig(166, 1)
			
# Brightness level 2
def clickButton_74203(player):
			player.getActionSender().sendConfig(505, 0);
			player.getActionSender().sendConfig(506, 1);
			player.getActionSender().sendConfig(507, 0);
			player.getActionSender().sendConfig(508, 0);
			player.getActionSender().sendConfig(166, 2);

# Brightness level 3
def clickButton_74204(player):
			player.getActionSender().sendConfig(505, 0);
			player.getActionSender().sendConfig(506, 0);
			player.getActionSender().sendConfig(507, 1);
			player.getActionSender().sendConfig(508, 0);
			player.getActionSender().sendConfig(166, 3);
			
# Brightness level 4
def clickButton_74205(player):
			player.getActionSender().sendConfig(505, 0);
			player.getActionSender().sendConfig(506, 0);
			player.getActionSender().sendConfig(507, 0);
			player.getActionSender().sendConfig(508, 1);
			player.getActionSender().sendConfig(166, 4);