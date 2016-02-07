package git.openpi;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public final class Loader extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String CLIENT_URL = "http://www.SERVER_NAME.extension/client.jar";
    private static final String CLIENT_DIRECTORY = System.getProperty("user.home") + "/.SERVER_NAME/";

    private JLabel status;
    private JProgressBar progressBar;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final Loader frame = new Loader();
                    frame.setVisible(true);

                    double version = Double.parseDouble(System.getProperty("java.version").substring(0, 3));
                    if(version < 1.6) {
                        JOptionPane.showMessageDialog(null, "Please update your Java at www.java.com/download/.");
                        return;
                    }

                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() throws Exception {

                            File dir = new File(CLIENT_DIRECTORY);
                            if (!dir.exists() && !dir.mkdir()) {
                                throw new Exception("Unable to create directory " + CLIENT_DIRECTORY);
                            }

                            URL url = new URL(CLIENT_URL);
                            URLConnection conn = url.openConnection();
                            conn.setRequestProperty("User-Agent", "SERVER_NAME, Loader 1.00");

                            int fileSize = conn.getContentLength();

                            File existingClient = new File(CLIENT_DIRECTORY + "client.jar");
                            if (existingClient.exists() && existingClient.length() == fileSize) {
                                return null;
                            }

                            InputStream in = null;
                            OutputStream out = null;

                            try {
                                in = new BufferedInputStream(url.openStream());
                                out = new FileOutputStream(CLIENT_DIRECTORY + "client.jar");

                                byte data[] = new byte[1024];

                                long total = 0;
                                int count = 0;

                                while ((count = in.read(data)) != -1) {
                                    out.write(data, 0, count);

                                    total += count;
                                    setProgress((int) ((total * 100) / fileSize));
                                }
                            } catch (IOException e) {
                                frame.status.setText("Error loading client...");
                                JOptionPane.showMessageDialog(null, e.getMessage());
                            } finally {
                                if(in != null) {
                                    in.close();
                                }
                                if(out != null) {
                                    out.close();
                                }
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            frame.status.setText("Starting client please wait...");
                            try {
                                ProcessBuilder pb = new ProcessBuilder(new String[] { "java", "-Xmx768m", "-Xss2m", "-Dsun.java2d.noddraw=true", "-XX:CompileThreshold=1500", "-Xincgc", "-XX:+UseConcMarkSweepGC", "-XX:+UseParNewGC", "-jar", "client.jar"});

                                // osx fix
                                if(System.getProperty("os.name").equalsIgnoreCase("mac os x")) {
                                    pb = new ProcessBuilder(new String[] { "/usr/libexec/java_home", "-v", "1.6.*", "--exec", "java", "-Xmx768m", "-Xss2m", "-Dsun.java2d.noddraw=true", "-XX:CompileThreshold=1500", "-Xincgc", "-XX:+UseConcMarkSweepGC", "-XX:+UseParNewGC", "-jar", "client.jar"});
                                }

                                pb.directory(new File(CLIENT_DIRECTORY));
                                pb.start();

                                System.exit(-1);

                                /*System.out.println(url.toString());

                                URL[] urls = new URL[]{ url };

                                ClassLoader cl = new URLClassLoader(urls);
                                Class<?> clientClass = cl.loadClass("b");
                                Applet loader = (Applet) clientClass.newInstance();

                                frame.setTitle("SERVER_NAME Client");
                                frame.setSize(771, 532);
                                frame.setContentPane(loader);
                                frame.setLocationRelativeTo(null);

                                loader.init();*/
                            } catch (Exception e) {
                                frame.status.setText("Error loading client...");
                                JOptionPane.showMessageDialog(null, e.getMessage());

                                e.printStackTrace();
                            }
                        }
                    };

                    worker.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent event) {
                            if (event.getPropertyName().equalsIgnoreCase("progress")) {
                                int progress = (Integer) event.getNewValue();
                                frame.progressBar.setValue(progress);
                            }
                        }
                    });

                    worker.execute();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Loader() {
        setTitle("SERVER_NAME Loader 1.02");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 295, 97);
        setLocationRelativeTo(null);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(ClassLoader.getSystemResource("logo.png"));
        setIconImage(img);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setResizable(false);

        progressBar = new JProgressBar();
        progressBar.setBounds(6, 34, 283, 20);
        contentPane.add(progressBar);

        status = new JLabel("Getting latest client...");
        status.setHorizontalAlignment(SwingConstants.CENTER);
        status.setBounds(6, 6, 283, 16);
        contentPane.add(status);
    }

}