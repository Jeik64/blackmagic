package nebula_ui.data;

public class RunVariables {
    // Get platform value (e.g., "AOS" or "IOS") from environment variable, default to "AOS"
    public static final Platform PLATFORM = "AOS".equals(System.getenv("PLATFORM")) ? Platform.AOS : Platform.IOS;;
    // Get device1 and device2 names from environment variables, default to "device1" and "device2"
    public static final String DEVICE1 = System.getenv("DEVICE1") != null ? System.getenv("DEVICE1") : "device1";
    public static final String SERVER_ADDRESS = System.getenv("SERVER_ADDRESS") != null ? System.getenv("SERVER_ADDRESS") : "http://0.0.0.0:4723/";


}
