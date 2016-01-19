// HIER DIE MAIN-METHODE DER LOGIK

package crawler;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.validation.*;

import org.xml.sax.helpers.*;
import org.w3c.dom.*;

/**
 * The class RSSThread is the top level class of the RSS server. An object of
 * this class reads the property file "configuration.properties" providing the
 * directory for the archive, the directories for the subscriptions and sources
 * files and a directory containing the schema definitions. The corresponding
 * property keys are "archivedir", "sourcedir", "subscriptiondir" and
 * "schemadir".
 * 
 * <p>
 * An example of such a property file may have the following content:
 * <p>
 * archivedir=C:/User/Richard/diverses/RSS/rssfiles/<br>
 * <p>
 * sourcedir=C:/User/Richard/diverses/RSS/sources/<br>
 * <p>
 * subscriptiondir=C:/User/Richard/diverses/RSS/subscriptions/<br>
 * <p>
 * schemadir=C:/User/Richard/diverses/RSS/schemas/
 * 
 * <p>
 * The location of this file has to be in the working directory of the RSS
 * server.
 * 
 * The RSS server reads the XML files from the "sourcedir" and "subscriptiondir"
 * first. Afterwards the method "run" is started, reading repeatedly RSS files
 * from sources specified by files in the directory "sourcedir" using an object
 * of class RSSParser. For every new article a file is generated and stored in a
 * subdirectory of "archivedir". This file contains all information from the RSS
 * file about the article and the extracted text from the article itself. The
 * test is extracted by using the boiler pipe library. The RSS server writes
 * further files into notification directories as specified by XML files in the
 * directory "subscriptiondir". These files contain the names of the new article
 * files stored in the archive.
 * 
 * @author Richard Goebel
 *
 */
public class RSSThread extends Thread implements Runnable, Observer {

	

	/**
	 * The static variable "archivedir" specifies the directory for the archive.
	 * The value for the variable is read from file "configuration.properties"
	 * in the working directory of the application.
	 * 
	 */
	public static String archivedir;
	/**
	 * The static variable "sourcedir" specifies the directory for the XML files
	 * specifying different RSS sources. The value for the variable is read from
	 * file "configuration.properties" in the working directory of the
	 * application.
	 * 
	 */
	public static String sourcedir;
	/**
	 * The static variable "subscriptiondir" specifies the directory for the
	 * subscription files. The value for the variable is read from file
	 * "configuration.properties" in the working directory of the application.
	 * 
	 */
	public static String subscriptiondir;
	/**
	 * The static variable "schemadir" specifies the directory for the schema
	 * files. The value for the variable is read from file
	 * "configuration.properties" in the working directory of the application.
	 * 
	 */
	public static String schemadir;

	private static String currdir;
	private static ServerLogfile messageFile;
	private static ServerLogfile errorFile;
	private static File terminate;

	private HashMap<URL, RSSTask> tasks = new HashMap<URL, RSSTask>();
	private HashMap<String, RSSSubscription> subscriptions = new HashMap<String, RSSSubscription>();
	private int sleepTime = 100; // "sleeping time" in ms

	private DirectoryMonitor sourceMonitor;
	private DirectoryMonitor subscriptionMonitor;

	private boolean initialSubscriptions = false; // initial subscriptions read
	private boolean initialSources = false; // initial sources read

	/**
	 * This constructor sets the sleep time for the run method and registers two
	 * monitors for the sources and the subscriptions directory. The RSS server
	 * will be notified by these monitors if new files are added or existing
	 * files are modified in these directories. New or modified files will be
	 * loaded and their content will be considered. Note that the deletion of
	 * files has no effect on the RSS server. As a consequence sources or
	 * subscriptions are only removed if the server is restarted.
	 * 
	 * @param sleepTime
	 *            sleeping time of method run
	 */
	public RSSThread(int sleepTime) {
		this.sleepTime = sleepTime;
		sourceMonitor = new DirectoryMonitor(sourcedir, 100, errorFile);
		sourceMonitor.addObserver(this);
		sourceMonitor.startMonitor();

		subscriptionMonitor = new DirectoryMonitor(subscriptiondir, 100, errorFile);
		subscriptionMonitor.addObserver(this);
		subscriptionMonitor.startMonitor();
	}

