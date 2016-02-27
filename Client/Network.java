import java.util.HashMap;

/**
 * The {@link #Network(String)} enumeration is a simple way of keeping track of
 * all of the developers IPs. The network IPs are used in
 * {@link Client #Client()} to determine where the connection will be taken to.
 * 
 * @author Dennis
 *
 */
public enum Network 
{
	LOCAL("127.0.0.1", 43594),
	PUBLIC("192.0.0.0", 43594),
	DEVELOPER("192.0.0.0" , 43594);

	/**
	 * The variable known as {@link #IP} contains the core values of the
	 * enumeration known as {@link #Network(String)} and determines where the
	 * connection will go. {@link #Port} controls the socket port used when
	 * connecting to {@link #IP}.
	 */
	private String IP;
	private int Port;

	/**
	 * Gets the {@link #IP} data and returns it in {@link #getIP()}.
	 * 
	 * @return IP
	 */
	public String getIP() {
		return IP;
	}

	/**
	 * Gets the {@link #Port} data and returns it in {@link #getPort()}.
	 * 
	 * @return
	 */
	public int getPort() {
		return Port;
	}

	/**
	 * Constructs the {@link #Network(String)} enumeration and sets the ip
	 * values accordingly to the enumeration.
	 * 
	 * @param IP
	 */
	private Network(String IP, int Port) {
		this.IP = IP;
		this.Port = Port;
	}

	/**
	 * Storing the {@link #Network(String)} variables in a {@link #networkMap}.
	 */
	private static HashMap<String, Network> networkMap = new HashMap<String, Network>();

	/**
	 * Loops through the enumeration values and puts them inside of the HashMap.
	 */
	static {
		for (Network network : values()) {
			networkMap.put(network.getIP(), network);
		}
	}
}