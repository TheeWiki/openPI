package server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import server.Constants;

public class Plugin {

	private static int loaded;
	public static PythonInterpreter pythonInterpreter = new PythonInterpreter();

	static {
		Plugin.pythonInterpreter.setOut(new Logger(System.out));
		Plugin.pythonInterpreter.setErr(new Logger(System.err));
	}

	public static Object callFunc(Class<?> synthax, String variable, Object... binds) {
		try {
			final PyObject obj = Plugin.pythonInterpreter.get(variable);
			if (obj != null && obj instanceof PyFunction) {
				final PyFunction function = (PyFunction) obj;
				final PyObject[] objects = new PyObject[binds.length];
				for (int count = 0; count < binds.length; count++) {
					final Object bind = binds[count];
					objects[count] = Py.java2py(bind);
				}
				return function.__call__(objects).__tojava__(synthax);
			} else {
				return null;
			}
		} catch (final PyException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static boolean execute(String funcName, Object... binds) {
		try {
			final PyObject obj = Plugin.pythonInterpreter.get(funcName);
			if (obj != null && obj instanceof PyFunction) {
				final PyFunction func = (PyFunction) obj;
				final PyObject[] objects = new PyObject[binds.length];
				for (int count = 0; count < binds.length; count++) {
					final Object bind = binds[count];
					objects[count] = Py.java2py(bind);
				}
				func.__call__(objects);
				return true;
			} else {
				return false;
			}
		} catch (final PyException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static PyObject getVariable(String variable) {
		try {
			return Plugin.pythonInterpreter.get(variable);
		} catch (final PyException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void load() throws IOException {
		Plugin.pythonInterpreter.cleanup();
		final File plugins = new File(Constants.SCRIPT_DIRECTORY);
		if (plugins.isDirectory() && !plugins.getName().startsWith(".")) {
			final File[] children = plugins.listFiles();
			for (final File child : children) {
				if (child.isFile()) {
					if (child.getName().endsWith(Constants.SCRIPT_FILE_EXTENSION)) {
						pythonInterpreter.execfile(new FileInputStream(child));
						loaded++;
					}
				} else {
					Plugin.recurse(child.getPath());
				}
			}
		}
		System.out.println("Loaded " + loaded + " Python plugins.");
		Plugin.loaded = 0;
	}

	private static void recurse(String directory) throws IOException {
		final File scriptDir = new File(directory);
		if (scriptDir.isDirectory() && !scriptDir.getName().startsWith(".")) {
			final File[] children = scriptDir.listFiles();
			for (final File child : children) {
				if (child.isFile()) {
					if (child.getName().endsWith(Constants.SCRIPT_FILE_EXTENSION)) {
						if (Constants.PRINT_PLUGIN_DIRECTORIES)
							System.out.println(child.getPath());
						Plugin.pythonInterpreter.execfile(new FileInputStream(child));
						Plugin.loaded++;
					}
				} else {
					Plugin.recurse(child.getPath());
				}
			}
		}
	}
}