	/**
	 * This method adds a new RSSTask to the server. An RSSTask corresponds to a
	 * single RSS source.
	 * 
	 * @param task
	 *            Task to be added
	 */
	public void addTask(RSSTask task) {
		tasks.put(task.getURL(), task);
	}

	/**
	 * This method removes an RSSTask from the server. An RSSTask corresponds to
	 * a single RSS source.
	 * 
	 * @param url
	 *            URL of task to be removed
	 */
	public boolean remTask(URL url) {
		return (tasks.remove(url) != null);
	}

	/**
	 * The method "readSourceFile" reads a source file from the directory
	 * specified by "sourcedir" and generates a single RSSTask from this file.
	 * 
	 * @param file
	 *            XML file specifying an RSS source
	 */
	public void readSourceFile(File file) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;

		dbf.setValidating(true);
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setExpandEntityReferences(true);
		dbf.setNamespaceAware(true);

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new DefaultHandler());
			doc = db.parse(file);
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File(schemadir + "RSSFeeds.xsd"));
			Validator v = schema.newValidator();
			v.validate(new DOMSource(doc));
		} catch (Exception e) {
			errorFile.writeEvent("RSSThread", "readSourceFile", "Error message: " + e);
		}

		NodeList taskElems = doc.getElementsByTagName("address");
		Element taskElem;
		RSSTask task;
		URL url;
		String country;
		String lang;
		String topic;
		int interval;
		for (int i = 0; i < taskElems.getLength(); i++) {
			taskElem = (Element) taskElems.item(i);
			try {
				url = new URL(taskElem.getAttribute("link"));
			} catch (Exception e) {
				errorFile.writeEvent("RSSThread", "readSourceFile",
						"Ignoring invalid URI: " + taskElem.getAttribute("link"));
				continue;
			}
			country = taskElem.getAttribute("country");
			lang = taskElem.getAttribute("language");
			topic = taskElem.getAttribute("topic");
			interval = Integer.parseInt(taskElem.getAttribute("ttl")) * 60000;
			task = new RSSTask(url, country, lang, topic, interval);
			addTask(task);
		}
	}

	/**
	 * The method "readSubscription" reads a subscription file from the
	 * directory specified by "sourcedir" and generates a single RSSSubscription
	 * from this file.
	 * 
	 * @param file
	 *            XML file specifying a subscription
	 */
	public void readSubscription(File file) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;

		dbf.setValidating(true);
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setExpandEntityReferences(true);
		dbf.setNamespaceAware(true);

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new DefaultHandler());
			doc = db.parse(file);
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File(schemadir + "RSSSubscription.xsd"));
			Validator v = schema.newValidator();
			v.validate(new DOMSource(doc));
		} catch (Exception e) {
			errorFile.writeEvent("RSSThread", "readSubscription", "Error message: " + e);
		}

		Element targetdir = (Element) doc.getElementsByTagName("targetdir").item(0);
		String path = targetdir.getAttribute("path");
		RSSSubscription subscription = new RSSSubscription(targetdir.getAttribute("path"));
		subscriptions.put(path, subscription);
		RSSPattern pattern;

		NodeList patterns = doc.getElementsByTagName("pattern");
		Element patElem;

		for (int i = 0; i < patterns.getLength(); i++) {
			pattern = new RSSPattern();
			patElem = (Element) patterns.item(i);
			if (!patElem.getAttribute("country").isEmpty()) {
				pattern.setCountry(patElem.getAttribute("country"));
			}
			if (!patElem.getAttribute("language").isEmpty()) {
				pattern.setLang(patElem.getAttribute("language"));
			}
			if (!patElem.getAttribute("topic").isEmpty()) {
				pattern.setTopic(patElem.getAttribute("topic"));
			}
			if (!patElem.getAttribute("source").isEmpty()) {
				pattern.setSource(patElem.getAttribute("source"));
			}
			if (!patElem.getAttribute("syear").isEmpty()) {
				pattern.setSyear(Integer.parseInt(patElem.getAttribute("syear")));
			}
			if (!patElem.getAttribute("smonth").isEmpty()) {
				pattern.setSmonth(Integer.parseInt(patElem.getAttribute("smonth")));
			}
			if (!patElem.getAttribute("sday").isEmpty()) {
				pattern.setSday(Integer.parseInt(patElem.getAttribute("sday")));
			}
			if (!patElem.getAttribute("eyear").isEmpty()) {
				pattern.setEyear(Integer.parseInt(patElem.getAttribute("eyear")));
			}
			if (!patElem.getAttribute("emonth").isEmpty()) {
				pattern.setEmonth(Integer.parseInt(patElem.getAttribute("emonth")));
			}
			if (!patElem.getAttribute("eday").isEmpty()) {
				pattern.setEday(Integer.parseInt(patElem.getAttribute("eday")));
			}
			subscription.addPattern(pattern);
		}
	}

	/**
	 * The method "processSubscription" processes articles represented by an
	 * object of class "Channel" and checks, whether these articles satisfy the
	 * patterns specified by the subscription. This check uses also some
	 * information from the corresponding RSSTask. File names of articles which
	 * satisfy at least one pattern are written to a file in the notification
	 * directory specified by the RSSSubscription object.
	 * 
	 * @param subscription
	 * @param task
	 * @param channel
	 */
	public void processSubscription(RSSSubscription subscription, RSSTask task, Channel channel) {
		String outfile = subscription.getFilename() + "File" + System.currentTimeMillis() + ".txt";
		LinkedList<String> filenames = new LinkedList<String>();
		for (Item item : channel.getItems()) {
			if (subscription.check(task.getCountry(), task.getLang(), task.getTopic(), channel.getTitle(), item)) {
				filenames.add(item.getFileName());
			}
		}
		if (filenames.size() > 0) {
			FileWriter fw;
			try {
				fw = new FileWriter(outfile);
				for (String s : filenames) {
					fw.write(s + '\n');
				}
				fw.close();
			} catch (IOException e) {
				errorFile.writeEvent("RSSThread", "processSubscription",
						"Writing file for subscription failed: " + outfile);
			}
		}
	}

	/**
	 * The method processSubscritpions processes all registered subscriptions
	 * for new articles represented by an object of class Channel. A
	 * subscription considers also parameters from an RSSTask.
	 * 
	 * @param task
	 *            corresponding RSS task
	 * @param channel
	 *            summary of new articles
	 */
	public void processSubscriptions(RSSTask task, Channel channel) {
		for (RSSSubscription subscription : subscriptions.values()) {
			processSubscription(subscription, task, channel);
		}
	}

	/**
	 * This is the method "run" for the RSS server thread. As the very first
	 * step this method waits for for the two monitors of the sources and
	 * subscription directories to provide the initial files. Afterwards all
	 * specified sources are visited for the first time and the "time to live"
	 * parameter is adjusted according the the retrieved RSS files. Then all
	 * sources are retrieved periodically according to the adjusted schedule.
	 * The run method is finished if a file with the name "stop" is put into the
	 * working directory of the application.
	 * 
	 * 
	 */
	public void run() {
		try {
			RSSParser parser = new RSSParser(messageFile, errorFile);
			Channel channel;
			long ctime;

			// waiting for the availability of initial sources and subscriptions
			while (!initialSubscriptions || !initialSources || terminate.exists()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					return;
				}
			}

			messageFile.writeEvent("RSSThread", "run", "RSS Server started!");
			// parse feeds for the first time
			synchronized (tasks) {
				synchronized (subscriptions) {
					for (RSSTask t : tasks.values()) {
						messageFile.writeEvent("RSSThread", "run", "First visit: " + t.getURL());
						channel = parser.parseFeed(t.getURL(), t.getCountry(), t.getLang(), t.getTopic(), archivedir);
						if (channel == null)
							continue;
						if (channel.getTTL() != 0) {
							int min = channel.getTTL() * 60 * 1000;
							if (min > t.getInterval()) {
								t.setInterval(min);
								messageFile.writeEvent("RSSThread", "run", "Override interval with " + channel.getTTL()
										+ " minutes for " + channel.getTitle());
							}
						}
						processSubscriptions(t, channel);
					}
				}
			}

			ctime = System.currentTimeMillis();
			while (true) {
				if (terminate.exists()) {
					messageFile.writeEvent("RSSThread", "run", "RSS Server finished!");
					if (!terminate.delete()) {
						messageFile.writeEvent("RSSThread", "run", "Stop file could not be deleted!");
					}
					System.exit(0);
				}
				synchronized (tasks) {
					synchronized (subscriptions) {
						for (RSSTask t : tasks.values()) {
							t.addElapsed(System.currentTimeMillis() - ctime);
							if (t.getInterval() < t.getElapsed()) {
								messageFile.writeEvent("RSSThread", "run", "Visiting: " + t.getURL());
								channel = parser.parseFeed(t.getURL(), t.getCountry(), t.getLang(), t.getTopic(),
										archivedir);
								if (channel != null) {
									processSubscriptions(t, channel);
								}
								t.resetElapsed();
							}
						}
					}
				}
				ctime = System.currentTimeMillis();
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					messageFile.writeEvent("RSSThread", "run", "RSS Server finished!");
					System.exit(0);
				}
			}
		} catch (Throwable t) {
			errorFile.writeEvent("RSSThread", "run", "Unhandled exception: " + t);
		}
	}

	/**
	 * Method "update" is usually called by a DirectoryMonitor when existing
	 * files from the sources or subscriptions directories are modified or new
	 * files are added. In this case existing objects of RSSTasks or
	 * RSSSubscriptions are updated and/or new objects of these classes are
	 * generated.
	 */
	public void update(Observable arg0, Object arg1) {
		synchronized (tasks) {
			synchronized (subscriptions) {
				File[] files = (File[]) arg1;
				if (arg0 == sourceMonitor) {
					messageFile.writeEvent("RSSThread", "update", "Start to read new sources");
					for (int i = 0; i < files.length; i++) {
						readSourceFile(files[i]);
					}
					initialSources = true;
					messageFile.writeEvent("RSSThread", "update", "Finish to read new sources");
				} else if (arg0 == subscriptionMonitor) {
					messageFile.writeEvent("RSSThread", "update", "Start to read new subscriptions");
					for (int i = 0; i < files.length; i++) {
						readSubscription(files[i]);
					}
					initialSubscriptions = true;
					messageFile.writeEvent("RSSThread", "update", "Finish to read new subscriptions");
				}
			}
		}
	}

	

	/**
	 * The main function reads the configuration file "configuration.properties"
	 * from the working directory of the application and initializes the static
	 * variables "archivedir", "sourcedir", "subscriptiondir" and "schemadir"
	 * from this content of this file. Afterwards an object of the class
	 * RSSThread is generated and the thread is started.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args, boolean started) throws Exception {
		try {
			currdir = new java.io.File(".").getCanonicalPath();
			messageFile = new ServerLogfile(currdir + "/messages.log");
			errorFile = new ServerLogfile(currdir + "/errors.log");

			terminate = new File(currdir + "/stop");
			String configFile = currdir + "/configuration.properties";
			Properties properties = new Properties();
			properties.load(new FileReader(configFile));
			archivedir = properties.getProperty("archivedir");
			sourcedir = properties.getProperty("sourcedir");
			subscriptiondir = properties.getProperty("subscriptiondir");
			schemadir = properties.getProperty("schemadir");

			RSSThread thread = new RSSThread(100);

			thread.start();

		} catch (Throwable t) {
			errorFile.writeEvent("RSSThread", "main", "Unhandled exception: " + t);
		}
	}

	public static String getCurrdir() {
		return currdir;
	}

	public static void setCurrdir(String currdir) {
		RSSThread.currdir = currdir;
	}

	public static ServerLogfile getMessageFile() {
		return messageFile;
	}

	public static void setMessageFile(ServerLogfile messageFile) {
		RSSThread.messageFile = messageFile;
	}

	public static ServerLogfile getErrorFile() {
		return errorFile;
	}

	public static void setErrorFile(ServerLogfile errorFile) {
		RSSThread.errorFile = errorFile;
	}

	public static File getTerminate() {
		return terminate;
	}

	public static void setTerminate(File terminate) {
		RSSThread.terminate = terminate;
	}


}